package com.vodafone.parkingticket.dto.park;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ParkingVehicleResponseDto {
    private Integer vehicleId;
    private List<Integer> occupiedSlots = new ArrayList<>();
}
