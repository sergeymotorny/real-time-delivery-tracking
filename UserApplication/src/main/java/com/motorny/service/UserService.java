package com.motorny.service;

import com.motorny.dto.UserDto;
import com.motorny.dto.admin.AdminUserDto;
import com.motorny.dto.user.UserCreateDto;
import com.motorny.dto.user.UserUpdatePasswordDto;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<AdminUserDto> getAllUsers();
    Optional<UserDto> getUserByEmail(String email);
    UserDto getUserById(Long id);
    UserDto getUserProfile(UserDetails userDetails);
    List<AdminUserDto> getUsersByRole(String roleName);
    UserDto updateUser(Long id, UserDto userDto);
    void createUser(UserCreateDto userCreateDto);
    void changeUserPassword(UserUpdatePasswordDto userUpdatePassword, String password);
    boolean checkIfValidOldPassword(UserCreateDto userCreateDto, String oldPassword);
    AdminUserDto setUserStatusDeleted(Long id);
    AdminUserDto setUserStatusActive(Long id);
}
