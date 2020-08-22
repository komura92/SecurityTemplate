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
@Table(name = LoginConfiguration.RESET_LINK_TABLE_NAME)
public class UserResetPasswordLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @Column(nullable = false)
    private String resetPasswordLink;

    @Column(nullable = false)
    private LocalDateTime generatedDate;

    private LocalDateTime usedDate;

    @Column(nullable = false)
    private boolean expired;
}