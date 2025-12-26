# 📚 Spring Boot Repositories (Spring Data JPA) - Complete Guide

## 🎯 **What are Repositories?**

Repositories in Spring Boot provide a data access layer that abstracts the complexity of data persistence. They use Spring Data JPA to automatically implement common database operations.

---

## 🏗️ **Basic Repository Structure**

### **Simple Repository Interface**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Basic CRUD operations are automatically provided
    // findById, save, delete, findAll, etc.
}
```

---

## 🔧 **Core Repository Interfaces**

### **JpaRepository**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Extends PagingAndSortingRepository and CrudRepository
    // Provides all basic CRUD operations + pagination + sorting
}

// Available methods:
// save(), saveAll(), findById(), findAll(), findAllById()
// count(), delete(), deleteById(), deleteAll(), existsById()
// flush(), saveAndFlush(), deleteAllInBatch(), deleteInBatch()
```

### **CrudRepository**
```java
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    // Basic CRUD operations only
    // save(), findById(), findAll(), delete(), count(), existsById()
}
```

### **PagingAndSortingRepository**
```java
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    // CRUD + pagination and sorting
    // findAll(Sort sort), findAll(Pageable pageable)
}
```

---

## 🔍 **Query Methods**

### **Method Name Query Derivation**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find by single field
    User findByUsername(String username);
    User findByEmail(String email);
    List<User> findByFirstName(String firstName);
    
    // Find by multiple fields
    User findByUsernameAndEmail(String username, String email);
    List<User> findByFirstNameOrLastName(String firstName, String lastName);
    
    // Find with conditions
    List<User> findByAgeGreaterThan(int age);
    List<User> findByAgeLessThanEqual(int age);
    List<User> findByAgeBetween(int minAge, int maxAge);
    
    // Find with null checks
    List<User> findByPhoneNumberIsNull();
    List<User> findByPhoneNumberIsNotNull();
    
    // Find with string operations
    List<User> findByFirstNameLike(String pattern);
    List<User> findByFirstNameContaining(String substring);
    List<User> findByFirstNameStartingWith(String prefix);
    List<User> findByFirstNameEndingWith(String suffix);
    
    // Find with ordering
    List<User> findByAgeOrderByFirstNameAsc(int age);
    List<User> findByAgeOrderByFirstNameDesc(int age);
    
    // Find with limiting
    User findFirstByOrderByCreatedAtDesc();
    List<User> findTop3ByOrderByAgeDesc();
    
    // Find with distinct
    List<User> findDistinctByFirstName(String firstName);
    
    // Find with ignoring case
    List<User> findByFirstNameIgnoreCase(String firstName);
}
```

### **Complex Query Methods**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Multiple conditions with AND/OR
    List<User> findByFirstNameAndLastNameAndAgeGreaterThan(
        String firstName, String lastName, int age);
    
    List<User> findByFirstNameOrLastNameOrEmailContaining(
        String firstName, String lastName, String email);
    
    // Nested properties
    List<User> findByAddressCity(String city);
    List<User> findByAddressStateAndAddressCity(String state, String city);
    
    // Collection operations
    List<User> findByRolesName(String roleName);
    List<User> findByOrdersStatus(OrderStatus status);
    
    // Count queries
    long countByFirstName(String firstName);
    long countByAgeGreaterThan(int age);
    
    // Exists queries
    boolean existsByUsername(String username);
    boolean existsByEmailAndIsActiveTrue(String email);
    
    // Delete queries
    void deleteByUsername(String username);
    long deleteByAgeLessThan(int age);
}
```

---

## 📝 **Custom Queries with @Query**

### **JPQL Queries**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query("SELECT u FROM User u WHERE u.age > ?1")
    List<User> findUsersOlderThan(int age);
    
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %?1% OR u.lastName LIKE %?1%")
    List<User> findUsersByNameContaining(String name);
    
    @Query("SELECT u FROM User u WHERE u.createdAt >= ?1 AND u.createdAt <= ?2")
    List<User> findUsersCreatedBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1")
    List<User> findUsersByRoleName(String roleName);
    
    @Query("SELECT u FROM User u WHERE u.address.city = ?1 AND u.isActive = true")
    List<User> findActiveUsersByCity(String city);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.age BETWEEN ?1 AND ?2")
    long countUsersByAgeRange(int minAge, int maxAge);
    
    @Query("SELECT u FROM User u WHERE u.email = ?1 AND u.password = ?2")
    Optional<User> findUserByEmailAndPassword(String email, String password);
}
```

### **Native SQL Queries**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query(value = "SELECT * FROM users WHERE age > ?1", nativeQuery = true)
    List<User> findUsersOlderThanNative(int age);
    
    @Query(value = "SELECT * FROM users u JOIN user_roles ur ON u.id = ur.user_id " +
                   "JOIN roles r ON ur.role_id = r.id WHERE r.name = ?1", nativeQuery = true)
    List<User> findUsersByRoleNameNative(String roleName);
    
    @Query(value = "SELECT COUNT(*) FROM users WHERE created_at >= ?1", nativeQuery = true)
    long countUsersCreatedAfterNative(LocalDateTime date);
    
    @Query(value = "SELECT * FROM users WHERE username LIKE %?1% LIMIT ?2", nativeQuery = true)
    List<User> findUsersByUsernameLikeLimit(String username, int limit);
}
```

### **Named Queries**
```java
@Entity
@NamedQueries({
    @NamedQuery(name = "User.findByAgeRange", 
                query = "SELECT u FROM User u WHERE u.age BETWEEN :minAge AND :maxAge"),
    @NamedQuery(name = "User.findByCityAndStatus", 
                query = "SELECT u FROM User u WHERE u.address.city = :city AND u.status = :status")
})
public class User {
    // Entity fields
}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Query(name = "User.findByAgeRange")
    List<User> findByAgeRange(@Param("minAge") int minAge, @Param("maxAge") int maxAge);
    
    @Query(name = "User.findByCityAndStatus")
    List<User> findByCityAndStatus(@Param("city") String city, @Param("status") UserStatus status);
}
```

---

## 📊 **Pagination and Sorting**

### **Basic Pagination**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Method name with Pageable
    Page<User> findByAge(int age, Pageable pageable);
    
    // Custom query with pagination
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    Page<User> findActiveUsers(Pageable pageable);
    
    // Slice for better performance (no total count)
    Slice<User> findByFirstNameContaining(String firstName, Pageable pageable);
}

// Usage in service
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public Page<User> getUsers(int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findActiveUsers(pageable);
    }
}
```

### **Advanced Sorting**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Multiple sort criteria
    @Query("SELECT u FROM User u WHERE u.isActive = true")
    Page<User> findActiveUsers(Pageable pageable);
}

// Usage
Sort sort = Sort.by(
    Sort.Order.asc("firstName"),
    Sort.Order.desc("lastName"),
    Sort.Order.asc("age")
);

Pageable pageable = PageRequest.of(0, 10, sort);
Page<User> users = userRepository.findActiveUsers(pageable);
```

---

## 🔄 **Modifying Queries**

### **@Modifying with @Query**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = ?1 WHERE u.id = ?2")
    @Transactional
    int updateLastLogin(LocalDateTime lastLogin, Long userId);
    
    @Modifying
    @Query("UPDATE User u SET u.isActive = ?1 WHERE u.age < ?2")
    @Transactional
    int deactivateUsersByAge(boolean isActive, int age);
    
    @Modifying
    @Query("DELETE FROM User u WHERE u.createdAt < ?1")
    @Transactional
    int deleteUsersCreatedBefore(LocalDateTime date);
    
    @Modifying
    @Query("UPDATE User u SET u.status = ?1 WHERE u.lastLogin < ?2")
    @Transactional
    int updateStatusForInactiveUsers(UserStatus status, LocalDateTime lastLogin);
}
```

### **Bulk Operations**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Modifying
    @Query("UPDATE User u SET u.isActive = false WHERE u.lastLogin < ?1")
    @Transactional
    int deactivateInactiveUsers(LocalDateTime cutoffDate);
    
    @Modifying
    @Query("DELETE FROM User u WHERE u.status = ?1 AND u.createdAt < ?2")
    @Transactional
    int deleteOldUsersByStatus(UserStatus status, LocalDateTime cutoffDate);
}
```

---

## 🎨 **Advanced Repository Features**

### **Specifications (Dynamic Queries)**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    // Extends JpaSpecificationExecutor for dynamic queries
}

// Specification class
public class UserSpecifications {
    
    public static Specification<User> hasFirstName(String firstName) {
        return (root, query, criteriaBuilder) -> {
            if (firstName == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("firstName"), "%" + firstName + "%");
        };
    }
    
    public static Specification<User> hasAge(int age) {
        return (root, query, criteriaBuilder) -> {
            if (age <= 0) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("age"), age);
        };
    }
    
    public static Specification<User> isActive() {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.isTrue(root.get("isActive"));
    }
    
    public static Specification<User> hasRole(String roleName) {
        return (root, query, criteriaBuilder) -> {
            if (roleName == null) {
                return criteriaBuilder.conjunction();
            }
            Join<User, Role> roleJoin = root.join("roles");
            return criteriaBuilder.equal(roleJoin.get("name"), roleName);
        };
    }
}

// Usage
@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public List<User> findUsers(String firstName, Integer age, String roleName) {
        Specification<User> spec = Specification.where(UserSpecifications.hasFirstName(firstName))
            .and(UserSpecifications.hasAge(age))
            .and(UserSpecifications.hasRole(roleName))
            .and(UserSpecifications.isActive());
        
        return userRepository.findAll(spec);
    }
}
```

### **Projections**
```java
// Interface-based projection
public interface UserSummary {
    Long getId();
    String getFirstName();
    String getLastName();
    String getEmail();
}

// Class-based projection
public class UserDetail {
    private Long id;
    private String fullName;
    private String email;
    private int orderCount;
    
    public UserDetail(Long id, String firstName, String lastName, String email, int orderCount) {
        this.id = id;
        this.fullName = firstName + " " + lastName;
        this.email = email;
        this.orderCount = orderCount;
    }
    
    // Getters
}

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Interface projection
    List<UserSummary> findByIsActiveTrue();
    
    // Class projection
    @Query("SELECT new com.example.UserDetail(u.id, u.firstName, u.lastName, u.email, SIZE(u.orders)) " +
           "FROM User u WHERE u.isActive = true")
    List<UserDetail> findActiveUserDetails();
    
    // Dynamic projection
    <T> List<T> findByIsActiveTrue(Class<T> type);
}
```

---

## 🚀 **Repository Best Practices**

### **1. Use Appropriate Repository Interface**
```java
// For basic CRUD operations
public interface UserRepository extends CrudRepository<User, Long> {}

// For pagination and sorting
public interface UserRepository extends PagingAndSortingRepository<User, Long> {}

// For all features (recommended)
public interface UserRepository extends JpaRepository<User, Long> {}
```

### **2. Method Naming Conventions**
```java
// Good naming
findByFirstNameAndLastName(String firstName, String lastName)
findByAgeGreaterThanAndIsActiveTrue(int age)
findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start, LocalDateTime end)

// Avoid complex method names
// findByFirstNameAndLastNameAndAgeAndEmailAndStatusAndIsActiveTrue - Too long!
```

### **3. Use @Query for Complex Queries**
```java
// Use @Query for complex queries instead of long method names
@Query("SELECT u FROM User u WHERE u.firstName LIKE %?1% OR u.lastName LIKE %?1%")
List<User> searchUsersByName(String name);

// Instead of:
// findByFirstNameContainingOrLastNameContaining(String firstName, String lastName)
```

### **4. Handle Transactions Properly**
```java
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = ?1 WHERE u.id = ?2")
    @Transactional
    int updateLastLogin(LocalDateTime lastLogin, Long userId);
}

// Or at service level
@Service
@Transactional
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    public void updateUserLastLogin(Long userId) {
        userRepository.updateLastLogin(LocalDateTime.now(), userId);
    }
}
```

### **5. Use Specifications for Dynamic Queries**
```java
// Instead of multiple repository methods
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {}

// Use specifications for flexible querying
public List<User> findUsers(UserSearchCriteria criteria) {
    Specification<User> spec = buildSpecification(criteria);
    return userRepository.findAll(spec);
}
```

---

## 📊 **Complete Repository Example**

```java
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    
    // Basic find methods
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    
    // Find by multiple criteria
    List<User> findByFirstNameAndLastName(String firstName, String lastName);
    List<User> findByAgeBetween(int minAge, int maxAge);
    List<User> findByStatusAndIsActiveTrue(UserStatus status);
    
    // Find with ordering
    List<User> findByIsActiveTrueOrderByCreatedAtDesc();
    List<User> findTop10ByOrderByLastLoginDesc();
    
    // Find with pagination
    Page<User> findByIsActiveTrue(Pageable pageable);
    Page<User> findByAgeGreaterThan(int age, Pageable pageable);
    
    // Custom queries
    @Query("SELECT u FROM User u WHERE u.firstName LIKE %?1% OR u.lastName LIKE %?1%")
    List<User> searchByName(String name);
    
    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = ?1")
    List<User> findByRoleName(String roleName);
    
    @Query("SELECT COUNT(u) FROM User u WHERE u.createdAt >= ?1")
    long countUsersCreatedAfter(LocalDateTime date);
    
    // Modifying queries
    @Modifying
    @Query("UPDATE User u SET u.lastLogin = ?1 WHERE u.id = ?2")
    @Transactional
    int updateLastLogin(LocalDateTime lastLogin, Long userId);
    
    @Modifying
    @Query("UPDATE User u SET u.isActive = ?1 WHERE u.lastLogin < ?2")
    @Transactional
    int updateStatusForInactiveUsers(boolean isActive, LocalDateTime lastLogin);
    
    // Projections
    List<UserSummary> findByIsActiveTrue();
    
    @Query("SELECT new com.example.UserDetail(u.id, u.firstName, u.lastName, u.email, SIZE(u.orders)) " +
           "FROM User u WHERE u.isActive = true")
    List<UserDetail> findActiveUserDetails();
}
```

---

## 🎯 **Summary**

Spring Data JPA Repositories provide:
- Automatic CRUD operations
- Method name query derivation
- Custom queries with @Query
- Pagination and sorting
- Specifications for dynamic queries
- Projections for selective data retrieval
- Transaction management

**Key Benefits:**
- Reduce boilerplate code
- Type-safe queries
- Easy testing
- Performance optimization
- Flexible querying

---

**Happy Repository Development! 📚✨**
