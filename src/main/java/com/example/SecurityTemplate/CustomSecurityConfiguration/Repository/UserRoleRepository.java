package com.example.SecurityTemplate.CustomSecurityConfiguration.Repository;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

}
