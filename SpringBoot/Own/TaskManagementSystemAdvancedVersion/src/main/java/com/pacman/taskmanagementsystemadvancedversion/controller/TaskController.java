package com.pacman.taskmanagementsystemadvancedversion.controller;

import com.pacman.taskmanagementsystemadvancedversion.dto.TaskDTO;
import com.pacman.taskmanagementsystemadvancedversion.service.TaskService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    public final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.getAllTasks();
    }
    @PostMapping
    public TaskDTO getTaskById(@RequestBody @Validated TaskDTO taskDTO){
        return taskService.createTask(taskDTO);
    }
    @DeleteMapping
    public void deleteTaskById(@RequestBody @Validated TaskDTO taskDTO){
        taskService.deleteTask(taskDTO.getId());
    }
}
