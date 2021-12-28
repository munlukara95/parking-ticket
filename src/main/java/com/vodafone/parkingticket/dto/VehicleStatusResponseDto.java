package com.vodafone.parkingticket.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleStatusResponseDto {
    private Integer vehicleId;
    private String plate;
    private String colorOfVehicle;
    private List<Integer> occupiedSlots = new ArrayList<>();
}
