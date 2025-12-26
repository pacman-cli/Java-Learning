
package com.puspo.codearena.businessanalytics.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.MonthlyRevenueDto;
import com.puspo.codearena.businessanalytics.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
        // --->JPQL queries
        @Query("""
                            SELECT new com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto(
                                            COUNT(o),
                                            CAST(COALESCE(SUM(CASE WHEN o.status = 'COMPLETED' THEN o.totalAmount ELSE 0 END),0) AS double),
                                            CAST(COALESCE(AVG(o.totalAmount),0) AS double),
                                            SUM(CASE WHEN o.status = 'COMPLETED' THEN 1 ELSE 0 END),
                                            SUM(CASE WHEN o.status = 'CANCELED' THEN 1 ELSE 0 END)
                                        )
                                        FROM Order o
                        """)
        DashboardSummaryDto fetchDashboardSummary();

        @Query("""
                        SELECT new com.puspo.codearena.businessanalytics.dto.intermediate.MonthlyRevenueDto(
                                    CAST(FUNCTION('to_char', o.createdAt, 'YYYY-MM') AS String),
                                    SUM(o.totalAmount)
                        )
                        FROM Order o
                        WHERE o.status = 'COMPLETED'
                        GROUP BY FUNCTION('to_char', o.createdAt, 'YYYY-MM')
                        ORDER BY FUNCTION('to_char', o.createdAt, 'YYYY-MM')
                        """)
        List<MonthlyRevenueDto> fetchMonthlyRevenue();

        @Query(value = """
                        SELECT
                                        c.name AS category_name,
                                        SUM(oi.quantity * oi.price) AS total_revenue,
                                        ROUND(
                                                    SUM(oi.quantity * oi.price)*100/
                                                    SUM(SUM(oi.quantity * oi.price)) OVER (),2
                                        ) AS revenue_share_percent
                        FROM order_items oi
                        JOIN products p ON oi.product_id = p.id
                        JOIN categories c ON oi.categories_id = c.id
                        JOIN orders o ON oi.order_id = p.id
                        WHERE o.status = 'COMPLETED'
                        GROUP BY c.name
                        """, nativeQuery = true)
        List<Object[]> fetchCategoryRevenueShare();

        @Query(value = """
                        SELECT
                        r.name AS region_name,
                        SUM(CASE WHEN o.status='COMPLETED' THEN 1 ELSE 0 END) AS completed_orders,
                        SUM(CASE WHEN o.status='CANCELED' THEN 1 ElSE 0 END) AS canceled_orders,
                        ROUND(
                        SUM(CASE WHEN o.status='COMPLETED' THEN 1 ELSE 0 END)*100/
                        NULLIF(COUNT(o.id),0),2)) AS conversion_rate_percent
                        FROM orders o
                        JOIN regions r ON o.region_id = r.id
                        GROUP BY r.name
                        ORDER BY conversion_rate_percent DESC
                                """, nativeQuery = true)
        List<Object[]> fetchConversionRateByRegion();

        @Query(value = """
                        SELECT
                        DATE(created_at) AS order_date,
                        SUM(total_amount) AS total_revenue
                        ROUND(
                        AVG(SUM(total_amount)) OVER (ORDER BY DATE(created_at) ROWS BETWEEN 6 PRECEDING AND CURRENT ROW), 2
                        ) AS rolling_7_day_avg
                        FROM orders o
                        WHERE o.status = 'COMPLETED'
                        GROUP BY DATE(created_at)
                        ORDER BY order_date
                        """, nativeQuery = true)
        List<Object[]> fetchRolling7DayRevenue();

        @Query(value = """
                        SELECT
                        c.name AS customer_name,
                        SUM(o.total_amount) AS lifetime_revenue,
                        ROW_NUMBER() OVER(ORDER BY SUM(o.total_amount) DESC
                        ) AS rank
                        FROM orders o
                        JOIN customers c ON o.customer_id = c.id
                        WHERE o.status = 'COMPLETED'
                        GROUP BY c.name
                        LIMIT 10
                        """, nativeQuery = true)
        List<Object[]> fetchTopCustomers();
}
