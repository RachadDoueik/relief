package com.app.relief.service;

import com.app.relief.dto.UserDto;
import com.app.relief.entity.User;
import com.app.relief.mapper.UserMapper;
import com.app.relief.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

       private final UserRepository userRepository;
       private final UserMapper userMapper;

       public UserService(UserRepository userRepository , UserMapper userMapper) {
              this.userRepository = userRepository;
              this.userMapper = userMapper;
       }

       //get all users publicly
       public List<UserDto> getAllUsersPublic() {
           return userRepository.findAll().stream().map(userMapper::userToUserDto).collect(Collectors.toList());
       }

       //get all users
       public List<User> getAllUsers() {
              return userRepository.findAll();
       }
       //get user by id
       public User getUserById(Long id) {
           return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found!"));
       }

       //get user by username
       public User getUserByUsername(String username) {
           return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User with username " + username + " not found!"));
       }

       //update user
       public boolean updateUser(Long id, UserDto userDto) {
            Optional<User> optionalUser = userRepository.findById(id);
            if(optionalUser.isPresent()){
                Optional<User> userWithSameUsername = userRepository.findByUsername(userDto.getUsername());
                if(userWithSameUsername.isPresent() && !userWithSameUsername.get().getId().equals(id)){
                    // Username already taken by another user
                    return false;
                }
                Optional<User> userWithSameEmail = userRepository.findByEmail(userDto.getEmail());
                if(userWithSameEmail.isPresent() && !userWithSameEmail.get().getId().equals(id)){
                    // Email already taken by another user
                    return false;
                }
                User userToUpdate = optionalUser.get();
                userToUpdate.setUsername(userDto.getUsername());
                userToUpdate.setEmail(userDto.getEmail());
                userToUpdate.setUserRole(userDto.getUserRole());
                userRepository.save(userToUpdate);
                return true;
            }
            return false;
       }

       //delete user
       public boolean deleteUser(Long id) {
              return userRepository.findById(id).map(user -> {
                user.setDeleted(true);
                return true;
              }).orElse(false);
       }
}
