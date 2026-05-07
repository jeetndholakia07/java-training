package com.company.authservice.service;

import com.company.authservice.dto.RoleRequest;
import com.company.authservice.exception.EntityNotFoundException;
import com.company.authservice.model.Role;
import com.company.authservice.model.User;
import com.company.authservice.repository.RoleRepository;
import com.company.authservice.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final AuthService authService;
    public RoleService(UserRepository userRepository, RoleRepository roleRepository, AuthService authService){
        this.roleRepository = roleRepository;
        this.authService = authService;
    }
    public void createRole(RoleRequest request){
        Role role = new Role();
        role.setGuid(authService.generateUUID());
        role.setRoleName(request.getRole());
        roleRepository.save(role);
    }
}
