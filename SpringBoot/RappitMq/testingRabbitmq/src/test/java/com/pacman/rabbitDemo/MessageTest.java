package com.pacman.rabbitDemo;

import com.pacman.rabbitDemo.services.MessageProducer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MessageTest {
    @Autowired
    private MessageProducer messageProducer;

    @Test
    public void testMessage(){
        messageProducer.sendMessage("Hello World");
    }
}
