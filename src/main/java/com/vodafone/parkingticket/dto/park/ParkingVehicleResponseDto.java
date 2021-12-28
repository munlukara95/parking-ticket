package com.vodafone.parkingticket.dto.park;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingVehicleResponseDto {
    private Integer vehicleId;
    private List<Integer> occupiedSlots = new ArrayList<>();
}
