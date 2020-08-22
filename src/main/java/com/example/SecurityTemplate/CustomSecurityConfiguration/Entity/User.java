package com.example.SecurityTemplate.CustomSecurityConfiguration.Entity;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = LoginConfiguration.USER_TABLE_NAME)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(nullable = false, unique = true)
    protected String username;

    @Column(nullable = false)
    protected String password;

    @Column(nullable = false, unique = true)
    protected String email;

    @OneToMany(mappedBy = "user")
    protected List<UserRole> roles;

    @Column(nullable = false)
    protected boolean activated;

    @Column(nullable = false)
    protected boolean blocked;
}
