package com.example.SecurityTemplate.CustomSecurityConfiguration.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResetPasswordModel extends UserModel {
    private String resetPasswordLink;
}
