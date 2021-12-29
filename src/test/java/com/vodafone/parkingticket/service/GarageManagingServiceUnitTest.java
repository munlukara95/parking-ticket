package com.vodafone.parkingticket.service;

import com.vodafone.parkingticket.data.Slot;
import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.exception.GarageOperationException;
import com.vodafone.parkingticket.exception.NotFoundException;
import com.vodafone.parkingticket.type.VehicleType;
import com.vodafone.parkingticket.util.MockDataUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for GarageManagingService Unit Test")
class GarageManagingServiceUnitTest{

    @AfterEach
    void tearDown(){
        GarageManagingService.forceAllVehicleLeaveFromGarage();
    }

    @Test
    void givenValidVehicle_whenFirstParkingVehicle_thenParkingSucceed(){
        // Arrange
        Vehicle vehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.CAR);
        Slot slot = GarageManagingService.parkingVehicle(vehicle);
        // Assert
        Assertions.assertNotNull(slot);
        Assertions.assertSame(slot.getVehicle(), vehicle);
        Assertions.assertEquals(slot.getOccupiedInitialSlotIndex(), slot.getVehicle().getVehicleId());
        Assertions.assertEquals(0, slot.getOccupiedInitialSlotIndex());
        Assertions.assertEquals(2, slot.getOccupiedIndexLength());
    }

    @Test
    void givenValidVehicle_whenParkingVehicle_thenParkingSucceedAndExistInGarage(){
        // Arrange
        Vehicle vehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.CAR);
        Slot slot = GarageManagingService.parkingVehicle(vehicle);
        List<Slot> slots = GarageManagingService.getStatusOfSlots();
        // Assert
        Assertions.assertNotNull(slot);
        Assertions.assertNotNull(slots);
        Assertions.assertEquals(1, slots.size());
        Assertions.assertSame(slot, slots.get(0));
    }

    @Test
    void givenValidVehicle_whenSecondParkingVehicle_thenParkingSucceed(){
        // Arrange
        Vehicle firstVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.CAR);
        Vehicle secondVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.JEEP);
        Slot firstVehicleSlot = GarageManagingService.parkingVehicle(firstVehicle);
        Slot secondVehicleSlot = GarageManagingService.parkingVehicle(secondVehicle);
        // Assert
        Assertions.assertNotNull(firstVehicleSlot);
        Assertions.assertNotNull(secondVehicleSlot);
        Assertions.assertSame(firstVehicleSlot.getVehicle(), firstVehicle);
        Assertions.assertSame(secondVehicleSlot.getVehicle(), secondVehicle);
        Assertions.assertEquals(firstVehicleSlot.getOccupiedInitialSlotIndex(), firstVehicleSlot.getVehicle().getVehicleId());
        Assertions.assertEquals(secondVehicleSlot.getOccupiedInitialSlotIndex(), secondVehicleSlot.getVehicle().getVehicleId());
        Assertions.assertEquals(0, firstVehicleSlot.getOccupiedInitialSlotIndex());
        Assertions.assertEquals(2, firstVehicleSlot.getOccupiedIndexLength());
        Assertions.assertEquals(2, firstVehicleSlot.getOccupiedLastSlotIndex());
        Assertions.assertEquals(2, secondVehicleSlot.getOccupiedInitialSlotIndex());
        Assertions.assertEquals(3, secondVehicleSlot.getOccupiedIndexLength());
        Assertions.assertEquals(5, secondVehicleSlot.getOccupiedLastSlotIndex());
    }

    @Test
    void givenValidVehicleId_whenLeavingVehicle_thenLeavingSucceed(){
        // Arrange
        Vehicle vehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.CAR);
        Slot slot = GarageManagingService.parkingVehicle(vehicle);
        GarageManagingService.leavingVehicle(slot.getVehicle().getVehicleId());
        List<Slot> slots = GarageManagingService.getStatusOfSlots();
        // Assert
        Assertions.assertNotNull(slot);
        Assertions.assertSame(slot.getVehicle(), vehicle);
        Assertions.assertEquals(0, slots.size());
    }

    @Test
    void givenValidVehicle_whenBetweenParkingVehicle_thenParkingSucceed(){
        // Arrange
        Vehicle firstVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.CAR);
        Vehicle secondVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.JEEP);
        Vehicle thirdVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.TRUCK);
        Slot firstVehicleSlot = GarageManagingService.parkingVehicle(firstVehicle);
        Slot secondVehicleSlot = GarageManagingService.parkingVehicle(secondVehicle);
        Slot thirdVehicleSlot = GarageManagingService.parkingVehicle(thirdVehicle);
        GarageManagingService.leavingVehicle(secondVehicleSlot.getVehicle().getVehicleId());
        Slot reparkingSecondVehicleSlot = GarageManagingService.parkingVehicle(secondVehicle);
        // Assert
        Assertions.assertNotNull(firstVehicleSlot);
        Assertions.assertNotNull(secondVehicleSlot);
        Assertions.assertNotNull(thirdVehicleSlot);
        Assertions.assertNotNull(reparkingSecondVehicleSlot);
        Assertions.assertEquals(0, firstVehicleSlot.getOccupiedInitialSlotIndex());
        Assertions.assertEquals(2, secondVehicleSlot.getOccupiedInitialSlotIndex());
        Assertions.assertEquals(5, thirdVehicleSlot.getOccupiedInitialSlotIndex());
        Assertions.assertNotSame(secondVehicleSlot, reparkingSecondVehicleSlot);
        Assertions.assertEquals(2, reparkingSecondVehicleSlot.getOccupiedInitialSlotIndex());
    }

    @Test
    void givenValidVehicle_whenParkingFirstPotentialSlot_thenParkingSucceed(){
        // Arrange
        Vehicle firstVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.CAR);
        Vehicle secondVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.JEEP);
        Vehicle thirdVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.TRUCK);
        Slot firstVehicleSlot = GarageManagingService.parkingVehicle(firstVehicle);
        Slot secondVehicleSlot = GarageManagingService.parkingVehicle(secondVehicle);
        Slot thirdVehicleSlot = GarageManagingService.parkingVehicle(thirdVehicle);
        GarageManagingService.leavingVehicle(firstVehicleSlot.getVehicle().getVehicleId());
        Slot reparkingFirstVehicleSlot = GarageManagingService.parkingVehicle(firstVehicle);
        // Assert
        Assertions.assertNotNull(firstVehicleSlot);
        Assertions.assertNotNull(secondVehicleSlot);
        Assertions.assertNotNull(thirdVehicleSlot);
        Assertions.assertNotNull(reparkingFirstVehicleSlot);
        Assertions.assertEquals(0, firstVehicleSlot.getOccupiedInitialSlotIndex());
        Assertions.assertEquals(2, secondVehicleSlot.getOccupiedInitialSlotIndex());
        Assertions.assertEquals(5, thirdVehicleSlot.getOccupiedInitialSlotIndex());
        Assertions.assertNotSame(firstVehicleSlot, reparkingFirstVehicleSlot);
        Assertions.assertEquals(0, reparkingFirstVehicleSlot.getOccupiedInitialSlotIndex());
    }

    @Test
    void givenValidVehicle_whenParkingVehiclesIfIsGarageFull_thenThrowsGarageOperationException(){
        // Arrange
        Vehicle firstVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.TRUCK);
        Vehicle secondVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.TRUCK);
        Vehicle thirdVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.TRUCK);
        GarageManagingService.parkingVehicle(firstVehicle);
        GarageManagingService.parkingVehicle(secondVehicle);
        // Assert
        Assertions.assertThrows(GarageOperationException.class, () -> GarageManagingService.parkingVehicle(thirdVehicle));
    }

    @Test
    void givenValidVehicleId_whenLeavingVehicleIfVehicleNotExistInGarage_thenThrowsNotFoundException(){
        // Arrange
        // Act
        // Assert
        Assertions.assertThrows(NotFoundException.class, () -> GarageManagingService.leavingVehicle(0));
    }
}
