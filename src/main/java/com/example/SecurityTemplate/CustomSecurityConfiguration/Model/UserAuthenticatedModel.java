package com.example.SecurityTemplate.CustomSecurityConfiguration.Model;

import lombok.*;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAuthenticatedModel {
    private Long id;
    private String JWT;
    private List<String> roles;
}
