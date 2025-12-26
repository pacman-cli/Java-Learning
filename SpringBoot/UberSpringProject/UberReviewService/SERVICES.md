# 🚀 Spring Boot Services - Complete Guide

## 🎯 **What are Services?**

Services contain business logic between controllers and repositories. They handle business rules, validations, and orchestrate data operations.

---

## 🏗️ **Basic Service Structure**

```java
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    
    public UserResponse createUser(CreateUserRequest request) {
        validateRequest(request);
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }
}
```

---

## 🔧 **Service Annotations**

### **@Service and @Transactional**
```java
@Service
@Transactional
public class UserService {
    // All methods are transactional by default
}

@Service
public class UserService {
    
    @Transactional
    public User createUser(CreateUserRequest request) {
        // Transactional method
    }
    
    @Transactional(readOnly = true)
    public List<User> getAllUsers() {
        // Read-only transaction for performance
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void auditUserCreation(User user) {
        // New transaction regardless of caller
    }
}
```

---

## 📊 **Service Layer Patterns**

### **1. CRUD Service**
```java
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    // Create
    public UserResponse createUser(CreateUserRequest request) {
        validateCreateRequest(request);
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        
        User user = userMapper.toEntity(request);
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }
    
    // Read
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }
    
    public Page<UserResponse> getUsers(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<User> users = userRepository.findAll(pageable);
        return users.map(userMapper::toDTO);
    }
    
    // Update
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        userMapper.updateEntityFromRequest(request, user);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }
    
    // Delete
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
    
    private void validateCreateRequest(CreateUserRequest request) {
        if (request.getAge() < 18) {
            throw new ValidationException("User must be at least 18 years old");
        }
    }
}
```

### **2. Business Logic Service**
```java
@Service
@Transactional
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    
    public OrderResponse createOrder(CreateOrderRequest request) {
        // Validate user exists
        User user = userRepository.findById(request.getUserId())
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        // Validate products
        List<Product> products = validateAndGetProducts(request.getProductIds());
        
        // Calculate total
        BigDecimal total = calculateOrderTotal(products, request.getQuantities());
        
        // Check credit limit
        if (total.compareTo(user.getCreditLimit()) > 0) {
            throw new InsufficientCreditException("Insufficient credit limit");
        }
        
        // Create and save order
        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        order.setTotal(total);
        order.setStatus(OrderStatus.PENDING);
        
        Order savedOrder = orderRepository.save(order);
        
        // Update inventory
        updateProductInventory(products, request.getQuantities());
        
        return orderMapper.toDTO(savedOrder);
    }
    
    private List<Product> validateAndGetProducts(List<Long> productIds) {
        List<Product> products = productRepository.findAllById(productIds);
        if (products.size() != productIds.size()) {
            throw new ProductNotFoundException("Some products not found");
        }
        return products;
    }
    
    private BigDecimal calculateOrderTotal(List<Product> products, List<Integer> quantities) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < products.size(); i++) {
            total = total.add(products.get(i).getPrice().multiply(BigDecimal.valueOf(quantities.get(i))));
        }
        return total;
    }
    
    private void updateProductInventory(List<Product> products, List<Integer> quantities) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            product.setStockQuantity(product.getStockQuantity() - quantities.get(i));
            productRepository.save(product);
        }
    }
}
```

---

## 🔄 **Transaction Management**

### **Transaction Propagation**
```java
@Service
@Transactional
public class UserService {
    
    @Transactional(propagation = Propagation.REQUIRED)
    public UserResponse createUser(CreateUserRequest request) {
        // Runs in transaction
        User user = createUserEntity(request);
        auditService.auditUserCreation(user); // Same transaction
        return userMapper.toDTO(user);
    }
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void auditUserCreation(User user) {
        // New transaction
    }
    
    @Transactional(propagation = Propagation.SUPPORTS)
    public List<User> searchUsers(String query) {
        // Can run with or without transaction
    }
}
```

### **Transaction Isolation**
```java
@Service
public class AccountService {
    
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void transferMoney(Long fromId, Long toId, BigDecimal amount) {
        // Prevents dirty reads
    }
    
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void processBatchTransfer(List<TransferRequest> transfers) {
        // Highest isolation level
    }
}
```

---

## 🎨 **Advanced Service Patterns**

### **1. Event Publishing**
```java
@Service
@Transactional
public class UserService {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public UserResponse createUser(CreateUserRequest request) {
        User user = createUserEntity(request);
        User savedUser = userRepository.save(user);
        
        // Publish event
        eventPublisher.publishEvent(new UserCreatedEvent(savedUser));
        
        return userMapper.toDTO(savedUser);
    }
}

// Event classes
public class UserCreatedEvent {
    private final User user;
    public UserCreatedEvent(User user) { this.user = user; }
    public User getUser() { return user; }
}

// Event listeners
@Component
public class UserEventListener {
    
    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        // Send welcome email, create profile, etc.
    }
}
```

### **2. Caching**
```java
@Service
@CacheConfig(cacheNames = "users")
public class UserService {
    
    @Cacheable(key = "#id")
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }
    
    @CacheEvict(key = "#id")
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        // Update logic
    }
    
    @CacheEvict(allEntries = true)
    public void clearAllCaches() {
        // Clear all caches
    }
}
```

### **3. Async Processing**
```java
@Service
@Transactional
public class UserService {
    
    @Async
    public CompletableFuture<String> sendWelcomeEmailAsync(User user) {
        try {
            emailService.sendWelcomeEmail(user.getEmail());
            return CompletableFuture.completedFuture("Email sent successfully");
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
    
    public UserResponse createUser(CreateUserRequest request) {
        User user = createUserEntity(request);
        User savedUser = userRepository.save(user);
        
        // Async operations
        sendWelcomeEmailAsync(savedUser);
        
        return userMapper.toDTO(savedUser);
    }
}
```

---

## 🚀 **Service Best Practices**

### **1. Constructor Injection**
```java
@Service
public class GoodUserService {
    
    private final UserRepository userRepository;
    
    public GoodUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}

// Avoid field injection
@Service
public class BadUserService {
    
    @Autowired
    private UserRepository userRepository; // Not recommended
}
```

### **2. Exception Handling**
```java
@Service
@Transactional
public class UserService {
    
    public UserResponse createUser(CreateUserRequest request) {
        try {
            validateRequest(request);
            User user = createUserEntity(request);
            User savedUser = userRepository.save(user);
            return userMapper.toDTO(savedUser);
        } catch (ValidationException e) {
            logger.error("Validation error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error", e);
            throw new ServiceException("Failed to create user", e);
        }
    }
}
```

### **3. Use DTOs**
```java
@Service
public class UserService {
    
    public UserResponse createUser(CreateUserRequest request) {
        User user = userMapper.toEntity(request);
        User savedUser = userRepository.save(user);
        return userMapper.toDTO(savedUser);
    }
}
```

### **4. Proper Logging**
```java
@Service
@Slf4j
public class UserService {
    
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with email: {}", request.getEmail());
        
        try {
            User user = createUserEntity(request);
            User savedUser = userRepository.save(user);
            
            log.info("User created successfully with ID: {}", savedUser.getId());
            return userMapper.toDTO(savedUser);
            
        } catch (Exception e) {
            log.error("Failed to create user", e);
            throw e;
        }
    }
}
```

---

## 📊 **Complete Service Example**

```java
@Service
@Transactional
@Slf4j
public class UserService {
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher eventPublisher;
    
    public UserService(UserRepository userRepository, UserMapper userMapper,
                      PasswordEncoder passwordEncoder, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.eventPublisher = eventPublisher;
    }
    
    public UserResponse createUser(CreateUserRequest request) {
        log.info("Creating user with email: {}", request.getEmail());
        
        validateCreateRequest(request);
        
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new UserAlreadyExistsException("User already exists");
        }
        
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        
        User savedUser = userRepository.save(user);
        
        // Publish event
        eventPublisher.publishEvent(new UserCreatedEvent(savedUser));
        
        log.info("User created successfully with ID: {}", savedUser.getId());
        return userMapper.toDTO(savedUser);
    }
    
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        return userMapper.toDTO(user);
    }
    
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found"));
        
        userMapper.updateEntityFromRequest(request, user);
        user.setUpdatedAt(LocalDateTime.now());
        
        User updatedUser = userRepository.save(user);
        return userMapper.toDTO(updatedUser);
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteById(id);
    }
    
    private void validateCreateRequest(CreateUserRequest request) {
        if (request.getAge() < 18) {
            throw new ValidationException("User must be at least 18 years old");
        }
        if (request.getPassword().length() < 8) {
            throw new ValidationException("Password must be at least 8 characters");
        }
    }
}
```

---

## 🎯 **Summary**

Spring Boot Services provide:
- Business logic implementation
- Transaction management
- Data validation
- Event publishing
- Caching support
- Async processing

**Key Benefits:**
- Separation of concerns
- Reusable business logic
- Easy testing
- Transaction management

---

**Happy Service Development! 🚀✨**
