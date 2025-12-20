
package com.puspo.codearena.businessanalytics.repository;

import java.util.List;
import java.util.Objects;

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
            """,
            nativeQuery = true
    )
    List<Objects[]> fetchCategoryRevenueShare();
}
