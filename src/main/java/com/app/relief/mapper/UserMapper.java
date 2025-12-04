package com.app.relief.mapper;

import com.app.relief.dto.UserDto;
import com.app.relief.entity.User;
import org.springframework.stereotype.Component;

@Component // Register this class as a Spring Bean so it can be @Autowired
public class UserMapper {

    /**
     * Converts a User entity to a UserDto.
     */
    public UserDto userToUserDto(User user) {
        if (user == null) {
            return null;
        }

        UserDto userDto = new UserDto();
        userDto.setUsername(user.getUsername());
        userDto.setEmail(user.getEmail());
        // Map any other necessary fields...

        return userDto;
    }

    /**
     * Converts a UserDto to a User entity, ignoring sensitive fields like password.
     */
    public User userDtoToUser(UserDto userDto) {
        if (userDto == null) {
            return null;
        }

        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());

        return user;
    }
}