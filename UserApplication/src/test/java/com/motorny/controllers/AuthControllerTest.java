package com.motorny.controllers;

import com.motorny.dto.UserDto;
import com.motorny.dto.user.UserCreateDto;
import com.motorny.models.User;
import com.motorny.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(AuthController.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Mock private PasswordEncoder passwordEncoder;

    UserCreateDto userCreateDto;
    UserDto userDto;
    User user;

    @BeforeEach
    public void setup() {
        userCreateDto = UserCreateDto.builder()
                .firstName("Nika")
                .lastName("Ronald")
                .phone("+380953332030")
                .email("n_ronald@gmail.com")
                .password("nika123")
                .build();

        //mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Test
    @WithMockUser(username = "n_ronald@gmail.com", roles = "CLIENT")
    void whenPostRegister_thenRedirectsToSuccessPage() throws Exception {
        BDDMockito.willDoNothing()
                .given(userService)
                .createUser(ArgumentMatchers.any(UserCreateDto.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())
                        .param("firstName", userCreateDto.getFirstName())
                        .param("lastName", userCreateDto.getLastName())
                        .param("phone", userCreateDto.getPhone())
                        .param("email", userCreateDto.getEmail())
                        .param("password", userCreateDto.getPassword()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    @Test
    void whenNotAuthenticated_thenReturns401() throws Exception {
        BDDMockito.willDoNothing()
                .given(userService)
                .createUser(ArgumentMatchers.any(UserCreateDto.class));

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .with(csrf())
                        .param("firstName", userCreateDto.getFirstName())
                        .param("lastName", userCreateDto.getLastName())
                        .param("phone", userCreateDto.getPhone())
                        .param("email", userCreateDto.getEmail())
                        .param("password", userCreateDto.getPassword()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnauthorized());
    }


}