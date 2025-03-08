package com.motorny.mappers;

import com.motorny.dto.UserDto;
import com.motorny.models.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);

    User toUser(UserDto userDto);
}
