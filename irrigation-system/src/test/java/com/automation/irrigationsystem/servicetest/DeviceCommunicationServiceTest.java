package com.automation.irrigationsystem.servicetest;

import com.automation.irrigationsystem.exception.SensorNotAvailableException;
import com.automation.irrigationsystem.services.DeviceCommunicationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@DisplayName("Communicate with device test cases")
class DeviceCommunicationServiceTest {

    @InjectMocks
    DeviceCommunicationService deviceCommunicationService;

    @Test
    @DisplayName("When device is not responding")
    void deviceNotResponding() {
        assertThrows(SensorNotAvailableException.class, () -> deviceCommunicationService.startCommunication(BigDecimal.ZERO, 2, BigDecimal.ONE));
    }

    @Test
    @DisplayName("When device communicated")
    void deviceResponded() throws SensorNotAvailableException {
        deviceCommunicationService.startCommunication(BigDecimal.ONE, 2, BigDecimal.ONE);
    }

}
