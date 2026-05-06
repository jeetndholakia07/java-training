package com.company.authservice.service;

import com.company.authservice.dto.UserLoginRequest;
import com.company.authservice.dto.UserPayload;
import com.company.authservice.dto.UserRegisterRequest;
import com.company.authservice.exception.EntityNotFoundException;
import com.company.authservice.model.User;
import com.company.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AuthService authService;
    public UserService(UserRepository repository,AuthService authService){
        this.userRepository = repository;
        this.authService = authService;
    }
    public void registerUser(UserRegisterRequest request){
        if(userRepository.getUserByEmail(request.getEmail())!=null){
            throw new EntityNotFoundException("User","User already exists.");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(authService.hashPassword(request.getPassword()));
        user.setGuid(authService.generateUUID());
        userRepository.save(user);
    }
    public Map<String,String> verifyUser(UserLoginRequest request){
        User user = userRepository.getUserByEmail(request.getEmail());
        if(user==null){
            throw new EntityNotFoundException("User","User not found");
        }
        String hashedPassword = user.getPasswordHash();
        boolean isValid = authService.verifyPassword(request.getPassword(),hashedPassword);
        if(!isValid){
            throw new IllegalStateException("Invalid email or password.");
        }
        UserPayload userPayload = new UserPayload();
        Map<String,String> response = new HashMap<>();
        userPayload.setUserGuid(user.getGuid());
        userPayload.setUsername(user.getUsername());
        userPayload.setRole(user.getRole().getRoleName());
        response.put("access_token",authService.getJwtService().generateToken(userPayload));
        return response;
    }
}
