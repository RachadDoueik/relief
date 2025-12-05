package com.app.relief.controller;


import com.app.relief.dto.UserDto;
import com.app.relief.mapper.UserMapper;
import com.app.relief.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
