package com.vodafone.parkingticket.service.impl;

import com.vodafone.parkingticket.data.Slot;
import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.mapper.SlotMapper;
import com.vodafone.parkingticket.service.GarageManagingService;
import com.vodafone.parkingticket.type.VehicleType;
import com.vodafone.parkingticket.util.MockDataUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.StampedLock;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for CarVehicleParkingService Unit Test")
public class CarVehicleParkingServiceUnitTest {

    CarVehicleParkingService carVehicleParkingService;

    @Mock
    private SlotMapper slotMapper;

    @Mock
    private StampedLock stampedLock;

    private ExecutorService executorService;

    private CountDownLatch countDownLatch;

    private final Integer THREAD_NUMBER = 2;

    @BeforeEach
    void setUp(){
        carVehicleParkingService = new CarVehicleParkingService(stampedLock, slotMapper);
        executorService = Executors.newFixedThreadPool(THREAD_NUMBER);
        countDownLatch = new CountDownLatch(THREAD_NUMBER);
    }

    @AfterEach
    void tearDown(){
        countDownLatch = null;
        executorService.shutdown();
        GarageManagingService.forceAllVehicleLeaveFromGarage();
        carVehicleParkingService = null;
    }

    @Test
    void givenValidVehicle_whenMultiRequestIncoming_thenSucceed() throws InterruptedException{
        // Arrange
        when(stampedLock.writeLock()).thenCallRealMethod();
        doCallRealMethod().when(stampedLock).unlockWrite(anyLong());
        // Act
        for(int i = 0; i < THREAD_NUMBER; i++) {
            executorService.execute(() -> {
                Vehicle vehicle = MockDataUtil.createMockVehicleByVehicleTypeWithoutOccupiedNumberOfSlots(VehicleType.CAR);
                carVehicleParkingService.parkingVehicle(vehicle);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        List<Slot> slots = GarageManagingService.getStatusOfSlots();
        Slot firstSlot = slots.get(0);
        Slot secondSlot = slots.get(1);
        // Assert
        Assertions.assertNotNull(firstSlot);
        Assertions.assertNotNull(secondSlot);
        Assertions.assertEquals(firstSlot.getVehicle().getOccupiedNumberOfSlots(), secondSlot.getVehicle().getOccupiedNumberOfSlots());
        Assertions.assertNotEquals(firstSlot.getVehicle().getVehicleId(), secondSlot.getVehicle().getVehicleId());
        Assertions.assertNotEquals(firstSlot.getOccupiedInitialSlotIndex(), secondSlot.getOccupiedInitialSlotIndex());
    }
}
