package com.example.SimpleSecurity.SimpleSecurity.Repository;

import com.example.SimpleSecurity.SimpleSecurity.Entity.User;
import com.example.SimpleSecurity.SimpleSecurity.Entity.UserResetPasswordLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserResetPasswordLinkRepository extends JpaRepository<UserResetPasswordLink, Long> {
    Optional<UserResetPasswordLink> findByResetPasswordLink(String resetPasswordLink);

    Optional<UserResetPasswordLink> findByResetPasswordLinkAndExpired(String resetPasswordLink, boolean expired);

    Optional<UserResetPasswordLink> findByUserAndExpired(User user, boolean expired);
}
