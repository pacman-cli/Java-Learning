package com.puspo.codearena.moduler_monolith.modules.product.entity;

import com.puspo.codearena.moduler_monolith.core.models.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "product")
public class Product extends BaseEntity {
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private boolean active;

}
