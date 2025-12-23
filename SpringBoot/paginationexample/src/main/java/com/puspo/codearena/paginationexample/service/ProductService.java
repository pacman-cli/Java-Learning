package com.puspo.codearena.paginationexample.service;

import com.puspo.codearena.paginationexample.dto.ProductDto;
import com.puspo.codearena.paginationexample.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> getAllProductsSorted(String sortField);

    Page<Product> getAll(Pageable pageable);

    Product save(ProductDto productDto);

    Optional<Product> findById(Long id);

    void deleteById(Long id);

}
