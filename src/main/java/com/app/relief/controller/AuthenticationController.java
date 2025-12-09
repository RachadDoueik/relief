package com.app.relief.controller;


import com.app.relief.dto.*;
import com.app.relief.mapper.UserMapper;
import com.app.relief.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody UserDto request) {
        try {
            AuthResponse response = authenticationService.signup(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null , null, 0, e.getMessage()));
        }
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginUserDto) {
        try {
            AuthResponse response = authenticationService.login(loginUserDto);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null , null, 0, e.getMessage()));
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestHeader("Authorization") String authHeader , @RequestBody RefreshRequest refreshRequest) {
        try {
            AuthResponse response = authenticationService.refresh(refreshRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new AuthResponse(null , null, 0, e.getMessage()));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponse> logout(@RequestHeader("Authorization") String authHeader , @RequestBody LogoutRequest logoutRequest) {
        try {
            LogoutResponse logoutResponse = authenticationService.logout(logoutRequest);
            return ResponseEntity.ok(logoutResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new LogoutResponse(logoutRequest.getRefreshToken(), e.getMessage()));
        }
    }
}
