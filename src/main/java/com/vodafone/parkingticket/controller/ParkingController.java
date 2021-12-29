package com.vodafone.parkingticket.controller;

import com.vodafone.parkingticket.dto.VehicleStatusResponseDto;
import com.vodafone.parkingticket.dto.park.ParkingVehicleRequestDto;
import com.vodafone.parkingticket.dto.park.ParkingVehicleResponseDto;
import com.vodafone.parkingticket.service.facade.VehicleFacadeService;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;

import javax.validation.Valid;
import java.util.List;

import static com.vodafone.parkingticket.constant.ApiConstants.API_PREFIX;

@RestController
@RequestMapping(API_PREFIX + "/parking")
@RequestScope
@RequiredArgsConstructor
@Validated
public class ParkingController {

    private final VehicleFacadeService vehicleFacadeService;

    @PostMapping("/park")
    public ResponseEntity<ParkingVehicleResponseDto> parkingVehicle(@Valid @RequestBody ParkingVehicleRequestDto parkingVehicleRequestDto){
        ParkingVehicleResponseDto parkingVehicleResponseDto = vehicleFacadeService.parkingVehicle(parkingVehicleRequestDto);
        return ResponseEntity.ok(parkingVehicleResponseDto);
    }

    @GetMapping("/status")
    public ResponseEntity<List<VehicleStatusResponseDto>> getStatusVehicles(){
        List<VehicleStatusResponseDto> vehicleStatusResponseDtos = vehicleFacadeService.getStatus();
        return ResponseEntity.ok(vehicleStatusResponseDtos);
    }

    @DeleteMapping("/leave/{vehicleId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void leavingVehicle(@PathVariable("vehicleId") @Range(min = 0, max = 9, message = "VehicleId must be between 0 and 9") Integer vehicleId){
        vehicleFacadeService.leavingVehicle(vehicleId);
    }
}
