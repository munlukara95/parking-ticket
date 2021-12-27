package com.vodafone.parkingticket.service.facade;

import com.vodafone.parkingticket.exception.NotFoundException;
import com.vodafone.parkingticket.exception.ResultStatus;
import com.vodafone.parkingticket.service.VehicleParkingService;
import com.vodafone.parkingticket.service.impl.CarVehicleParkingService;
import com.vodafone.parkingticket.service.impl.JeepVehicleParkingService;
import com.vodafone.parkingticket.service.impl.TruckVehicleParkingService;
import com.vodafone.parkingticket.type.VehicleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public abstract class AbstractFacadeService {

    @Autowired
    private ApplicationContext context;

    protected VehicleParkingService instanceOf(VehicleType vehicleType){
        return switch (vehicleType){
            case CAR ->  context.getBean(CarVehicleParkingService.class);
            case JEEP -> context.getBean(JeepVehicleParkingService.class);
            case TRUCK -> context.getBean(TruckVehicleParkingService.class);
            default -> throw new NotFoundException(ResultStatus.VEHICLE_TYPE_NOT_FOUND.getStatus());
        };
    }
}
