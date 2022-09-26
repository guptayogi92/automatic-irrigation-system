package com.automation.irrigationsystem.servicetest;

import com.automation.irrigationsystem.entity.CustomSchedule;
import com.automation.irrigationsystem.entity.Plot;
import com.automation.irrigationsystem.exception.DataNotFoundException;
import com.automation.irrigationsystem.exception.InvalidRequestException;
import com.automation.irrigationsystem.mockdata.CustomScheduleMockData;
import com.automation.irrigationsystem.mockdata.PlotMockData;
import com.automation.irrigationsystem.repository.CustomScheduleRepository;
import com.automation.irrigationsystem.repository.PlotRepository;
import com.automation.irrigationsystem.request.ScheduleRequest;
import com.automation.irrigationsystem.requestmockdata.NewPlotRequestMockData;
import com.automation.irrigationsystem.requestmockdata.ScheduleRequestMockData;
import com.automation.irrigationsystem.response.NewScheduleResponse;
import com.automation.irrigationsystem.services.ScheduleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ScheduleServiceTest {

    @InjectMocks
    ScheduleService scheduleService;

    @Mock
    PlotRepository plotRepository;

    @Mock
    CustomScheduleRepository customScheduleRepository;

    @Nested
    @DisplayName("Add Schedule test cases")
    @MockitoSettings(strictness = Strictness.LENIENT) //Prevent to throw UnnecessaryStubbingException
    class addScheduleTestCases {

        @BeforeEach
        void setUp() {

            when(plotRepository.findByCustomerIdAndIdAndActiveFlg(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE))
                    .thenReturn(PlotMockData.getPlot("1", "1", NewPlotRequestMockData.getNewPlotRequestCustomSched()));

            when(plotRepository.findByCustomerIdAndIdAndActiveFlg(BigDecimal.ONE, BigDecimal.valueOf(2), BigDecimal.ONE))
                    .thenReturn(null);

            when(customScheduleRepository.save(Mockito.any(CustomSchedule.class))).thenReturn(CustomScheduleMockData.getCustomSchedule());

            Plot plot = PlotMockData.getPlot("1", "1", NewPlotRequestMockData.getNewPlotRequestCustomSched());
            when(plotRepository.save(Mockito.any(Plot.class))).thenReturn(plot);
        }

        @Test
        @DisplayName("When Customer id is empty")
        void custIdIsNull() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(InvalidRequestException.class, () -> scheduleService.addNewCustomSchedule("", "1", req));
        }

        @Test
        @DisplayName("When Plot id is empty")
        void plotIdIsNull() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(InvalidRequestException.class, () -> scheduleService.addNewCustomSchedule("1", "", req));
        }

        @Test
        @DisplayName("When Customer id is Invalid")
        void custIdIsInvalid() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(InvalidRequestException.class, () -> scheduleService.addNewCustomSchedule("A", "1", req));
        }

        @Test
        @DisplayName("When Plot id is Invalid")
        void plotIdIsInvalid() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(InvalidRequestException.class, () -> scheduleService.addNewCustomSchedule("1", "A", req));
        }

        @Test
        @DisplayName("When hours to irrigate is null")
        void hoursToIrrigateNull() {
            ScheduleRequest request = ScheduleRequestMockData.getSchData();
            request.setHoursToIrrigate(null);
            assertThrows(InvalidRequestException.class, () -> scheduleService.addNewCustomSchedule("1", "2", request));
        }

        @Test
        @DisplayName("When Plot Not found")
        void plotNotFound() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(DataNotFoundException.class, () -> scheduleService.addNewCustomSchedule("1", "2", req));
        }

        @Test
        @DisplayName("Schedule added successfully")
        void scheduleAdded() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertInstanceOf(NewScheduleResponse.class, scheduleService.addNewCustomSchedule("1", "1", req));
        }
    }


    @Nested
    @DisplayName("update schedule test cases")
    @MockitoSettings(strictness = Strictness.LENIENT) //Prevent to throw UnnecessaryStubbingException
    class updateScheduleTestCases {

        @BeforeEach
        void setUp() {

            when(customScheduleRepository.findByIdAndCustomerIdAndActiveFlg(
                    BigDecimal.valueOf(2), BigDecimal.ONE, BigDecimal.ONE)).thenReturn(
                    null);

            when(customScheduleRepository.findByIdAndCustomerIdAndActiveFlg(
                    BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE)).thenReturn(
                            CustomScheduleMockData.getCustomSchedule());

            when(plotRepository.findByIdAndCustomerIdAndCustomSchedIdAndActiveFlg(
                    BigDecimal.valueOf(2) ,BigDecimal.ONE
                    , BigDecimal.ONE, BigDecimal.ONE)).thenReturn(
                    null);

            when(plotRepository.findByIdAndCustomerIdAndCustomSchedIdAndActiveFlg(
                    BigDecimal.ONE ,BigDecimal.ONE
                    , BigDecimal.ONE, BigDecimal.ONE)).thenReturn(
                            PlotMockData.getPlot("1", "1", NewPlotRequestMockData.getNewPlotRequestCustomSched()));

            when(customScheduleRepository.save(Mockito.any(CustomSchedule.class))).thenReturn(CustomScheduleMockData.getCustomSchedule());
        }

        @Test
        @DisplayName("When Customer id is empty")
        void custIdIsNull() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(InvalidRequestException.class, () -> scheduleService.updateCustomSchedule("", "1", "1", req));
        }

        @Test
        @DisplayName("When Plot id is empty")
        void plotIdIsNull() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(InvalidRequestException.class, () -> scheduleService.updateCustomSchedule("1", "", "1", req));
        }

        @Test
        @DisplayName("When schedule id is empty")
        void schedIdIsNull() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(InvalidRequestException.class, () -> scheduleService.updateCustomSchedule("1", "1", "", req));
        }

        @Test
        @DisplayName("When Customer id is Invalid")
        void custIdIsInvalid() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(InvalidRequestException.class, () -> scheduleService.updateCustomSchedule("A", "1", "1", req));
        }

        @Test
        @DisplayName("When Plot id is Invalid")
        void plotIdIsInvalid() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(InvalidRequestException.class, () -> scheduleService.updateCustomSchedule("1", "A", "1", req));
        }

        @Test
        @DisplayName("When Schedule id is Invalid")
        void schedIdIsInvalid() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(InvalidRequestException.class, () -> scheduleService.updateCustomSchedule("1", "1", "A", req));
        }

        @Test
        @DisplayName("When Schedule not found for the requested id")
        void schedNotAvailable() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(DataNotFoundException.class, () -> scheduleService.updateCustomSchedule("1", "1", "2", req));
        }

        @Test
        @DisplayName("When Plot not found for the requested id")
        void plotNotAvailable() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            assertThrows(DataNotFoundException.class, () -> scheduleService.updateCustomSchedule("1", "2", "1", req));
        }

        @Test
        @DisplayName("When schedule updated successfully")
        void schedUpdatedSuccessfully() {
            ScheduleRequest req = ScheduleRequestMockData.getSchData();
            scheduleService.updateCustomSchedule("1", "1", "1", req);
        }
    }


    @Nested
    @DisplayName("Delete schedule test cases")
    @MockitoSettings(strictness = Strictness.LENIENT) //Prevent to throw UnnecessaryStubbingException
    class deleteScheduleTestCases {

        @BeforeEach
        void setUp() {

            when(customScheduleRepository.findByIdAndCustomerIdAndActiveFlg(
                    BigDecimal.valueOf(2), BigDecimal.ONE, BigDecimal.ONE)).thenReturn(
                    null);

            when(customScheduleRepository.findByIdAndCustomerIdAndActiveFlg(
                    BigDecimal.ONE, BigDecimal.valueOf(2), BigDecimal.ONE)).thenReturn(
                    CustomScheduleMockData.getCustomSchedule());

            when(customScheduleRepository.findByIdAndCustomerIdAndActiveFlg(
                    BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE)).thenReturn(
                    CustomScheduleMockData.getCustomSchedule());

            when(plotRepository.findByCustomerIdAndCustomSchedIdAndActiveFlg(
                    BigDecimal.valueOf(2), BigDecimal.ONE, BigDecimal.ONE)).thenReturn(Collections.emptyList());

            when(plotRepository.findByCustomerIdAndCustomSchedIdAndActiveFlg(
                    BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE)).thenReturn(PlotMockData.customPlotListWithSched());

            CustomSchedule cs = CustomScheduleMockData.getCustomSchedule();
            cs.setActiveFlg(BigDecimal.ZERO);
            when(customScheduleRepository.save(Mockito.any(CustomSchedule.class))).thenReturn(cs);
        }

        @Test
        @DisplayName("When Customer id is empty")
        void custIdIsNull() {
            assertThrows(InvalidRequestException.class, () -> scheduleService.deleteCustomSchedule("", "1", "1"));
        }

        @Test
        @DisplayName("When Plot id is empty")
        void plotIdIsNull() {
            assertThrows(InvalidRequestException.class, () -> scheduleService.deleteCustomSchedule("1", "", "1"));
        }

        @Test
        @DisplayName("When schedule id is empty")
        void schedIdIsNull() {
            assertThrows(InvalidRequestException.class, () -> scheduleService.deleteCustomSchedule("1", "1", ""));
        }

        @Test
        @DisplayName("When Customer id is Invalid")
        void custIdIsInvalid() {
            assertThrows(InvalidRequestException.class, () -> scheduleService.deleteCustomSchedule("A", "1", "1"));
        }

        @Test
        @DisplayName("When Plot id is Invalid")
        void plotIdIsInvalid() {
            assertThrows(InvalidRequestException.class, () -> scheduleService.deleteCustomSchedule("1", "A", "1"));
        }

        @Test
        @DisplayName("When Schedule id is Invalid")
        void schedIdIsInvalid() {
            assertThrows(InvalidRequestException.class, () -> scheduleService.deleteCustomSchedule("1", "1", "A"));
        }

        @Test
        @DisplayName("When Schedule not found for the requested id")
        void schedNotAvailable() {
            assertThrows(DataNotFoundException.class, () -> scheduleService.deleteCustomSchedule("1", "1", "2"));
        }

        @Test
        @DisplayName("When plot not found for the requested id")
        void plotNotAvailable() {
            assertThrows(DataNotFoundException.class, () -> scheduleService.deleteCustomSchedule("2", "1", "1"));
        }

        @Test
        @DisplayName("schedule deleted successfully.")
        void scheduleDeletedSuccess() {
            scheduleService.deleteCustomSchedule("1", "1", "1");
        }
    }
}
