package com.todoapp.todolist.controller;

import com.todoapp.todolist.entity.UserEntity;
import com.todoapp.todolist.model.Task;
import com.todoapp.todolist.repository.TaskRepository;
import com.todoapp.todolist.repository.UserRepository;
import com.todoapp.todolist.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Task createTask(@RequestBody Task task) {
        return taskService.createTask(task);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> markTaskAsCompleted(@PathVariable Long id) {
        Task updatedTask = taskService.markTaskAsCompleted(id);
        if (updatedTask == null) {
            System.out.println("Task not found or already completed!");
            return ResponseEntity.status(404).build(); // Return 404 not 500
        }
        return ResponseEntity.ok(updatedTask);
    }

    @GetMapping("/my")
    public List<Task> getMyTasks(Authentication authentication) {
        String username = authentication.getName();
        return taskService.getTasksByUser(username);
    }

    @GetMapping("/my-tasks")
    public String getUserTasks(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("username", username);
        model.addAttribute("tasks", taskService.getTasksByUser(username));
        return "user-tasks"; // Thymeleaf template
    }

    @PostMapping("/api/tasks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addTask(@RequestBody Map<String, String> request) {
        String title = request.get("title");
        String dueDate = request.get("dueDate");
        String username = request.get("username");

        UserEntity user = userRepository.findByUsername(username);
        if (user == null) return ResponseEntity.badRequest().body("User not found");

        Task task = new Task();
        task.setTitle(title);
        task.setDueDate(LocalDate.parse(dueDate));
        task.setCompleted(false);
        task.setUser(user);

        return ResponseEntity.ok(taskRepository.save(task));
    }
}
