package com.pacman.todo.user;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    public UserController( UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<User> getUserById(@PathVariable Long id){
//        Optional<User> user = userService.getUserById(id);
//        return user.map(ResponseEntity::ok).orElse(
//                ResponseEntity.notFound().build()
//        );
//    }

//    @PostMapping //we are using it in AuthController
//    public ResponseEntity<User> createUser(@RequestBody User user){
//        return ResponseEntity.ok(userService.createUser(user));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
