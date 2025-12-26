package com.pacman.basictodorestapi;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

//@Primary //there are multiple implementation in same bean then we can mark one
//of them as primary this will prioritise that one first
@Service("anotherTodoService") // to do all the business logic
public class AnotherTodoService implements TodoService {
    @TimeMonitor
    public String doSomething() {
        for (long i=0; i<10000000000L; i++) {}
        return "Do something in AnotherTodoService";
  }
}
