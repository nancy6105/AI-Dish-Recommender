package com.aifood.service.user;

import com.aifood.dto.user.LoginRequest;
import com.aifood.dto.user.LoginResponse;
import com.aifood.dto.user.ProfileResponse;
import com.aifood.dto.user.RegisterUserRequest;
import com.aifood.dto.user.UserResponse;

public interface UserService {
    
    UserResponse register(RegisterUserRequest request);

    LoginResponse login(LoginRequest request);

    ProfileResponse getProfile(String email);
}

