package com.puspo.codearena.businessanalytics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "customers")
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
//🧠 Why no @OneToMany<Order>?
//Aggregation doesn’t need it
//Prevents N+1 problems
//Cleaner queries
