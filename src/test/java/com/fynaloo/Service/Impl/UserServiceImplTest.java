package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.LoginRequest;
import com.fynaloo.Dto.RegistrationRequest;
import com.fynaloo.Dto.UserDetailsDTO;
import com.fynaloo.Mapper.UserMapper;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Repository.UserRepository;
import com.fynaloo.Configuration.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_get_current_user_DTO() {
        User user = new User();
        user.setUsername("testuser");
        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(authenticationToken);
        SecurityContextHolder.setContext(context);

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        when(userMapper.toUserDetailsDTO(any(User.class))).thenReturn(userDetailsDTO);

        // when
        UserDetailsDTO result = userService.getCurrentUserDTO();

        // then
        assertThat(result).isNotNull();
        verify(userMapper).toUserDetailsDTO(user);
    }

    @Test
    void should_throw_exception_when_no_authenticated_user() {
        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(context);

        assertThatThrownBy(() -> userService.getCurrentUser())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("No authenticated user found");
    }

    @Test
    void should_register_new_user() {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("newuser");
        request.setPassword("password");
        request.setEmail("test@example.com");

        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        userService.registerUser(request);

        verify(userRepository).save(any(User.class));
    }

    @Test
    void should_throw_exception_when_username_already_exists() {
        RegistrationRequest request = new RegistrationRequest();
        request.setUsername("existinguser");

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Username already exists");
    }

    @Test
    void should_login_user() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password");

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);
        HttpSession oldSession = mock(HttpSession.class);
        HttpSession newSession = mock(HttpSession.class);

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(httpRequest.getSession(false)).thenReturn(oldSession);
        when(httpRequest.getSession(true)).thenReturn(newSession);

        User user = new User();
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        UserDetailsDTO userDetailsDTO = new UserDetailsDTO();
        when(userMapper.toUserDetailsDTO(user)).thenReturn(userDetailsDTO);

        UserDetailsDTO result = userService.login(loginRequest, httpRequest);

        assertThat(result).isNotNull();
        verify(newSession).setAttribute(eq(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY), any());
        verify(userMapper).toUserDetailsDTO(user);
    }

    @Test
    void should_throw_exception_when_user_not_found_on_login() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("unknownuser");
        loginRequest.setPassword("password");

        HttpServletRequest httpRequest = mock(HttpServletRequest.class);

        when(authenticationManager.authenticate(any())).thenReturn(mock(Authentication.class));
        when(httpRequest.getSession(false)).thenReturn(null);
        when(httpRequest.getSession(true)).thenReturn(mock(HttpSession.class));
        when(userRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.login(loginRequest, httpRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("User not found");
    }
}