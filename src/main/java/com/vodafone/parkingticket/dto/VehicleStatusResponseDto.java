package com.vodafone.parkingticket.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class VehicleStatusResponseDto {
    private Integer vehicleId;
    private String plate;
    private String colorOfVehicle;
    private List<Integer> occupiedSlots = new ArrayList<>();
}
