package com.vodafone.parkingticket.service.impl;

import com.vodafone.parkingticket.data.Slot;
import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.dto.park.ParkingVehicleResponseDto;
import com.vodafone.parkingticket.mapper.SlotMapper;
import com.vodafone.parkingticket.service.GarageManagingService;
import com.vodafone.parkingticket.service.VehicleParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.locks.StampedLock;

@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class CarVehicleParkingService implements VehicleParkingService {

    private final StampedLock stampedLock;
    private final SlotMapper slotMapper;

    @Override
    public ParkingVehicleResponseDto parkingVehicle(Vehicle vehicle) {
        final Integer CAR_OCCUPIED_SLOT = 1;
        vehicle.setOccupiedNumberOfSlots(CAR_OCCUPIED_SLOT);
        Slot slot = null;
        long stamp = stampedLock.writeLock();
        try{
            slot = GarageManagingService.parkingVehicle(vehicle);
        }finally {
            stampedLock.unlockWrite(stamp);
        }

        return slotMapper.toParkingVehicleResponseDto(slot);
    }
}
