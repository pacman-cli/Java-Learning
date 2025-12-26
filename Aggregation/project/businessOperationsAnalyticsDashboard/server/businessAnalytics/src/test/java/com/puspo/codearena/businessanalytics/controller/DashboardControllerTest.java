package com.puspo.codearena.businessanalytics.controller;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.service.DashboardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class DashboardControllerTest {

    private MockMvc mockMvc;

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private DashboardController dashboardController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(dashboardController).build();
    }

    @Test
    void getSummary_ShouldReturnDashboardSummary_WhenDataExists() throws Exception {
        // Arrange
        DashboardSummaryDto expectedDto = new DashboardSummaryDto(
                100L,
                50000.0,
                500.0,
                80L,
                20L
        );
        when(dashboardService.getSummary()).thenReturn(expectedDto);

        // Act & Assert
        mockMvc.perform(get("/api/dashboard/summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.totalOrders", is(100)))
                .andExpect(jsonPath("$.totalRevenue", is(50000.0)))
                .andExpect(jsonPath("$.avgOrderValue", is(500.0)))
                .andExpect(jsonPath("$.completedOrders", is(80)))
                .andExpect(jsonPath("$.canceledOrders", is(20)));

        verify(dashboardService, times(1)).getSummary();
    }

    @Test
    void getSummary_ShouldReturnZeroValues_WhenNoDataExists() throws Exception {
        // Arrange
        DashboardSummaryDto expectedDto = new DashboardSummaryDto(
                0L,
                0.0,
                0.0,
                0L,
                0L
        );
        when(dashboardService.getSummary()).thenReturn(expectedDto);

        // Act & Assert
        mockMvc.perform(get("/api/dashboard/summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrders", is(0)))
                .andExpect(jsonPath("$.totalRevenue", is(0.0)))
                .andExpect(jsonPath("$.avgOrderValue", is(0.0)))
                .andExpect(jsonPath("$.completedOrders", is(0)))
                .andExpect(jsonPath("$.canceledOrders", is(0)));

        verify(dashboardService, times(1)).getSummary();
    }

    @Test
    void getSummary_ShouldHandleLargeNumbers() throws Exception {
        // Arrange
        DashboardSummaryDto expectedDto = new DashboardSummaryDto(
                999999L,
                9999999.99,
                9999.99,
                800000L,
                199999L
        );
        when(dashboardService.getSummary()).thenReturn(expectedDto);

        // Act & Assert
        mockMvc.perform(get("/api/dashboard/summary")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrders", is(999999)))
                .andExpect(jsonPath("$.totalRevenue", is(9999999.99)))
                .andExpect(jsonPath("$.avgOrderValue", is(9999.99)))
                .andExpect(jsonPath("$.completedOrders", is(800000)))
                .andExpect(jsonPath("$.canceledOrders", is(199999)));

        verify(dashboardService, times(1)).getSummary();
    }

    @Test
    void getSummary_ShouldVerifyCorrectEndpoint() throws Exception {
        // Arrange
        DashboardSummaryDto expectedDto = new DashboardSummaryDto(10L, 1000.0, 100.0, 8L, 2L);
        when(dashboardService.getSummary()).thenReturn(expectedDto);

        // Act & Assert - verify the exact endpoint path
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk());

        verify(dashboardService).getSummary();
    }

    @Test
    void getSummary_ShouldReturnJsonContentType() throws Exception {
        // Arrange
        DashboardSummaryDto expectedDto = new DashboardSummaryDto(1L, 100.0, 100.0, 1L, 0L);
        when(dashboardService.getSummary()).thenReturn(expectedDto);

        // Act & Assert
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getSummary_ShouldCallServiceExactlyOnce() throws Exception {
        // Arrange
        DashboardSummaryDto expectedDto = new DashboardSummaryDto(5L, 500.0, 100.0, 4L, 1L);
        when(dashboardService.getSummary()).thenReturn(expectedDto);

        // Act
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk());

        // Assert
        verify(dashboardService, times(1)).getSummary();
        verifyNoMoreInteractions(dashboardService);
    }

    @Test
    void getSummary_ShouldHandleDecimalPrecision() throws Exception {
        // Arrange - Test with precise decimal values
        DashboardSummaryDto expectedDto = new DashboardSummaryDto(
                15L,
                12345.67,
                823.04,
                12L,
                3L
        );
        when(dashboardService.getSummary()).thenReturn(expectedDto);

        // Act & Assert
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRevenue", is(12345.67)))
                .andExpect(jsonPath("$.avgOrderValue", is(823.04)));
    }

    @Test
    void getSummary_ShouldReturnAllFieldsInResponse() throws Exception {
        // Arrange
        DashboardSummaryDto expectedDto = new DashboardSummaryDto(50L, 25000.0, 500.0, 40L, 10L);
        when(dashboardService.getSummary()).thenReturn(expectedDto);

        // Act & Assert - Verify all fields are present
        mockMvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalOrders").exists())
                .andExpect(jsonPath("$.totalRevenue").exists())
                .andExpect(jsonPath("$.avgOrderValue").exists())
                .andExpect(jsonPath("$.completedOrders").exists())
                .andExpect(jsonPath("$.canceledOrders").exists());
    }
}