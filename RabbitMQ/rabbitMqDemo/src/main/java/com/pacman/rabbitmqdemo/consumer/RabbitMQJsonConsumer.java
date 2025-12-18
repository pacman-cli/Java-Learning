package com.pacman.rabbitmqdemo.consumer;

import com.pacman.rabbitmqdemo.dto.User;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQJsonConsumer {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(RabbitMQJsonConsumer.class);

    @RabbitListener(queues = {"${rabbitmq.json.queue.name}"}) //-> if we use this then there will be no messages in
    // the rabbitmq ui queue list
    public void receiveJsonMessage(User user) {
        logger.info("Received message --> {}", user.toString());
    }
}
