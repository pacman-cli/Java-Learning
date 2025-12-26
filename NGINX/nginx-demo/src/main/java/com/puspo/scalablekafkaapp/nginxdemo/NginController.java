package com.puspo.scalablekafkaapp.nginxdemo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class NginController {
    @GetMapping("hello")
    public String get() {
        return "Hello From NginX";
    }
}
