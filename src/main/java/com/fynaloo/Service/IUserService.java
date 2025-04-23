package com.fynaloo.Service;

import com.fynaloo.Dto.UserDetailsDTO;
import com.fynaloo.Dto.LoginRequest;
import com.fynaloo.Dto.RegistrationRequest;
import com.fynaloo.Model.Entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface IUserService {

    UserDetailsDTO getCurrentUserDTO();

    User getCurrentUser();

    void registerUser(RegistrationRequest request);

    UserDetailsDTO login(LoginRequest request, HttpServletRequest httpRequest);
}
