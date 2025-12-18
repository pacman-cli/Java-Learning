package com.pacman.concepts;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

//@Component // Generic Spring-managed bean
//@Repository // DAO layer — translates exception
//@Service // Business logic layer
//@Controller // Web layer (Spring MVC)
//@RestController     // @Controller + @ResponseBody
//@Configuration // Defines @Bean methods
@Configuration
public class AppConfig {


    //    Beans are singleton by default. Use @Scope for prototype/session/request.
    @Bean
    @Scope("prototype") // or "singleton" (default)
    public MyService myService() {
        return new MyService();
    }

    @Bean
    public InitializingBean myInitializingBean() {
        return () -> System.out.println("InitializingBean invoked");
    }
}
