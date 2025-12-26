# Optional Next Steps (Action Guide)

This file lists concise, actionable steps so you can implement them yourself. Use the checklists and code templates as references.

---

## 1) Update Remaining Controllers to use DTOs + Adapters

Targets: `PassengerController`, `BookingController`

Steps:
- Replace entity request/response bodies with DTOs.
- Inject and use the corresponding Adapter.
- Map entity <-> DTO at controller boundaries only.
- Keep service/repository logic unchanged.

Endpoints (suggested):
- PassengerController:
  - GET `/api/passengers` → `List<PassengerDto>`
  - GET `/api/passengers/{id}` → `PassengerDto`
  - POST `/api/passengers` (PassengerDto) → `PassengerDto`
  - PUT `/api/passengers/{id}` (PassengerDto) → `PassengerDto`
  - DELETE `/api/passengers/{id}` → 204
- BookingController:
  - GET `/api/bookings` → `List<BookingDto>`
  - GET `/api/bookings/{id}` → `BookingDto`
  - GET `/api/bookings/status/{status}` → `List<BookingDto>`
  - POST `/api/bookings` (BookingDto) → `BookingDto`
  - PUT `/api/bookings/{id}` (BookingDto) → `BookingDto`
  - DELETE `/api/bookings/{id}` → 204

Controller template (sketch):
```java
@RestController
@RequestMapping("/api/passengers")
public class PassengerController {
    private final PassengerRepository passengerRepository;
    private final PassengerAdapter passengerAdapter;

    public PassengerController(PassengerRepository passengerRepository, PassengerAdapter passengerAdapter) {
        this.passengerRepository = passengerRepository;
        this.passengerAdapter = passengerAdapter;
    }

    @GetMapping
    public ResponseEntity<List<PassengerDto>> all() {
        var list = passengerRepository.findAll().stream()
            .map(passengerAdapter::toDto)
            .toList();
        return ResponseEntity.ok(list);
    }

    @PostMapping
    public ResponseEntity<PassengerDto> create(@RequestBody PassengerDto dto) {
        var entity = passengerAdapter.toEntity(dto);
        var saved = passengerRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(passengerAdapter.toDto(saved));
    }
}
```

---

## 2) Add Validation to DTOs (Bean Validation)

Dependency:
- Spring Boot 3+ already includes Jakarta Validation via `spring-boot-starter-validation`. If missing, add:
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-validation'
}
```

Annotate DTOs (examples):
```java
public class DriverDto {
    @NotBlank
    private String name;

    @NotBlank
    private String licenceNumber;

    @Pattern(regexp = "^[+0-9 -]{7,20}$")
    private String phoneNumber;
}

public class ReviewDto {
    @NotBlank
    private String content;

    @DecimalMin("0.0") @DecimalMax("5.0")
    private Double rating;
}
```

Use `@Valid` in controllers:
```java
@PostMapping
public ResponseEntity<DriverDto> create(@Valid @RequestBody DriverDto dto) { ... }
```

Global validation error handling (sketch):
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(err -> errors.put(err.getField(), err.getDefaultMessage()));
        return errors;
    }
}
```

Checklist:
- [ ] Add annotations in DTOs
- [ ] Add `@Valid` to controller methods
- [ ] Add a `@RestControllerAdvice` for 400 responses

---

## 3) Tests for Adapters and Controllers

Unit tests (Adapters):
- Use JUnit + AssertJ + Mockito.
- Verify field-by-field mapping, null handling, date conversions.

Template:
```java
@ExtendWith(MockitoExtension.class)
class DriverAdapterTest {
    private final DriverAdapter adapter = new DriverAdapter();

    @Test
    void toDto_mapsAllFields() {
        var driver = Driver.builder().name("John").licenceNumber("LIC-1").phoneNumber("+123").build();
        driver.setId(10L);
        var dto = adapter.toDto(driver);
        assertThat(dto.getId()).isEqualTo(10L);
        assertThat(dto.getName()).isEqualTo("John");
    }
}
```

Slice tests (Controllers):
- Use `@WebMvcTest(Controller.class)` and `MockMvc`.
- Mock repositories/services and verify JSON contracts of DTOs.

Template:
```java
@WebMvcTest(controllers = DriverController.class)
class DriverControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockBean private DriverRepository repo;
    @MockBean private DriverAdapter adapter;

    @Test
    void getAllDrivers_returnsDtos() throws Exception {
        when(repo.findAll()).thenReturn(List.of(new Driver()));
        when(adapter.toDto(any())).thenReturn(DriverDto.builder().id(1L).name("John").build());

        mockMvc.perform(get("/api/drivers"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].name").value("John"));
    }
}
```

Integration tests:
- Use `@SpringBootTest(webEnvironment = RANDOM_PORT)` + `TestRestTemplate`.
- Hit real endpoints; assert DTO shapes and values.

Checklist:
- [ ] Unit tests for each Adapter (entity→DTO, DTO→entity)
- [ ] WebMvc slice tests for each Controller
- [ ] Happy-path + validation error scenarios

---

## 4) Add Caching for Frequently Accessed DTOs

Dependency (optional):
```gradle
dependencies {
    implementation 'org.springframework.boot:spring-boot-starter-cache'
    // Optional: Caffeine cache
    implementation 'com.github.ben-manes.caffeine:caffeine:3.1.8'
}
```

Enable caching:
```java
@SpringBootApplication
@EnableCaching
public class UberReviewServiceApplication { }
```

Annotate service methods (example):
```java
@Service
public class DriverReadService {
    private final DriverRepository repo;
    private final DriverAdapter adapter;

    @Cacheable(value = "drivers", key = "#id")
    public DriverDto getDriver(Long id) {
        return repo.findById(id).map(adapter::toDto).orElse(null);
    }

    @CacheEvict(value = "drivers", key = "#id")
    public void evictDriver(Long id) { /* no-op */ }
}
```

Basic CacheManager (Caffeine) example:
```java
@Configuration
public class CacheConfig {
    @Bean
    public CacheManager cacheManager() {
        return new CaffeineCacheManager("drivers", "reviews", "bookings", "passengers");
    }
}
```

Checklist:
- [ ] Add cache starter (and Caffeine if desired)
- [ ] Enable caching with `@EnableCaching`
- [ ] Cache read-heavy service methods
- [ ] Evict/refresh on writes where necessary

---

## Quick Master Checklist
- [ ] PassengerController uses DTOs + Adapter
- [ ] BookingController uses DTOs + Adapter
- [ ] DTOs annotated with Bean Validation
- [ ] `@Valid` added to controller methods
- [ ] Global validation handler in place
- [ ] Adapter unit tests written
- [ ] Controller slice tests written
- [ ] Integration tests for key flows
- [ ] Cache added for read-heavy endpoints

Use this guide to implement each step at your own pace. Keep responses DTO-only at controller boundaries and centralize mapping in adapters for clean separation of concerns.
