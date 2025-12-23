package com.puspo.codearena.moduler_monolith.modules.product.dto;

import java.math.BigDecimal;

@lombok.Builder
public record ProductResponse(
        Long id,
        String name,
        String description,
        BigDecimal price,
        Integer stockQuantity,
        boolean active) {
}
