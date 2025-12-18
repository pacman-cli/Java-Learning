package com.pacman.blogify.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "forward:/welcome.html";
    }

    @GetMapping("/blog")
    public String blog() {
        return "forward:/index.html";
    }
}