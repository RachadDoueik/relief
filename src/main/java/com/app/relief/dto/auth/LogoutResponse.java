package com.app.relief.dto.auth;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LogoutResponse {

     private String revokedToken;

     private String message;
}
