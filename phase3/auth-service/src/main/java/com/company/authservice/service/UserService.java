package com.company.authservice.service;

import com.company.authservice.dto.UserLoginRequest;
import com.company.authservice.dto.UserPayload;
import com.company.authservice.dto.UserRegisterRequest;
import com.company.authservice.exception.EntityExistsException;
import com.company.authservice.exception.EntityNotFoundException;
import com.company.authservice.model.Role;
import com.company.authservice.model.User;
import com.company.authservice.repository.RoleRepository;
import com.company.authservice.repository.UserRepository;
import com.company.authservice.utils.RegexValidation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final AuthService authService;
    private final RegexValidation regexValidation;
    public UserService(UserRepository userRepository,RoleRepository roleRepository,AuthService authService,
        RegexValidation regexValidation){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authService = authService;
        this.regexValidation = regexValidation;
    }
    public void registerUser(UserRegisterRequest request){
        if(!regexValidation.validateEmail(request.getEmail())){
            throw new IllegalStateException("Invalid email");
        }
        if(!regexValidation.validatePassword(request.getPassword())){
            throw new IllegalStateException("Invalid password");
        }
        if(userRepository.getUserByEmail(request.getEmail())!=null){
            throw new EntityExistsException("User","User already exists.");
        }
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(authService.hashPassword(request.getPassword()));
        user.setGuid(authService.generateUUID());
        Role role = roleRepository.findRoleByRoleName(request.getRole());
        if(role==null){
            throw new IllegalStateException("Invalid role");
        }
        user.setRole(role);
        userRepository.save(user);
    }
    public Map<String,String> verifyUser(UserLoginRequest request){
        if(!regexValidation.validateEmail(request.getEmail()) || !regexValidation.validatePassword(request.getPassword())){
            throw new IllegalStateException("Invalid credentials.");
        }
        User user = userRepository.getUserByEmail(request.getEmail());
        if(user==null){
            throw new IllegalStateException("Invalid credentials.");
        }
        String hashedPassword = user.getPasswordHash();
        boolean isValid = authService.verifyPassword(request.getPassword(),hashedPassword);
        if(!isValid){
            throw new IllegalStateException("Invalid credentials.");
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
