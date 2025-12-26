# 🚀 Advanced Spring Boot Features Guide

## Table of Contents
1. [Micrometer for Metrics](#micrometer-for-metrics)
2. [Spring Boot Actuator for Monitoring](#spring-boot-actuator-for-monitoring)
3. [Async Processing](#async-processing)
4. [WebSocket for Real-time Features](#websocket-for-real-time-features)
5. [Caching with Caffeine](#caching-with-caffeine)
6. [Practical Examples](#practical-examples)
7. [How to Use These Features](#how-to-use-these-features)

---

## 📊 Micrometer for Metrics

### **What is Micrometer?**
Micrometer is a metrics collection facade that provides a simple interface for recording application metrics. Think of it as a universal translator for different monitoring systems.

### **Why Use Micrometer?**
- **Universal Interface**: Works with Prometheus, InfluxDB, CloudWatch, etc.
- **Application Insights**: Track performance, errors, and business metrics
- **Production Monitoring**: Monitor your app in real-time
- **Performance Optimization**: Identify bottlenecks and slow operations

### **Types of Metrics**

#### 1. **Counters** - Count events
```java
// Example: Count how many images were uploaded
Counter.builder("media.upload")
    .tag("type", "image")
    .register(meterRegistry)
    .increment();
```

#### 2. **Timers** - Measure duration
```java
// Example: Measure how long image processing takes
Timer.builder("image.processing.time")
    .register(meterRegistry)
    .record(processingTime, TimeUnit.MILLISECONDS);
```

#### 3. **Gauges** - Current value
```java
// Example: Current number of active uploads
Gauge.builder("active.uploads")
    .register(meterRegistry, this, obj -> getActiveUploadCount());
```

### **Real-World Example in Your App**

```java
@Service
public class ImageService {
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    public Image uploadImage(MultipartFile file) {
        // Start timing
        Timer.Sample sample = Timer.start(meterRegistry);
        
        try {
            // Your upload logic here
            Image image = performUpload(file);
            
            // Record success metrics
            Counter.builder("media.upload")
                .tag("type", "image")
                .tag("status", "success")
                .register(meterRegistry)
                .increment();
                
            return image;
            
        } catch (Exception e) {
            // Record error metrics
            Counter.builder("media.upload")
                .tag("type", "image")
                .tag("status", "error")
                .tag("error_type", e.getClass().getSimpleName())
                .register(meterRegistry)
                .increment();
            throw e;
        } finally {
            // Record processing time
            sample.stop(Timer.builder("image.upload.time")
                .register(meterRegistry));
        }
    }
}
```

---

## 🔍 Spring Boot Actuator for Monitoring

### **What is Actuator?**
Actuator provides production-ready features to help you monitor and manage your application. It exposes endpoints that give you insights into your application's health, metrics, and configuration.

### **Why Use Actuator?**
- **Health Checks**: Know if your app is running properly
- **Metrics**: See performance data in real-time
- **Configuration**: View and modify app settings
- **Production Ready**: Built for monitoring production applications

### **Key Endpoints**

#### 1. **Health Endpoint** - `/actuator/health`
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "MySQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": {
      "status": "UP",
      "details": {
        "total": 499963174912,
        "free": 250790453248,
        "threshold": 10485760
      }
    }
  }
}
```

#### 2. **Metrics Endpoint** - `/actuator/metrics`
```json
{
  "names": [
    "jvm.memory.used",
    "jvm.threads.live",
    "media.upload",
    "image.processing.time"
  ]
}
```

#### 3. **Specific Metric** - `/actuator/metrics/media.upload`
```json
{
  "name": "media.upload",
  "description": "Number of media uploads",
  "measurements": [
    {
      "statistic": "COUNT",
      "value": 42
    }
  ],
  "availableTags": [
    {
      "tag": "type",
      "values": ["image", "video"]
    }
  ]
}
```

### **How to Access These Endpoints**

1. **Start your application**
2. **Visit these URLs:**
   - `http://localhost:8080/actuator/health` - Check if app is healthy
   - `http://localhost:8080/actuator/metrics` - See all available metrics
   - `http://localhost:8080/actuator/info` - Application information

---

## ⚡ Async Processing

### **What is Async Processing?**
Async processing allows your application to handle tasks in the background without blocking the main thread. This improves user experience and application performance.

### **Why Use Async Processing?**
- **Better Performance**: Don't block users while processing
- **Scalability**: Handle more requests simultaneously
- **User Experience**: Immediate response to users
- **Resource Utilization**: Use all available CPU cores

### **Real-World Example**

#### **Without Async (Blocking)**
```java
@PostMapping("/upload")
public ResponseEntity<String> uploadImage(MultipartFile file) {
    // This blocks the user until processing is complete
    processImage(file);        // Takes 5 seconds
    generateThumbnail(file);   // Takes 3 seconds
    updateDatabase(file);      // Takes 2 seconds
    // Total: 10 seconds of waiting!
    
    return ResponseEntity.ok("Upload complete");
}
```

#### **With Async (Non-blocking)**
```java
@PostMapping("/upload")
public ResponseEntity<String> uploadImage(MultipartFile file) {
    // Immediate response to user
    String uploadId = UUID.randomUUID().toString();
    
    // Process in background
    asyncImageProcessor.processImageAsync(file, uploadId);
    
    return ResponseEntity.ok("Upload started! ID: " + uploadId);
}

@Service
public class AsyncImageProcessor {
    
    @Async
    public CompletableFuture<String> processImageAsync(MultipartFile file, String uploadId) {
        try {
            // Heavy processing in background
            processImage(file);
            generateThumbnail(file);
            updateDatabase(file);
            
            return CompletableFuture.completedFuture("Processing complete");
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
```

### **Configuration**
```java
@Configuration
@EnableAsync
public class AsyncConfig {
    
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);        // Minimum threads
        executor.setMaxPoolSize(20);        // Maximum threads
        executor.setQueueCapacity(100);     // Queue size
        executor.setThreadNamePrefix("Async-");
        executor.initialize();
        return executor;
    }
}
```

---

## 🔌 WebSocket for Real-time Features

### **What is WebSocket?**
WebSocket provides full-duplex communication between client and server. Unlike HTTP, it maintains a persistent connection, allowing real-time data exchange.

### **Why Use WebSocket?**
- **Real-time Updates**: Send data instantly to clients
- **Live Notifications**: Notify users of events immediately
- **Live Progress**: Show upload progress in real-time
- **Chat Features**: Enable real-time messaging

### **Real-World Example: Live Upload Progress**

#### **WebSocket Configuration**
```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new UploadProgressHandler(), "/upload-progress")
                .setAllowedOrigins("*");
    }
}

@Component
public class UploadProgressHandler extends TextWebSocketHandler {
    
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
        System.out.println("Client connected: " + session.getId());
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
        System.out.println("Client disconnected: " + session.getId());
    }
    
    public void sendProgress(String sessionId, int progress) {
        WebSocketSession session = sessions.get(sessionId);
        if (session != null && session.isOpen()) {
            try {
                session.sendMessage(new TextMessage("{\"progress\": " + progress + "}"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
```

#### **Frontend JavaScript**
```javascript
// Connect to WebSocket
const socket = new WebSocket('ws://localhost:8080/upload-progress');

socket.onopen = function(event) {
    console.log('Connected to upload progress');
};

socket.onmessage = function(event) {
    const data = JSON.parse(event.data);
    updateProgressBar(data.progress);
};

function updateProgressBar(progress) {
    document.getElementById('progress-bar').style.width = progress + '%';
    document.getElementById('progress-text').textContent = progress + '%';
}
```

---

## 🗄️ Caching with Caffeine

### **What is Caching?**
Caching stores frequently accessed data in memory for faster retrieval. Instead of hitting the database every time, you get data from memory.

### **Why Use Caching?**
- **Performance**: 10-100x faster than database queries
- **Reduced Load**: Less pressure on database
- **Better User Experience**: Faster response times
- **Cost Savings**: Fewer database operations

### **Real-World Example**

#### **Without Caching (Slow)**
```java
@GetMapping("/images/{id}")
public ResponseEntity<Image> getImage(@PathVariable Long id) {
    // This hits the database every time
    Image image = imageRepository.findById(id).orElse(null);
    return ResponseEntity.ok(image);
}
```

#### **With Caching (Fast)**
```java
@GetMapping("/images/{id}")
@Cacheable(value = "images", key = "#id")
public ResponseEntity<Image> getImage(@PathVariable Long id) {
    // Only hits database if not in cache
    Image image = imageRepository.findById(id).orElse(null);
    return ResponseEntity.ok(image);
}

@PostMapping("/images")
@CacheEvict(value = "images", allEntries = true)
public ResponseEntity<Image> createImage(@RequestBody Image image) {
    // Clear cache when new image is added
    Image savedImage = imageRepository.save(image);
    return ResponseEntity.ok(savedImage);
}
```

### **Cache Configuration**
```java
@Configuration
@EnableCaching
public class CacheConfig {
    
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .maximumSize(1000)                    // Max 1000 entries
            .expireAfterWrite(1, TimeUnit.HOURS)  // Expire after 1 hour
            .recordStats());                      // Enable statistics
        return cacheManager;
    }
}
```

---

## 🎯 Practical Examples

### **Example 1: Complete Upload with All Features**

```java
@RestController
public class MediaController {
    
    @Autowired
    private MeterRegistry meterRegistry;
    
    @Autowired
    private AsyncImageProcessor asyncProcessor;
    
    @PostMapping("/upload")
    @Async
    public CompletableFuture<ResponseEntity<String>> uploadMedia(
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
        
        String sessionId = request.getSession().getId();
        Timer.Sample sample = Timer.start(meterRegistry);
        
        try {
            // Validate file
            fileValidationService.validateImageFile(file);
            
            // Start async processing
            CompletableFuture<Image> result = asyncProcessor.processImageAsync(file, sessionId);
            
            // Record metrics
            Counter.builder("media.upload")
                .tag("type", "image")
                .tag("status", "started")
                .register(meterRegistry)
                .increment();
            
            return result.thenApply(image -> {
                sample.stop(Timer.builder("media.upload.time")
                    .register(meterRegistry));
                
                return ResponseEntity.ok("Upload successful: " + image.getId());
            });
            
        } catch (Exception e) {
            sample.stop(Timer.builder("media.upload.time")
                .register(meterRegistry));
            
            Counter.builder("media.upload")
                .tag("type", "image")
                .tag("status", "error")
                .register(meterRegistry)
                .increment();
            
            return CompletableFuture.completedFuture(
                ResponseEntity.status(500).body("Upload failed: " + e.getMessage()));
        }
    }
}
```

### **Example 2: Real-time Progress Updates**

```java
@Service
public class UploadProgressService {
    
    @Autowired
    private UploadProgressHandler progressHandler;
    
    @Async
    public CompletableFuture<Void> processWithProgress(
            MultipartFile file, String sessionId) {
        
        try {
            // Simulate processing steps
            for (int i = 0; i <= 100; i += 10) {
                Thread.sleep(100); // Simulate work
                progressHandler.sendProgress(sessionId, i);
            }
            
            return CompletableFuture.completedFuture(null);
            
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
```

---

## 🚀 How to Use These Features

### **Step 1: Start Your Application**
```bash
cd /Users/puspo/JavaCourse/SpringBoot/Cloudinary/videoshare
mvn spring-boot:run
```

### **Step 2: Check Health**
Visit: `http://localhost:8080/actuator/health`
```json
{
  "status": "UP",
  "components": {
    "db": {"status": "UP"},
    "diskSpace": {"status": "UP"}
  }
}
```

### **Step 3: View Metrics**
Visit: `http://localhost:8080/actuator/metrics`
```json
{
  "names": [
    "jvm.memory.used",
    "media.upload",
    "image.processing.time"
  ]
}
```

### **Step 4: Monitor Specific Metrics**
Visit: `http://localhost:8080/actuator/metrics/media.upload`
```json
{
  "name": "media.upload",
  "measurements": [{"statistic": "COUNT", "value": 5}],
  "availableTags": [
    {"tag": "type", "values": ["image", "video"]}
  ]
}
```

### **Step 5: Test Real-time Features**
1. Open WebSocket connection to `ws://localhost:8080/upload-progress`
2. Upload a file
3. Watch real-time progress updates

---

## 📈 Benefits in Your VideoShare App

### **1. Performance Monitoring**
- Track upload success/failure rates
- Monitor processing times
- Identify slow operations

### **2. User Experience**
- Real-time upload progress
- Faster response times with caching
- Non-blocking operations

### **3. Production Readiness**
- Health checks for monitoring
- Metrics for alerting
- Error tracking and reporting

### **4. Scalability**
- Handle more concurrent users
- Reduce database load
- Better resource utilization

---

## 🎯 Quick Start Checklist

- [ ] **Metrics**: Upload files and check `/actuator/metrics`
- [ ] **Health**: Visit `/actuator/health` to see app status
- [ ] **Caching**: Notice faster responses after first load
- [ ] **Async**: Upload files and see immediate response
- [ ] **WebSocket**: Connect to real-time progress updates

---

## 🔧 Troubleshooting

### **Common Issues**

1. **Actuator endpoints not working**
   - Check if `management.endpoints.web.exposure.include=*` is set
   - Ensure Spring Boot Actuator dependency is added

2. **Async not working**
   - Add `@EnableAsync` to configuration
   - Use `@Async` on methods
   - Call async methods from outside the same class

3. **Cache not working**
   - Add `@EnableCaching` to configuration
   - Use `@Cacheable` on methods
   - Check cache configuration

4. **WebSocket connection failed**
   - Check CORS configuration
   - Ensure WebSocket handler is registered
   - Verify client connection URL

---

## 📚 Further Learning

- **Spring Boot Actuator**: [Official Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/actuator.html)
- **Micrometer**: [Official Documentation](https://micrometer.io/docs)
- **WebSocket**: [Spring WebSocket Guide](https://spring.io/guides/gs/messaging-stomp-websocket/)
- **Caching**: [Spring Cache Abstraction](https://docs.spring.io/spring-framework/docs/current/reference/html/integration.html#cache)

---

**🎉 Congratulations!** You now understand all the advanced Spring Boot features in your VideoShare application. These features make your app production-ready, scalable, and user-friendly!
