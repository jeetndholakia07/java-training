package com.company.authservice.repository;

import com.company.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u JOIN FETCH u.role where u.email=:email")
    User getUserByEmail(@Param("email") String email);
    User getUserByGuid(String guid);
}
