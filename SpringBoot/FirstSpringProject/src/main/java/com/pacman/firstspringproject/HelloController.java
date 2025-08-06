package com.pacman.firstspringproject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "<h1><b>Hello World</b></h1>";
    }
    @GetMapping("/")
    public String index(){
        return "<h1><b>Welcome to the Home Page.</b></h1>";
    }
}
