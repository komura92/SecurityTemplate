package com.example.SimpleSecurity.SimpleSecurity.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResetPasswordModel extends UserModel {
    private String resetPasswordLink;
}
