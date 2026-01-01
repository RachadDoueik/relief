package com.app.relief.dto.auth;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogoutRequest {

    @NotNull
    public String refreshToken;
}
