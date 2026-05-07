package com.company.authservice.repository;

import com.company.authservice.model.Role;
import com.company.authservice.utils.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findRoleByRoleName(RoleEnum roleName);
}
