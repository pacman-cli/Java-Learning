package com.puspo.codearena.paginationexample.mapper;

import com.puspo.codearena.paginationexample.dto.ProductDto;
import com.puspo.codearena.paginationexample.entity.Product;

public class ProductMapper {
    public static ProductDto toDto(Product e) {
        if (e == null) {
            return null;
        }
        return ProductDto.builder()
                .id(e.getId())
                .name(e.getName())
                .description(e.getDescription())
                .price(e.getPrice())
                .build();
    }

    public static Product toEntity(ProductDto d) {
        if (d == null) {
            return null;
        }
        return Product.builder()
                .id(d.getId())
                .name(d.getName())
                .description(d.getDescription())
                .price(d.getPrice())
                .build();
    }
}
