package com.pacman.taskmanagementsystemadvancedversion.service;

import com.pacman.taskmanagementsystemadvancedversion.dto.TaskDTO;
import com.pacman.taskmanagementsystemadvancedversion.model.Task;
import com.pacman.taskmanagementsystemadvancedversion.repository.TaskRepository;
import com.pacman.taskmanagementsystemadvancedversion.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepo;
    private final UserRepository userRepo;

    public TaskService(TaskRepository taskRepo, UserRepository userRepo) {
        this.taskRepo = taskRepo;
        this.userRepo = userRepo;
    }

    public List<TaskDTO> getAllTasks() {
        return taskRepo.findAll().stream().map(this::mapToDTO).toList();
    }

    public TaskDTO createTask(TaskDTO dto) {
        Task task = new Task();
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setStatus(dto.getStatus());
        task.setPriority(dto.getPriority());
        task.setUser(userRepo.findById(dto.getUserId()).orElseThrow());

        return mapToDTO(taskRepo.save(task));
    }

    public void deleteTask(Long id) {
        taskRepo.deleteById(id);
    }

    private TaskDTO mapToDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setUserId(task.getUser().getId());
        return dto;
    }
}
