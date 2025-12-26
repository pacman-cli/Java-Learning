package com.pacman.basictodorestapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.EnableLoadTimeWeaving;

@SpringBootApplication
@EnableAspectJAutoProxy
public class BasicTodoRestApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(BasicTodoRestApiApplication.class, args);
  }
}
