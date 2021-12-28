package com.vodafone.parkingticket.dto.park;

import com.vodafone.parkingticket.type.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingVehicleRequestDto {
    @NotNull
    private String plate;
    @NotNull
    private String colorOfVehicle;
    @NotNull
    private VehicleType vehicleType;
}
