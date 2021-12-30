package com.vodafone.parkingticket.util;

import com.vodafone.parkingticket.data.Slot;
import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.dto.VehicleStatusResponseDto;
import com.vodafone.parkingticket.dto.park.ParkingVehicleRequestDto;
import com.vodafone.parkingticket.type.VehicleType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class MockDataUtil {

    private static final String mockPlate = "34-VF-1969";
    private static final String mockColorOfVehicle = "NightBlue";

    public static Vehicle createMockVehicleByVehicleType(VehicleType vehicleType){
        Vehicle vehicle = createMockVehicleByVehicleTypeWithoutOccupiedNumberOfSlots(vehicleType);
        switch (vehicleType){
            case CAR -> vehicle.setOccupiedNumberOfSlots(1);
            case JEEP -> vehicle.setOccupiedNumberOfSlots(2);
            case TRUCK -> vehicle.setOccupiedNumberOfSlots(4);
        }
        return vehicle;
    }

    public static Vehicle createMockVehicleByVehicleTypeWithoutOccupiedNumberOfSlots(VehicleType vehicleType){
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate(mockPlate);
        vehicle.setColorOfVehicle(mockColorOfVehicle);
        vehicle.setVehicleType(vehicleType);
        return vehicle;
    }

    public static ParkingVehicleRequestDto createMockParkingVehicleRequestDtoByVehicleType(VehicleType vehicleType){
        ParkingVehicleRequestDto parkingVehicleRequestDto = new ParkingVehicleRequestDto();
        parkingVehicleRequestDto.setVehicleType(vehicleType);
        parkingVehicleRequestDto.setPlate(mockPlate);
        parkingVehicleRequestDto.setColorOfVehicle(mockColorOfVehicle);
        return parkingVehicleRequestDto;
    }

    public static List<VehicleStatusResponseDto> createMockVehicleStatusResponseDtoList(List<Slot> slots){
        List<VehicleStatusResponseDto> vehicleStatusResponseDtos = new ArrayList<>();
        for(Slot slot : slots){
            VehicleStatusResponseDto vehicleStatusResponseDto = new VehicleStatusResponseDto();
            vehicleStatusResponseDto.setVehicleId(slot.getVehicle().getVehicleId());
            vehicleStatusResponseDto.setOccupiedSlots(IntStream.range(slot.getOccupiedInitialSlotIndex(), slot.getOccupiedLastSlotIndex())
                    .boxed().collect(Collectors.toList()));
            vehicleStatusResponseDto.setColorOfVehicle(slot.getVehicle().getColorOfVehicle());
            vehicleStatusResponseDto.setPlate(slot.getVehicle().getPlate());
            vehicleStatusResponseDtos.add(vehicleStatusResponseDto);
        }

        return vehicleStatusResponseDtos;
    }
}
