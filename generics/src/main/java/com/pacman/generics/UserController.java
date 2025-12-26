package com.pacman.generics;

import org.apache.catalina.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {
    @GetMapping("/{id}")
    public ApiResponse<User> getUser(@PathVariable Long id){
        User user = new User(id,"Ashik","puspo@gmail.com") ;
        return new ApiResponse<>(true,user,"User fetched successfully.");
    }
}
