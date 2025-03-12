package com.motorny.mappers;

import com.motorny.dto.UserDto;
import com.motorny.dto.user.UserAuthDto;
import com.motorny.dto.user.UserCreateDto;
import com.motorny.dto.user.UserResetPasswordDto;
import com.motorny.dto.user.UserUpdatePasswordDto;
import com.motorny.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto toUserDto(User user);
    User toUser(UserDto userDto);

    UserCreateDto toUserCreateDto(User user);
    User toUser(UserCreateDto userCreateDto);

    @Mapping(source = "password", target = "oldPassword")
    @Mapping(source = "password", target = "newPassword")
    UserUpdatePasswordDto toUserUpdatePasswordDto(User user);

    @Mapping(source = "password", target = "newPassword")
    UserResetPasswordDto toUserResetPasswordDto(User user);

    UserAuthDto toUserAuthDto(User user);
}
