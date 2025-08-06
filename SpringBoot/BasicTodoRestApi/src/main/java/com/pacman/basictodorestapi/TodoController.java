package com.pacman.basictodorestapi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
  private static List<Todo> todoList;

  //@Autowired //--> this will automatically inject
  //@Qualifier("fakeTodoService")//--> instead of doing here we will do it in constructor param
  private TodoService todoService; //composition
  private TodoService todoService2;

  TodoController(@Qualifier("fakeTodoService") TodoService todoService,
                 @Qualifier("anotherTodoService") TodoService todoService2) {
    this.todoService = todoService;
    this.todoService2 = todoService2;
    todoList = new ArrayList<>();
    todoList.add(new Todo(1, false, "Todo 1", 1));
    todoList.add(new Todo(2, true, "Todo 2", 2));
  }

  @GetMapping
  public ResponseEntity<List<Todo>>
  getTodos(@RequestParam(required = false,defaultValue = "true") boolean isCompleted) {
    // return ResponseEntity.status(HttpStatus.OK).body(todoList); //this ok
    // will return status ok only
    System.out.println("Incoming query params: " + isCompleted +" "+ this.todoService.doSomething());
    System.out.println(this.todoService2.doSomething());
    return ResponseEntity.ok(todoList); // we can use any of them
  }

  // @PostMapping("/todos")
  // public Todo createTodos(@RequestBody Todo newTodo){
  // /*
  // * @ResponseStatus(HttpStatus.CREATED)-->we can use this annotation for
  // status code*/ todoList.add(newTodo); return newTodo;
  // }
  @PostMapping
  public ResponseEntity<Todo> createTodos(
      @RequestBody Todo newTodo) { // other way to using ResponseEntity class
    // that helps us to manage the whole response
    // object which we are sending back to the
    // client manually
    todoList.add(newTodo);
    return ResponseEntity.status(HttpStatus.CREATED).body(newTodo);
  }

  @GetMapping("/todos/{todoId}")
  public ResponseEntity<?> getTodoById(@PathVariable Long todoId) {
    // public ResponseEntity<Todo> getTodoById(@PathVariable Long todoId){
    for (Todo todo : todoList) {
      if (todo.getId() == todoId) {
        return ResponseEntity.ok(todo);
      }
    }
    ErrorResponse error = new ErrorResponse(
        "Todo with ID " + todoId + " not found", HttpStatus.NOT_FOUND.value());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }
}
