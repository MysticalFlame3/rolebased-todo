package com.todoapp.todolist.controller;

import com.todoapp.todolist.service.TaskService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final TaskService taskService;

    // Constructor injection of TaskService
    public DashboardController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("username", username);

        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                return "admin-dashboard"; // renders admin-dashboard.html
            }
        }
        return "user-dashboard"; // renders user-dashboard.html
    }

    @GetMapping("/my-tasks")
    public String userTasks(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("username", username);
        model.addAttribute("tasks", taskService.getTasksByUser(username));
        return "user-tasks";
    }
}

