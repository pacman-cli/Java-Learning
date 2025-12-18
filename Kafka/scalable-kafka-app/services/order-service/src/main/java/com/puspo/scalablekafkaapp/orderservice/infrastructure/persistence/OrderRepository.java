package com.puspo.scalablekafkaapp.orderservice.infrastructure.persistence;

import com.puspo.scalablekafkaapp.orderservice.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
