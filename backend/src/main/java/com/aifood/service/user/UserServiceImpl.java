package com.aifood.service.user;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.aifood.dto.user.LoginRequest;
import com.aifood.dto.user.LoginResponse;
import com.aifood.dto.user.ProfileResponse;
import com.aifood.dto.user.RegisterUserRequest;
import com.aifood.dto.user.UserResponse;
import com.aifood.entity.user.User;
import com.aifood.enums.Role;
import com.aifood.exception.EmailAlreadyExistsException;
import com.aifood.exception.InvalidCredentialsException;
import com.aifood.mapper.user.UserMapper;
import com.aifood.repository.user.UserRepository;
import com.aifood.security.JwtService;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UserResponse register(RegisterUserRequest request) {

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if(existingUser.isPresent()){
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = UserMapper.toEntity(request);

        user.setPassword(passwordEncoder.encode(request.getPassword()));
        
        user.setRole(Role.USER);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new InvalidCredentialsException("Invalid email or password"));


        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(user.getEmail());

        LoginResponse response = new LoginResponse();

        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole().name());
        response.setMessage("Login Successful");
        response.setToken(token);

        return response;
    }

    @Override
    public ProfileResponse getProfile(String email){
        
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> 
                    new RuntimeException("User not found"));

        ProfileResponse response = new ProfileResponse();
        
        response.setId(user.getId());
        response.setFullname(user.getFullName());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());

        return response;
    }
}
