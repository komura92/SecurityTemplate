package com.example.SimpleSecurity.SimpleSecurity.Repository;

import com.example.SimpleSecurity.SimpleSecurity.Entity.User;
import com.example.SimpleSecurity.SimpleSecurity.Entity.UserActivationLink;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserActivationLinkRepository extends JpaRepository<UserActivationLink, Long> {
    Optional<UserActivationLink> findByActivationLink(String activationLink);

    Optional<UserActivationLink> findByUserAndExpired(User user, boolean expired);

    Optional<UserActivationLink> findByActivationLinkAndExpired(String activationLink, boolean expired);
}
