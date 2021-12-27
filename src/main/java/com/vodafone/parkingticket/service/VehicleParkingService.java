package com.vodafone.parkingticket.service;

import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.dto.park.ParkingVehicleResponseDto;

public interface VehicleParkingService {
    public ParkingVehicleResponseDto parkingVehicle(Vehicle vehicle);
}
