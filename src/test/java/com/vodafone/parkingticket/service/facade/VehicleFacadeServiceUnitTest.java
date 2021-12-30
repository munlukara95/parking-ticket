package com.vodafone.parkingticket.service.facade;

import com.vodafone.parkingticket.data.Slot;
import com.vodafone.parkingticket.data.Vehicle;
import com.vodafone.parkingticket.dto.VehicleStatusResponseDto;
import com.vodafone.parkingticket.dto.park.ParkingVehicleRequestDto;
import com.vodafone.parkingticket.dto.park.ParkingVehicleResponseDto;
import com.vodafone.parkingticket.mapper.SlotMapper;
import com.vodafone.parkingticket.mapper.SlotMapperImpl;
import com.vodafone.parkingticket.mapper.VehicleMapper;
import com.vodafone.parkingticket.service.GarageManagingService;
import com.vodafone.parkingticket.service.impl.CarVehicleParkingService;
import com.vodafone.parkingticket.service.impl.JeepVehicleParkingService;
import com.vodafone.parkingticket.service.impl.TruckVehicleParkingService;
import com.vodafone.parkingticket.type.VehicleType;
import com.vodafone.parkingticket.util.MockDataUtil;
import org.apache.tomcat.util.threads.ThreadPoolExecutor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.StampedLock;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Tests for VehicleFacadeService Unit Test")
class VehicleFacadeServiceUnitTest{

    VehicleFacadeService vehicleFacadeService;

    @Mock
    private SlotMapper slotMapper;


    @Mock
    private VehicleMapper vehicleMapper;

    @Mock
    private StampedLock stampedLock;

    private ExecutorService executorService;

    private CountDownLatch countDownLatch;

    private final Integer THREAD_NUMBER = 2;

    @BeforeEach
    void setUp(){
        vehicleFacadeService = new VehicleFacadeService(slotMapper, stampedLock, vehicleMapper);
        executorService = Executors.newFixedThreadPool(2);
        countDownLatch = new CountDownLatch(2);
    }

    @AfterEach
    void tearDown(){
        countDownLatch = null;
        executorService.shutdown();
        GarageManagingService.forceAllVehicleLeaveFromGarage();
        vehicleFacadeService = null;
    }

    @Test
    void givenValidVehicleId_whenMultiRequestIncoming_thenLeavingVehicleSucceed() throws InterruptedException{
        // Arrange
        Vehicle firstVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.CAR);
        Vehicle secondVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.JEEP);

        Slot firstSlot = GarageManagingService.parkingVehicle(firstVehicle);
        Slot secondSlot = GarageManagingService.parkingVehicle(secondVehicle);

        List<VehicleStatusResponseDto> vehicleStatusResponseDtos = MockDataUtil.createMockVehicleStatusResponseDtoList(Arrays.asList(firstSlot, secondSlot));

        when(stampedLock.writeLock()).thenCallRealMethod();
        doCallRealMethod().when(stampedLock).unlockWrite(anyLong());

        // Act
        for(int i = 0; i < THREAD_NUMBER; i++) {
            executorService.execute(() -> {
                long threadId = Thread.currentThread().getId()%THREAD_NUMBER +1;
                if(threadId % 2 == 0){
                    vehicleFacadeService.leavingVehicle(vehicleStatusResponseDtos.get(0).getVehicleId());
                } else {
                    vehicleFacadeService.leavingVehicle(vehicleStatusResponseDtos.get(1).getVehicleId());
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        List<Slot> slots = GarageManagingService.getStatusOfSlots();
        // Assert
        Assertions.assertNotNull(slots);
        Assertions.assertEquals(0, slots.size());
    }

    @Test
    void whenMultiRequestIncoming_thenGettingGarageStatusSucceed() throws InterruptedException{
        // Arrange
        Vehicle firstVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.CAR);
        Vehicle secondVehicle = MockDataUtil.createMockVehicleByVehicleType(VehicleType.JEEP);

        Slot firstSlot = GarageManagingService.parkingVehicle(firstVehicle);
        Slot secondSlot = GarageManagingService.parkingVehicle(secondVehicle);

        List<VehicleStatusResponseDto> vehicleStatusResponseDtos = MockDataUtil.createMockVehicleStatusResponseDtoList(Arrays.asList(firstSlot, secondSlot));
        when(stampedLock.readLock()).thenCallRealMethod();
        doCallRealMethod().when(stampedLock).unlockRead(anyLong());
        when(slotMapper.toVehicleStatusResponseDto(anyList())).thenReturn(vehicleStatusResponseDtos);

        AtomicReference<List<List<VehicleStatusResponseDto>>> vehicleStatusResponseDtosAtomicReference = new AtomicReference<>(new ArrayList<>());
        // Act
        for(int i = 0; i < THREAD_NUMBER; i++) {
            executorService.execute(() -> {
                List<VehicleStatusResponseDto> vehicleStatusResponseDtoList = vehicleFacadeService.getStatus();
                List<List<VehicleStatusResponseDto>> vehicleResponseDtosList = vehicleStatusResponseDtosAtomicReference.get();
                vehicleResponseDtosList.add(vehicleStatusResponseDtoList);
                vehicleStatusResponseDtosAtomicReference.set(vehicleResponseDtosList);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        List<VehicleStatusResponseDto> firstVehicleStatusResponseDto = vehicleStatusResponseDtosAtomicReference.get().get(0);
        List<VehicleStatusResponseDto> secondVehicleStatusResponseDto = vehicleStatusResponseDtosAtomicReference.get().get(1);

        // Assert
        Assertions.assertNotNull(firstVehicleStatusResponseDto);
        Assertions.assertNotNull(secondVehicleStatusResponseDto);
        Assertions.assertSame(firstVehicleStatusResponseDto, secondVehicleStatusResponseDto);
        Assertions.assertEquals(firstVehicleStatusResponseDto.size(), secondVehicleStatusResponseDto.size());
    }
}
