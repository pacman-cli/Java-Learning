package com.pacman.rabbitDemo.config;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Component
public class MemoryStorage {
     private final List<String> messages = new LinkedList<>();

    public synchronized void addMessage(String msg){
        messages.add(msg);
    }

    public synchronized List<String> getMessages(){
        return Collections.unmodifiableList(messages);
    }
}
