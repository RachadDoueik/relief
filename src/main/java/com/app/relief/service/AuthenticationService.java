package com.app.relief.service;

import com.app.relief.dto.auth.*;
import com.app.relief.dto.user.UserDto;
import com.app.relief.entity.RefreshToken;
import com.app.relief.entity.User;
import com.app.relief.mapper.UserMapper;
import com.app.relief.repository.RefreshTokenRepository;
import com.app.relief.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Setter
    @Getter
    @Value("${security.jwt.refresh-expiration}")
    private long refreshExpiration;


    public AuthResponse signup(UserDto request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username already taken");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = userMapper.userDtoToUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);

        String accessToken = jwtService.generateAccessToken(user);

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(refreshTokenExpirationDate());
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken, refreshToken.getToken(), jwtService.getAccessTokenExpiration(), "User registered successfully");
    }


    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found."));

        // generate access token
        String accessToken = jwtService.generateAccessToken(user);

        // generate new refresh token
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken(UUID.randomUUID().toString());
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(refreshTokenExpirationDate());
        refreshToken.setRevoked(false);
        refreshTokenRepository.save(refreshToken);

        return new AuthResponse(accessToken, refreshToken.getToken(), jwtService.getAccessTokenExpiration() , "User logged in successfully");
    }



    public AuthResponse refresh(RefreshRequest refreshRequest) {
        RefreshToken storedToken = refreshTokenRepository.findByToken(refreshRequest.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (storedToken.getExpiryDate().before(new java.util.Date())) {
            throw new RuntimeException("Refresh token expired");
        }

        if (storedToken.isRevoked()) {
            throw new RuntimeException("Refresh token has been revoked");
        }

        User user = storedToken.getUser();

        // generate new tokens
        String newAccessToken = jwtService.generateAccessToken(user);

        // rotate refresh tokens: delete old → add new
        refreshTokenRepository.delete(storedToken);

        RefreshToken newToken = new RefreshToken();
        newToken.setToken(UUID.randomUUID().toString());
        newToken.setUser(user);
        newToken.setExpiryDate(refreshTokenExpirationDate());
        refreshTokenRepository.save(newToken);

        return new AuthResponse(newAccessToken, newToken.getToken(), jwtService.getAccessTokenExpiration() , "Token refreshed successfully");
    }

    public LogoutResponse logout(LogoutRequest logoutRequest) {

        RefreshToken storedToken = refreshTokenRepository.findByToken(logoutRequest.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if(storedToken.isRevoked()) {
            throw new RuntimeException("Refresh token already revoked");
        }

        // revoke the refresh token
        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        return new LogoutResponse(storedToken.getToken() , "Logout successful");
    }

    public boolean isAccessTokenExpired(String accessToken) {
        return jwtService.isTokenExpired(accessToken);
    }

    public Date refreshTokenExpirationDate() {
        return new Date(System.currentTimeMillis() + refreshExpiration);
    }

}
