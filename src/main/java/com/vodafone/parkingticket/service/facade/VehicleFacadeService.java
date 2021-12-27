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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.locks.StampedLock;

@Component
@RequiredArgsConstructor
public class VehicleFacadeService extends AbstractFacadeService {

    private final SlotMapper slotMapper;
    private final StampedLock stampedLock;
    private final VehicleMapper vehicleMapper;

    public void leavingVehicle(Integer vehicleId){
        long stamp = stampedLock.writeLock();
        try{
            GarageManagingService.leavingVehicle(vehicleId);
        }finally{
            stampedLock.unlockWrite(stamp);
        }
    }

    public List<VehicleStatusResponseDto> getStatus(){
        List<VehicleStatusResponseDto> statusDtos = null;
        long stamp = stampedLock.tryOptimisticRead();
        try{
            statusDtos = slotMapper.toVehicleStatusResponseDto(GarageManagingService.getStatusOfSlots());
        }finally {
            stampedLock.unlock(stamp);
        }
        return statusDtos;
    }

    public ParkingVehicleResponseDto parkingVehicle(ParkingVehicleRequestDto parkingVehicleRequestDto){
        Vehicle vehicle = vehicleMapper.toVehicle(parkingVehicleRequestDto);
        VehicleParkingService vehicleParkingServiceByVehicleType = instanceOf(parkingVehicleRequestDto.getVehicleType());
        return vehicleParkingServiceByVehicleType.parkingVehicle(vehicle);
    }
}
