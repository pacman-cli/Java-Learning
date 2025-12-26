# 🚀 Quick Reference Cheat Sheet

A fast reference guide for common patterns in this Spring Boot project.

---

## 📋 Table of Contents

1. [Generic Types Quick Reference](#generic-types-quick-reference)
2. [API Response Patterns](#api-response-patterns)
3. [Controller Patterns](#controller-patterns)
4. [Service Patterns](#service-patterns)
5. [Common Code Snippets](#common-code-snippets)
6. [HTTP Status Codes](#http-status-codes)

---

## Generic Types Quick Reference

### What Do The Angle Brackets Mean?

```java
<T>          // T = Generic type (can be any type)
<T, U>       // Multiple generic types
<?>          // Wildcard (unknown type)
List<T>      // List containing type T
```

### Common Generic Type Names

| Symbol | Meaning | Example |
|--------|---------|---------|
| `T` | Type (general purpose) | `ApiResponse<T>` |
| `E` | Element | `List<E>` |
| `K` | Key | `Map<K, V>` |
| `V` | Value | `Map<K, V>` |
| `R` | Result | `Function<T, R>` |

### Examples in This Project

```java
// Single object response
ApiResponse<UserDTO>                        // Contains one UserDTO

// List response
ApiResponse<List<UserDTO>>                  // Contains list of UserDTOs

// Paginated response
ApiResponse<PageResponse<UserDTO>>          // Contains page of UserDTOs

// No data response
ApiResponse<Void>                           // No data, just message

// Boolean response
ApiResponse<Boolean>                        // Contains true/false
```

---

## API Response Patterns

### 1. Success with Data

```java
// Single object
UserDTO user = userService.getUserById(1L);
return ResponseEntity.ok(
    ApiResponse.success(user)
);

// JSON Output:
{
  "success": true,
  "message": "Operation successful",
  "data": { "id": 1, "name": "John" },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 2. Success with Custom Message

```java
UserDTO user = userService.createUser(request);
return ResponseEntity.status(HttpStatus.CREATED).body(
    ApiResponse.success(user, "User created successfully")
);

// JSON Output:
{
  "success": true,
  "message": "User created successfully",
  "data": { user object },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 3. Success without Data

```java
userService.deleteUser(id);
return ResponseEntity.ok(
    ApiResponse.success("User deleted successfully")
);

// JSON Output:
{
  "success": true,
  "message": "User deleted successfully",
  "timestamp": "2024-01-15T10:30:00"
}
```

### 4. Paginated Response

```java
Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
PageResponse<UserDTO> users = userService.getAllUsers(pageable);
return ResponseEntity.ok(
    ApiResponse.success(users)
);

// JSON Output:
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "content": [ { user1 }, { user2 } ],
    "pageNumber": 0,
    "pageSize": 10,
    "totalElements": 100,
    "totalPages": 10,
    "first": true,
    "last": false
  },
  "timestamp": "2024-01-15T10:30:00"
}
```

### 5. Error Response

```java
// In GlobalExceptionHandler or custom exception handler
throw new ResourceNotFoundException("User not found with id: " + id);

// Automatically handled by GlobalExceptionHandler, returns:
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with id: 123",
  "path": "/api/v1/users/123"
}
```

---

## Controller Patterns

### Basic CRUD Operations

```java
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // CREATE - POST
    @PostMapping
    public ResponseEntity<ApiResponse<UserDTO>> create(
        @Valid @RequestBody CreateUserRequest request
    ) {
        UserDTO user = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(user, "User created"));
    }

    // READ ONE - GET
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> getById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success(user));
    }

    // READ ALL - GET with Pagination
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getAll(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        PageResponse<UserDTO> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(ApiResponse.success(users));
    }

    // UPDATE - PUT
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UserDTO>> update(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request
    ) {
        UserDTO user = userService.updateUser(id, request);
        return ResponseEntity.ok(ApiResponse.success(user, "User updated"));
    }

    // DELETE - DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted"));
    }
}
```

### Search and Filter Patterns

```java
// Search by term
@GetMapping("/search")
public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> search(
    @RequestParam String searchTerm,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
) {
    Pageable pageable = PageRequest.of(page, size);
    PageResponse<UserDTO> results = userService.search(searchTerm, pageable);
    return ResponseEntity.ok(ApiResponse.success(results));
}

// Filter by enum (status, role, etc.)
@GetMapping("/status/{status}")
public ResponseEntity<ApiResponse<PageResponse<UserDTO>>> getByStatus(
    @PathVariable UserStatus status,
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size
) {
    Pageable pageable = PageRequest.of(page, size);
    PageResponse<UserDTO> users = userService.getUsersByStatus(status, pageable);
    return ResponseEntity.ok(ApiResponse.success(users));
}

// Check existence
@GetMapping("/exists/email")
public ResponseEntity<ApiResponse<Boolean>> emailExists(
    @RequestParam String email
) {
    boolean exists = userService.existsByEmail(email);
    return ResponseEntity.ok(ApiResponse.success(exists));
}
```

---

## Service Patterns

### Basic Service Implementation

```java
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)  // Default to read-only
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // CREATE (needs @Transactional for write)
    @Override
    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        // 1. Validate
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ConflictException("Email already exists");
        }

        // 2. Build entity
        User user = User.builder()
            .email(request.getEmail())
            .firstName(request.getFirstName())
            .build();

        // 3. Save
        User saved = userRepository.save(user);

        // 4. Convert to DTO and return
        return userMapper.toDTO(saved);
    }

    // READ ONE
    @Override
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        return userMapper.toDTO(user);
    }

    // READ ALL with Pagination
    @Override
    public PageResponse<UserDTO> getAllUsers(Pageable pageable) {
        Page<User> userPage = userRepository.findAll(pageable);
        List<UserDTO> userDTOs = userMapper.toDTOList(userPage.getContent());
        return buildPageResponse(userPage, userDTOs);
    }

    // UPDATE
    @Override
    @Transactional
    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        
        // Update fields
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        
        User updated = userRepository.save(user);
        return userMapper.toDTO(updated);
    }

    // DELETE
    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + id));
        userRepository.delete(user);
    }

    // Helper: Build PageResponse
    private <T> PageResponse<T> buildPageResponse(Page<?> page, List<T> content) {
        return PageResponse.<T>builder()
            .content(content)
            .pageNumber(page.getNumber())
            .pageSize(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .last(page.isLast())
            .first(page.isFirst())
            .hasNext(page.hasNext())
            .hasPrevious(page.hasPrevious())
            .numberOfElements(page.getNumberOfElements())
            .empty(page.isEmpty())
            .build();
    }
}
```

---

## Common Code Snippets

### 1. Create Pageable with Sorting

```java
// Simple pagination
Pageable pageable = PageRequest.of(page, size);

// With ascending sort
Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());

// With descending sort
Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

// Multiple sort fields
Pageable pageable = PageRequest.of(page, size, 
    Sort.by("lastName").ascending()
        .and(Sort.by("firstName").ascending())
);

// From string (e.g., "id,desc")
String[] sortParams = sortString.split(",");
String field = sortParams[0];
Sort.Direction direction = sortParams.length > 1 && 
    sortParams[1].equalsIgnoreCase("desc") 
    ? Sort.Direction.DESC 
    : Sort.Direction.ASC;
Pageable pageable = PageRequest.of(page, size, Sort.by(direction, field));
```

### 2. Custom Exceptions

```java
// Throw exceptions (handled by GlobalExceptionHandler)

// 404 Not Found
throw new ResourceNotFoundException("User not found with id: " + id);

// 409 Conflict
throw new ConflictException("Email already exists: " + email);

// 400 Bad Request
throw new BadRequestException("Invalid input data");

// 401 Unauthorized
throw new UnauthorizedException("Authentication required");

// 403 Forbidden
throw new ForbiddenException("Access denied");
```

### 3. Validation Annotations

```java
public class CreateUserRequest {
    
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be 2-50 characters")
    private String firstName;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
    
    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Must be at least 18 years old")
    @Max(value = 120, message = "Age must be realistic")
    private Integer age;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phoneNumber;
}
```

### 4. Repository Query Methods

```java
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find by single field
    Optional<User> findByEmail(String email);
    
    // Find by multiple fields
    Optional<User> findByEmailAndDeleted(String email, Boolean deleted);
    
    // Exists check
    boolean existsByEmail(String email);
    boolean existsByEmailAndDeletedFalse(String email);
    
    // Find with pagination
    Page<User> findByStatus(UserStatus status, Pageable pageable);
    
    // Search (custom query)
    @Query("SELECT u FROM User u WHERE " +
           "LOWER(u.firstName) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
           "LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%'))")
    Page<User> search(@Param("term") String term, Pageable pageable);
    
    // Count
    long countByStatus(UserStatus status);
    long countByRole(UserRole role);
}
```

---

## HTTP Status Codes

### Success Codes (2xx)

```java
// 200 OK - Standard success response
return ResponseEntity.ok(data);
return ResponseEntity.status(HttpStatus.OK).body(data);

// 201 Created - Resource created successfully
return ResponseEntity.status(HttpStatus.CREATED).body(data);

// 204 No Content - Success but no data to return
return ResponseEntity.noContent().build();
```

### Client Error Codes (4xx)

```java
// 400 Bad Request
throw new BadRequestException("Invalid data");

// 401 Unauthorized
throw new UnauthorizedException("Login required");

// 403 Forbidden
throw new ForbiddenException("Access denied");

// 404 Not Found
throw new ResourceNotFoundException("User not found");

// 409 Conflict
throw new ConflictException("Email already exists");
```

### Server Error Codes (5xx)

```java
// 500 Internal Server Error
// Automatically handled by GlobalExceptionHandler for unexpected exceptions
```

---

## Testing Patterns

### Controller Tests (Integration)

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreateUser() throws Exception {
        String requestBody = """
            {
              "firstName": "John",
              "lastName": "Doe",
              "email": "john@example.com"
            }
            """;

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.success").value(true))
            .andExpect(jsonPath("$.data.email").value("john@example.com"));
    }
}
```

### Service Tests (Unit)

```java
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldGetUserById() {
        // Given
        User user = User.builder().id(1L).email("test@test.com").build();
        UserDTO userDTO = UserDTO.builder().id(1L).email("test@test.com").build();
        
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toDTO(user)).thenReturn(userDTO);

        // When
        UserDTO result = userService.getUserById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@test.com");
        verify(userRepository).findById(1L);
    }
}
```

---

## Common Lombok Annotations

```java
@Data                  // Generates getters, setters, toString, equals, hashCode
@Getter                // Generates only getters
@Setter                // Generates only setters
@NoArgsConstructor     // Generates no-args constructor
@AllArgsConstructor    // Generates constructor with all fields
@RequiredArgsConstructor  // Constructor for final/required fields
@Builder               // Generates builder pattern
@Slf4j                 // Generates logger (log variable)
@ToString              // Generates toString method
@EqualsAndHashCode     // Generates equals and hashCode
```

---

## Quick Tips

### 1. Always Use DTOs

```java
// ❌ Don't return entities directly
@GetMapping("/{id}")
public User getUser(@PathVariable Long id) {
    return userRepository.findById(id).orElseThrow();
}

// ✅ Use DTOs
@GetMapping("/{id}")
public ResponseEntity<ApiResponse<UserDTO>> getUser(@PathVariable Long id) {
    UserDTO user = userService.getUserById(id);
    return ResponseEntity.ok(ApiResponse.success(user));
}
```

### 2. Use @Transactional Properly

```java
@Service
@Transactional(readOnly = true)  // Default for all methods
public class UserService {

    // Read-only operations inherit the class-level @Transactional
    public UserDTO getUserById(Long id) { ... }

    // Write operations override with @Transactional
    @Transactional
    public UserDTO createUser(CreateUserRequest request) { ... }
}
```

### 3. Validate Input

```java
// Always use @Valid for request bodies
@PostMapping
public ResponseEntity<ApiResponse<UserDTO>> create(
    @Valid @RequestBody CreateUserRequest request  // @Valid triggers validation
) {
    // Validation errors automatically handled by GlobalExceptionHandler
}
```

### 4. Use Meaningful Exceptions

```java
// ❌ Generic exceptions
throw new RuntimeException("Something went wrong");

// ✅ Specific exceptions
throw new ResourceNotFoundException("User not found with id: " + id);
throw new ConflictException("Email already exists: " + email);
```

---

## File Structure Reference

```
src/main/java/com/zedcode/
├── ZedCodeApplication.java          # Main application
├── common/
│   ├── constants/                   # Constants
│   ├── dto/                         # Common DTOs (ApiResponse, PageResponse)
│   ├── exception/                   # Custom exceptions
│   └── util/                        # Utility classes
├── config/                          # Configuration classes
└── module/
    └── user/
        ├── controller/              # REST endpoints
        ├── dto/                     # Request/Response DTOs
        ├── entity/                  # JPA entities
        ├── mapper/                  # MapStruct mappers
        ├── repository/              # Spring Data repositories
        └── service/                 # Business logic
```

---

## Next Steps

- Read `GENERICS_GUIDE.md` for in-depth explanation of generic types
- Check `API_GUIDE.md` for API usage examples
- See `ARCHITECTURE.md` for project structure details
- Read inline comments in `ApiResponse.java` and `PageResponse.java`

---

**Happy Coding! 🚀**