//package com.pacman.todo.todo;
//
//import com.pacman.todo.user.User;
//import com.pacman.todo.user.UserRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class TodoService {
//    private final TodoRepository todoRepository;
//    private final UserRepository userRepository;
//
//    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
//        this.todoRepository = todoRepository;
//        this.userRepository = userRepository;
//    }
//
//    public List<Todo> getAllTodos(){
//        return todoRepository.findAll();
//    }
//    public Todo getTodoById(Long id){
//        return todoRepository.findById(id).orElseThrow(()-> new RuntimeException("Todo not found"));
//    }
//    public Todo createTodo(Long id,Todo todo){
//        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found"));
//        todo.setUsers(user);
//        return todoRepository.save(todo);
//    }
//    public Todo updateTodo(long id,Todo todoDetails){
//        Todo todo = getTodoById(id);
//        todo.setTitle(todoDetails.getTitle());
//        todo.setDescription(todoDetails.getDescription());
//        todo.setCompleted(todoDetails.isCompleted());
//        todo.setDueDate(todoDetails.getDueDate());
//        return todoRepository.save(todo);
//    }
//    public void deleteTodo(Long id){
//        todoRepository.deleteById(id);
//    }
//}

package com.pacman.todo.todo;

import com.pacman.todo.user.User;
import com.pacman.todo.user.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoService {
    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    public TodoService(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    // Fetch the User by username from the JWT.
    //
    // Query the TodoRepository for todos belonging to that user.
    //
    // Similarly, create, update, and delete operations verify the user owns the
    // todo.
    //
    // ✅ This ensures users can only access their own todos.
    public List<Todo> getAllTodosByUsername(String username) {
        System.out.println("Getting todos for username: " + username);
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            System.out.println("User not found: " + username);
            throw new RuntimeException("User not found: " + username);
        }
        System.out.println("Found user: " + user.getUsername() + " with ID: " + user.getId());
        List<Todo> todos = todoRepository.findByUserOrderByCreatedAtDesc(user);
        System.out.println("Found " + todos.size() + " todos");
        return todos;
    }

    public Todo getTodoById(Long id, String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }

        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo != null && todo.getUser().getId().equals(user.getId())) {
            return todo;
        }
        return null;
    }

    public Todo createTodo(String username, Todo todo) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found: " + username);
        }

        // Validate input
        if (todo.getTitle() == null || todo.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Todo title cannot be null or empty");
        }

        todo.setUser(user);
        return todoRepository.save(todo);
    }

    public Todo updateTodo(Long id, Todo todoDetails, String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }

        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo == null || !todo.getUser().getId().equals(user.getId())) {
            return null;
        }

        // Update fields
        if (todoDetails.getTitle() != null && !todoDetails.getTitle().trim().isEmpty()) {
            todo.setTitle(todoDetails.getTitle());
        }
        if (todoDetails.getDescription() != null) {
            todo.setDescription(todoDetails.getDescription());
        }
        todo.setCompleted(todoDetails.isCompleted());
        if (todoDetails.getDueDate() != null) {
            todo.setDueDate(todoDetails.getDueDate());
        }

        return todoRepository.save(todo);
    }

    public boolean deleteTodo(Long id, String username) {
        User user = userRepository.findByUsername(username).orElse(null);
        if (user == null) {
            return false;
        }

        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo == null || !todo.getUser().getId().equals(user.getId())) {
            return false;
        }

        todoRepository.deleteById(id);
        return true;
    }
}