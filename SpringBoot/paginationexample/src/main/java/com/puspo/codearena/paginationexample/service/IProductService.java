package com.puspo.codearena.paginationexample.service;

import com.puspo.codearena.paginationexample.dto.ProductDto;
import com.puspo.codearena.paginationexample.entity.Product;
import com.puspo.codearena.paginationexample.mapper.ProductMapper;
import com.puspo.codearena.paginationexample.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class IProductService implements ProductService {

    private final ProductRepository productRepository;

    public IProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<Product> getAllProductsSorted(String sortField) {
        // Ensure the sortField is cleaned of extra characters like brackets or quotes
        String cleanSortField = sortField.replaceAll("[\"\\[\\]]", "");
        return productRepository.findAll(Sort.by(cleanSortField));
    }

    @Override
    public Page<Product> getAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Product save(ProductDto productDto) {
        return productRepository.save(ProductMapper.toEntity(productDto));
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }
}
