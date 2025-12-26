package com.puspo.codearena.moduler_monolith.modules.product.mapper;

import com.puspo.codearena.moduler_monolith.modules.product.dto.ProductRequest;
import com.puspo.codearena.moduler_monolith.modules.product.dto.ProductResponse;
import com.puspo.codearena.moduler_monolith.modules.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponse toProductResponse(Product e) {
        return ProductResponse.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .price(e.getPrice())
                .stockQuantity(e.getStockQuantity())
                .active(e.isActive())
                .build();
    }

    public Product toEntity(ProductRequest d) {
        if (d == null) {
            return null;
        }
        return Product.builder()
                .name(d.getName())
                .description(d.getDescription())
                .price(d.getPrice())
                .stockQuantity(d.getStockQuantity())
                .build();
    }
}
