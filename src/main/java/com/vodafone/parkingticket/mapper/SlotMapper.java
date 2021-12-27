package com.vodafone.parkingticket.mapper;

import com.vodafone.parkingticket.data.Slot;
import com.vodafone.parkingticket.dto.VehicleStatusResponseDto;
import com.vodafone.parkingticket.dto.park.ParkingVehicleResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring")
public interface SlotMapper {

    List<VehicleStatusResponseDto> toVehicleStatusResponseDto(List<Slot> slots);

    default VehicleStatusResponseDto toVehicleStatusResponseDto(Slot slot){
        VehicleStatusResponseDto vehicleStatusResponseDto = new VehicleStatusResponseDto();
        vehicleStatusResponseDto.setColorOfVehicle(slot.getVehicle().getColorOfVehicle());
        vehicleStatusResponseDto.setPlate(slot.getVehicle().getPlate());
        List<Integer> occupiedSlots = getOccupiedSlots(slot.getOccupiedInitialSlotIndex(), slot.getOccupiedLastSlotIndex());
        vehicleStatusResponseDto.setOccupiedSlots(occupiedSlots);
        return vehicleStatusResponseDto;
    }

    default List<Integer> getOccupiedSlots(Integer initial, Integer last){
        return IntStream.rangeClosed(initial, last)
                .boxed().collect(Collectors.toList());
    }

    @Mappings({
            @Mapping(target = "occupiedSlots", expression = "java(getOccupiedSlots(slot.getOccupiedInitialSlotIndex(), slot.getOccupiedLastSlotIndex()))"),
            @Mapping(target = "vehicleId", source = "vehicle.vehicleId")
    })
    ParkingVehicleResponseDto toParkingVehicleResponseDto(Slot slot);

}
