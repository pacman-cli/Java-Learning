package com.puspo.codearena.businessanalytics.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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

// 9️⃣ Why We Avoid Bidirectional Relationships
// ❌ Bad Practice
// @OneToMany(mappedBy = "order")
// private List<OrderItem> items;
// 🔥 Why this kills aggregation:
// Loads unnecessary data
// Causes memory pressure
// Breaks pagination
// Confuses Hibernate query planner
// ✅ We join explicitly in queries
