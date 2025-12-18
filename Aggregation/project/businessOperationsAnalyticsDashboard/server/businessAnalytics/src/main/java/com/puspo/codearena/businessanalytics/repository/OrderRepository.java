package com.puspo.codearena.businessanalytics.repository;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.MonthlyRevenueDto;
import com.puspo.codearena.businessanalytics.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    //--->JPQL queries
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
                        FUNCTION('to_char',o.createdAt,'YYYY-MM'),
                        SUM (o.totalAmount)
            )
            FROM Order o
            WHERE o.status='COMPLETED'
            GROUP BY FUNCTION('to_char',o.createdAt,'YYYY-MM') 
            ORDER BY FUNCTION('to_char',o.createdAt,'YYYY-MM') 
            """)
    List<MonthlyRevenueDto> fetchMonthlyRevenue();
}


