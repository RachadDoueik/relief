package com.app.relief.controller;

import com.app.relief.dto.UserDto;
import com.app.relief.entity.User;
import com.app.relief.mapper.UserMapper;
import com.app.relief.service.UserService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {


    private final UserService userService;

    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/all")
    public List<UserDto> showAllUsers() {
        return userService.getAllUsersPublic();
    }

    @GetMapping("/user/{id}")
    public User getUser(@PathVariable Long id, @AuthenticationPrincipal User user) {
        if (!user.getId().equals(id)) {
            throw new AccessDeniedException("Not allowed");
        }
        return userService.getUserById(id);
    }
}
