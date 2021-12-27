package com.vodafone.parkingticket.controller;

import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.dto.VehicleStatusResponseDto;
import com.vodafone.parkingticket.dto.park.ParkingVehicleRequestDto;
import com.vodafone.parkingticket.dto.park.ParkingVehicleResponseDto;
import com.vodafone.parkingticket.mapper.VehicleMapper;
import com.vodafone.parkingticket.service.VehicleParkingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import java.util.List;

@RestController
@RequestMapping("/parking")
@RequestScope
@RequiredArgsConstructor
public class ParkingController {

    private final VehicleParkingService vehicleParkingService;
    private final VehicleMapper vehicleMapper;

    @PostMapping("/park")
    public ResponseEntity<ParkingVehicleResponseDto> parkingVehicle(@RequestBody ParkingVehicleRequestDto parkingVehicleRequestDto){
        VehicleParkingService vehicleParkingServiceByVehicleType = VehicleParkingService.instanceOf(parkingVehicleRequestDto.getVehicleType());
        Vehicle vehicle = vehicleMapper.toVehicle(parkingVehicleRequestDto);
        ParkingVehicleResponseDto parkingVehicleResponseDto = vehicleParkingServiceByVehicleType.parkingVehicle(vehicle);
        return ResponseEntity.ok(parkingVehicleResponseDto);
    }

    @GetMapping("/status")
    public ResponseEntity<List<VehicleStatusResponseDto>> getStatusVehicles(){
        List<VehicleStatusResponseDto> vehicleStatusResponseDtos = vehicleParkingService.getStatus();
        return ResponseEntity.ok(vehicleStatusResponseDtos);
    }

    @DeleteMapping("/leave/{vehicleId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void leavingVehicle(@PathVariable Integer vehicleId){
        vehicleParkingService.leavingVehicle(vehicleId);
    }
}
