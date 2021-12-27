package com.vodafone.parkingticket.dto.park;

import com.vodafone.parkingticket.type.VehicleType;
import lombok.Data;
import lombok.NonNull;

@Data
public class ParkingVehicleRequestDto {
    @NonNull
    private String plate;
    @NonNull
    private String colorOfVehicle;
    @NonNull
    private VehicleType vehicleType;
}
