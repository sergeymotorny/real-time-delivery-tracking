package com.motorny.controllers;

import com.motorny.dto.UserDto;
import com.motorny.mappers.UserMapper;
import com.motorny.models.User;
import com.motorny.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(UserController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    User user;

    @BeforeEach
    public void setup() {
        user = User.builder()
                .firstName("Nika")
                .lastName("Ronald")
                .phone("+380953332030")
                .email("n_ronald@gmail.com")
                .password(passwordEncoder.encode("nika123"))
                .build();
    }

    @Test
    void getAllOrdersByUser() {
    }

    @Test
    void registerUser_2() {


        Mockito.when(userService.getUserById(1L)).thenReturn(null);
//        mockMvc.perform(MockMvcRequestBuilders.get());
    }
}