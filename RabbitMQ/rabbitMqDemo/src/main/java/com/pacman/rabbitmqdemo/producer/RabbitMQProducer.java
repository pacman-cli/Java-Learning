package com.pacman.rabbitmqdemo.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQProducer {
    @Value( "${rabbitmq.exchange.name}")
    public String exchange;

    @Value( "${rabbitmq.routing.key}")
    public String routingKey;


    private final static Logger logger = LoggerFactory.getLogger(RabbitMQProducer.class); //this is for logging
    // purpose only
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendMessage(String message){
        logger.info("Sending message --> {}", message);
        rabbitTemplate.convertAndSend(exchange,routingKey,message); //sending message to rabbitmq
    }
}
