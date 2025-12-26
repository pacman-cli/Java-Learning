package com.pacman.rabbitmqdemo.producer;


import com.pacman.rabbitmqdemo.dto.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQJsonProducer {
    @Value( "${rabbitmq.exchange.name}")
    public String exchange;

    @Value( "${rabbitmq.routing.json.key}")
    public String routingJsonKey;


    private final static Logger logger = LoggerFactory.getLogger(RabbitMQJsonProducer.class); //this is for logging
    // purpose only
    private final RabbitTemplate rabbitTemplate;

    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(User user){
        logger.info("Sending message --> {}", user.toString());
        rabbitTemplate.convertAndSend(exchange,routingJsonKey,user);
    }
}
