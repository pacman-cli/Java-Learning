package com.pacman.minitodoapp.repository;

import com.pacman.minitodoapp.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task,Long> {
}
