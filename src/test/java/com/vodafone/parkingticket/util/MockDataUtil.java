package com.vodafone.parkingticket.util;

import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.type.VehicleType;

public final class MockDataUtil {

    public static Vehicle createMockVehicleByVehicleType(VehicleType vehicleType){
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate("34-VF-1969");
        vehicle.setColorOfVehicle("NightBlue");
        vehicle.setVehicleType(vehicleType);
        switch (vehicleType){
            case CAR -> vehicle.setOccupiedNumberOfSlots(1);
            case JEEP -> vehicle.setOccupiedNumberOfSlots(2);
            case TRUCK -> vehicle.setOccupiedNumberOfSlots(4);
        }
        return vehicle;
    }
}
