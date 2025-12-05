package com.app.relief.controller;

import com.app.relief.entity.User;
import com.app.relief.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/all")
    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }
}
