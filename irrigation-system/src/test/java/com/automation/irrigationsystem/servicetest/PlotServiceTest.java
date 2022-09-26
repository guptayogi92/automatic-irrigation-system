package com.automation.irrigationsystem.servicetest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.automation.irrigationsystem.entity.Plot;
import com.automation.irrigationsystem.exception.DataNotFoundException;
import com.automation.irrigationsystem.exception.InvalidRequestException;
import com.automation.irrigationsystem.mockdata.PlotMockData;
import com.automation.irrigationsystem.mockdata.ScheduleMockData;
import com.automation.irrigationsystem.repository.CustomScheduleRepository;
import com.automation.irrigationsystem.repository.PlotRepository;
import com.automation.irrigationsystem.repository.ScheduleRepository;
import com.automation.irrigationsystem.request.EditPlotRequest;
import com.automation.irrigationsystem.request.NewPlotRequest;
import com.automation.irrigationsystem.requestmockdata.EditPlotRequestMockData;
import com.automation.irrigationsystem.requestmockdata.NewPlotRequestMockData;
import com.automation.irrigationsystem.response.GetAllPlotResponse;
import com.automation.irrigationsystem.response.NewPlotResponse;
import com.automation.irrigationsystem.services.PlotService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SpringBootTest
public class PlotServiceTest {

    @InjectMocks
    PlotService plotService;

    @Mock
    PlotService plotServiceMock;

    @Mock
    PlotRepository plotRepository;

    @Mock
    ScheduleRepository scheduleRepository;

    @Mock
    CustomScheduleRepository customScheduleRepository;

    @Nested
    @DisplayName("Test GetAll Plots service")
    @MockitoSettings(strictness = Strictness.LENIENT) //Prevent to throw UnnecessaryStubbingException
    class getPlotTestCases {

        @BeforeEach
        void setUp() {

            when(plotRepository.findByCustomerIdAndActiveFlg(BigDecimal.ZERO, BigDecimal.ONE)).thenReturn(Collections.emptyList());

            when(plotRepository.findByCustomerIdAndActiveFlg(BigDecimal.ONE, BigDecimal.ONE)).thenReturn(PlotMockData.regPlotListWithoutSched());

            when(plotRepository.findByCustomerIdAndActiveFlg(BigDecimal.valueOf(2), BigDecimal.ONE)).thenReturn(PlotMockData.regPlotListWithSched());

            when(plotRepository.findByCustomerIdAndActiveFlg(BigDecimal.valueOf(3), BigDecimal.ONE)).thenReturn(PlotMockData.customPlotListWithoutSched());

            when(plotRepository.findByCustomerIdAndActiveFlg(BigDecimal.valueOf(4), BigDecimal.ONE)).thenReturn(PlotMockData.customPlotListWithSched());

            when(scheduleRepository.findByCropNameAndSoilTypeAndWeatherTypeAndActiveFlg("WHEAT", "RED SOIL", "AUTUMN", BigDecimal.ONE))
                    .thenReturn(ScheduleMockData.getSchduleList());
        }

        @Test
        @DisplayName("When input parameter is null")
        void inputsIsNull() {
            assertThrows(InvalidRequestException.class, () -> plotService.getAllPlots(""));
        }

        @Test
        @DisplayName("When input parameter is not a valid number")
        void inputsIsInvalid() {
            assertThrows(InvalidRequestException.class, () -> plotService.getAllPlots("A"));
        }

        @Test
        @DisplayName("When requested input not found in DB")
        void inputsNotFound() {
            assertThrows(DataNotFoundException.class, () -> plotService.getAllPlots("0"));
        }

        @Test
        @DisplayName("When Regular Plot found but schedule is not available")
        void inputIsValidButScheduleNotFound() {
            assertInstanceOf(GetAllPlotResponse.class, plotService.getAllPlots("1"));
        }

        @Test
        @DisplayName("When Regular Plot found with schedule")
        void inputIsValidWithRegSch() {
            assertInstanceOf(GetAllPlotResponse.class, plotService.getAllPlots("2"));
        }

        @Test
        @DisplayName("When custom Plot found but schedule is not available")
        void customSchFound() {
            assertInstanceOf(GetAllPlotResponse.class, plotService.getAllPlots("3"));
        }

        @Test
        @DisplayName("When custom Plot found with schedule")
        void customSchNotFound() {
            assertInstanceOf(GetAllPlotResponse.class, plotService.getAllPlots("4"));
        }
    }

    @Nested
    @DisplayName("Test Add plot service")
    class addNewPlotTestCases {

        @BeforeEach
        void setUp() {
            when(plotRepository.save(Mockito.any(Plot.class))).thenReturn(PlotMockData.getPlot("1", "1", NewPlotRequestMockData.getNewPlotRequestRegularSched()));
        }

        @Test
        @DisplayName("When Input is empty")
        void whenInputIsInvalid() {
            NewPlotRequest req = new NewPlotRequest();
            assertThrows(InvalidRequestException.class, () -> plotService.addNewPlot("1", req));
        }

        @Test
        @DisplayName("When Customer Id input is invalid")
        void whenCustomerIdIsInvalid() {
            NewPlotRequest req = NewPlotRequestMockData.getNewPlotRequestCustomSched();
            assertThrows(InvalidRequestException.class, () -> plotService.addNewPlot("INVALID", req));
        }

        @Test
        @DisplayName("Create plot with CUSTOM schedule")
        void createPlotCustomScheduleSuccess() {
            when(plotRepository.save(Mockito.any(Plot.class))).thenReturn(PlotMockData.getPlot("1", "1", NewPlotRequestMockData.getNewPlotRequestCustomSched()));
            assertInstanceOf(NewPlotResponse.class, plotService.addNewPlot("1", NewPlotRequestMockData.getNewPlotRequestCustomSched()));
        }

        @Test
        @DisplayName("Create plot with REGULAR schedule")
        void createPlotRegularSuccess() {
            when(plotRepository.save(Mockito.any(Plot.class))).thenReturn(PlotMockData.getPlot("1", "1", NewPlotRequestMockData.getNewPlotRequestRegularSched()));
            assertInstanceOf(NewPlotResponse.class, plotService.addNewPlot("1", NewPlotRequestMockData.getNewPlotRequestRegularSched()));
        }
    }


    @Nested
    @DisplayName("Test Add plot service")
    class EditPlotTestCases {

        @BeforeEach
        void setUp() {
            when(plotRepository.findByCustomerIdAndIdAndActiveFlg(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE))
                    .thenReturn(PlotMockData.getPlot("1", "1", NewPlotRequestMockData.getNewPlotRequestRegularSched()));
            when(plotRepository.save(Mockito.any(Plot.class))).thenReturn(PlotMockData.getPlot("1", "1", NewPlotRequestMockData.getNewPlotRequestRegularSched()));

            when(plotRepository.findByCustomerIdAndIdAndActiveFlg(BigDecimal.ONE, BigDecimal.valueOf(2), BigDecimal.ONE)).thenReturn(null);
        }

        @Test
        @DisplayName("When Plot id is empty")
        void emptyPlotId() {
            EditPlotRequest req = new EditPlotRequest();
            assertThrows(InvalidRequestException.class, () -> plotService.updatePlot("1", "", req));
        }

        @Test
        @DisplayName("When customer id is empty")
        void emptyCustomerId() {
            EditPlotRequest req = new EditPlotRequest();
            assertThrows(InvalidRequestException.class, () -> plotService.updatePlot("", "1", req));
        }

        @Test
        @DisplayName("Invalid edit request data 'Crop name' ")
        void invalidEditReqData() {
            EditPlotRequest request = EditPlotRequestMockData.getEditPlotReqData(NewPlotRequestMockData.getNewPlotRequestRegularSched());
            request.setCropName("ABC"); //setting invalid crop name
            assertThrows(InvalidRequestException.class, () -> plotService.updatePlot("", "1", request));
        }

        @Test
        @DisplayName("When plot not found for request data")
        void plotNotFound() {
            EditPlotRequest request = EditPlotRequestMockData.getEditPlotReqData(NewPlotRequestMockData.getNewPlotRequestRegularSched());
            assertThrows(DataNotFoundException.class, () -> plotService.updatePlot("1", "2", request));
        }

        @Test
        @DisplayName("Success case")
        void SuccessReqData() {
            EditPlotRequest request = EditPlotRequestMockData.getEditPlotReqData(NewPlotRequestMockData.getNewPlotRequestRegularSched());
            plotService.updatePlot("1", "1", request);
        }
    }

    @Nested
    @DisplayName("Test Delete plot service")
    class DeletePlotTestCases {

        @BeforeEach
        public void setUp() {
            when(plotRepository.findByCustomerIdAndIdAndActiveFlg(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE))
                    .thenReturn(PlotMockData.getPlot("1", "1", NewPlotRequestMockData.getNewPlotRequestRegularSched()));
            when(plotRepository.findByCustomerIdAndIdAndActiveFlg(BigDecimal.ONE, BigDecimal.valueOf(2), BigDecimal.ONE))
                    .thenReturn(null);
            Plot plot = PlotMockData.getPlot("1", "1", NewPlotRequestMockData.getNewPlotRequestRegularSched());
            plot.setActiveFlg(BigDecimal.ONE);
            when(plotRepository.save(Mockito.any(Plot.class))).thenReturn(plot);
        }

        @Test
        @DisplayName("When Plot id is empty")
        void emptyPlotId() {
            assertThrows(InvalidRequestException.class, () -> plotService.deletePlot("1", ""));
        }

        @Test
        @DisplayName("When customer id is empty")
        void emptyCustomerId() {
            assertThrows(InvalidRequestException.class, () -> plotService.deletePlot("", "1"));
        }

        @Test
        @DisplayName("When customer id is invalid")
        void invalidCustomerId() {
            assertThrows(InvalidRequestException.class, () -> plotService.deletePlot("A", "1"));
        }

        @Test
        @DisplayName("When plot id is invalid")
        void invalidPlotId() {
            assertThrows(InvalidRequestException.class, () -> plotService.deletePlot("1", "A"));
        }

        @Test
        @DisplayName("When plot not found for request data")
        void plotNotFound() {
            assertThrows(DataNotFoundException.class, () -> plotService.deletePlot("1", "2"));
        }

        @Test
        @DisplayName("Success case")
        void SuccessReqData() {
            plotService.deletePlot("1", "1");
        }
    }
}