package com.vodafone.parkingticket.dto.park;

import com.vodafone.parkingticket.annotation.EnumNamePattern;
import com.vodafone.parkingticket.type.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParkingVehicleRequestDto {
    @NotEmpty(message = "Plate must be required")
    @Pattern(regexp = "[0-9]{2}-[A-Z]{1,3}-[0-9]{2,4}", message = "Plate pattern must be DD-SSS-DDDD")
    private String plate;

    @NotEmpty(message = "ColorOfVehicle must be required")
    private String colorOfVehicle;

    @NotNull(message = "VehicleType must be required")
    @EnumNamePattern(regexp = "CAR|JEEP|TRUCK")
    private VehicleType vehicleType;
}
