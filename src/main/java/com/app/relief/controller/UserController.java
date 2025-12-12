package com.app.relief.controller;

import com.app.relief.dto.UpdateEmailResponse;
import com.app.relief.dto.UpdateUserEmailRequest;
import com.app.relief.dto.UserDto;
import com.app.relief.entity.User;
import com.app.relief.mapper.UserMapper;
import com.app.relief.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
    }

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public List<UserDto> showAllUsers() {
        return userService.getAllUsersPublic();
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("isAuthenticated()")
    public User getUser(@PathVariable Long id, @AuthenticationPrincipal User user) {
        if (!user.getId().equals(id)) throw new AccessDeniedException("Not allowed");
        return userService.getUserById(id);
    }

    @GetMapping("/profile/{userId}")
    @PreAuthorize("isAuthenticated()")
    public UserDto getUserProfile(@PathVariable Long userId , @AuthenticationPrincipal User user){
        if (!user.getId().equals(userId)) throw new AccessDeniedException("Not allowed");
        return userService.getUserProfileById(userId);
    }

    @PutMapping("/profile/updateEmail/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UpdateEmailResponse> updateUserEmail(@RequestBody UpdateUserEmailRequest request , @PathVariable Long id , @AuthenticationPrincipal User user){
        if(!user.getId().equals(id)) throw new AccessDeniedException("Not Allowed");
        try {
            UpdateEmailResponse response = userService.updateUserEmail(request , id);
            return ResponseEntity.ok(response);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new UpdateEmailResponse(e.getMessage()));
        }
    }
}
