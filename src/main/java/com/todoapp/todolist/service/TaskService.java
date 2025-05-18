package com.todoapp.todolist.service;

import com.todoapp.todolist.model.Task;
import com.todoapp.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Task markTaskAsCompleted(Long id) {
        return taskRepository.findById(id).map(task -> {
            if (task.isCompleted()) return null;
            task.setCompleted(true);
            task.setCompletionDate(LocalDate.now());
            return taskRepository.save(task);
        }).orElse(null);
    }
}
