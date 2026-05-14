package com.company.authservice.service;

import com.company.authservice.dto.UserLoginRequest;
import com.company.authservice.dto.UserPayload;
import com.company.authservice.dto.UserRegisterRequest;
import com.company.authservice.exception.EntityExistsException;
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

    public UserService(UserRepository userRepository, RoleRepository roleRepository, AuthService authService,
        RegexValidation regexValidation) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authService = authService;
        this.regexValidation = regexValidation;
    }

    public void registerUser(UserRegisterRequest request) {
        if (!regexValidation.validateEmail(request.email())) {
            throw new IllegalStateException("Invalid email");
        }
        if (!regexValidation.validatePassword(request.password())) {
            throw new IllegalStateException("Invalid password");
        }
        if (userRepository.getUserByEmail(request.email()) != null) {
            throw new EntityExistsException("User", "User already exists.");
        }
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(authService.hashPassword(request.password()));
        user.setGuid(authService.generateUUID());
        Role role = roleRepository.findRoleByRoleName(request.role());
        if (role == null) {
            throw new IllegalStateException("Invalid role");
        }
        user.setRole(role);
        userRepository.save(user);
    }

    public Map<String, String> verifyUser(UserLoginRequest request) {
        if (!regexValidation.validateEmail(request.email()) || !regexValidation.validatePassword(request.password())) {
            throw new IllegalStateException("Invalid email or password.");
        }
        User user = userRepository.getUserByEmail(request.email());
        if (user == null) {
            throw new IllegalStateException("Invalid email or password");
        }
        String hashedPassword = user.getPasswordHash();
        boolean isValid = authService.verifyPassword(request.password(), hashedPassword);
        if (!isValid) {
            throw new IllegalStateException("Invalid email or password.");
        }
        UserPayload userPayload = new UserPayload(
                user.getGuid(),
                user.getUsername(),
                user.getRole().getRoleName()
        );
        Map<String, String> response = new HashMap<>();
        response.put("access_token", authService.getJwtService().generateToken(userPayload));
        return response;
    }
}
