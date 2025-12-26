# ☕ Java Programming - Essential Concepts for Spring Boot

## 🎯 **Table of Contents**

1. [Basic Concepts](#-basic-concepts)
2. [Object-Oriented Programming](#-object-oriented-programming)
3. [Collections & Generics](#-collections--generics)
4. [Lambda Expressions & Streams](#-lambda-expressions--streams)
5. [Exception Handling](#-exception-handling)
6. [Annotations](#-annotations)

---

## 🔰 **Basic Concepts**

### **Variables and Data Types**
```java
// Primitive Types
byte byteValue = 127;                    // 8-bit
short shortValue = 32767;                // 16-bit
int intValue = 2147483647;               // 32-bit
long longValue = 9223372036854775807L;   // 64-bit

float floatValue = 3.14f;                // 32-bit float
double doubleValue = 3.14159265359;      // 64-bit float

char charValue = 'A';                    // 16-bit Unicode
boolean booleanValue = true;             // true/false

// Reference Types
String stringValue = "Hello World";
Integer integerObject = 100;
Double doubleObject = 3.14;
```

### **Operators and Control Flow**
```java
// Arithmetic
int a = 10, b = 3;
int sum = a + b;        // 13
int quotient = a / b;   // 3
int remainder = a % b;  // 1

// Comparison
boolean isEqual = (a == b);      // false
boolean isGreater = (a > b);     // true

// Logical
boolean and = true && false;     // false
boolean or = true || false;      // true
boolean not = !true;             // false

// Control Flow
if (age >= 18) {
    System.out.println("Adult");
} else if (age >= 13) {
    System.out.println("Teenager");
} else {
    System.out.println("Child");
}

// Loops
for (int i = 0; i < 5; i++) {
    System.out.println("Count: " + i);
}

while (count < 5) {
    System.out.println("Count: " + count);
    count++;
}

// Enhanced For Loop
String[] fruits = {"Apple", "Banana", "Orange"};
for (String fruit : fruits) {
    System.out.println(fruit);
}
```

---

## 🏗️ **Object-Oriented Programming**

### **Classes and Objects**
```java
public class Car {
    // Instance Variables
    private String brand;
    private String model;
    private int year;
    
    // Constructor
    public Car(String brand, String model, int year) {
        this.brand = brand;
        this.model = model;
        this.year = year;
    }
    
    // Methods
    public void start() {
        System.out.println(brand + " " + model + " is starting...");
    }
    
    // Getters and Setters
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
}

// Using Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    private String brand;
    private String model;
    private int year;
}
```

### **Inheritance**
```java
// Base Class
public class Vehicle {
    protected String brand;
    protected String model;
    
    public void start() {
        System.out.println("Vehicle is starting...");
    }
}

// Derived Class
public class Car extends Vehicle {
    private int numberOfDoors;
    
    @Override
    public void start() {
        System.out.println("Car engine starting...");
    }
    
    public void openTrunk() {
        System.out.println("Opening trunk...");
    }
}
```

### **Interfaces and Abstract Classes**
```java
// Interface
public interface Drivable {
    void drive();
    void stop();
    
    // Default method (Java 8+)
    default void honk() {
        System.out.println("Honking...");
    }
}

// Abstract Class
public abstract class Vehicle {
    protected String brand;
    
    public abstract void start();
    
    public void displayBrand() {
        System.out.println("Brand: " + brand);
    }
}

// Implementation
public class Car extends Vehicle implements Drivable {
    
    @Override
    public void start() {
        System.out.println("Car starting...");
    }
    
    @Override
    public void drive() {
        System.out.println("Car driving...");
    }
    
    @Override
    public void stop() {
        System.out.println("Car stopping...");
    }
}
```

### **Polymorphism**
```java
// Polymorphic usage
Vehicle[] vehicles = {
    new Car("Toyota", "Camry"),
    new Motorcycle("Honda", "CBR")
};

for (Vehicle vehicle : vehicles) {
    vehicle.start();  // Different behavior for each type
}

Drivable[] drivables = {
    new Car("Ford", "Focus"),
    new Bicycle("Trek", "Mountain")
};

for (Drivable drivable : drivables) {
    drivable.drive();  // Different behavior for each type
}
```

---

## 📚 **Collections & Generics**

### **Collection Framework**
```java
import java.util.*;

// List
List<String> arrayList = new ArrayList<>();
arrayList.add("Apple");
arrayList.add("Banana");
arrayList.add(1, "Mango");  // Insert at index

// Set
Set<String> hashSet = new HashSet<>();
hashSet.add("Apple");
hashSet.add("Apple");  // Duplicate ignored

// Map
Map<String, Integer> hashMap = new HashMap<>();
hashMap.put("Apple", 1);
hashMap.put("Banana", 2);
Integer value = hashMap.get("Apple");

// Queue
Queue<Integer> priorityQueue = new PriorityQueue<>();
priorityQueue.offer(5);
priorityQueue.offer(2);
Integer first = priorityQueue.poll();  // 2 (smallest first)
```

### **Generics**
```java
// Generic Class
public class Box<T> {
    private T content;
    
    public Box(T content) {
        this.content = content;
    }
    
    public T getContent() {
        return content;
    }
    
    public void setContent(T content) {
        this.content = content;
    }
}

// Usage
Box<String> stringBox = new Box<>("Hello");
Box<Integer> intBox = new Box<>(42);

// Generic Methods
public static <T> void printArray(T[] array) {
    for (T element : array) {
        System.out.print(element + " ");
    }
}

// Wildcards
public static void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

public static double sumOfNumbers(List<? extends Number> numbers) {
    double sum = 0.0;
    for (Number number : numbers) {
        sum += number.doubleValue();
    }
    return sum;
}
```

---

## 🚀 **Lambda Expressions & Streams**

### **Lambda Expressions**
```java
// Functional Interfaces
@FunctionalInterface
public interface MathOperation {
    int operate(int a, int b);
}

// Lambda usage
MathOperation add = (a, b) -> a + b;
MathOperation multiply = (a, b) -> a * b;

int result1 = add.operate(5, 3);      // 8
int result2 = multiply.operate(5, 3); // 15

// Built-in functional interfaces
Predicate<String> isLong = name -> name.length() > 4;
Function<String, Integer> getLength = String::length;
Consumer<String> printer = System.out::println;
Supplier<String> supplier = () -> "Default";

// Usage
boolean hasLongName = isLong.test("John");     // false
Integer length = getLength.apply("Hello");     // 5
printer.accept("Hello World");                // prints "Hello World"
String defaultName = supplier.get();           // "Default"
```

### **Streams API**
```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Basic operations
List<Integer> evenNumbers = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());

List<Integer> doubledNumbers = numbers.stream()
    .map(n -> n * 2)
    .collect(Collectors.toList());

int sum = numbers.stream()
    .reduce(0, Integer::sum);

// Complex operations
List<Integer> result = numbers.stream()
    .filter(n -> n % 2 == 0)
    .map(n -> n * n)
    .filter(n -> n > 20)
    .collect(Collectors.toList());

// Statistics
IntSummaryStatistics stats = numbers.stream()
    .mapToInt(Integer::intValue)
    .summaryStatistics();

System.out.println("Count: " + stats.getCount());
System.out.println("Sum: " + stats.getSum());
System.out.println("Average: " + stats.getAverage());
System.out.println("Min: " + stats.getMin());
System.out.println("Max: " + stats.getMax());
```

---

## ⚠️ **Exception Handling**

### **Try-Catch Blocks**
```java
public void divideNumbers(int a, int b) {
    try {
        int result = a / b;
        System.out.println("Result: " + result);
    } catch (ArithmeticException e) {
        System.out.println("Error: Division by zero");
        e.printStackTrace();
    } catch (Exception e) {
        System.out.println("General error: " + e.getMessage());
    } finally {
        System.out.println("This always executes");
    }
}

// Try-with-resources (Java 7+)
public void readFile(String filename) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
    } catch (IOException e) {
        System.err.println("Error reading file: " + e.getMessage());
    }
}
```

### **Custom Exceptions**
```java
// Checked Exception
public class InsufficientFundsException extends Exception {
    private double amount;
    private double balance;
    
    public InsufficientFundsException(double amount, double balance) {
        super("Insufficient funds. Required: " + amount + ", Available: " + balance);
        this.amount = amount;
        this.balance = balance;
    }
}

// Unchecked Exception
public class InvalidAgeException extends RuntimeException {
    private int age;
    
    public InvalidAgeException(int age) {
        super("Invalid age: " + age + ". Age must be between 0 and 150.");
        this.age = age;
    }
}

// Usage
public void withdraw(double amount) throws InsufficientFundsException {
    if (amount > balance) {
        throw new InsufficientFundsException(amount, balance);
    }
    balance -= amount;
}

public void setAge(int age) {
    if (age < 0 || age > 150) {
        throw new InvalidAgeException(age);
    }
    this.age = age;
}
```

---

## 🏷️ **Annotations**

### **Common Annotations**
```java
// Class-level annotations
@Entity
@Table(name = "users")
@RestController
@RequestMapping("/api/users")
@Service
@Repository
@Component

// Method-level annotations
@Override
@PostMapping("/users")
@GetMapping("/{id}")
@Transactional
@Cacheable(key = "#id")
@Async

// Field-level annotations
@Autowired
@Value("${app.name}")
@Column(name = "user_name")
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)

// Parameter annotations
@PathVariable Long id
@RequestBody CreateUserRequest request
@RequestParam String name
@Valid CreateUserRequest request
```

### **Custom Annotations**
```java
// Define custom annotation
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogExecutionTime {
    String value() default "";
}

// Use custom annotation
@LogExecutionTime("User creation")
public User createUser(CreateUserRequest request) {
    // Method implementation
}

// Process custom annotation
@Aspect
@Component
public class LogExecutionTimeAspect {
    
    @Around("@annotation(logExecutionTime)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, 
                                  LogExecutionTime logExecutionTime) throws Throwable {
        long start = System.currentTimeMillis();
        
        Object result = joinPoint.proceed();
        
        long executionTime = System.currentTimeMillis() - start;
        System.out.println(logExecutionTime.value() + " executed in " + executionTime + "ms");
        
        return result;
    }
}
```

---

## 🚀 **Best Practices**

### **1. Use Constructor Injection**
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

### **2. Proper Exception Handling**
```java
public void processUser(String userId) throws UserNotFoundException, InvalidUserDataException {
    if (userId == null || userId.trim().isEmpty()) {
        throw new IllegalArgumentException("User ID cannot be null or empty");
    }
    
    User user = userRepository.findById(userId);
    if (user == null) {
        throw new UserNotFoundException("User not found: " + userId);
    }
    
    if (!user.isValid()) {
        throw new InvalidUserDataException("User data is invalid: " + userId);
    }
    
    // Process user
}
```

### **3. Use Optional for Null Safety**
```java
// Instead of returning null
public User findUserById(Long id) {
    return userRepository.findById(id).orElse(null);
}

// Use Optional
public Optional<User> findUserById(Long id) {
    return userRepository.findById(id);
}

// Usage
Optional<User> userOpt = findUserById(1L);
userOpt.ifPresent(user -> System.out.println("Found user: " + user.getName()));

User user = userOpt.orElseThrow(() -> new UserNotFoundException("User not found"));
```

### **4. Implement Proper Logging**
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

## 🎯 **Summary**

**Key Java Concepts for Spring Boot:**
- **OOP Principles**: Classes, inheritance, interfaces, polymorphism
- **Collections**: Lists, Sets, Maps with generics
- **Lambda & Streams**: Functional programming features
- **Exception Handling**: Proper error management
- **Annotations**: Metadata and configuration
- **Best Practices**: Clean, maintainable code

**Next Steps:**
- Practice with real projects
- Learn Spring Framework concepts
- Study design patterns
- Explore testing frameworks

---

**Happy Java Programming! ☕🚀**
