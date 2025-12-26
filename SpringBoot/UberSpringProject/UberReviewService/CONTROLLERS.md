# 🎮 Spring Boot Controllers - Complete Guide

## 🎯 **What are Controllers?**

Controllers are the entry points for HTTP requests in Spring Boot applications. They handle incoming requests, process them, and return appropriate responses.

---

## 🏗️ **Basic Controller Structure**

### **Simple Controller Example**
```java
@RestController
@RequestMapping("/api")
public class SimpleController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }
    
    @PostMapping("/echo")
    public String echo(@RequestBody String message) {
        return "Echo: " + message;
    }
}
```

---

## 📡 **HTTP Method Annotations**

### **@GetMapping**
```java
@GetMapping("/users")
public List<User> getAllUsers() {
    return userService.findAll();
}

@GetMapping("/users/{id}")
public User getUserById(@PathVariable Long id) {
    return userService.findById(id);
}

@GetMapping("/users/search")
public List<User> searchUsers(@RequestParam String name) {
    return userService.findByName(name);
}
```

### **@PostMapping**
```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest request) {
    User user = userService.createUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
}
```

### **@PutMapping**
```java
@PutMapping("/users/{id}")
public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
    User user = userService.updateUser(id, request);
    return ResponseEntity.ok(user);
}
```

### **@DeleteMapping**
```java
@DeleteMapping("/users/{id}")
public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
    return ResponseEntity.noContent().build();
}
```

### **@PatchMapping**
```java
@PatchMapping("/users/{id}")
public ResponseEntity<User> partialUpdateUser(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
    User user = userService.partialUpdate(id, updates);
    return ResponseEntity.ok(user);
}
```

---

## 🔧 **Request Parameter Handling**

### **@PathVariable**
```java
@GetMapping("/users/{id}/orders/{orderId}")
public Order getUserOrder(@PathVariable Long id, @PathVariable Long orderId) {
    return orderService.findByUserAndOrder(id, orderId);
}

@GetMapping("/users/{id}/posts/{postId}/comments/{commentId}")
public Comment getComment(@PathVariable Long id, @PathVariable Long postId, @PathVariable Long commentId) {
    return commentService.findById(commentId);
}
```

### **@RequestParam**
```java
@GetMapping("/users")
public Page<User> getUsers(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(defaultValue = "id") String sortBy,
    @RequestParam(defaultValue = "ASC") String sortDirection,
    @RequestParam(required = false) String name,
    @RequestParam(required = false) String email
) {
    return userService.findUsers(page, size, sortBy, sortDirection, name, email);
}
```

### **@RequestBody**
```java
@PostMapping("/users")
public User createUser(@RequestBody @Valid CreateUserRequest request) {
    return userService.createUser(request);
}

@PutMapping("/users/{id}")
public User updateUser(@PathVariable Long id, @RequestBody @Valid UpdateUserRequest request) {
    return userService.updateUser(id, request);
}
```

---

## 📋 **Response Handling**

### **ResponseEntity**
```java
@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable Long id) {
    User user = userService.findById(id);
    if (user != null) {
        return ResponseEntity.ok(user);
    } else {
        return ResponseEntity.notFound().build();
    }
}

@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest request) {
    User user = userService.createUser(request);
    return ResponseEntity.status(HttpStatus.CREATED)
        .header("Location", "/api/users/" + user.getId())
        .body(user);
}
```

### **HTTP Status Codes**
```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest request) {
    try {
        User user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    } catch (ValidationException e) {
        return ResponseEntity.badRequest().build();
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
```

---

## ✅ **Validation and Error Handling**

### **Bean Validation**
```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody @Valid CreateUserRequest request) {
    User user = userService.createUser(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(user);
}

public class CreateUserRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    
    @Min(value = 18, message = "Age must be at least 18")
    @Max(value = 120, message = "Age must be less than 120")
    private Integer age;
}
```

### **Global Exception Handling**
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        ErrorResponse error = new ErrorResponse("VALIDATION_ERROR", e.getMessage());
        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException e) {
        ErrorResponse error = new ErrorResponse("NOT_FOUND", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception e) {
        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "An unexpected error occurred");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
```

---

## 🎨 **Advanced Controller Features**

### **@CrossOrigin**
```java
@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:3000", "https://myapp.com"})
public class UserController {
    // Controller methods
}

// Or for specific methods
@GetMapping("/users")
@CrossOrigin(origins = "http://localhost:3000")
public List<User> getUsers() {
    return userService.findAll();
}
```

### **@ResponseStatus**
```java
@PostMapping("/users")
@ResponseStatus(HttpStatus.CREATED)
public User createUser(@RequestBody @Valid CreateUserRequest request) {
    return userService.createUser(request);
}

@DeleteMapping("/users/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
}
```

### **Async Controllers**
```java
@RestController
@RequestMapping("/api/async")
public class AsyncController {
    
    @GetMapping("/users")
    public CompletableFuture<List<User>> getUsersAsync() {
        return CompletableFuture.supplyAsync(() -> userService.findAll());
    }
    
    @PostMapping("/users")
    public CompletableFuture<User> createUserAsync(@RequestBody @Valid CreateUserRequest request) {
        return CompletableFuture.supplyAsync(() -> userService.createUser(request));
    }
}
```

---

## 📊 **Complete Controller Example**

```java
@RestController
@RequestMapping("/api/users")
@Validated
@CrossOrigin(origins = {"http://localhost:3000"})
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
    
    private final UserService userService;
    private final UserMapper userMapper;
    
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }
    
    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve a paginated list of users")
    public ResponseEntity<PageResponse<UserResponse>> getUsers(
            @Valid UserSearchRequest searchRequest,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDirection) {
        
        Page<User> users = userService.searchUsers(searchRequest, page, size, sortBy, sortDirection);
        List<UserResponse> responses = userMapper.toDTOList(users.getContent());
        PageResponse<UserResponse> pageResponse = PageResponse.from(users);
        pageResponse.setContent(responses);
        
        return ResponseEntity.ok(pageResponse);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable @Min(1) Long id) {
        User user = userService.findById(id);
        UserResponse response = userMapper.toDTO(user);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    @Operation(summary = "Create new user", description = "Create a new user account")
    public ResponseEntity<UserResponse> createUser(@RequestBody @Valid CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        User savedUser = userService.createUser(user);
        UserResponse response = userMapper.toDTO(savedUser);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .header("Location", "/api/users/" + savedUser.getId())
            .body(response);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update user", description = "Update an existing user's information")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable @Min(1) Long id,
            @RequestBody @Valid UpdateUserRequest request) {
        
        User updatedUser = userService.updateUser(id, request);
        UserResponse response = userMapper.toDTO(updatedUser);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Delete a user account")
    public ResponseEntity<Void> deleteUser(@PathVariable @Min(1) Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}")
    @Operation(summary = "Partial update user", description = "Partially update user information")
    public ResponseEntity<UserResponse> partialUpdateUser(
            @PathVariable @Min(1) Long id,
            @RequestBody Map<String, Object> updates) {
        
        User updatedUser = userService.partialUpdate(id, updates);
        UserResponse response = userMapper.toDTO(updatedUser);
        return ResponseEntity.ok(response);
    }
}
```

---

## 🚀 **Best Practices**

### **1. Use Constructor Injection**
```java
@RestController
public class GoodController {
    private final UserService userService;
    
    public GoodController(UserService userService) {
        this.userService = userService;
    }
}

// Avoid field injection
@RestController
public class BadController {
    @Autowired
    private UserService userService; // Not recommended
}
```

### **2. Proper Exception Handling**
```java
@ControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.toList());
        
        ValidationErrorResponse error = new ValidationErrorResponse("VALIDATION_ERROR", errors);
        return ResponseEntity.badRequest().body(error);
    }
}
```

### **3. Use DTOs for Request/Response**
```java
// Request DTO
public class CreateUserRequest {
    @NotBlank(message = "Name is required")
    private String name;
    
    @Email(message = "Invalid email format")
    private String email;
}

// Response DTO
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}
```

### **4. Proper HTTP Status Codes**
- `200 OK` - Successful GET, PUT, PATCH
- `201 Created` - Successful POST
- `204 No Content` - Successful DELETE
- `400 Bad Request` - Validation errors
- `401 Unauthorized` - Authentication required
- `403 Forbidden` - Insufficient permissions
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server errors

---

## 🎯 **Summary**

Controllers are the heart of REST APIs in Spring Boot:
- Handle HTTP requests and responses
- Use appropriate HTTP methods and status codes
- Implement proper validation and error handling
- Follow REST conventions
- Use DTOs for data transfer
- Implement proper exception handling

---

**Happy Controller Development! 🎮✨**
