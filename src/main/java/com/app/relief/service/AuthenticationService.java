package com.app.relief.service;

import com.app.relief.dto.UserDto;
import com.app.relief.entity.User;
import com.app.relief.mapper.UserMapper;
import com.app.relief.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
        User user = userMapper.userDtoToUser(input);
        user.setPassword(passwordEncoder.encode(input.getPassword()));
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
                .orElseThrow(() -> new UsernameNotFoundException("User not found after successful authentication."));
    }
}