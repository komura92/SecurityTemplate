package com.example.SimpleSecurity.SimpleSecurity.Model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class UserChangePasswordModel extends UserModel{
    private String newPassword;
}
