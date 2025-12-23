package com.puspo.codearena.moduler_monolith.modules.product.service;

import com.puspo.codearena.moduler_monolith.modules.product.dto.ProductRequest;
import com.puspo.codearena.moduler_monolith.modules.product.dto.ProductResponse;
import com.puspo.codearena.moduler_monolith.modules.product.entity.Product;
import com.puspo.codearena.moduler_monolith.modules.product.mapper.ProductMapper;
import com.puspo.codearena.moduler_monolith.modules.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;

    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);
    }
}
