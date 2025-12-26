package com.pacman.minitodoapp.controller;

import com.pacman.minitodoapp.model.Task;
import com.pacman.minitodoapp.repository.TaskRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    public TaskRepository taskRepository;
    public TaskController(TaskRepository taskRepository){
        this.taskRepository=taskRepository;
    }
    @GetMapping
    public List<Task> getTasks(){
        return taskRepository.findAll();
    }
    @PostMapping
    public Task createTask(@RequestBody Task task){
        return taskRepository.save(task);
    }
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){ //`@PathVariable`: Extracts `id` from the URL path.
        taskRepository.deleteById(id);
    }
}
