package com.puspo.codearena.businessanalytics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
@Getter
@Setter
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private String method;
    private String status;

    @Column(name = "paid_amount")
    private BigDecimal paidAmount;
}

//9️⃣ Why We Avoid Bidirectional Relationships
//❌ Bad Practice
//@OneToMany(mappedBy = "order")
//private List<OrderItem> items;
//🔥 Why this kills aggregation:
//Loads unnecessary data
//Causes memory pressure
//Breaks pagination
//Confuses Hibernate query planner
//✅ We join explicitly in queries