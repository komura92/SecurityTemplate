package com.example.SecurityTemplate.CustomSecurityConfiguration.Repository;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.User;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.UserActivationLink;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.UserResetPasswordLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserResetPasswordLinkRepository extends JpaRepository<UserResetPasswordLink, Long> {
    Optional<UserResetPasswordLink> findByResetPasswordLink(String resetPasswordLink);

    Optional<UserResetPasswordLink> findByResetPasswordLinkAndExpired(String resetPasswordLink, boolean expired);

    Optional<UserResetPasswordLink> findByUserAndExpired(User user, boolean expired);
}
