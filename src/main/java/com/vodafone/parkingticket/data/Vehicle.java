package com.vodafone.parkingticket.data;

import com.vodafone.parkingticket.type.VehicleType;
import lombok.Data;

@Data
public class Vehicle {
    private Integer vehicleId;
    private String plate;
    private String colorOfVehicle;
    private VehicleType vehicleType;
    private Integer occupiedNumberOfSlots;
}
