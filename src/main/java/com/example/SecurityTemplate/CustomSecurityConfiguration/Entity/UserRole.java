package com.example.SecurityTemplate.CustomSecurityConfiguration.Entity;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;
import com.fasterxml.jackson.annotation.JsonBackReference;
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
