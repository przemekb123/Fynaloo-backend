package com.fynaloo.Service.Impl;

import com.fynaloo.Dto.UserDetailsDTO;
import com.fynaloo.Dto.LoginRequest;
import com.fynaloo.Dto.RegistrationRequest;
import com.fynaloo.Mapper.UserMapper;
import com.fynaloo.Model.Entity.User;
import com.fynaloo.Repository.UserRepository;
import com.fynaloo.Configuration.CustomUserDetails;
import com.fynaloo.Service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    @Override
    public UserDetailsDTO getCurrentUserDTO() {
        User currentUser = getCurrentUser();
        return userMapper.toUserDetailsDTO(currentUser);
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof CustomUserDetails customUserDetails) {
            Long userId = customUserDetails.getUser().getId();
            return userRepository.findByIdWithGroups(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));
        }
        throw new IllegalStateException("No authenticated user found");
    }


    @Override
    public void registerUser(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRegistrationDate(LocalDateTime.now());

        userRepository.save(user);
    }

    @Override
    public UserDetailsDTO login(LoginRequest request, HttpServletRequest httpRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = httpRequest.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        session = httpRequest.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toUserDetailsDTO(user);
    }
}
