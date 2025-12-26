package com.pacman.taskmanagementsystemadvancedversion.repository;

import com.pacman.taskmanagementsystemadvancedversion.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
}
