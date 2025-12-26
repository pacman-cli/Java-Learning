package com.puspo.codearena.paginationexample.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@Schema(description = "Product Data Transfer Object")
public class ProductDto {

    @Schema(
        description = "Product ID (auto-generated)",
        example = "1",
        accessMode = Schema.AccessMode.READ_ONLY
    )
    private Long id;

    @NotBlank(message = "Product name is required")
    @Schema(description = "Product name", example = "Laptop", required = true)
    private String name;

    @Schema(
        description = "Product description",
        example = "High-performance laptop with 16GB RAM"
    )
    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(
        value = "0.0",
        inclusive = false,
        message = "Price must be greater than 0"
    )
    @Schema(description = "Product price", example = "999.99", required = true)
    private BigDecimal price;
}
