package com.company.authservice.controller;

import com.company.authservice.dto.RoleRequest;
import com.company.authservice.service.RoleService;
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
@RequestMapping("/v1/roles")
public class RoleController {
    private final RoleService roleService;
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }
    @PostMapping("/")
    public ResponseEntity<Map<String,String>> createRole(@RequestBody @Valid RoleRequest request){
        Map<String,String> response = new HashMap<>();
        roleService.createRole(request);
        response.put("message", "Role created successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
