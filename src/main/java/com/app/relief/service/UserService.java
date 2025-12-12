package com.app.relief.service;

import com.app.relief.dto.UpdateEmailResponse;
import com.app.relief.dto.UpdateUserEmailRequest;
import com.app.relief.dto.UserDto;
import com.app.relief.entity.User;
import com.app.relief.mapper.UserMapper;
import com.app.relief.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {

       private final UserRepository userRepository;
       private final UserMapper userMapper;
       private final PasswordEncoder passwordEncoder;

       public UserService(UserRepository userRepository , UserMapper userMapper , PasswordEncoder passwordEncoder) {
              this.userRepository = userRepository;
              this.userMapper = userMapper;
              this.passwordEncoder = passwordEncoder;
       }

       //get all users publicly
       public List<UserDto> getAllUsersPublic() {
           return userRepository.findAll().stream().map(userMapper::userToUserDto).collect(Collectors.toList());
       }

       //get all users (full details) (admin only)
       public List<User> getAllUsers() {
              return userRepository.findAll();
       }

       //get user by id
       public User getUserById(Long userId){
            return userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with id " + userId + "not found!"));
       }

       //get userProfile by id
       public UserDto getUserProfileById(Long id) {
           User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with id " + id + " not found!"));
           UserDto userDto = userMapper.userToUserDto(user);
           userDto.setPassword(null);
           return userDto;
       }

       //update a user's email
       public UpdateEmailResponse updateUserEmail(UpdateUserEmailRequest updateUserRequest , Long id){

           User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User with userId" + id + " not found!"));

           Optional<User> userByEmail = userRepository.findByEmail(updateUserRequest.getEmail());
           if(userByEmail.isPresent()) throw new RuntimeException("user with same email already exists !");

           user.setEmail(updateUserRequest.getEmail());
           userRepository.save(user);

           return new UpdateEmailResponse("email updated successfully !");
       }

       //delete user
       public boolean deleteUser(Long id) {
              return userRepository.findById(id).map(user -> {
                user.setDeleted(true);
                return true;
              }).orElse(false);
       }
}
