package com.app.relief.dto;

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
}
