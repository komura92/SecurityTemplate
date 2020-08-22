package com.example.SecurityTemplate.CustomSecurityConfiguration.Entity;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Setter
@Getter
@Builder
@AllArgsConstructor
@Table(name = LoginConfiguration.ACTIVATION_LINK_TABLE_NAME)
public class UserActivationLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String activationLink;

    @Column(nullable = false)
    private LocalDateTime generatedDate;

    private LocalDateTime activationDate;

    @Column(nullable = false)
    private boolean expired;
}
