package com.example.SimpleSecurity.SimpleSecurity.Repository;

import com.example.SimpleSecurity.SimpleSecurity.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserById(Long id);
    Optional<User> findUserByUsername(String username);
    Optional<User> findUserByUsernameAndPassword(String login, String password);

    List<User> findUsersByUsernameAndPassword(String username, String encode);
}
