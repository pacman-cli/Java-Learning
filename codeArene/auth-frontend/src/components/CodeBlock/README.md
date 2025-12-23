# CodeBlock Component

A React component for displaying syntax-highlighted code blocks with Java-specific color customization.

## Features

- **Java Syntax Highlighting**: Custom color scheme optimized for Java code
- **Multiple Language Support**: Java, JavaScript, TypeScript, Python, SQL, JSON, CSS, HTML
- **Copy to Clipboard**: One-click code copying functionality
- **Line Numbers**: Optional line numbering for better code reference
- **Responsive Design**: Mobile-friendly with custom scrollbars
- **Custom Styling**: Dark theme with customizable colors
- **Accessibility**: Keyboard navigation and screen reader support

## Installation

The component uses PrismJS for syntax highlighting. Make sure you have the required dependencies:

```bash
npm install prismjs @types/prismjs
```

## Usage

### Basic Usage

```tsx
import CodeBlock from '../components/CodeBlock';

const MyComponent = () => {
  const javaCode = `
public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
    }
}`;

  return (
    <CodeBlock 
      code={javaCode}
      language="java"
    />
  );
};
```

### Advanced Usage

```tsx
import CodeBlock from '../components/CodeBlock';

const AdvancedExample = () => {
  const springBootCode = `
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
}`;

  return (
    <CodeBlock 
      code={springBootCode}
      language="java"
      title="UserController.java"
      showLineNumbers={true}
      className="my-custom-class"
    />
  );
};
```

## Props

| Prop | Type | Default | Description |
|------|------|---------|-------------|
| `code` | `string` | **required** | The code content to display |
| `language` | `string` | `"java"` | Programming language for syntax highlighting |
| `className` | `string` | `""` | Additional CSS classes |
| `showLineNumbers` | `boolean` | `false` | Whether to show line numbers |
| `title` | `string` | `undefined` | Optional title/filename header |

## Supported Languages

- `java` - Java (default)
- `javascript` / `js` - JavaScript
- `typescript` / `ts` - TypeScript
- `python` / `py` - Python
- `sql` - SQL
- `json` - JSON
- `html` / `markup` - HTML
- `css` - CSS

## Java Syntax Color Scheme

The component uses a custom color scheme optimized for Java code:

| Element | Color | Description |
|---------|-------|-------------|
| Keywords | `#ff7875` (Red) | `public`, `private`, `class`, `if`, `else`, etc. |
| Class Names | `#73d0ff` (Light Blue) | Class names and types |
| Method Names | `#bae67e` (Green) | Function/method names |
| Strings | `#d5ff80` (Light Green) | String literals and characters |
| Numbers | `#ffb454` (Orange) | Numeric literals |
| Booleans | `#ff7875` (Red) | `true`, `false`, `null` |
| Operators | `#f29e74` (Light Orange) | `+`, `-`, `=`, `&&`, etc. |
| Comments | `#5c6773` (Gray) | Single and multi-line comments |
| Annotations | `#ffd580` (Yellow) | `@Override`, `@Autowired`, etc. |
| Built-ins | `#5ccfe6` (Cyan) | Built-in types and methods |

## Customization

### Custom CSS

You can override the default styles by targeting the CSS classes:

```css
.code-block-container pre[class*="language-"] {
  background: #1a1a1a !important;
  border-radius: 8px;
}

.language-java .token.keyword {
  color: #ff6b6b !important;
  font-weight: bold;
}
```

### Custom Theme

To create your own theme, modify the CSS variables in `CodeBlock.css`:

```css
:root {
  --code-bg-color: #2d3748;
  --code-text-color: #e2e8f0;
  --code-keyword-color: #ff7875;
  --code-string-color: #d5ff80;
  /* ... other variables */
}
```

## Examples

### Spring Boot Service Class

```tsx
const serviceCode = `
@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public UserService(UserRepository userRepository, 
                      PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public User createUser(CreateUserRequest request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userRepository.save(user);
    }
}`;

<CodeBlock 
  code={serviceCode}
  language="java"
  title="UserService.java"
  showLineNumbers={true}
/>
```

### Exception Handling

```tsx
const exceptionCode = `
try {
    User user = userService.findById(userId);
    return ResponseEntity.ok(user);
} catch (UserNotFoundException e) {
    logger.error("User not found: " + userId, e);
    return ResponseEntity.notFound().build();
} catch (Exception e) {
    logger.error("Unexpected error", e);
    return ResponseEntity.internalServerError().build();
}`;

<CodeBlock 
  code={exceptionCode}
  language="java"
  title="Exception Handling Example"
/>
```

## Browser Support

- Chrome 60+
- Firefox 55+
- Safari 12+
- Edge 79+

## Accessibility

The component includes:
- Keyboard navigation support
- Focus indicators
- Screen reader compatibility
- High contrast mode support
- Reduced motion support

## Performance

- Code highlighting happens after component mount
- Large code blocks are handled efficiently
- Minimal bundle size impact with tree shaking

## Contributing

To add support for new languages:

1. Import the language component from PrismJS
2. Add the language to the supported languages list
3. Create custom color rules if needed
4. Update documentation

```tsx
import 'prismjs/components/prism-rust';
```

## License

This component is part of the Auth Service frontend and follows the same license terms.