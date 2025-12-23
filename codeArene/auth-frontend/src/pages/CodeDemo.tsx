import React from 'react';
import CodeBlock from '../components/CodeBlock';

const CodeDemo: React.FC = () => {
  const javaExamples = {
    basicClass: `public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");

        // Variable declarations
        int number = 42;
        String message = "Welcome to Java";
        boolean isActive = true;
        char grade = 'A';

        // Conditional statements
        if (isActive) {
            System.out.println("User is active");
        } else {
            System.out.println("User is inactive");
        }

        // Loop example
        for (int i = 0; i < 5; i++) {
            System.out.println("Count: " + i);
        }
    }
}`,

    springBootController: `@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    public AuthController(AuthService authService, JwtUtils jwtUtils) {
        this.authService = authService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authService.authenticate(
                request.getUsername(),
                request.getPassword()
            );

            String token = jwtUtils.generateToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            return ResponseEntity.ok(new LoginResponse(
                token,
                userDetails.getUsername(),
                userDetails.getAuthorities()
            ));
        } catch (BadCredentialsException e) {
            logger.warn("Failed login attempt for user: {}", request.getUsername());
            throw new UnauthorizedException("Invalid credentials");
        }
    }
}`,

    dataStructures: `import java.util.*;
import java.util.stream.Collectors;

public class DataStructureExample {

    public static void main(String[] args) {
        // Collections and Generics
        List<String> names = new ArrayList<>();
        names.add("Alice");
        names.add("Bob");
        names.add("Charlie");

        Map<String, Integer> scores = new HashMap<>();
        scores.put("Math", 95);
        scores.put("Science", 87);
        scores.put("History", 92);

        // Stream API with lambda expressions
        List<String> filteredNames = names.stream()
            .filter(name -> name.length() > 3)
            .map(String::toUpperCase)
            .collect(Collectors.toList());

        // Enhanced for loop
        for (String name : filteredNames) {
            System.out.println("Name: " + name);
        }

        // Optional handling
        Optional<Integer> maxScore = scores.values().stream()
            .max(Integer::compareTo);

        maxScore.ifPresent(score ->
            System.out.println("Highest score: " + score)
        );
    }
}`,

    exceptionHandling: `public class UserService {

    private final UserRepository userRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    public User createUser(CreateUserRequest request) throws UserServiceException {
        try {
            // Validation
            validateUserRequest(request);

            // Check if user already exists
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new UserAlreadyExistsException("User with email already exists");
            }

            // Create new user
            User user = new User();
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(Role.USER);
            user.setCreatedAt(LocalDateTime.now());
            user.setEnabled(true);

            User savedUser = userRepository.save(user);
            LOGGER.info("Created new user with ID: {}", savedUser.getId());

            return savedUser;

        } catch (DataIntegrityViolationException e) {
            LOGGER.error("Database error while creating user", e);
            throw new UserServiceException("Failed to create user due to database error");
        } catch (Exception e) {
            LOGGER.error("Unexpected error while creating user", e);
            throw new UserServiceException("An unexpected error occurred");
        }
    }

    private void validateUserRequest(CreateUserRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("User request cannot be null");
        }

        // Additional validation logic here
    }
}`,

    inheritance: `// Abstract base class
public abstract class Animal {
    protected String name;
    protected int age;

    public Animal(String name, int age) {
        this.name = name;
        this.age = age;
    }

    // Abstract method
    public abstract void makeSound();

    // Concrete method
    public void sleep() {
        System.out.println(name + " is sleeping");
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}

// Interface
public interface Flyable {
    void fly();
    default void land() {
        System.out.println("Landing safely");
    }
}

// Concrete implementation
public class Dog extends Animal implements Comparable<Dog> {
    private String breed;

    public Dog(String name, int age, String breed) {
        super(name, age);
        this.breed = breed;
    }

    @Override
    public void makeSound() {
        System.out.println(name + " barks: Woof! Woof!");
    }

    @Override
    public int compareTo(Dog other) {
        return Integer.compare(this.age, other.age);
    }

    public String getBreed() { return breed; }
}`
  };

  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-3xl font-bold text-gray-900">Java Syntax Highlighting Demo</h1>
        <p className="mt-2 text-lg text-gray-600">
          Showcase of custom Java syntax highlighting with different code examples
        </p>
      </div>

      <div className="space-y-8">
        <div>
          <h2 className="text-2xl font-semibold text-gray-800 mb-4">Basic Java Class</h2>
          <CodeBlock
            code={javaExamples.basicClass}
            language="java"
            title="HelloWorld.java"
            showLineNumbers={true}
          />
        </div>

        <div>
          <h2 className="text-2xl font-semibold text-gray-800 mb-4">Spring Boot Controller</h2>
          <CodeBlock
            code={javaExamples.springBootController}
            language="java"
            title="AuthController.java"
            showLineNumbers={true}
          />
        </div>

        <div>
          <h2 className="text-2xl font-semibold text-gray-800 mb-4">Data Structures & Collections</h2>
          <CodeBlock
            code={javaExamples.dataStructures}
            language="java"
            title="DataStructureExample.java"
            showLineNumbers={true}
          />
        </div>

        <div>
          <h2 className="text-2xl font-semibold text-gray-800 mb-4">Exception Handling</h2>
          <CodeBlock
            code={javaExamples.exceptionHandling}
            language="java"
            title="UserService.java"
            showLineNumbers={true}
          />
        </div>

        <div>
          <h2 className="text-2xl font-semibold text-gray-800 mb-4">Inheritance & Interfaces</h2>
          <CodeBlock
            code={javaExamples.inheritance}
            language="java"
            title="Animal.java"
            showLineNumbers={true}
          />
        </div>
      </div>

      {/* Color Legend */}
      <div className="bg-white rounded-lg shadow p-6">
        <h3 className="text-lg font-semibold text-gray-800 mb-4">Java Syntax Color Legend</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          <div className="flex items-center space-x-3">
            <div className="w-4 h-4 rounded" style={{ backgroundColor: '#ff7875' }}></div>
            <span className="text-sm text-gray-600">Keywords (public, private, class, if, etc.)</span>
          </div>
          <div className="flex items-center space-x-3">
            <div className="w-4 h-4 rounded" style={{ backgroundColor: '#73d0ff' }}></div>
            <span className="text-sm text-gray-600">Class Names & Types</span>
          </div>
          <div className="flex items-center space-x-3">
            <div className="w-4 h-4 rounded" style={{ backgroundColor: '#bae67e' }}></div>
            <span className="text-sm text-gray-600">Method Names</span>
          </div>
          <div className="flex items-center space-x-3">
            <div className="w-4 h-4 rounded" style={{ backgroundColor: '#d5ff80' }}></div>
            <span className="text-sm text-gray-600">Strings & Characters</span>
          </div>
          <div className="flex items-center space-x-3">
            <div className="w-4 h-4 rounded" style={{ backgroundColor: '#ffb454' }}></div>
            <span className="text-sm text-gray-600">Numbers</span>
          </div>
          <div className="flex items-center space-x-3">
            <div className="w-4 h-4 rounded" style={{ backgroundColor: '#ffd580' }}></div>
            <span className="text-sm text-gray-600">Annotations (@Override, @Autowired)</span>
          </div>
          <div className="flex items-center space-x-3">
            <div className="w-4 h-4 rounded" style={{ backgroundColor: '#5c6773' }}></div>
            <span className="text-sm text-gray-600">Comments</span>
          </div>
          <div className="flex items-center space-x-3">
            <div className="w-4 h-4 rounded" style={{ backgroundColor: '#f29e74' }}></div>
            <span className="text-sm text-gray-600">Operators (+, -, =, etc.)</span>
          </div>
          <div className="flex items-center space-x-3">
            <div className="w-4 h-4 rounded" style={{ backgroundColor: '#5ccfe6' }}></div>
            <span className="text-sm text-gray-600">Built-in Types & Methods</span>
          </div>
        </div>
      </div>

      {/* Features */}
      <div className="bg-gradient-to-r from-blue-50 to-indigo-50 rounded-lg p-6">
        <h3 className="text-lg font-semibold text-gray-800 mb-4">Features</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="flex items-start space-x-3">
            <div className="flex-shrink-0 w-2 h-2 bg-green-400 rounded-full mt-2"></div>
            <div>
              <h4 className="font-medium text-gray-800">Syntax Highlighting</h4>
              <p className="text-sm text-gray-600">Custom colors for Java keywords, classes, methods, and more</p>
            </div>
          </div>
          <div className="flex items-start space-x-3">
            <div className="flex-shrink-0 w-2 h-2 bg-green-400 rounded-full mt-2"></div>
            <div>
              <h4 className="font-medium text-gray-800">Copy to Clipboard</h4>
              <p className="text-sm text-gray-600">Click the copy button to copy code snippets</p>
            </div>
          </div>
          <div className="flex items-start space-x-3">
            <div className="flex-shrink-0 w-2 h-2 bg-green-400 rounded-full mt-2"></div>
            <div>
              <h4 className="font-medium text-gray-800">Line Numbers</h4>
              <p className="text-sm text-gray-600">Optional line numbering for better code reference</p>
            </div>
          </div>
          <div className="flex items-start space-x-3">
            <div className="flex-shrink-0 w-2 h-2 bg-green-400 rounded-full mt-2"></div>
            <div>
              <h4 className="font-medium text-gray-800">Responsive Design</h4>
              <p className="text-sm text-gray-600">Optimized for both desktop and mobile viewing</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default CodeDemo;
