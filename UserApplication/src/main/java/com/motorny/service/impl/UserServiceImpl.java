package com.motorny.service.impl;

import com.motorny.dto.UserDto;
import com.motorny.dto.admin.AdminUserDto;
import com.motorny.dto.user.UserCreateDto;
import com.motorny.dto.user.UserUpdatePasswordDto;
import com.motorny.exceptions.RoleNotFoundException;
import com.motorny.exceptions.UserAlreadyExistsException;
import com.motorny.mappers.UserMapper;
import com.motorny.models.Role;
import com.motorny.models.User;
import com.motorny.models.enums.Status;
import com.motorny.repositories.RoleRepository;
import com.motorny.repositories.UserRepository;
import com.motorny.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public List<AdminUserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toAdminUserDto)
                .toList();
    }

    @Override
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(userMapper::toUserDto);
    }

    @Override
    public UserDto getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toUserDto)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id '" + id + "'"));
    }

    @Override
    public UserDto getUserProfile(UserDetails userDetails) {
        String userByEmail = userDetails.getUsername();
        User foundUser = userRepository.findByEmail(userByEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email '" + userByEmail + "'"));
        return userMapper.toUserDto(foundUser);
    }

    @Override
    public List<AdminUserDto> getUsersByRole(String roleName) {
        List<User> users = userRepository.findByRolesName(roleName);
        return users.stream()
                .map(userMapper::toAdminUserDto)
                .toList();
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id '" + id + "'"));

        foundUser.setFirstName(userDto.getFirstName());
        foundUser.setLastName(userDto.getLastName());
        foundUser.setPhone(userDto.getPhone());
        foundUser.setEmail(userDto.getEmail());
        User updatedUser = userRepository.save(foundUser);

        return userMapper.toUserDto(updatedUser);
    }

    @Override
    public void createUser(UserCreateDto userCreateDto) {
        if (emailExists(userCreateDto.getEmail())) {
            throw new UserAlreadyExistsException("An account with this email address already exists: " + userCreateDto.getEmail());
        }

        Role clientRole = roleRepository.findByName("CLIENT")
                        .orElseThrow(() -> new RoleNotFoundException("Default role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(clientRole);

        User saveUser = userMapper.toUserCreate(userCreateDto);
        saveUser.setRoles(roles);
        saveUser.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        saveUser.setStatus(Status.ACTIVE);

        userRepository.save(saveUser);
    }

    @Override
    public void changeUserPassword(UserUpdatePasswordDto userUpdatePassword, String password) {
        userUpdatePassword.setNewPassword(passwordEncoder.encode(password));

//        userRepository.save(userUpdatePassword); // it doesn't work
    }

    @Override
    public boolean checkIfValidOldPassword(UserCreateDto userCreate, String oldPassword) {
        return passwordEncoder.matches(oldPassword, userCreate.getPassword());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public AdminUserDto setUserStatusDeleted(Long id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id '" + id + "'"));
        foundUser.setStatus(Status.DELETED);
        userRepository.save(foundUser);
        return userMapper.toAdminUserDto(foundUser);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public AdminUserDto setUserStatusActive(Long id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id '" + id + "'"));
        foundUser.setStatus(Status.ACTIVE);
        userRepository.save(foundUser);
        return userMapper.toAdminUserDto(foundUser);
    }

    private boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }


}
