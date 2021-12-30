package com.vodafone.parkingticket.service.facade;

import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.dto.VehicleStatusResponseDto;
import com.vodafone.parkingticket.dto.park.ParkingVehicleRequestDto;
import com.vodafone.parkingticket.dto.park.ParkingVehicleResponseDto;
import com.vodafone.parkingticket.mapper.SlotMapper;
import com.vodafone.parkingticket.mapper.VehicleMapper;
import com.vodafone.parkingticket.service.GarageManagingService;
import com.vodafone.parkingticket.service.VehicleParkingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.locks.StampedLock;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
@Slf4j
public class VehicleFacadeService extends AbstractFacadeService {

    private final SlotMapper slotMapper;
    private final StampedLock stampedLock;
    private final VehicleMapper vehicleMapper;

    public void leavingVehicle(Integer vehicleId){
        log.info("Thread running id: {}, name: {}, state: {}",Thread.currentThread().getId(), Thread.currentThread().getName(), Thread.currentThread().getState());
        long stamp = stampedLock.writeLock();
        log.info("Thread running id: {}, name: {}, state: {}",Thread.currentThread().getId(), Thread.currentThread().getName(), Thread.currentThread().getState());
        try{
            GarageManagingService.leavingVehicle(vehicleId);
            log.info("Thread running id: {}, name: {}, state: {}",Thread.currentThread().getId(), Thread.currentThread().getName(), Thread.currentThread().getState());
        }finally{
            stampedLock.unlockWrite(stamp);
        }
        log.info("Leaved vehicleId: {}", vehicleId);
    }

    public List<VehicleStatusResponseDto> getStatus(){
        log.info("Thread running id: {}, name: {}, state: {}",Thread.currentThread().getId(), Thread.currentThread().getName(), Thread.currentThread().getState());
        List<VehicleStatusResponseDto> statusDtos = null;
        long stamp = stampedLock.readLock();
        log.info("Thread running id: {}, name: {}, state: {}",Thread.currentThread().getId(), Thread.currentThread().getName(), Thread.currentThread().getState());
        try {
            statusDtos = slotMapper.toVehicleStatusResponseDto(GarageManagingService.getStatusOfSlots());
            log.info("Thread running id: {}, name: {}, state: {}",Thread.currentThread().getId(), Thread.currentThread().getName(), Thread.currentThread().getState());
        } finally {
            stampedLock.unlockRead(stamp);
        }
        log.info("Garage status : {}", statusDtos);
        return statusDtos;
    }

    public ParkingVehicleResponseDto parkingVehicle(ParkingVehicleRequestDto parkingVehicleRequestDto){
        Vehicle vehicle = vehicleMapper.toVehicle(parkingVehicleRequestDto);
        VehicleParkingService vehicleParkingServiceByVehicleType = instanceOf(parkingVehicleRequestDto.getVehicleType());
        return vehicleParkingServiceByVehicleType.parkingVehicle(vehicle);
    }
}
