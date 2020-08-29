package com.example.SimpleSecurity.SimpleSecurity.Model;

import lombok.*;

import java.util.List;

@Getter
@Builder
public class UserAuthenticatedModel {
    private String JWT;
    private List<String> roles;
}
