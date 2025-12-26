package com.pacman.taskmanagementsystemadvancedversion.dto;

import com.pacman.taskmanagementsystemadvancedversion.model.Priority;
import com.pacman.taskmanagementsystemadvancedversion.model.TaskStatus;
import lombok.Data;

@Data
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private Priority priority;
    private Long userId;

}
