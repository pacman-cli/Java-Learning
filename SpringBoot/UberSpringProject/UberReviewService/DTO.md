# 📦 DTO (Data Transfer Object) - Complete Guide

## 🎯 **What is a DTO?**

A **Data Transfer Object (DTO)** is a design pattern used to transfer data between different layers of an application. It's a simple object that contains only the data needed for a specific operation, without any business logic.

### **Why Use DTOs?**

1. **Data Encapsulation**: Hide internal entity structure from external clients
2. **Performance**: Transfer only necessary data, reducing network overhead
3. **Security**: Control what data is exposed to clients
4. **Flexibility**: Different DTOs for different use cases
5. **Versioning**: Handle API changes without affecting entities

---

## 🏗️ **DTO vs Entity**

| Aspect | Entity | DTO |
|--------|--------|-----|
| **Purpose** | Database mapping | Data transfer |
| **Validation** | JPA annotations | Bean validation |
| **Business Logic** | May contain | Never contains |
| **Serialization** | Full object | Selective fields |
| **Lifecycle** | Managed by JPA | Simple POJO |

---

## 📝 **Basic DTO Structure**

### **Simple DTO Example**
```java
public class DriverDTO {
    private Long id;
    private String name;
    private String licenceNumber;
    private String phoneNumber;
    
    // Constructors
    public DriverDTO() {}
    
    public DriverDTO(Long id, String name, String licenceNumber, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.licenceNumber = licenceNumber;
        this.phoneNumber = phoneNumber;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getLicenceNumber() { return licenceNumber; }
    public void setLicenceNumber(String licenceNumber) { this.licenceNumber = licenceNumber; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}
```

### **Using Lombok for Cleaner Code**
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDTO {
    private Long id;
    private String name;
    private String licenceNumber;
    private String phoneNumber;
}
```

---

## 🔄 **DTO Conversion Methods**

### **1. Manual Conversion**
```java
// Entity to DTO
public DriverDTO toDTO(Driver driver) {
    return DriverDTO.builder()
        .id(driver.getId())
        .name(driver.getName())
        .licenceNumber(driver.getLicenceNumber())
        .phoneNumber(driver.getPhoneNumber())
        .build();
}

// DTO to Entity
public Driver toEntity(DriverDTO dto) {
    return Driver.builder()
        .id(dto.getId())
        .name(dto.getName())
        .licenceNumber(dto.getLicenceNumber())
        .phoneNumber(dto.getPhoneNumber())
        .build();
}
```

### **2. Using MapStruct (Recommended)**
```java
@Mapper(componentModel = "spring")
public interface DriverMapper {
    DriverDTO toDTO(Driver driver);
    Driver toEntity(DriverDTO dto);
    List<DriverDTO> toDTOList(List<Driver> drivers);
    List<Driver> toEntityList(List<DriverDTO> dtos);
}
```

### **3. Using ModelMapper**
```java
@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
            .setSkipNullEnabled(true)
            .setPropertyCondition(context -> 
                context.getSource() != null);
        return mapper;
    }
}

// Usage
@Service
public class DriverService {
    
    @Autowired
    private ModelMapper modelMapper;
    
    public DriverDTO convertToDTO(Driver driver) {
        return modelMapper.map(driver, DriverDTO.class);
    }
}
```

---

## 📊 **Different Types of DTOs**

### **1. Request DTOs (Input)**
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateDriverRequest {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @NotBlank(message = "License number is required")
    @Pattern(regexp = "^[A-Z]{2}\\d{6}$", message = "Invalid license format")
    private String licenceNumber;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phoneNumber;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDriverRequest {
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phoneNumber;
}
```

### **2. Response DTOs (Output)**
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverResponse {
    private Long id;
    private String name;
    private String licenceNumber;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DriverDetailResponse {
    private Long id;
    private String name;
    private String licenceNumber;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<BookingSummary> recentBookings;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookingSummary {
        private Long id;
        private String status;
        private LocalDateTime startTime;
        private Double rating;
    }
}
```

### **3. Search/Filter DTOs**
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DriverSearchRequest {
    private String name;
    private String licenceNumber;
    private String phoneNumber;
    private LocalDateTime createdAfter;
    private LocalDateTime createdBefore;
    private Integer page = 0;
    private Integer size = 10;
    private String sortBy = "id";
    private String sortDirection = "ASC";
}
```

---

## 🎨 **Advanced DTO Patterns**

### **1. Generic Response Wrapper**
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private List<String> errors;
    private LocalDateTime timestamp;
    
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
            .success(true)
            .data(data)
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    public static <T> ApiResponse<T> error(String message, List<String> errors) {
        return ApiResponse.<T>builder()
            .success(false)
            .message(message)
            .errors(errors)
            .timestamp(LocalDateTime.now())
            .build();
    }
}
```

### **2. Pagination DTO**
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;
    private boolean hasNext;
    private boolean hasPrevious;
    
    public static <T> PageResponse<T> from(Page<T> page) {
        return PageResponse.<T>builder()
            .content(page.getContent())
            .pageNumber(page.getNumber())
            .pageSize(page.getSize())
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .first(page.isFirst())
            .last(page.isLast())
            .hasNext(page.hasNext())
            .hasPrevious(page.hasPrevious())
            .build();
    }
}
```

### **3. Nested DTOs**
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {
    private Long id;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer totalTime;
    private DriverSummary driver;
    private PassengerSummary passenger;
    private ReviewSummary review;
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DriverSummary {
        private Long id;
        private String name;
        private String licenceNumber;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PassengerSummary {
        private Long id;
        private String name;
    }
    
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReviewSummary {
        private Long id;
        private String content;
        private Double rating;
    }
}
```

---

## 🔧 **DTO Implementation in Controllers**

### **Controller with DTOs**
```java
@RestController
@RequestMapping("/api/drivers")
@Validated
public class DriverController {
    
    @Autowired
    private DriverService driverService;
    
    @Autowired
    private DriverMapper driverMapper;
    
    @PostMapping
    public ResponseEntity<ApiResponse<DriverResponse>> createDriver(
            @Valid @RequestBody CreateDriverRequest request) {
        
        Driver driver = driverMapper.toEntity(request);
        Driver savedDriver = driverService.createDriver(driver);
        DriverResponse response = driverMapper.toDTO(savedDriver);
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(ApiResponse.success(response));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<DriverResponse>> updateDriver(
            @PathVariable Long id,
            @Valid @RequestBody UpdateDriverRequest request) {
        
        Driver updatedDriver = driverService.updateDriver(id, request);
        DriverResponse response = driverMapper.toDTO(updatedDriver);
        
        return ResponseEntity.ok(ApiResponse.success(response));
    }
    
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<DriverResponse>>> getDrivers(
            @Valid DriverSearchRequest searchRequest) {
        
        Page<Driver> drivers = driverService.searchDrivers(searchRequest);
        List<DriverResponse> responses = driverMapper.toDTOList(drivers.getContent());
        PageResponse<DriverResponse> pageResponse = PageResponse.from(drivers);
        pageResponse.setContent(responses);
        
        return ResponseEntity.ok(ApiResponse.success(pageResponse));
    }
}
```

---

## ✅ **DTO Best Practices**

### **1. Validation**
- Use Bean Validation annotations (`@NotNull`, `@Size`, `@Pattern`)
- Validate at the controller level, not in services
- Provide meaningful error messages

### **2. Naming Conventions**
- `CreateXxxRequest` for POST requests
- `UpdateXxxRequest` for PUT requests
- `XxxResponse` for responses
- `XxxSearchRequest` for search operations

### **3. Immutability**
```java
@Value
@Builder
public class ImmutableDriverDTO {
    Long id;
    String name;
    String licenceNumber;
    String phoneNumber;
}
```

### **4. Documentation**
```java
@Schema(description = "Driver information for API responses")
public class DriverResponse {
    
    @Schema(description = "Unique identifier for the driver", example = "1")
    private Long id;
    
    @Schema(description = "Driver's full name", example = "John Smith")
    private String name;
    
    @Schema(description = "Driver's license number", example = "DL001234")
    private String licenceNumber;
}
```

---

## 🚀 **DTO with Spring Boot Features**

### **1. Using @JsonView**
```java
public class Views {
    public interface Summary {}
    public interface Detail extends Summary {}
}

public class DriverDTO {
    @JsonView(Views.Summary.class)
    private Long id;
    
    @JsonView(Views.Summary.class)
    private String name;
    
    @JsonView(Views.Detail.class)
    private String licenceNumber;
    
    @JsonView(Views.Detail.class)
    private String phoneNumber;
}

// Usage in controller
@GetMapping("/{id}")
@JsonView(Views.Detail.class)
public DriverDTO getDriverDetail(@PathVariable Long id) {
    // Returns all fields
}

@GetMapping
@JsonView(Views.Summary.class)
public List<DriverDTO> getDrivers() {
    // Returns only summary fields
}
```

### **2. Using @JsonFilter**
```java
@JsonFilter("driverFilter")
public class DriverDTO {
    private Long id;
    private String name;
    private String licenceNumber;
    private String phoneNumber;
}

// Usage in controller
@GetMapping("/{id}")
public MappingJacksonValue getDriver(@PathVariable Long id, @RequestParam String[] fields) {
    DriverDTO driver = driverService.getDriver(id);
    MappingJacksonValue wrapper = new MappingJacksonValue(driver);
    
    SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter.filterOutAllExcept(fields);
    FilterProvider filters = new SimpleFilterProvider().addFilter("driverFilter", filter);
    wrapper.setFilters(filters);
    
    return wrapper;
}
```

---

## 📚 **Complete Example Implementation**

### **DriverDTO.java**
```java
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Driver Data Transfer Object")
public class DriverDTO {
    
    @Schema(description = "Unique identifier", example = "1")
    private Long id;
    
    @Schema(description = "Driver's full name", example = "John Smith")
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;
    
    @Schema(description = "Driver's license number", example = "DL001234")
    @NotBlank(message = "License number is required")
    @Pattern(regexp = "^[A-Z]{2}\\d{6}$", message = "Invalid license format")
    private String licenceNumber;
    
    @Schema(description = "Driver's phone number", example = "+1234567890")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number")
    private String phoneNumber;
    
    @Schema(description = "Creation timestamp")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp")
    private LocalDateTime updatedAt;
}
```

### **DriverMapper.java**
```java
@Mapper(componentModel = "spring")
public interface DriverMapper {
    
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    DriverDTO toDTO(Driver driver);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    Driver toEntity(DriverDTO dto);
    
    List<DriverDTO> toDTOList(List<Driver> drivers);
    List<Driver> toEntityList(List<DriverDTO> dtos);
}
```

---

## 🎯 **Summary**

DTOs are essential for:
- **Clean API design**
- **Data validation**
- **Performance optimization**
- **Security control**
- **API versioning**

**Key Points:**
1. Use DTOs to separate external API from internal entities
2. Implement proper validation at the DTO level
3. Use mapping libraries (MapStruct, ModelMapper) for conversions
4. Follow consistent naming conventions
5. Document DTOs with OpenAPI annotations
6. Consider using generic response wrappers for consistency

---

**Happy DTO Implementation! 🚀**
