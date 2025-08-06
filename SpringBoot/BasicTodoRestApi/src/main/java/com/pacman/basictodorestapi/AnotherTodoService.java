package com.pacman.basictodorestapi;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

//@Primary //there are multiple implementation in same bean then we can mark one
//of them as primary this will prioritise that one first
@Service("anotherTodoService") // to do all the business logic
public class AnotherTodoService implements TodoService {
  @Override
  public String doSomething() {
    return "Do something in AnotherTodoService";
  }
}
