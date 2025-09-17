# 🏗️ Spring Boot Models (JPA Entities) - Complete Guide

## 🎯 **What are Models/Entities?**

Models in Spring Boot are Java classes that represent database tables using JPA (Java Persistence API) annotations. They define the structure, relationships, and behavior of your data.

---

## 🏗️ **Basic Entity Structure**

### **Simple Entity Example**
```java
@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;
    
    @Column(name = "email", nullable = false, unique = true)
    private String email;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    // Constructors, getters, setters
}
```

---

## 🏷️ **Core JPA Annotations**

### **@Entity**
```java
@Entity
public class User {
    // Entity class
}

// With custom table name
@Entity(name = "user_table")
@Table(name = "users")
public class User {
    // Entity with custom table name
}
```

### **@Table**
```java
@Entity
@Table(
    name = "users",
    schema = "public",
    catalog = "myapp",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username", "email"})
    },
    indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email")
    }
)
public class User {
    // Entity with table configuration
}
```

### **@Id and @GeneratedValue**
```java
@Entity
public class User {
    
    // Auto-increment primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // UUID primary key
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;
    
    // Sequence primary key
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_sequence", allocationSize = 1)
    private Long sequenceId;
    
    // Table strategy
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "user_table_gen")
    @TableGenerator(name = "user_table_gen", table = "id_generator", 
                   pkColumnName = "gen_name", valueColumnName = "gen_value", 
                   pkColumnValue = "user_id", allocationSize = 1)
    private Long tableId;
}
```

### **@Column**
```java
@Entity
public class User {
    
    @Column(
        name = "username",
        nullable = false,
        unique = true,
        length = 50,
        precision = 10,
        scale = 2,
        insertable = true,
        updatable = true,
        columnDefinition = "VARCHAR(50) NOT NULL"
    )
    private String username;
    
    @Column(name = "age")
    private Integer age;
    
    @Column(name = "salary", precision = 10, scale = 2)
    private BigDecimal salary;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "is_active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive;
}
```

---

## 🔗 **Relationship Annotations**

### **@OneToOne**
```java
@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private UserProfile profile;
    
    // Unidirectional relationship
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id")
    private Address address;
}

@Entity
public class UserProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(mappedBy = "profile")
    private User user;
}
```

### **@OneToMany and @ManyToOne**
```java
@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @OrderBy("createdAt DESC")
    private List<Post> posts = new ArrayList<>();
}

@Entity
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
```

### **@ManyToMany**
```java
@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}

@Entity
public class Role {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();
}
```

---

## 🧬 **Inheritance Strategies**

### **Single Table Inheritance**
```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USER")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
}

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {
    
    private String adminLevel;
    private String permissions;
}

@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends User {
    
    private String customerType;
    private LocalDate membershipDate;
}
```

### **Joined Table Inheritance**
```java
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
}

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Admin extends User {
    
    private String adminLevel;
    private String permissions;
}

@Entity
@PrimaryKeyJoinColumn(name = "user_id")
public class Customer extends User {
    
    private String customerType;
    private LocalDate membershipDate;
}
```

### **Table Per Class Inheritance**
```java
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
}

@Entity
public class Admin extends User {
    
    private String adminLevel;
    private String permissions;
}

@Entity
public class Customer extends User {
    
    private String customerType;
    private LocalDate membershipDate;
}
```

---

## 📊 **Advanced Annotations**

### **@Embeddable and @Embedded**
```java
@Embeddable
public class Address {
    
    @Column(name = "street")
    private String street;
    
    @Column(name = "city")
    private String city;
    
    @Column(name = "state")
    private String state;
    
    @Column(name = "zip_code")
    private String zipCode;
    
    // Getters and setters
}

@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "home_street")),
        @AttributeOverride(name = "city", column = @Column(name = "home_city")),
        @AttributeOverride(name = "state", column = @Column(name = "home_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "home_zip"))
    })
    private Address homeAddress;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "work_street")),
        @AttributeOverride(name = "city", column = @Column(name = "work_city")),
        @AttributeOverride(name = "state", column = @Column(name = "work_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "work_zip"))
    })
    private Address workAddress;
}
```

### **@Enumerated**
```java
@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type")
    private UserType type;
}

public enum UserStatus {
    ACTIVE, INACTIVE, SUSPENDED, DELETED
}

public enum UserType {
    ADMIN, USER, MODERATOR
}
```

### **@Temporal and @Transient**
```java
@Entity
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "birth_date")
    private Date birthDate;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_login")
    private Date lastLogin;
    
    @Transient
    private String fullName; // Not persisted to database
    
    @Transient
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
```

---

## 🔄 **Lifecycle Annotations**

### **Entity Lifecycle Callbacks**
```java
@Entity
@EntityListeners(UserAuditListener.class)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String email;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    @PostLoad
    protected void onLoad() {
        System.out.println("User loaded: " + name);
    }
    
    @PostPersist
    protected void onPersist() {
        System.out.println("User persisted: " + name);
    }
    
    @PostUpdate
    protected void onUpdate() {
        System.out.println("User updated: " + name);
    }
    
    @PostRemove
    protected void onRemove() {
        System.out.println("User removed: " + name);
    }
}

// Custom Entity Listener
public class UserAuditListener {
    
    @PrePersist
    public void prePersist(User user) {
        // Custom logic before persist
    }
    
    @PostPersist
    public void postPersist(User user) {
        // Custom logic after persist
    }
}
```

---

## 📝 **Complete Entity Example**

```java
@Entity
@Table(
    name = "users",
    indexes = {
        @Index(name = "idx_username", columnList = "username"),
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_created_at", columnList = "created_at")
    }
)
@EntityListeners(UserAuditListener.class)
public class User extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", nullable = false, unique = true, length = 50)
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;
    
    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    
    @Column(name = "first_name", nullable = false, length = 100)
    @NotBlank(message = "First name is required")
    @Size(max = 100, message = "First name cannot exceed 100 characters")
    private String firstName;
    
    @Column(name = "last_name", nullable = false, length = 100)
    @NotBlank(message = "Last name is required")
    @Size(max = 100, message = "Last name cannot exceed 100 characters")
    private String lastName;
    
    @Column(name = "phone_number", length = 20)
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus status = UserStatus.ACTIVE;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UserType type = UserType.USER;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
    
    @Column(name = "last_login")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
    
    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "street", column = @Column(name = "address_street")),
        @AttributeOverride(name = "city", column = @Column(name = "address_city")),
        @AttributeOverride(name = "state", column = @Column(name = "address_state")),
        @AttributeOverride(name = "zipCode", column = @Column(name = "address_zip"))
    })
    private Address address;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("createdAt DESC")
    private List<Order> orders = new ArrayList<>();
    
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
    
    // Constructors
    public User() {}
    
    public User(String username, String email, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Business methods
    public void addOrder(Order order) {
        orders.add(order);
        order.setUser(this);
    }
    
    public void removeOrder(Order order) {
        orders.remove(order);
        order.setUser(null);
    }
    
    public void addRole(Role role) {
        roles.add(role);
        role.getUsers().add(this);
    }
    
    public void removeRole(Role role) {
        roles.remove(role);
        role.getUsers().remove(this);
    }
    
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public boolean hasRole(String roleName) {
        return roles.stream()
            .anyMatch(role -> role.getName().equals(roleName));
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public UserStatus getStatus() { return status; }
    public void setStatus(UserStatus status) { this.status = status; }
    
    public UserType getType() { return type; }
    public void setType(UserType type) { this.type = type; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public Date getLastLogin() { return lastLogin; }
    public void setLastLogin(Date lastLogin) { this.lastLogin = lastLogin; }
    
    public Address getAddress() { return address; }
    public void setAddress(Address address) { this.address = address; }
    
    public List<Order> getOrders() { return orders; }
    public void setOrders(List<Order> orders) { this.orders = orders; }
    
    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
    
    // toString, equals, hashCode
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", status=" + status +
                ", type=" + type +
                ", isActive=" + isActive +
                '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) &&
               Objects.equals(username, user.username) &&
               Objects.equals(email, user.email);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, username, email);
    }
}
```

---

## 🚀 **Best Practices**

### **1. Use Appropriate Fetch Types**
```java
// Use LAZY for collections and associations
@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
private List<Order> orders;

// Use EAGER only when necessary
@OneToOne(fetch = FetchType.EAGER)
private UserProfile profile;
```

### **2. Implement Proper equals() and hashCode()**
```java
@Override
public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return Objects.equals(id, user.id);
}

@Override
public int hashCode() {
    return Objects.hash(id);
}
```

### **3. Use Cascade Types Appropriately**
```java
// Use ALL for parent-child relationships
@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
private List<Order> orders;

// Use PERSIST for independent entities
@ManyToOne(cascade = CascadeType.PERSIST)
private Address address;
```

### **4. Handle Bidirectional Relationships**
```java
public void addOrder(Order order) {
    orders.add(order);
    order.setUser(this);
}

public void removeOrder(Order order) {
    orders.remove(order);
    order.setUser(null);
}
```

---

## 🎯 **Summary**

JPA Entities are the foundation of data persistence:
- Use appropriate annotations for mapping
- Choose correct inheritance strategies
- Implement proper relationships
- Handle lifecycle events
- Follow best practices for performance

---

**Happy Entity Modeling! 🏗️✨**
