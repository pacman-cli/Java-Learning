package com.puspo.codearena.businessanalytics.service;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    private DashboardSummaryDto mockDashboardSummary;

    @BeforeEach
    void setUp() {
        mockDashboardSummary = new DashboardSummaryDto(
                100L,
                50000.0,
                500.0,
                80L,
                20L
        );
    }

    @Test
    void getSummary_ShouldReturnDashboardSummary_WhenRepositoryReturnsData() {
        // Arrange
        when(orderRepository.fetchDashboardSummary()).thenReturn(mockDashboardSummary);

        // Act
        DashboardSummaryDto result = dashboardService.getSummary();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalOrders()).isEqualTo(100L);
        assertThat(result.getTotalRevenue()).isEqualTo(50000.0);
        assertThat(result.getAvgOrderValue()).isEqualTo(500.0);
        assertThat(result.getCompletedOrders()).isEqualTo(80L);
        assertThat(result.getCanceledOrders()).isEqualTo(20L);
        
        verify(orderRepository, times(1)).fetchDashboardSummary();
    }

    @Test
    void getSummary_ShouldReturnZeroValues_WhenNoOrdersExist() {
        // Arrange
        DashboardSummaryDto emptyDto = new DashboardSummaryDto(0L, 0.0, 0.0, 0L, 0L);
        when(orderRepository.fetchDashboardSummary()).thenReturn(emptyDto);

        // Act
        DashboardSummaryDto result = dashboardService.getSummary();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalOrders()).isZero();
        assertThat(result.getTotalRevenue()).isZero();
        assertThat(result.getAvgOrderValue()).isZero();
        assertThat(result.getCompletedOrders()).isZero();
        assertThat(result.getCanceledOrders()).isZero();
        
        verify(orderRepository).fetchDashboardSummary();
    }

    @Test
    void getSummary_ShouldCallRepositoryExactlyOnce() {
        // Arrange
        when(orderRepository.fetchDashboardSummary()).thenReturn(mockDashboardSummary);

        // Act
        dashboardService.getSummary();

        // Assert
        verify(orderRepository, times(1)).fetchDashboardSummary();
        verifyNoMoreInteractions(orderRepository);
    }

    @Test
    void getSummary_ShouldReturnSameInstanceFromRepository() {
        // Arrange
        when(orderRepository.fetchDashboardSummary()).thenReturn(mockDashboardSummary);

        // Act
        DashboardSummaryDto result = dashboardService.getSummary();

        // Assert
        assertThat(result).isSameAs(mockDashboardSummary);
    }

    @Test
    void getSummary_ShouldHandleLargeOrderVolumes() {
        // Arrange
        DashboardSummaryDto largeVolumeDto = new DashboardSummaryDto(
                1000000L,
                99999999.99,
                99.99,
                950000L,
                50000L
        );
        when(orderRepository.fetchDashboardSummary()).thenReturn(largeVolumeDto);

        // Act
        DashboardSummaryDto result = dashboardService.getSummary();

        // Assert
        assertThat(result.getTotalOrders()).isEqualTo(1000000L);
        assertThat(result.getTotalRevenue()).isEqualTo(99999999.99);
        assertThat(result.getAvgOrderValue()).isEqualTo(99.99);
        assertThat(result.getCompletedOrders()).isEqualTo(950000L);
        assertThat(result.getCanceledOrders()).isEqualTo(50000L);
    }

    @Test
    void getSummary_ShouldHandleHighCancellationRate() {
        // Arrange - More canceled than completed orders
        DashboardSummaryDto highCancellationDto = new DashboardSummaryDto(
                100L,
                10000.0,
                100.0,
                20L,
                80L
        );
        when(orderRepository.fetchDashboardSummary()).thenReturn(highCancellationDto);

        // Act
        DashboardSummaryDto result = dashboardService.getSummary();

        // Assert
        assertThat(result.getCompletedOrders()).isLessThan(result.getCanceledOrders());
        assertThat(result.getCanceledOrders()).isEqualTo(80L);
        assertThat(result.getCompletedOrders()).isEqualTo(20L);
    }

    @Test
    void getSummary_ShouldHandleAllCompletedOrders() {
        // Arrange - No canceled orders
        DashboardSummaryDto allCompletedDto = new DashboardSummaryDto(
                100L,
                50000.0,
                500.0,
                100L,
                0L
        );
        when(orderRepository.fetchDashboardSummary()).thenReturn(allCompletedDto);

        // Act
        DashboardSummaryDto result = dashboardService.getSummary();

        // Assert
        assertThat(result.getTotalOrders()).isEqualTo(result.getCompletedOrders());
        assertThat(result.getCanceledOrders()).isZero();
    }

    @Test
    void getSummary_ShouldHandleAllCanceledOrders() {
        // Arrange - No completed orders
        DashboardSummaryDto allCanceledDto = new DashboardSummaryDto(
                50L,
                0.0,
                0.0,
                0L,
                50L
        );
        when(orderRepository.fetchDashboardSummary()).thenReturn(allCanceledDto);

        // Act
        DashboardSummaryDto result = dashboardService.getSummary();

        // Assert
        assertThat(result.getTotalOrders()).isEqualTo(result.getCanceledOrders());
        assertThat(result.getCompletedOrders()).isZero();
        assertThat(result.getTotalRevenue()).isZero();
    }

    @Test
    void getSummary_ShouldHandleDecimalPrecision() {
        // Arrange
        DashboardSummaryDto preciseDto = new DashboardSummaryDto(
                10L,
                12345.67,
                1234.56,
                8L,
                2L
        );
        when(orderRepository.fetchDashboardSummary()).thenReturn(preciseDto);

        // Act
        DashboardSummaryDto result = dashboardService.getSummary();

        // Assert
        assertThat(result.getTotalRevenue()).isEqualTo(12345.67);
        assertThat(result.getAvgOrderValue()).isEqualTo(1234.56);
    }

    @Test
    void getSummary_ShouldVerifyOrderCountConsistency() {
        // Arrange - Verify completed + canceled <= total
        DashboardSummaryDto dto = new DashboardSummaryDto(
                100L,
                40000.0,
                400.0,
                60L,
                30L
        );
        when(orderRepository.fetchDashboardSummary()).thenReturn(dto);

        // Act
        DashboardSummaryDto result = dashboardService.getSummary();

        // Assert
        long sumOfStatusOrders = result.getCompletedOrders() + result.getCanceledOrders();
        assertThat(sumOfStatusOrders).isLessThanOrEqualTo(result.getTotalOrders());
    }

    @Test
    void getSummary_ShouldHandleSingleOrder() {
        // Arrange
        DashboardSummaryDto singleOrderDto = new DashboardSummaryDto(
                1L,
                99.99,
                99.99,
                1L,
                0L
        );
        when(orderRepository.fetchDashboardSummary()).thenReturn(singleOrderDto);

        // Act
        DashboardSummaryDto result = dashboardService.getSummary();

        // Assert
        assertThat(result.getTotalOrders()).isEqualTo(1L);
        assertThat(result.getTotalRevenue()).isEqualTo(result.getAvgOrderValue());
    }

    @Test
    void constructor_ShouldInitializeWithOrderRepository() {
        // Arrange & Act
        DashboardServiceImpl service = new DashboardServiceImpl(orderRepository);

        // Assert
        assertThat(service).isNotNull();
    }

    @Test
    void getSummary_ShouldHandleVerySmallOrderValue() {
        // Arrange
        DashboardSummaryDto smallValueDto = new DashboardSummaryDto(
                1000L,
                0.01,
                0.00001,
                1000L,
                0L
        );
        when(orderRepository.fetchDashboardSummary()).thenReturn(smallValueDto);

        // Act
        DashboardSummaryDto result = dashboardService.getSummary();

        // Assert
        assertThat(result.getTotalRevenue()).isPositive();
        assertThat(result.getAvgOrderValue()).isPositive();
    }

    @Test
    void getSummary_ShouldNotModifyRepositoryData() {
        // Arrange
        when(orderRepository.fetchDashboardSummary()).thenReturn(mockDashboardSummary);

        // Act
        DashboardSummaryDto result1 = dashboardService.getSummary();
        DashboardSummaryDto result2 = dashboardService.getSummary();

        // Assert - Each call should go through repository
        verify(orderRepository, times(2)).fetchDashboardSummary();
    }
}