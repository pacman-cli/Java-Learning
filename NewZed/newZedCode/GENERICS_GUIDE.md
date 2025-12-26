# 🎓 Beginner's Guide to Generic Types (`<T>`) in This Project

## 📚 Table of Contents
1. [What Are Generics?](#what-are-generics)
2. [Why Do We Use Generics?](#why-do-we-use-generics)
3. [Understanding `<T>` Notation](#understanding-t-notation)
4. [Real Examples from Our Project](#real-examples-from-our-project)
5. [Common Generic Patterns](#common-generic-patterns)
6. [Tips and Best Practices](#tips-and-best-practices)

---

## What Are Generics?

### Simple Analogy 🎁

Think of generics like a **gift box**:
- The box itself is always the same shape and size
- But you can put DIFFERENT things inside: toys, books, clothes, etc.
- The box adapts to hold whatever you put in it

In programming:
- `ApiResponse` is the box
- `<T>` means "this box can hold any type of item"
- When you use it, you specify: `ApiResponse<UserDTO>` means "a box holding UserDTO"

### Technical Definition

**Generics** allow you to write code that works with ANY type, while still being type-safe.

Instead of writing:
```java
// ❌ Not type-safe - can put anything
public class ApiResponse {
    private Object data;  // Could be anything!
}
```

We write:
```java
// ✅ Type-safe - Java knows exactly what type is inside
public class ApiResponse<T> {
    private T data;  // T will be replaced with actual type
}
```

---

## Why Do We Use Generics?

### Without Generics (Old Way) ❌

You'd need separate classes for each type:

```java
public class UserApiResponse {
    private boolean success;
    private String message;
    private UserDTO data;  // Only works for UserDTO
}

public class ProductApiResponse {
    private boolean success;
    private String message;
    private ProductDTO data;  // Only works for ProductDTO
}

public class OrderApiResponse {
    private boolean success;
    private String message;
    private OrderDTO data;  // Only works for OrderDTO
}

// 😫 Need 100 classes for 100 different types!
```

### With Generics (Modern Way) ✅

One class works for EVERYTHING:

```java
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;  // Works with ANY type!
}

// Usage:
ApiResponse<UserDTO> userResponse;      // For users
ApiResponse<ProductDTO> productResponse; // For products
ApiResponse<OrderDTO> orderResponse;     // For orders
ApiResponse<String> messageResponse;     // Even for simple strings!
```

**Benefits:**
- ✅ **Less code duplication** - Write once, use everywhere
- ✅ **Type safety** - Compiler catches mistakes
- ✅ **Cleaner code** - No need for casting
- ✅ **Reusable** - Same class works for all types

---

## Understanding `<T>` Notation

### Basic Syntax

```java
public class ClassName<T> {
    //                 ^
    //                 |
    //   Generic type parameter - a placeholder
}
```

### What Does `T` Mean?

- `T` stands for **"Type"** (it's just a naming convention)
- You can use any letter: `E`, `K`, `V`, etc.
- Common conventions:
  - `T` = Type (general purpose)
  - `E` = Element (in collections)
  - `K` = Key (in maps)
  - `V` = Value (in maps)
  - `R` = Result/Return type

### Multiple Generic Parameters

```java
public class KeyValuePair<K, V> {
    private K key;    // K is one type
    private V value;  // V is another type
}

// Usage:
KeyValuePair<String, Integer> pair = new KeyValuePair<>();
// K = String, V = Integer
```

---

## Real Examples from Our Project

### Example 1: ApiResponse<T>

**Location:** `com.zedcode.common.dto.ApiResponse`

```java
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;  // <-- T is used here
    private LocalDateTime timestamp;
}
```

**How It Works:**

```java
// Example 1: Return a single user
UserDTO user = userService.getUserById(1L);
ApiResponse<UserDTO> response = ApiResponse.success(user);
//           ^                                        ^
//           |                                        |
//    T becomes UserDTO                         Data is UserDTO

// JSON Output:
{
  "success": true,
  "message": "Operation successful",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com"
  }
}

// Example 2: Return a list of users
List<UserDTO> users = userService.getAllUsers();
ApiResponse<List<UserDTO>> response = ApiResponse.success(users);
//           ^
//           |
//    T becomes List<UserDTO>

// JSON Output:
{
  "success": true,
  "message": "Operation successful",
  "data": [
    { "id": 1, "name": "John" },
    { "id": 2, "name": "Jane" }
  ]
}

// Example 3: Return just a message
ApiResponse<Void> response = ApiResponse.success("Deleted successfully");
//           ^
//           |
//    T is Void (no data)

// JSON Output:
{
  "success": true,
  "message": "Deleted successfully"
}
```

### Example 2: PageResponse<T>

**Location:** `com.zedcode.common.dto.PageResponse`

```java
public class PageResponse<T> {
    private List<T> content;  // <-- T is used in a List
    private int pageNumber;
    private int pageSize;
    private long totalElements;
}
```

**How It Works:**

```java
// Get page of users
PageResponse<UserDTO> userPage = userService.getAllUsers(pageable);
//            ^                                           ^
//            |                                           |
//    T becomes UserDTO                    Returns page of UserDTOs

// Access the data:
List<UserDTO> users = userPage.getContent();  // List of UserDTOs
int pageNum = userPage.getPageNumber();       // Page number
long total = userPage.getTotalElements();     // Total count

// JSON Output:
{
  "content": [
    { "id": 1, "name": "John" },
    { "id": 2, "name": "Jane" },
    { "id": 3, "name": "Bob" }
  ],
  "pageNumber": 0,
  "pageSize": 3,
  "totalElements": 100,
  "totalPages": 34
}
```

### Example 3: Generic Method

**Location:** `com.zedcode.module.user.service.UserServiceImpl`

```java
private <T> PageResponse<T> buildPageResponse(Page<?> page, List<T> content) {
    //  ^                                                      ^
    //  |                                                      |
    // Method is generic                              T is used here
    
    return PageResponse.<T>builder()
        .content(content)
        .pageNumber(page.getNumber())
        .totalElements(page.getTotalElements())
        .build();
}
```

**Breaking It Down:**

```java
// Step-by-step explanation:

// 1. This is a GENERIC METHOD (note the <T> before return type)
private <T> PageResponse<T> buildPageResponse(Page<?> page, List<T> content)
//      ^   ^                                                ^
//      |   |                                                |
//      |   Return type uses T                         Parameter uses T
//      |
//   Generic type declaration for this method

// 2. When you call this method:
Page<User> userPage = userRepository.findAll(pageable);
List<UserDTO> dtos = userMapper.toDTOList(userPage.getContent());
PageResponse<UserDTO> response = buildPageResponse(userPage, dtos);
//           ^                                                  ^
//           |                                                  |
//      T becomes UserDTO                             Java infers T from the List

// 3. Java automatically figures out: T = UserDTO
```

---

## Common Generic Patterns

### Pattern 1: Generic Class

```java
public class Box<T> {
    private T item;
    
    public void put(T item) {
        this.item = item;
    }
    
    public T get() {
        return item;
    }
}

// Usage:
Box<String> stringBox = new Box<>();
stringBox.put("Hello");
String value = stringBox.get();  // Returns String

Box<Integer> intBox = new Box<>();
intBox.put(42);
Integer number = intBox.get();  // Returns Integer
```

### Pattern 2: Generic Method

```java
public class Utility {
    // Generic method - <T> comes before return type
    public static <T> T getFirst(List<T> list) {
        //         ^      ^            ^
        //         |      |            |
        //    Declaration  Return   Parameter
        
        return list.isEmpty() ? null : list.get(0);
    }
}

// Usage:
List<String> names = Arrays.asList("John", "Jane");
String first = Utility.getFirst(names);  // Returns "John" (String)

List<Integer> numbers = Arrays.asList(1, 2, 3);
Integer firstNum = Utility.getFirst(numbers);  // Returns 1 (Integer)
```

### Pattern 3: Bounded Generics

```java
// T must extend Number (can only be Number or its subclasses)
public class Calculator<T extends Number> {
    private T value;
    
    public double getDouble() {
        return value.doubleValue();  // Works because T is a Number
    }
}

// Usage:
Calculator<Integer> intCalc = new Calculator<>();    // ✅ OK - Integer extends Number
Calculator<Double> doubleCalc = new Calculator<>();  // ✅ OK - Double extends Number
Calculator<String> stringCalc = new Calculator<>();  // ❌ ERROR - String doesn't extend Number
```

### Pattern 4: Wildcards

```java
// Unknown type - can read but not write
public void printList(List<?> list) {
    //                     ^
    //                     |
    //              Wildcard - accepts any type
    
    for (Object item : list) {
        System.out.println(item);
    }
}

// Usage:
printList(Arrays.asList(1, 2, 3));           // Works
printList(Arrays.asList("a", "b", "c"));     // Works
printList(Arrays.asList(user1, user2));      // Works
```

---

## Tips and Best Practices

### 1. ✅ Use Descriptive Type Parameter Names (When Appropriate)

```java
// Generic - good for general use
public class ApiResponse<T> { }

// More specific - better for domain-specific classes
public class Cache<Key, Value> { }
public class Repository<Entity, Id> { }
```

### 2. ✅ Understand Type Erasure

Java generics use **"type erasure"** - the generic type information is removed at runtime.

```java
ApiResponse<UserDTO> response1 = new ApiResponse<>();
ApiResponse<ProductDTO> response2 = new ApiResponse<>();

// At runtime, both are just ApiResponse
// Generic type info only exists at compile time
```

### 3. ✅ Use Diamond Operator

```java
// Old way (pre-Java 7)
List<UserDTO> users = new ArrayList<UserDTO>();

// New way (Java 7+) - compiler infers the type
List<UserDTO> users = new ArrayList<>();  // <-- Diamond operator
```

### 4. ✅ Static Methods Need Own Generic Declaration

```java
public class ApiResponse<T> {
    // ❌ Wrong - can't use class T in static method
    // public static ApiResponse<T> success(T data) { }
    
    // ✅ Correct - declare own generic type
    public static <T> ApiResponse<T> success(T data) {
        //         ^   ^
        //         |   |
        //    Declares T  Uses T
        return new ApiResponse<>(data);
    }
}
```

### 5. ✅ Generic Methods vs Generic Classes

```java
// Generic CLASS - T is available to all instance methods
public class Box<T> {
    private T item;
    
    public void set(T item) { this.item = item; }  // Uses class T
    public T get() { return item; }                // Uses class T
}

// Generic METHOD - E is only available in this method
public class Utility {
    public static <E> E getFirst(List<E> list) {
        //         ^  ^           ^
        //         |  |           |
        //    Declares E     Uses E
        return list.isEmpty() ? null : list.get(0);
    }
}
```

---

## Quick Reference Table

| Code | Meaning | Example |
|------|---------|---------|
| `<T>` | Generic type parameter | `ApiResponse<T>` |
| `List<T>` | List of type T | `List<UserDTO>` |
| `<T, U>` | Multiple type parameters | `Map<String, Integer>` |
| `<?>` | Unknown type (wildcard) | `List<?>` |
| `<T extends Something>` | Bounded type parameter | `<T extends Number>` |
| `<? extends T>` | Upper bounded wildcard | `List<? extends Number>` |
| `<? super T>` | Lower bounded wildcard | `List<? super Integer>` |

---

## Common Questions

### Q: Why not just use `Object` everywhere?

**A:** Object loses type safety!

```java
// With Object - NOT type-safe
Box box = new Box();
box.put("Hello");
Integer num = (Integer) box.get();  // ❌ Runtime error! ClassCastException

// With Generics - Type-safe
Box<String> box = new Box<>();
box.put("Hello");
String text = box.get();  // ✅ Compile-time safety, no casting needed
```

### Q: When should I use generics?

**A:** Use generics when:
- Your class/method works with multiple types
- You want type safety
- You want to avoid code duplication

### Q: Can I create an instance of T?

**A:** No, not directly (because of type erasure):

```java
// ❌ Doesn't work
public class Factory<T> {
    public T create() {
        return new T();  // ERROR: Cannot instantiate the type T
    }
}

// ✅ Solution: Pass a class reference
public class Factory<T> {
    private Class<T> type;
    
    public T create() throws Exception {
        return type.getDeclaredConstructor().newInstance();
    }
}
```

---

## Summary 🎯

1. **Generics (`<T>`) = Type Placeholders** - They let you write flexible, reusable code
2. **Used for type safety** - Catch errors at compile-time, not runtime
3. **Common in our project:**
   - `ApiResponse<T>` - Wraps any response data
   - `PageResponse<T>` - Wraps paginated data
   - `List<T>` - List of any type
4. **Remember:** T is just a placeholder - it gets replaced with actual types when you use the class

---

## Need More Help?

- Read the comments in `ApiResponse.java` - they have detailed explanations
- Look at `PageResponse.java` - shows practical pagination examples
- Check `UserServiceImpl.java` - see generics in action

Happy coding! 🚀