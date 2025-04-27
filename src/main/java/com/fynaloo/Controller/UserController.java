package com.fynaloo.Controller;

import com.fynaloo.Dto.*;
import com.fynaloo.Service.IJwtService;
import com.fynaloo.Service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final IJwtService jwtService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok(new ApiResponse("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> login(@RequestBody LoginRequest request) {
        // Autoryzacja loginu
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new JwtAuthResponse(token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsDTO> getCurrentUser() {

        UserDetailsDTO dto = userService.getCurrentUserDTO();
        return ResponseEntity.ok(dto);
    }

}
