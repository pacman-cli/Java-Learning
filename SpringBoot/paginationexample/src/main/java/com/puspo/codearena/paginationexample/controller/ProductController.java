package com.puspo.codearena.paginationexample.controller;

import com.puspo.codearena.paginationexample.dto.ProductDto;
import com.puspo.codearena.paginationexample.entity.Product;
import com.puspo.codearena.paginationexample.mapper.ProductMapper;
import com.puspo.codearena.paginationexample.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@Tag(
    name = "Product API",
    description = "API for managing products with pagination and sorting"
)
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
        summary = "Get all products sorted",
        description = "Retrieves all products sorted by the specified field. Common sort fields: 'name', 'price', 'id'"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved sorted products"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid sort field provided",
                content = @Content
            ),
        }
    )
    @GetMapping("/sort")
    public ResponseEntity<List<Product>> getAllProductsSorted(
        @Parameter(
            description = "Field to sort by (e.g., 'name', 'price', 'id')",
            required = true,
            example = "name"
        ) @RequestParam(defaultValue = "name") String sortField
    ) {
        List<Product> products = productService.getAllProductsSorted(sortField);
        return ResponseEntity.ok(products);
    }

    @Operation(
        summary = "Get all products with pagination",
        description = "Retrieves paginated list of products. Use query parameters: page (0-based), size, sort. " +
            "Example: ?page=0&size=10&sort=name,asc or ?page=1&size=5&sort=price,desc"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Successfully retrieved paginated products"
            ),
        }
    )
    @GetMapping
    public ResponseEntity<Page<ProductDto>> listAll(
        @ParameterObject Pageable pageable
    ) {
        Page<Product> page = productService.getAll(pageable);
        Page<ProductDto> dtoPage = page.map(ProductMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    @Operation(
        summary = "Create a new product",
        description = "Creates a new product with the provided details. The 'id' field is auto-generated and should be omitted."
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Product created successfully"
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Invalid input data",
                content = @Content
            ),
        }
    )
    @PostMapping
    public ResponseEntity<ProductDto> create(
        @Parameter(
            description = "Product data to create (omit 'id' field)",
            required = true
        ) @Valid @RequestBody ProductDto dto
    ) {
        Product saved = productService.save(dto);
        return ResponseEntity.status(201).body(ProductMapper.toDto(saved));
    }

    @Operation(
        summary = "Get product by ID",
        description = "Retrieves a specific product by its unique identifier"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Product found successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Product not found",
                content = @Content
            ),
        }
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getByID(
        @Parameter(
            description = "Product ID",
            required = true,
            example = "1"
        ) @PathVariable Long id
    ) {
        return productService
            .findById(id)
            .map(p -> ResponseEntity.ok(ProductMapper.toDto(p)))
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Delete product by ID",
        description = "Deletes a specific product by its unique identifier"
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "204",
                description = "Product deleted successfully"
            ),
            @ApiResponse(
                responseCode = "404",
                description = "Product not found",
                content = @Content
            ),
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteByID(
        @Parameter(
            description = "Product ID to delete",
            required = true,
            example = "1"
        ) @PathVariable Long id
    ) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
