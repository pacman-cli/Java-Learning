package com.puspo.codearena.businessanalytics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    private String status;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
//⚠️ No @OneToMany<OrderItem>
//We will aggregate from order_items table directly
