package com.vodafone.parkingticket.service;

import com.vodafone.parkingticket.data.Slot;
import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.exception.GarageOperationException;
import com.vodafone.parkingticket.exception.ResultStatus;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        SLOTS.remove(vehicleId);
    }

    public static List<Slot> getStatusOfSlots(){
        return SLOTS;
    }

    private static Integer findPotentialFitSlot(Vehicle vehicle){
        Integer firstIndex = 0;

        if(SLOTS.size() != 1) {
            for (int index = 0; index < SLOTS.size() - 1; index++) {
                Slot currentSlot = SLOTS.get(index);
                Slot nextSlot = SLOTS.get(index + 1);

                if(Objects.nonNull(nextSlot)) {
                    int distance = findDistanceBetweenSlots(currentSlot, nextSlot);

                    if (distance == 0 || distance != vehicle.getOccupiedNumberOfSlots() + SLOT_GAP) {
                        continue;
                    } else if (distance == vehicle.getOccupiedNumberOfSlots() + SLOT_GAP) {
                        firstIndex = currentSlot.getOccupiedLastSlotIndex();
                        break;
                    }
                } else {
                    if(currentSlot.getOccupiedLastSlotIndex() == 10) throw new GarageOperationException(ResultStatus.GARAGE_FULL.getStatus());
                    firstIndex = currentSlot.getOccupiedLastSlotIndex();
                    break;
                }
            }
        } else {
            firstIndex = SLOTS.get(0).getOccupiedLastSlotIndex();
        }

        return firstIndex;
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
