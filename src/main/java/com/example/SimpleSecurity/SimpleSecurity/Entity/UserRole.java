package com.example.SimpleSecurity.SimpleSecurity.Entity;

import com.example.SimpleSecurity.SimpleSecurity.Configuration.LoginConfiguration;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor
@Table(name = LoginConfiguration.USER_ROLE_TABLE_NAME)
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false, name = "user_role")
    private String role;
}
