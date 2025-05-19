package com.todoapp.todolist.repository;

import com.todoapp.todolist.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find all tasks assigned to a specific username
    List<Task> findByAssignedTo(String username);

}
