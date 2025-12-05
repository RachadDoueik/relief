package com.app.relief.service;

import com.app.relief.dto.UserDto;
import com.app.relief.entity.User;
import com.app.relief.enums.UserRole;
import com.app.relief.mapper.UserMapper;
import com.app.relief.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public AuthenticationService(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder,
                                 AuthenticationManager authenticationManager,
                                 UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.userMapper = userMapper;
    }

    //signup method
    public User signup(UserDto input) {

        Optional<User> existingUserByUsername = userRepository.findByUsername(input.getUsername());
        Optional<User> existingUserByEmail = userRepository.findByEmail(input.getEmail());

        if (existingUserByUsername.isPresent()) {
            throw new RuntimeException("Username already taken.");
        }

        if (existingUserByEmail.isPresent()) {
            throw new RuntimeException("Email already registered.");
        }

        User user = userMapper.userDtoToUser(input);
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        UserRole userRole = UserRole.valueOf(input.getUserRole().toString());
        user.setUserRole(userRole);

        return userRepository.save(user);
    }

    //login method
    public User authenticate(UserDto input) {
        // This line attempts the authentication process
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );

        // If authenticate() succeeds, the user exists and the password is correct.
        return userRepository.findByUsername(input.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }


    //logout method
    public void logout() {
        // Invalidate the JWT on the client side by removing it from storage (e.g., localStorage, cookies).
        // Server-side JWT invalidation typically requires a token blacklist or short expiration times.

    }
}