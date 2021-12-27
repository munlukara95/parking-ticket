package com.vodafone.parkingticket.mapper;

import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.dto.park.ParkingVehicleRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    Vehicle toVehicle(ParkingVehicleRequestDto parkingVehicleRequestDto);
}
