package com.aifood.controller.user;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aifood.dto.user.LoginRequest;
import com.aifood.dto.user.LoginResponse;
import com.aifood.dto.user.ProfileResponse;
import com.aifood.dto.user.RegisterUserRequest;
import com.aifood.dto.user.UserResponse;
import com.aifood.service.user.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("api/users")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody RegisterUserRequest request){
        return userService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request){
        return userService.login(request);
    }

    @GetMapping("/profile")
    public ProfileResponse getProfile(Principal principal){

        return userService.getProfile(principal.getName());
    }
}
