package com.todoapp.todolist.service;

import com.todoapp.todolist.model.Task;
import com.todoapp.todolist.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
        System.out.println("Marking task completed for id: " + id);
        return taskRepository.findById(id).map(task -> {
            if (task.isCompleted()) {
                System.out.println("Task already completed");
                return task; // ✅ Just return it again
            }
            task.setCompleted(true);
            task.setCompletionDate(LocalDate.now());
            System.out.println("Task marked complete");
            return taskRepository.save(task);
        }).orElse(null);

    }

    public List<Task> getTasksByUser(String username) {
        return taskRepository.findByAssignedTo(username);
    }


    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

}
