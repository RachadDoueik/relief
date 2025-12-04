package com.app.relief.mapper;

import com.app.relief.dto.UserDto;
import com.app.relief.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring") // Makes this a Spring Bean so it can @Autowired
public interface UserMapper {

    //field names match exactly so no annotation needed.
    UserDto userToUserDto(User user);

    //explicitly ignore fields for security (like password)
    @Mapping(target = "password", ignore = true)
    User userDtoToUser(UserDto userDto);
}
