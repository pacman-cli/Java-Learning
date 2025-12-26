package com.puspo.codearena.paginationexample.repository;

import com.puspo.codearena.paginationexample.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    //JpaRepository already supports pageable out of the box
}
