package com.company.authservice.controller;

import com.company.authservice.dto.UserLoginRequest;
import com.company.authservice.dto.UserRegisterRequest;
import com.company.authservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody @Valid UserRegisterRequest request){
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully.");
        userService.registerUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @PostMapping("/login")
    public ResponseEntity<Map<String,String>> loginUser(@RequestBody @Valid UserLoginRequest request){
        return new ResponseEntity<>(userService.verifyUser(request), HttpStatus.OK);
    }
}
