package com.vodafone.parkingticket.service;

import com.vodafone.parkingticket.data.Slot;
import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.exception.GarageOperationException;
import com.vodafone.parkingticket.exception.ResultStatus;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class GarageManagingService {
    private static final List<Slot> SLOTS = new ArrayList<>(5);
    private static final Integer SLOT_GAP = 1;

    private GarageManagingService(){}

    public static Slot parkingVehicle(Vehicle vehicle){
        Slot slot = null;
        if(ObjectUtils.isEmpty(SLOTS)){
            slot = createSlot(vehicle, 0);
        } else {
            Integer potentialFitSlotIndex = findPotentialFitSlot(vehicle);
            slot = createSlot(vehicle, potentialFitSlotIndex);
        }
        SLOTS.add(slot);

        return slot;
    }

    public static void leavingVehicle(Integer vehicleId){
        Optional<Slot> slotOptional = SLOTS.stream().filter(slot -> slot.getOccupiedInitialSlotIndex().equals(vehicleId)).findFirst();
        if(slotOptional.isPresent()){
            Slot willRemoveSlot = slotOptional.get();
            SLOTS.remove(willRemoveSlot);
        }
    }

    public static List<Slot> getStatusOfSlots(){
        return SLOTS;
    }

    private static Integer findPotentialFitSlot(Vehicle vehicle){
        Integer firstIndex = 0;
        List<Slot> safeCopiedSlots = SLOTS.stream().sorted(Comparator.comparingInt(Slot::getOccupiedInitialSlotIndex)).collect(Collectors.toList());

        if(safeCopiedSlots.get(0).getOccupiedInitialSlotIndex() != 0){
            Slot slot = Slot.builder().occupiedLastSlotIndex(0).build();
            safeCopiedSlots.add(0, slot);
        }


        if(safeCopiedSlots.size() > 1) {
            for (int index = 0; index < safeCopiedSlots.size() - 1; index++) {
                Slot currentSlot = safeCopiedSlots.get(index);
                Slot nextSlot = safeCopiedSlots.get(index + 1);

                int distance = findDistanceBetweenSlots(currentSlot, nextSlot);
                Integer occupiedNumberOfSlotsWithGap = getOccupiedNumberOfSlotsWithGap(vehicle);

                if (distance == 0 || distance != occupiedNumberOfSlotsWithGap) {
                    firstIndex = nextSlot.getOccupiedLastSlotIndex();
                }
                if (distance == occupiedNumberOfSlotsWithGap) {
                    firstIndex = currentSlot.getOccupiedLastSlotIndex();
                    break;
                }
                if(nextSlot.getOccupiedLastSlotIndex() == 10 || nextSlot.getOccupiedLastSlotIndex() + occupiedNumberOfSlotsWithGap > 10 ) throw new GarageOperationException(ResultStatus.GARAGE_FULL.getStatus());
            }
        } else {
            firstIndex = safeCopiedSlots.get(0).getOccupiedLastSlotIndex();
        }

        return firstIndex;
    }

    private static Integer getOccupiedNumberOfSlotsWithGap(Vehicle vehicle){
        return vehicle.getOccupiedNumberOfSlots() + SLOT_GAP;
    }

    private static Integer findDistanceBetweenSlots(Slot firstSlot, Slot secondSlot){
        return secondSlot.getOccupiedInitialSlotIndex() - firstSlot.getOccupiedLastSlotIndex();
    }

    private static Slot createSlot(Vehicle vehicle, Integer initialSlotIndex){
        Integer occupiedLastSlotIndex = initialSlotIndex + vehicle.getOccupiedNumberOfSlots() + SLOT_GAP;
        Integer occupiedSlotIndexLength = occupiedLastSlotIndex - initialSlotIndex;
        vehicle.setVehicleId(initialSlotIndex);
        return Slot
                .builder()
                .occupiedInitialSlotIndex(initialSlotIndex)
                .occupiedLastSlotIndex(occupiedLastSlotIndex)
                .occupiedIndexLength(occupiedSlotIndexLength)
                .vehicle(vehicle)
                .build();
    }
}
