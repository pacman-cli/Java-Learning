package com.puspo.codearena.businessanalytics.repository;

import com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto;
import com.puspo.codearena.businessanalytics.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("""
        SELECT new com.puspo.codearena.businessanalytics.dto.DashboardSummaryDto(
                        COUNT(o),
                        CAST(
                            COALESCE(
                                SUM(
                                    CASE WHEN o.status = 'COMPLETED' THEN o.totalAmount ELSE 0 END
                                        ),0
                                            ) AS double
                                                ),
                        CAST(
                            COALESCE(
                                AVG(
                                    o.totalAmount
                                        ),0
                                            ) AS double
                                                ),
                        SUM(CASE WHEN o.status = 'COMPLETED' THEN 1 ELSE 0 END),
                        SUM(CASE WHEN o.status = 'CANCELED' THEN 1 ELSE 0 END)
                    )
                    FROM Order o
    """)
    DashboardSummaryDto fetchDashboardSummary();
}


