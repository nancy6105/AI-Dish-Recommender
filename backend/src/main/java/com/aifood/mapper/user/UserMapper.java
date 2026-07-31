package com.aifood.mapper.user;

import com.aifood.dto.user.RegisterUserRequest;
import com.aifood.dto.user.UserResponse;
import com.aifood.entity.user.User;

public class UserMapper {

    public static User toEntity(RegisterUserRequest request) {

        User user = new User();

        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        return user;
    }

    public static UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();

        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }
}