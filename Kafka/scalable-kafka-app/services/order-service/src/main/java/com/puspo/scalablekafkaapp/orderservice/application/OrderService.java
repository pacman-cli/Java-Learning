package com.puspo.scalablekafkaapp.orderservice.application;

import com.puspo.scalablekafkaapp.orderservice.domain.Order;

import java.util.List;

public interface OrderService {
        Order createOrder(Order order);

        List<Order> getAllOrders();

        void handleNewUser(String message);

}
