package com.zedcode.module.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Base Entity class with common audit fields
 * All entities should extend this class to inherit audit functionality
 *
 * @author ZedCode
 * @version 1.0
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Primary key - Auto-generated ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    /**
     * Timestamp when the entity was created
     * Automatically populated by Spring Data JPA
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the entity was last updated
     * Automatically updated by Spring Data JPA
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * User who created this entity
     * Automatically populated by Spring Data JPA auditing
     */
    @CreatedBy
    @Column(name = "created_by", updatable = false)
    private String createdBy;

    /**
     * User who last updated this entity
     * Automatically updated by Spring Data JPA auditing
     */
    @LastModifiedBy
    @Column(name = "updated_by")
    private String updatedBy;

    /**
     * Soft delete flag
     * If true, the entity is considered deleted but still exists in the database
     */
    @Column(name = "deleted")
    private Boolean deleted = false;

    /**
     * Version field for optimistic locking
     * Prevents concurrent modification issues
     */
    @Version
    @Column(name = "version")
    private Long version;

    /**
     * PrePersist callback
     * Called before entity is persisted to the database
     */
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (deleted == null) {
            deleted = false;
        }
    }

    /**
     * PreUpdate callback
     * Called before entity is updated in the database
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Soft delete the entity
     */
    public void softDelete() {
        this.deleted = true;
    }

    /**
     * Restore a soft-deleted entity
     */
    public void restore() {
        this.deleted = false;
    }

    /**
     * Check if entity is deleted
     */
    public boolean isDeleted() {
        return deleted != null && deleted;
    }

    /**
     * Check if entity is new (not yet persisted)
     */
    public boolean isNew() {
        return id == null;
    }
}
