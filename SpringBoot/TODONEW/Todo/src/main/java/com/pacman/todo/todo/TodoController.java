package com.pacman.todo.todo;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//@RestController
//@RequestMapping("/api/todos")
//public class TodoController {
//    private final TodoService todoService;
//
//    public TodoController(TodoService todoService) {
//        this.todoService = todoService;
//    }
//
//    @GetMapping
//    public List<Todo>  getAllTodos(){
//        return todoService.getAllTodos();
//    }
//
//    @GetMapping("/{id}")
//    public Todo getTodoById(@PathVariable Long id){
//        return todoService.getTodoById(id);
//    }
//
//    @PostMapping("/user/{userId}")
//    public Todo createTodo(@PathVariable Long userId,@RequestBody Todo todo){
//        return todoService.createTodo(userId,todo);
//    }
//
//    @PutMapping("/{id}")
//    public Todo updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails) {
//        return todoService.updateTodo(id, todoDetails);
//    }
//
//    @DeleteMapping("/{id}")
//    public void deleteTodo(@PathVariable Long id) {
//        todoService.deleteTodo(id);
//    }
//}

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping
    public ResponseEntity<?> getAllTodos(HttpServletRequest request) {
        System.out.println("GET TODOS REQUEST RECEIVED");
        try {
            String username = (String) request.getAttribute("username");
            System.out.println("Username from request: " + username);
            if (username == null) {
                System.out.println("Username is null, returning unauthorized");
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            List<Todo> todos = todoService.getAllTodosByUsername(username);
            System.out.println("Retrieved " + todos.size() + " todos");
            return ResponseEntity.ok(todos);
        } catch (Exception e) {
            System.out.println("Error fetching todos: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch todos: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable Long id, HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");
            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            Todo todo = todoService.getTodoById(id, username);
            if (todo == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Todo not found"));
            }
            return ResponseEntity.ok(todo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to fetch todo: " + e.getMessage()));
        }
    }

    @PostMapping
    public ResponseEntity<?> createTodo(@RequestBody Todo todo, HttpServletRequest request) {
        System.out.println("CREATE TODO REQUEST RECEIVED");
        System.out.println("Request body: " + todo);
        try {
            String username = (String) request.getAttribute("username");
            System.out.println("Username from request: " + username);
            if (username == null) {
                System.out.println("Username is null, returning unauthorized");
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            Todo createdTodo = todoService.createTodo(username, todo);
            System.out.println("Created todo successfully: " + createdTodo.getId());
            return ResponseEntity.ok(createdTodo);
        } catch (Exception e) {
            System.out.println("Error creating todo: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body(Map.of("error", "Failed to create todo: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodo(@PathVariable Long id, @RequestBody Todo todoDetails,
            HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");
            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            Todo updatedTodo = todoService.updateTodo(id, todoDetails, username);
            if (updatedTodo == null) {
                return ResponseEntity.status(404).body(Map.of("error", "Todo not found"));
            }
            return ResponseEntity.ok(updatedTodo);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to update todo: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodo(@PathVariable Long id, HttpServletRequest request) {
        try {
            String username = (String) request.getAttribute("username");
            if (username == null) {
                return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
            }

            boolean deleted = todoService.deleteTodo(id, username);
            if (!deleted) {
                return ResponseEntity.status(404).body(Map.of("error", "Todo not found"));
            }
            return ResponseEntity.ok(Map.of("message", "Todo deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to delete todo: " + e.getMessage()));
        }
    }
}
