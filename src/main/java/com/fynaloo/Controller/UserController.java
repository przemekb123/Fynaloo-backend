package com.fynaloo.Controller;

import com.fynaloo.Dto.ApiResponse;
import com.fynaloo.Dto.LoginRequest;
import com.fynaloo.Dto.RegistrationRequest;
import com.fynaloo.Dto.UserDetailsDTO;
import com.fynaloo.Service.IUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.filters.ExpiresFilter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegistrationRequest request) {
        userService.registerUser(request);
        ApiResponse response = new ApiResponse("User register Successfully");

        return ResponseEntity.ok(response);

    }

    @PostMapping("/login")
    public ResponseEntity<UserDetailsDTO> login(@RequestBody LoginRequest request, HttpServletRequest httpRequest) {
        UserDetailsDTO userDetails = userService.login(request, httpRequest);
        return ResponseEntity.ok(userDetails);

    }

    @GetMapping("/me")
    public ResponseEntity<UserDetailsDTO> getCurrentUser() {
        UserDetailsDTO userDetails = userService.getCurrentUserDTO();
        return ResponseEntity.ok(userDetails);
    }

}
