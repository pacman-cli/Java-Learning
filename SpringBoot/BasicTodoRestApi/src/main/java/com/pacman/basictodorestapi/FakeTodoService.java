package com.pacman.basictodorestapi;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service("fakeTodoService")
public class FakeTodoService implements TodoService {
    @Override
    public String doSomething() {
        return "Fake todo doSomething";
    }

}
