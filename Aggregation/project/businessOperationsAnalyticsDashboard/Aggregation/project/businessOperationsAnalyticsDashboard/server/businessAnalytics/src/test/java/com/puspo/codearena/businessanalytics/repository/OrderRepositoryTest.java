package com.puspo.codearena.businessanalytics.repository;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.MonthlyRevenueDto;
import com.puspo.codearena.businessanalytics.entity.Customer;
import com.puspo.codearena.businessanalytics.entity.Order;
import com.puspo.codearena.businessanalytics.entity.Region;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.datasource.url=jdbc:h2:mem:testdb",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
class OrderRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    private Customer testCustomer;
    private Region testRegion;

    @BeforeEach
    void setUp() {
        // Create test customer
        testCustomer = new Customer();
        testCustomer.setName("Test Customer");
        testCustomer.setEmail("test@example.com");
        entityManager.persist(testCustomer);

        // Create test region
        testRegion = new Region();
        testRegion.setName("Test Region");
        entityManager.persist(testRegion);

        entityManager.flush();
    }

    @Test
    void fetchDashboardSummary_ShouldReturnZeroValues_WhenNoOrders() {
        // Act
        DashboardSummaryDto result = orderRepository.fetchDashboardSummary();

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getTotalOrders()).isZero();
        assertThat(result.getTotalRevenue()).isZero();
        assertThat(result.getAvgOrderValue()).isZero();
        assertThat(result.getCompletedOrders()).isZero();
        assertThat(result.getCanceledOrders()).isZero();
    }

    @Test
    void fetchDashboardSummary_ShouldReturnCorrectSummary_WithSingleCompletedOrder() {
        // Arrange
        Order order = createOrder("COMPLETED", new BigDecimal("100.00"));
        entityManager.persist(order);
        entityManager.flush();

        // Act
        DashboardSummaryDto result = orderRepository.fetchDashboardSummary();

        // Assert
        assertThat(result.getTotalOrders()).isEqualTo(1L);
        assertThat(result.getTotalRevenue()).isEqualTo(100.0);
        assertThat(result.getAvgOrderValue()).isEqualTo(100.0);
        assertThat(result.getCompletedOrders()).isEqualTo(1L);
        assertThat(result.getCanceledOrders()).isZero();
    }

    @Test
    void fetchDashboardSummary_ShouldReturnCorrectSummary_WithSingleCanceledOrder() {
        // Arrange
        Order order = createOrder("CANCELED", new BigDecimal("150.00"));
        entityManager.persist(order);
        entityManager.flush();

        // Act
        DashboardSummaryDto result = orderRepository.fetchDashboardSummary();

        // Assert
        assertThat(result.getTotalOrders()).isEqualTo(1L);
        assertThat(result.getTotalRevenue()).isZero(); // Canceled orders don't count in revenue
        assertThat(result.getAvgOrderValue()).isEqualTo(150.0); // Average includes all orders
        assertThat(result.getCompletedOrders()).isZero();
        assertThat(result.getCanceledOrders()).isEqualTo(1L);
    }

    @Test
    void fetchDashboardSummary_ShouldReturnCorrectSummary_WithMultipleOrders() {
        // Arrange
        persistOrder("COMPLETED", new BigDecimal("100.00"));
        persistOrder("COMPLETED", new BigDecimal("200.00"));
        persistOrder("CANCELED", new BigDecimal("50.00"));
        persistOrder("PENDING", new BigDecimal("75.00"));

        // Act
        DashboardSummaryDto result = orderRepository.fetchDashboardSummary();

        // Assert
        assertThat(result.getTotalOrders()).isEqualTo(4L);
        assertThat(result.getTotalRevenue()).isEqualTo(300.0); // Only completed orders
        assertThat(result.getAvgOrderValue()).isEqualTo(106.25); // Average of all orders
        assertThat(result.getCompletedOrders()).isEqualTo(2L);
        assertThat(result.getCanceledOrders()).isEqualTo(1L);
    }

    @Test
    void fetchDashboardSummary_ShouldHandleOnlyCompletedOrders() {
        // Arrange
        persistOrder("COMPLETED", new BigDecimal("100.00"));
        persistOrder("COMPLETED", new BigDecimal("200.00"));
        persistOrder("COMPLETED", new BigDecimal("300.00"));

        // Act
        DashboardSummaryDto result = orderRepository.fetchDashboardSummary();

        // Assert
        assertThat(result.getTotalOrders()).isEqualTo(3L);
        assertThat(result.getTotalRevenue()).isEqualTo(600.0);
        assertThat(result.getAvgOrderValue()).isEqualTo(200.0);
        assertThat(result.getCompletedOrders()).isEqualTo(3L);
        assertThat(result.getCanceledOrders()).isZero();
    }

    @Test
    void fetchDashboardSummary_ShouldHandleOnlyCanceledOrders() {
        // Arrange
        persistOrder("CANCELED", new BigDecimal("100.00"));
        persistOrder("CANCELED", new BigDecimal("200.00"));

        // Act
        DashboardSummaryDto result = orderRepository.fetchDashboardSummary();

        // Assert
        assertThat(result.getTotalOrders()).isEqualTo(2L);
        assertThat(result.getTotalRevenue()).isZero();
        assertThat(result.getAvgOrderValue()).isEqualTo(150.0);
        assertThat(result.getCompletedOrders()).isZero();
        assertThat(result.getCanceledOrders()).isEqualTo(2L);
    }

    @Test
    void fetchDashboardSummary_ShouldHandleDecimalValues() {
        // Arrange
        persistOrder("COMPLETED", new BigDecimal("99.99"));
        persistOrder("COMPLETED", new BigDecimal("150.50"));

        // Act
        DashboardSummaryDto result = orderRepository.fetchDashboardSummary();

        // Assert
        assertThat(result.getTotalRevenue()).isEqualTo(250.49);
        assertThat(result.getAvgOrderValue()).isCloseTo(125.245, 0.01);
    }

    @Test
    void fetchMonthlyRevenue_ShouldReturnEmptyList_WhenNoOrders() {
        // Act
        List<MonthlyRevenueDto> result = orderRepository.fetchMonthlyRevenue();

        // Assert
        assertThat(result).isEmpty();
    }

    @Test
    void fetchMonthlyRevenue_ShouldReturnMonthlyData_WithSingleMonth() {
        // Arrange
        Order order1 = createOrder("COMPLETED", new BigDecimal("100.00"));
        order1.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 0));
        entityManager.persist(order1);

        Order order2 = createOrder("COMPLETED", new BigDecimal("200.00"));
        order2.setCreatedAt(LocalDateTime.of(2024, 1, 20, 10, 0));
        entityManager.persist(order2);

        entityManager.flush();

        // Act
        List<MonthlyRevenueDto> result = orderRepository.fetchMonthlyRevenue();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMonth()).isEqualTo("2024-01");
        assertThat(result.get(0).getRevenue()).isEqualTo(300.0);
    }

    @Test
    void fetchMonthlyRevenue_ShouldReturnMonthlyData_WithMultipleMonths() {
        // Arrange
        Order order1 = createOrder("COMPLETED", new BigDecimal("100.00"));
        order1.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 0));
        entityManager.persist(order1);

        Order order2 = createOrder("COMPLETED", new BigDecimal("200.00"));
        order2.setCreatedAt(LocalDateTime.of(2024, 2, 15, 10, 0));
        entityManager.persist(order2);

        Order order3 = createOrder("COMPLETED", new BigDecimal("300.00"));
        order3.setCreatedAt(LocalDateTime.of(2024, 3, 15, 10, 0));
        entityManager.persist(order3);

        entityManager.flush();

        // Act
        List<MonthlyRevenueDto> result = orderRepository.fetchMonthlyRevenue();

        // Assert
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getMonth()).isEqualTo("2024-01");
        assertThat(result.get(1).getMonth()).isEqualTo("2024-02");
        assertThat(result.get(2).getMonth()).isEqualTo("2024-03");
    }

    @Test
    void fetchMonthlyRevenue_ShouldExcludeCanceledOrders() {
        // Arrange
        Order completed = createOrder("COMPLETED", new BigDecimal("100.00"));
        completed.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 0));
        entityManager.persist(completed);

        Order canceled = createOrder("CANCELED", new BigDecimal("500.00"));
        canceled.setCreatedAt(LocalDateTime.of(2024, 1, 20, 10, 0));
        entityManager.persist(canceled);

        entityManager.flush();

        // Act
        List<MonthlyRevenueDto> result = orderRepository.fetchMonthlyRevenue();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getRevenue()).isEqualTo(100.0); // Only completed order
    }

    @Test
    void fetchMonthlyRevenue_ShouldOrderByMonth() {
        // Arrange - Insert in non-chronological order
        Order order1 = createOrder("COMPLETED", new BigDecimal("300.00"));
        order1.setCreatedAt(LocalDateTime.of(2024, 3, 15, 10, 0));
        entityManager.persist(order1);

        Order order2 = createOrder("COMPLETED", new BigDecimal("100.00"));
        order2.setCreatedAt(LocalDateTime.of(2024, 1, 15, 10, 0));
        entityManager.persist(order2);

        Order order3 = createOrder("COMPLETED", new BigDecimal("200.00"));
        order3.setCreatedAt(LocalDateTime.of(2024, 2, 15, 10, 0));
        entityManager.persist(order3);

        entityManager.flush();

        // Act
        List<MonthlyRevenueDto> result = orderRepository.fetchMonthlyRevenue();

        // Assert - Should be ordered chronologically
        assertThat(result).hasSize(3);
        assertThat(result.get(0).getMonth()).isEqualTo("2024-01");
        assertThat(result.get(1).getMonth()).isEqualTo("2024-02");
        assertThat(result.get(2).getMonth()).isEqualTo("2024-03");
    }

    @Test
    void fetchMonthlyRevenue_ShouldGroupBySameMonth() {
        // Arrange
        Order order1 = createOrder("COMPLETED", new BigDecimal("100.00"));
        order1.setCreatedAt(LocalDateTime.of(2024, 1, 5, 10, 0));
        entityManager.persist(order1);

        Order order2 = createOrder("COMPLETED", new BigDecimal("150.00"));
        order2.setCreatedAt(LocalDateTime.of(2024, 1, 15, 14, 0));
        entityManager.persist(order2);

        Order order3 = createOrder("COMPLETED", new BigDecimal("200.00"));
        order3.setCreatedAt(LocalDateTime.of(2024, 1, 25, 18, 0));
        entityManager.persist(order3);

        entityManager.flush();

        // Act
        List<MonthlyRevenueDto> result = orderRepository.fetchMonthlyRevenue();

        // Assert
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getMonth()).isEqualTo("2024-01");
        assertThat(result.get(0).getRevenue()).isEqualTo(450.0);
    }

    private Order createOrder(String status, BigDecimal amount) {
        Order order = new Order();
        order.setCustomer(testCustomer);
        order.setRegion(testRegion);
        order.setStatus(status);
        order.setTotalAmount(amount);
        order.setCreatedAt(LocalDateTime.now());
        return order;
    }

    private void persistOrder(String status, BigDecimal amount) {
        Order order = createOrder(status, amount);
        entityManager.persist(order);
        entityManager.flush();
    }
}