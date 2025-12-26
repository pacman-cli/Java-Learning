package com.puspo.scalablekafkaapp.thumbnailservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ThumbnailServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThumbnailServiceApplication.class, args);
//        SpringApplication app = new SpringApplication(ThumbnailServiceApplication.class);
//        app.setWebApplicationType(WebApplicationType.NONE); // <- disables web server
//        app.run(args);
    }

}
