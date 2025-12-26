package com.puspo.codearena.businessanalytics.repository.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.puspo.codearena.businessanalytics.dto.intermediate.CategorySalesDto;
import com.puspo.codearena.businessanalytics.dto.intermediate.TopProductDto;
import com.puspo.codearena.businessanalytics.entity.Category;
import com.puspo.codearena.businessanalytics.entity.Order;
import com.puspo.codearena.businessanalytics.entity.OrderItem;
import com.puspo.codearena.businessanalytics.entity.Product;
import com.puspo.codearena.businessanalytics.repository.OrderAnalyticsRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;

@Repository
public class OrderAnalyticsRepositoryImpl implements OrderAnalyticsRepository {
        @PersistenceContext
        public EntityManager entityManager;

        // ------------------ Category-wise Sales ----------------

        @Override
        public List<CategorySalesDto> fetchCategorySales() {
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<CategorySalesDto> query = cb.createQuery(CategorySalesDto.class);
                Root<OrderItem> orderItemRoot = query.from(OrderItem.class);

                Join<OrderItem, Product> productJoin = orderItemRoot.join("product");
                Join<Product, Category> categoryJoin = productJoin.join("category");
                Join<OrderItem, Order> orderJoin = orderItemRoot.join("order");

                //
                query.select(
                                cb.construct(
                                                CategorySalesDto.class,
                                                categoryJoin.get("name"),
                                                cb.sum( // total revenue per category
                                                                cb.prod( // quantity*price
                                                                                orderItemRoot.get("quantity"),
                                                                                orderItemRoot.get("price")))));

                // Condition check (dynamic filter)
                query.where(
                                cb.equal(
                                                orderJoin.get("status"),
                                                "COMPLETED"));

                // Group by
                query.groupBy(categoryJoin.get("name"));

                return entityManager.createQuery(query).getResultList();
        }

        @Override
        public List<TopProductDto> fetchTopProducts() {
                CriteriaBuilder cb = entityManager.getCriteriaBuilder();
                CriteriaQuery<TopProductDto> query = cb.createQuery(TopProductDto.class);
                Root<OrderItem> orderItemRoot = query.from(OrderItem.class);

                Join<OrderItem, Product> productJoin = orderItemRoot.join("product");
                Join<OrderItem, Order> orderJoin = orderItemRoot.join("order");

                query.select(
                                cb.construct(
                                                TopProductDto.class,
                                                productJoin.get("name"),
                                                cb.sum(
                                                                orderItemRoot.get("quantity"))));

                query.where(cb.equal(orderJoin.get("status"), "COMPLETED"));
                query.groupBy(productJoin.get("name"));
                query.orderBy(cb.desc(cb.sum(orderItemRoot.get("quantity"))));

                return entityManager.createQuery(query).getResultList();
        }

}
