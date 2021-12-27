package com.vodafone.parkingticket.service;

import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.dto.VehicleStatusResponseDto;
import com.vodafone.parkingticket.dto.park.ParkingVehicleResponseDto;
import com.vodafone.parkingticket.exception.ResultStatus;
import com.vodafone.parkingticket.exception.NotFoundException;
import com.vodafone.parkingticket.mapper.SlotMapper;
import com.vodafone.parkingticket.service.impl.CarVehicleParkingService;
import com.vodafone.parkingticket.service.impl.JeepVehicleParkingService;
import com.vodafone.parkingticket.service.impl.TruckVehicleParkingService;
import com.vodafone.parkingticket.type.VehicleType;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.StampedLock;

@Service
@NoArgsConstructor
@AllArgsConstructor
public abstract class VehicleParkingService {

    @Autowired
    public SlotMapper slotMapper;

    @Autowired
    public StampedLock stampedLock;

    public abstract ParkingVehicleResponseDto parkingVehicle(Vehicle vehicle);

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

    public static VehicleParkingService instanceOf(VehicleType vehicleType){
        return switch (vehicleType){
            case CAR -> new CarVehicleParkingService();
            case JEEP -> new JeepVehicleParkingService();
            case TRUCK -> new TruckVehicleParkingService();
            default -> throw new NotFoundException(ResultStatus.VEHICLE_TYPE_NOT_FOUND.getStatus());
        };
    }
}
