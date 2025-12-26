package com.pacman.firstspringproject;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
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
package com.pacman.firstspringproject;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Todo {
    @Id //->this makes id as primary key
    @GeneratedValue(strategy = GenerationType.SEQUENCE) //-> this works as auto increment
    @Column(nullable = false)//->this is not null
    private Long id;

    @Column
    private String title;

    @Column
    private boolean completed;

}

