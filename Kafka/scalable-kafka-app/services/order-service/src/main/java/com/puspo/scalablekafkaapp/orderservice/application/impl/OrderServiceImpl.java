package com.puspo.scalablekafkaapp.orderservice.application.impl;

import com.puspo.scalablekafkaapp.orderservice.application.OrderService;
import com.puspo.scalablekafkaapp.orderservice.infrastructure.persistence.OrderRepository;
import com.puspo.scalablekafkaapp.orderservice.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public Order createOrder(Order order) {
        Order savedOrder = orderRepository.save(order);
        kafkaTemplate.send("order.created", savedOrder.getId() + ":" + savedOrder.getUserId());
        return savedOrder;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    @KafkaListener(topics = "user.created", groupId = "order-service-group")
    public void handleNewUser(String message) {
        System.out.println("Received message from user.created(Order Service): " + message);
        // Logic: e.g., create default orders or update cache
    }
    
}
