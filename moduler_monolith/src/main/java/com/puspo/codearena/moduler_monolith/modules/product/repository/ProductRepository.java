package com.puspo.codearena.moduler_monolith.modules.product.repository;

import com.puspo.codearena.moduler_monolith.modules.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
