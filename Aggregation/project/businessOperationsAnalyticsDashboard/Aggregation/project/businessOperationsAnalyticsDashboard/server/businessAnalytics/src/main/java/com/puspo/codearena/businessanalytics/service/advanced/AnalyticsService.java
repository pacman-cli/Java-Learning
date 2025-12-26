package com.puspo.codearena.businessanalytics.service.advanced;

import java.util.List;

import org.springframework.stereotype.Service;

import com.puspo.codearena.businessanalytics.dto.intermediate.CategoryRevenueShareDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CategorySalesDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.CustomerLifetimeValueDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.MonthlyRevenueDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.TopProductDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.Advanced.RegionConversionDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.Advanced.RollingRevenueDto;
import com.puspo.codearena.businessanalytics.dto.unified.DashboardFiltersDto;
import com.puspo.codearena.businessanalytics.entity.Category;
import com.puspo.codearena.businessanalytics.entity.Order;
import com.puspo.codearena.businessanalytics.entity.OrderItem;
import com.puspo.codearena.businessanalytics.entity.Product;
import com.puspo.codearena.businessanalytics.repository.OrderRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsService {

    private final OrderRepository orderRepository;
    private final EntityManager entityManager;

    public List<MonthlyRevenueDto> getMonthlyRevenue(DashboardFiltersDto filter) {
        // Using JPQL
        return orderRepository.fetchMonthlyRevenue();
    }

    public List<CategorySalesDto> getCategorySales(DashboardFiltersDto filter) {
        return fetchCategorySales(filter);
    }

    public List<TopProductDto> getTopProducts(DashboardFiltersDto filter) {
        return fetchTopProducts(filter);
    }

    // ---------- Criteria API methods ----------

    private List<CategorySalesDto> fetchCategorySales(DashboardFiltersDto filter) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CategorySalesDto> cq = cb.createQuery(CategorySalesDto.class);

        Root<OrderItem> orderItem = cq.from(OrderItem.class);
        Join<OrderItem, Product> product = orderItem.join("product");
        Join<Product, Category> category = product.join("category");
        Join<OrderItem, Order> order = orderItem.join("order");

        cq.select(cb.construct(
                CategorySalesDto.class,
                category.get("name"),
                cb.sum(cb.prod(orderItem.get("quantity"), orderItem.get("price")))));

        java.util.List<jakarta.persistence.criteria.Predicate> predicates = new java.util.ArrayList<>();
        predicates.add(cb.equal(order.get("status"), "COMPLETED"));

        cq.where(predicates.toArray(new jakarta.persistence.criteria.Predicate[0]));
        cq.groupBy(category.get("name"));

        return entityManager.createQuery(cq).getResultList();
    }

    private List<TopProductDto> fetchTopProducts(DashboardFiltersDto filter) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<TopProductDto> cq = cb.createQuery(TopProductDto.class);

        Root<OrderItem> orderItem = cq.from(OrderItem.class);
        Join<OrderItem, Product> product = orderItem.join("product");
        Join<OrderItem, Order> order = orderItem.join("order");

        cq.select(cb.construct(
                TopProductDto.class,
                product.get("name"),
                cb.sum(orderItem.get("quantity"))));

        cq.where(cb.equal(order.get("status"), "COMPLETED"));
        cq.groupBy(product.get("name"));
        cq.orderBy(cb.desc(cb.sum(orderItem.get("quantity"))));

        return entityManager.createQuery(cq)
                .setMaxResults(5)
                .getResultList();
    }

    public List<RollingRevenueDto> getRolling7DayRevenue(DashboardFiltersDto filter) {
        return orderRepository.fetchRolling7DayRevenue()
                .stream()
                .map(r -> new RollingRevenueDto(
                        ((java.sql.Date) r[0]).toLocalDate(),
                        (java.math.BigDecimal) r[1],
                        (java.math.BigDecimal) r[2]))
                .toList();
    }

    public List<RegionConversionDto> getConversionRateByRegion(DashboardFiltersDto filter) {
        return orderRepository.fetchConversionRateByRegion()
                .stream()
                .map(r -> new RegionConversionDto(
                        (String) r[0],
                        ((Number) r[1]).longValue(),
                        ((Number) r[2]).longValue(),
                        (java.math.BigDecimal) r[3]))
                .toList();
    }

    public List<CategoryRevenueShareDto> getCategoryRevenueShare(DashboardFiltersDto filter) {
        return orderRepository.fetchCategoryRevenueShare()
                .stream()
                .map(o -> new CategoryRevenueShareDto(
                        ((String) o[0]),
                        ((java.math.BigDecimal) o[1]),
                        ((java.math.BigDecimal) o[2])))
                .toList();
    }

    public List<CustomerLifetimeValueDto> getTopCustomers(DashboardFiltersDto filter) {
        return orderRepository.fetchTopCustomers()
                .stream()
                .map(o -> new CustomerLifetimeValueDto(
                        ((String) o[0]),
                        ((Number) o[1]).doubleValue(),
                        ((Number) o[2]).intValue()))
                .toList();
    }
}
