package com.puspo.codearena.moduler_monolith.core.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class) //They help you automate logic like auditing, timestamps, logging, and validation without writing this logic in controllers or services.
public abstract class BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}

// TODO: Why use EntityListeners?
//✔️ 1. Centralized entity lifecycle logic
//Instead of repeating the same code in services, you write it once and every save/update automatically triggers it.
//
//Example: Set createdAt and updatedAt before saving an entity.
//No need to manually write this in every repository call.
//
//        ✔️ 2. Audit & Security
//
//Record who created or modified an entity
//
//Store IP or request metadata
//
//Track change logs
//
//✔️ 3. Avoid business logic leakage
//
//Controller/Service should not worry about timestamps, auto-generated fields.
//Listeners keep entities clean and consistent.
