package com.fynaloo.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fynaloo.Dto.LoginRequest;
import com.fynaloo.Dto.RegistrationRequest;
import com.fynaloo.Dto.UserDetailsDTO;
import com.fynaloo.Security.SecurityConfigTest;
import com.fynaloo.Service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
@Import(SecurityConfigTest.class)


@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IUserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegistrationRequest registrationRequest;
    private LoginRequest loginRequest;
    private UserDetailsDTO userDetailsDTO;

    @BeforeEach
    void setUp() {
        registrationRequest = new RegistrationRequest();
        registrationRequest.setUsername("testuser");
        registrationRequest.setPassword("password123");
        registrationRequest.setEmail("test@example.com");
        registrationRequest.setFirstName("Test");
        registrationRequest.setLastName("User");

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        userDetailsDTO = new UserDetailsDTO();
        userDetailsDTO.setId(1L);
        userDetailsDTO.setUsername("testuser");
        userDetailsDTO.setEmail("test@example.com");
        userDetailsDTO.setFirstName("Test");
        userDetailsDTO.setLastName("User");
    }

    @Test
    void should_register_user_successfully() throws Exception {
        // given - mocked userService.registerUser

        // when - then
        mockMvc.perform(post("/api/users/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string("User register Successfully"));

        Mockito.verify(userService).registerUser(any(RegistrationRequest.class));
    }

    @Test
    void should_login_user_successfully() throws Exception {
        // given
        Mockito.when(userService.login(any(LoginRequest.class), any(HttpServletRequest.class)))
                .thenReturn(userDetailsDTO);

        // when - then
        mockMvc.perform(post("/api/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        Mockito.verify(userService).login(any(LoginRequest.class), any(HttpServletRequest.class));
    }

    @Test
    void should_get_current_user() throws Exception {
        // given
        Mockito.when(userService.getCurrentUserDTO()).thenReturn(userDetailsDTO);

        // when - then
        mockMvc.perform(get("/api/users/me")
                        .with(user("testuser").password("password123").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        Mockito.verify(userService).getCurrentUserDTO();
    }
}
