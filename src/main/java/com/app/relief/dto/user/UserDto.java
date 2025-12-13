package com.app.relief.dto.user;

import com.app.relief.enums.UserRole;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {

    @NotNull
    private String username;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @NotNull
    private UserRole userRole;

    @NotNull
    private boolean isDeleted;
}

