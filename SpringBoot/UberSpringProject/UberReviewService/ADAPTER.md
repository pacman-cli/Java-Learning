# 🔌 Adapter Pattern & Adapter Layer - Complete Guide

## 🎯 **What is the Adapter Pattern?**

The Adapter pattern is a structural design pattern that allows incompatible interfaces to work together. It acts as a bridge between two incompatible interfaces by wrapping an existing class with a new interface.

---

## 🏗️ **Adapter Pattern Overview**

### **Purpose**
- **Interface Compatibility**: Make incompatible interfaces work together
- **Legacy Integration**: Integrate existing/legacy systems with new code
- **Third-party Integration**: Adapt external APIs to your application's interface
- **Testing**: Create mock implementations for testing

### **When to Use**
- When you want to use an existing class, but its interface isn't compatible with your code
- When you need to create a reusable class that cooperates with classes that don't have compatible interfaces
- When you need to work with multiple existing subclasses, but it's impractical to adapt their interface by subclassing every one

---

## 🔧 **Adapter Pattern Structure**

### **Class Diagram**
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Client        │    │   Target        │    │   Adaptee       │
│                 │    │                 │    │                 │
│ + request()     │───▶│ + request()     │    │ + specificRequest()│
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                ▲
                                │
                                │
                       ┌─────────────────┐
                       │   Adapter       │
                       │                 │
                       │ + request()     │
                       │ - adaptee       │
                       └─────────────────┘
```

### **Components**
1. **Target**: The interface that the client expects
2. **Adaptee**: The existing class that needs to be adapted
3. **Adapter**: The class that implements the Target interface and wraps the Adaptee
4. **Client**: The class that uses the Target interface

---

## 📝 **Basic Adapter Implementation**

### **1. Target Interface**
```java
// Target interface that the client expects
public interface PaymentProcessor {
    boolean processPayment(double amount, String currency);
    String getTransactionId();
    PaymentStatus getStatus();
}
```

### **2. Adaptee (Existing Class)**
```java
// Existing third-party payment library
public class LegacyPaymentLibrary {
    
    public int authorizePayment(String amount, String currencyCode) {
        // Legacy implementation
        System.out.println("Legacy payment processing: " + amount + " " + currencyCode);
        return (int) (Math.random() * 10000); // Simulate transaction ID
    }
    
    public boolean isPaymentSuccessful(int transactionId) {
        // Legacy status check
        return transactionId % 2 == 0; // Simulate success/failure
    }
    
    public String getPaymentStatus(int transactionId) {
        return isPaymentSuccessful(transactionId) ? "SUCCESS" : "FAILED";
    }
}
```

### **3. Adapter Implementation**
```java
// Adapter that makes LegacyPaymentLibrary compatible with PaymentProcessor
public class LegacyPaymentAdapter implements PaymentProcessor {
    
    private final LegacyPaymentLibrary legacyLibrary;
    
    public LegacyPaymentAdapter(LegacyPaymentLibrary legacyLibrary) {
        this.legacyLibrary = legacyLibrary;
    }
    
    @Override
    public boolean processPayment(double amount, String currency) {
        // Convert double to String (legacy library expects String)
        String amountStr = String.valueOf(amount);
        
        // Call legacy method
        int transactionId = legacyLibrary.authorizePayment(amountStr, currency);
        
        // Return boolean result
        return legacyLibrary.isPaymentSuccessful(transactionId);
    }
    
    @Override
    public String getTransactionId() {
        // Generate a new transaction ID for demonstration
        int transactionId = (int) (Math.random() * 10000);
        return "LEGACY_" + transactionId;
    }
    
    @Override
    public PaymentStatus getStatus() {
        // Convert legacy status to enum
        String legacyStatus = legacyLibrary.getPaymentStatus(0);
        return "SUCCESS".equals(legacyStatus) ? PaymentStatus.SUCCESS : PaymentStatus.FAILED;
    }
}
```

### **4. Client Usage**
```java
@Service
public class PaymentService {
    
    private final PaymentProcessor paymentProcessor;
    
    public PaymentService(PaymentProcessor paymentProcessor) {
        this.paymentProcessor = paymentProcessor;
    }
    
    public PaymentResult processPayment(PaymentRequest request) {
        boolean success = paymentProcessor.processPayment(request.getAmount(), request.getCurrency());
        
        if (success) {
            return PaymentResult.success(paymentProcessor.getTransactionId());
        } else {
            return PaymentResult.failure("Payment processing failed");
        }
    }
}

// Usage
@Configuration
public class PaymentConfig {
    
    @Bean
    public LegacyPaymentLibrary legacyPaymentLibrary() {
        return new LegacyPaymentLibrary();
    }
    
    @Bean
    public PaymentProcessor paymentProcessor(LegacyPaymentLibrary legacyLibrary) {
        return new LegacyPaymentAdapter(legacyLibrary);
    }
}
```

---

## 🎨 **Advanced Adapter Patterns**

### **1. Object Adapter (Composition)**
```java
// Object Adapter using composition
public class ObjectPaymentAdapter implements PaymentProcessor {
    
    private final LegacyPaymentLibrary adaptee;
    
    public ObjectPaymentAdapter(LegacyPaymentLibrary adaptee) {
        this.adaptee = adaptee;
    }
    
    @Override
    public boolean processPayment(double amount, String currency) {
        // Adapt the interface
        String amountStr = String.valueOf(amount);
        int transactionId = adaptee.authorizePayment(amountStr, currency);
        return adaptee.isPaymentSuccessful(transactionId);
    }
    
    // Additional methods...
}
```

### **2. Class Adapter (Inheritance)**
```java
// Class Adapter using inheritance (if possible)
public class ClassPaymentAdapter extends LegacyPaymentLibrary implements PaymentProcessor {
    
    @Override
    public boolean processPayment(double amount, String currency) {
        String amountStr = String.valueOf(amount);
        int transactionId = authorizePayment(amountStr, currency);
        return isPaymentSuccessful(transactionId);
    }
    
    // Additional methods...
}
```

### **3. Two-Way Adapter**
```java
// Two-way adapter that can work in both directions
public class TwoWayPaymentAdapter implements PaymentProcessor, LegacyPaymentInterface {
    
    private final LegacyPaymentLibrary legacyLibrary;
    private final ModernPaymentProcessor modernProcessor;
    
    public TwoWayPaymentAdapter(LegacyPaymentLibrary legacyLibrary, 
                               ModernPaymentProcessor modernProcessor) {
        this.legacyLibrary = legacyLibrary;
        this.modernProcessor = modernProcessor;
    }
    
    // Implement PaymentProcessor (modern interface)
    @Override
    public boolean processPayment(double amount, String currency) {
        return modernProcessor.processPayment(amount, currency);
    }
    
    // Implement LegacyPaymentInterface (legacy interface)
    @Override
    public int authorizePayment(String amount, String currencyCode) {
        double amountDouble = Double.parseDouble(amount);
        boolean success = modernProcessor.processPayment(amountDouble, currencyCode);
        return success ? (int) (Math.random() * 10000) : -1;
    }
}
```

---

## 🚀 **Spring Boot Adapter Examples**

### **1. External API Adapter**
```java
// External weather API interface
public interface WeatherService {
    WeatherInfo getWeather(String city);
    List<WeatherForecast> getForecast(String city, int days);
}

// External API response DTOs
public class ExternalWeatherResponse {
    private String location;
    private double temperature;
    private String condition;
    private int humidity;
    // getters, setters
}

public class ExternalForecastResponse {
    private String date;
    private double maxTemp;
    private double minTemp;
    private String description;
    // getters, setters
}

// External API client
@Component
public class ExternalWeatherApiClient {
    
    private final RestTemplate restTemplate;
    private final String apiKey;
    
    public ExternalWeatherApiClient(RestTemplate restTemplate, 
                                   @Value("${weather.api.key}") String apiKey) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
    }
    
    public ExternalWeatherResponse getCurrentWeather(String city) {
        String url = String.format("https://api.weather.com/current?city=%s&key=%s", city, apiKey);
        return restTemplate.getForObject(url, ExternalWeatherResponse.class);
    }
    
    public List<ExternalForecastResponse> getForecast(String city, int days) {
        String url = String.format("https://api.weather.com/forecast?city=%s&days=%d&key=%s", 
                                 city, days, apiKey);
        ExternalForecastResponse[] response = restTemplate.getForObject(url, ExternalForecastResponse[].class);
        return Arrays.asList(response != null ? response : new ExternalForecastResponse[0]);
    }
}

// Adapter to convert external API to internal interface
@Component
public class ExternalWeatherAdapter implements WeatherService {
    
    private final ExternalWeatherApiClient externalClient;
    
    public ExternalWeatherAdapter(ExternalWeatherApiClient externalClient) {
        this.externalClient = externalClient;
    }
    
    @Override
    public WeatherInfo getWeather(String city) {
        ExternalWeatherResponse externalResponse = externalClient.getCurrentWeather(city);
        
        // Convert external response to internal format
        return WeatherInfo.builder()
            .city(externalResponse.getLocation())
            .temperature(externalResponse.getTemperature())
            .condition(externalResponse.getCondition())
            .humidity(externalResponse.getHumidity())
            .timestamp(LocalDateTime.now())
            .build();
    }
    
    @Override
    public List<WeatherForecast> getForecast(String city, int days) {
        List<ExternalForecastResponse> externalForecasts = externalClient.getForecast(city, days);
        
        // Convert external forecasts to internal format
        return externalForecasts.stream()
            .map(this::convertToInternalForecast)
            .collect(Collectors.toList());
    }
    
    private WeatherForecast convertToInternalForecast(ExternalForecastResponse external) {
        return WeatherForecast.builder()
            .date(LocalDate.parse(external.getDate()))
            .maxTemperature(external.getMaxTemp())
            .minTemperature(external.getMinTemp())
            .description(external.getDescription())
            .build();
    }
}
```

### **2. Database Adapter**
```java
// Internal repository interface
public interface UserRepository {
    User save(User user);
    Optional<User> findById(Long id);
    List<User> findAll();
    void deleteById(Long id);
}

// MongoDB implementation
@Repository
public class MongoUserRepository implements UserRepository {
    
    private final MongoTemplate mongoTemplate;
    
    public MongoUserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    @Override
    public User save(User user) {
        return mongoTemplate.save(user);
    }
    
    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(mongoTemplate.findById(id, User.class));
    }
    
    @Override
    public List<User> findAll() {
        return mongoTemplate.findAll(User.class);
    }
    
    @Override
    public void deleteById(Long id) {
        mongoTemplate.remove(Query.query(Criteria.where("id").is(id)), User.class);
    }
}

// MySQL implementation
@Repository
public class MySqlUserRepository implements UserRepository {
    
    private final JdbcTemplate jdbcTemplate;
    
    public MySqlUserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    
    @Override
    public User save(User user) {
        if (user.getId() == null) {
            // Insert
            String sql = "INSERT INTO users (name, email, created_at) VALUES (?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, user.getName());
                ps.setString(2, user.getEmail());
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                return ps;
            }, keyHolder);
            user.setId(keyHolder.getKey().longValue());
        } else {
            // Update
            String sql = "UPDATE users SET name = ?, email = ?, updated_at = ? WHERE id = ?";
            jdbcTemplate.update(sql, user.getName(), user.getEmail(), 
                             LocalDateTime.now(), user.getId());
        }
        return user;
    }
    
    @Override
    public Optional<User> findById(Long id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User user = jdbcTemplate.queryForObject(sql, new Object[]{id}, this::mapRow);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
    
    @Override
    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, this::mapRow);
    }
    
    @Override
    public void deleteById(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
    
    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
            .id(rs.getLong("id"))
            .name(rs.getString("name"))
            .email(rs.getString("email"))
            .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
            .updatedAt(rs.getTimestamp("updated_at") != null ? 
                      rs.getTimestamp("updated_at").toLocalDateTime() : null)
            .build();
    }
}
```

### **3. Message Queue Adapter**
```java
// Internal message interface
public interface MessagePublisher {
    void publish(String topic, Object message);
    void publishAsync(String topic, Object message);
}

public interface MessageSubscriber {
    void subscribe(String topic, MessageHandler handler);
    void unsubscribe(String topic);
}

// RabbitMQ adapter
@Component
public class RabbitMQMessageAdapter implements MessagePublisher, MessageSubscriber {
    
    private final RabbitTemplate rabbitTemplate;
    private final AmqpAdmin amqpAdmin;
    private final Map<String, MessageHandler> handlers = new ConcurrentHashMap<>();
    
    public RabbitMQMessageAdapter(RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin) {
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
    }
    
    @Override
    public void publish(String topic, Object message) {
        rabbitTemplate.convertAndSend(topic, message);
    }
    
    @Override
    public void publishAsync(String topic, Object message) {
        CompletableFuture.runAsync(() -> publish(topic, message));
    }
    
    @Override
    public void subscribe(String topic, MessageHandler handler) {
        handlers.put(topic, handler);
        
        // Create queue and binding
        Queue queue = new Queue(topic + "_queue", true);
        amqpAdmin.declareQueue(queue);
        
        Binding binding = new Binding(queue.getName(), Binding.DestinationType.QUEUE, 
                                    topic, topic, null);
        amqpAdmin.declareBinding(binding);
        
        // Set up message listener
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        rabbitTemplate.receiveAndConvert(topic + "_queue", message -> {
            MessageHandler messageHandler = handlers.get(topic);
            if (messageHandler != null) {
                messageHandler.handle(message);
            }
            return null;
        });
    }
    
    @Override
    public void unsubscribe(String topic) {
        handlers.remove(topic);
        // Remove queue and binding
        amqpAdmin.deleteQueue(topic + "_queue");
    }
}

// Kafka adapter
@Component
public class KafkaMessageAdapter implements MessagePublisher, MessageSubscriber {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> 
        kafkaListenerContainerFactory;
    private final Map<String, MessageHandler> handlers = new ConcurrentHashMap<>();
    
    public KafkaMessageAdapter(KafkaTemplate<String, Object> kafkaTemplate,
                              KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> 
                                  kafkaListenerContainerFactory) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaListenerContainerFactory = kafkaListenerContainerFactory;
    }
    
    @Override
    public void publish(String topic, Object message) {
        kafkaTemplate.send(topic, message);
    }
    
    @Override
    public void publishAsync(String topic, Object message) {
        CompletableFuture.runAsync(() -> publish(topic, message));
    }
    
    @Override
    public void subscribe(String topic, MessageHandler handler) {
        handlers.put(topic, handler);
        
        // Set up Kafka listener
        ConcurrentMessageListenerContainer<String, Object> container = 
            kafkaListenerContainerFactory.createContainer(topic);
        
        container.setupMessageListener((MessageListener<String, Object>) record -> {
            MessageHandler messageHandler = handlers.get(topic);
            if (messageHandler != null) {
                messageHandler.handle(record.value());
            }
        });
        
        container.start();
    }
    
    @Override
    public void unsubscribe(String topic) {
        handlers.remove(topic);
        // Stop Kafka listener container
        // Implementation depends on how containers are managed
    }
}
```

---

## 🎯 **Adapter Layer Architecture**

### **1. Layered Architecture with Adapters**
```
┌─────────────────────────────────────────────────────────────┐
│                    Presentation Layer                       │
│                    (Controllers)                           │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                     Business Layer                          │
│                     (Services)                              │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                    Adapter Layer                            │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐          │
│  │ Repository  │ │ External    │ │ Message     │          │
│  │ Adapters    │ │ API         │ │ Queue       │          │
│  │             │ │ Adapters    │ │ Adapters    │          │
│  └─────────────┘ └─────────────┘ └─────────────┘          │
└─────────────────────────────────────────────────────────────┘
                                │
                                ▼
┌─────────────────────────────────────────────────────────────┐
│                   Infrastructure Layer                      │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────┐          │
│  │ Database    │ │ External    │ │ Message     │          │
│  │ (MySQL,     │ │ APIs        │ │ Brokers     │          │
│  │  MongoDB)   │ │ (Weather,   │ │ (RabbitMQ,  │          │
│  │             │ │  Payment)   │ │  Kafka)     │          │
│  └─────────────┘ └─────────────┘ └─────────────┘          │
└─────────────────────────────────────────────────────────────┘
```

### **2. Configuration-Based Adapter Selection**
```java
@Configuration
public class AdapterConfiguration {
    
    @Bean
    @ConditionalOnProperty(name = "app.database.type", havingValue = "mysql")
    public UserRepository userRepository(JdbcTemplate jdbcTemplate) {
        return new MySqlUserRepository(jdbcTemplate);
    }
    
    @Bean
    @ConditionalOnProperty(name = "app.database.type", havingValue = "mongodb")
    public UserRepository userRepository(MongoTemplate mongoTemplate) {
        return new MongoUserRepository(mongoTemplate);
    }
    
    @Bean
    @ConditionalOnProperty(name = "app.message.broker", havingValue = "rabbitmq")
    public MessagePublisher messagePublisher(RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin) {
        return new RabbitMQMessageAdapter(rabbitTemplate, amqpAdmin);
    }
    
    @Bean
    @ConditionalOnProperty(name = "app.message.broker", havingValue = "kafka")
    public MessagePublisher messagePublisher(KafkaTemplate<String, Object> kafkaTemplate,
                                           KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Object>> 
                                               kafkaListenerContainerFactory) {
        return new KafkaMessageAdapter(kafkaTemplate, kafkaListenerContainerFactory);
    }
}
```

---

## 🧪 **Testing Adapters**

### **1. Unit Testing**
```java
@ExtendWith(MockitoExtension.class)
class ExternalWeatherAdapterTest {
    
    @Mock
    private ExternalWeatherApiClient externalClient;
    
    @InjectMocks
    private ExternalWeatherAdapter adapter;
    
    @Test
    void getWeather_ShouldConvertExternalResponseToInternalFormat() {
        // Arrange
        ExternalWeatherResponse externalResponse = new ExternalWeatherResponse();
        externalResponse.setLocation("New York");
        externalResponse.setTemperature(25.5);
        externalResponse.setCondition("Sunny");
        externalResponse.setHumidity(60);
        
        when(externalClient.getCurrentWeather("New York")).thenReturn(externalResponse);
        
        // Act
        WeatherInfo result = adapter.getWeather("New York");
        
        // Assert
        assertThat(result.getCity()).isEqualTo("New York");
        assertThat(result.getTemperature()).isEqualTo(25.5);
        assertThat(result.getCondition()).isEqualTo("Sunny");
        assertThat(result.getHumidity()).isEqualTo(60);
        assertThat(result.getTimestamp()).isNotNull();
    }
    
    @Test
    void getWeather_ShouldHandleNullResponse() {
        // Arrange
        when(externalClient.getCurrentWeather("Invalid City")).thenReturn(null);
        
        // Act & Assert
        assertThatThrownBy(() -> adapter.getWeather("Invalid City"))
            .isInstanceOf(WeatherServiceException.class)
            .hasMessage("Failed to get weather for city: Invalid City");
    }
}
```

### **2. Integration Testing**
```java
@SpringBootTest
@ActiveProfiles("test")
class WeatherServiceIntegrationTest {
    
    @Autowired
    private WeatherService weatherService;
    
    @Test
    void shouldGetWeatherForValidCity() {
        // Act
        WeatherInfo weather = weatherService.getWeather("London");
        
        // Assert
        assertThat(weather).isNotNull();
        assertThat(weather.getCity()).isEqualTo("London");
        assertThat(weather.getTemperature()).isNotNull();
    }
}
```

---

## 🚀 **Best Practices**

### **1. Interface Segregation**
```java
// Good: Separate interfaces for different concerns
public interface PaymentProcessor {
    boolean processPayment(double amount, String currency);
}

public interface PaymentValidator {
    boolean validatePayment(PaymentRequest request);
}

public interface PaymentNotifier {
    void notifyPaymentResult(PaymentResult result);
}

// Bad: Single interface with too many responsibilities
public interface PaymentService {
    boolean processPayment(double amount, String currency);
    boolean validatePayment(PaymentRequest request);
    void notifyPaymentResult(PaymentResult result);
    void generateInvoice(PaymentRequest request);
    void sendReceipt(String email);
}
```

### **2. Error Handling**
```java
public class ExternalApiAdapter implements InternalInterface {
    
    private final ExternalApiClient externalClient;
    
    @Override
    public InternalResponse process(InternalRequest request) {
        try {
            ExternalResponse externalResponse = externalClient.call(request);
            return convertToInternalResponse(externalResponse);
        } catch (ExternalApiException e) {
            // Log the error
            log.error("External API call failed", e);
            
            // Convert to internal exception
            throw new InternalServiceException("Service temporarily unavailable", e);
        } catch (Exception e) {
            // Handle unexpected errors
            log.error("Unexpected error in adapter", e);
            throw new InternalServiceException("Internal service error", e);
        }
    }
}
```

### **3. Caching and Performance**
```java
@Component
public class CachedWeatherAdapter implements WeatherService {
    
    private final WeatherService delegate;
    private final CacheManager cacheManager;
    
    public CachedWeatherAdapter(WeatherService delegate, CacheManager cacheManager) {
        this.delegate = delegate;
        this.cacheManager = cacheManager;
    }
    
    @Override
    @Cacheable(value = "weather", key = "#city")
    public WeatherInfo getWeather(String city) {
        return delegate.getWeather(city);
    }
    
    @Override
    @Cacheable(value = "forecast", key = "#city + '_' + #days")
    public List<WeatherForecast> getForecast(String city, int days) {
        return delegate.getForecast(city, days);
    }
}
```

---

## 🎯 **Summary**

The Adapter pattern provides:
- **Interface Compatibility**: Make incompatible interfaces work together
- **Legacy Integration**: Integrate existing systems with new code
- **Third-party Integration**: Adapt external APIs to your application
- **Testing Support**: Create mock implementations easily
- **Flexibility**: Switch between different implementations

**Key Benefits:**
- Maintains single responsibility principle
- Follows open/closed principle
- Improves code reusability
- Simplifies testing
- Enables loose coupling

---

## 🚀 **Quick Start Checklist**

- [ ] Identify incompatible interfaces
- [ ] Define target interface
- [ ] Create adapter class
- [ ] Implement conversion logic
- [ ] Add error handling
- [ ] Write unit tests
- [ ] Configure dependency injection
- [ ] Test integration

---

**Happy Adapter Implementation! 🔌✨**
