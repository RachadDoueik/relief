package com.app.relief.controller;


import com.app.relief.dto.UserDto;
import com.app.relief.entity.User;
import com.app.relief.mapper.UserMapper;
import com.app.relief.service.AuthenticationService;
import com.app.relief.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    public AuthenticationController(AuthenticationService authenticationService, JwtService jwtService, UserMapper userMapper) {
        this.authenticationService = authenticationService;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> register(@RequestBody UserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "User registered successfully");
        response.put("user", userMapper.userToUserDto(registeredUser));
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> authenticate(@RequestBody UserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        // Generate the JWT upon successful authentication
        String jwtToken = jwtService.generateToken(authenticatedUser);

        Map<String, String> response = new HashMap<>();
        response.put("token", jwtToken);
        response.put("expiresIn", String.valueOf(jwtService.getExpirationTime()));

        return ResponseEntity.ok(response);
    }
}
