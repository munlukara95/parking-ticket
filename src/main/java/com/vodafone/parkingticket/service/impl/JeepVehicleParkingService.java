package com.vodafone.parkingticket.service.impl;

import com.vodafone.parkingticket.data.Slot;
import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.dto.park.ParkingVehicleResponseDto;
import com.vodafone.parkingticket.service.GarageManagingService;
import com.vodafone.parkingticket.service.VehicleParkingService;

public class JeepVehicleParkingService extends VehicleParkingService {

    @Override
    public ParkingVehicleResponseDto parkingVehicle(Vehicle vehicle) {
        final Integer CAR_OCCUPIED_SLOT = 2;
        vehicle.setOccupiedNumberOfSlots(CAR_OCCUPIED_SLOT);
        Slot slot = null;
        long stamp = super.stampedLock.writeLock();
        try{
            slot = GarageManagingService.parkingVehicle(vehicle);
        }finally {
            super.stampedLock.unlockWrite(stamp);
        }

        return super.slotMapper.toParkingVehicleResponseDto(slot);
    }
}
