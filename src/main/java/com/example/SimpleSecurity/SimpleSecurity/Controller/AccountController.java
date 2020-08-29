package com.example.SimpleSecurity.SimpleSecurity.Controller;

import com.example.SimpleSecurity.SimpleSecurity.Configuration.LoginConfiguration;
import com.example.SimpleSecurity.SimpleSecurity.Facade.LoginFacade;
import com.example.SimpleSecurity.SimpleSecurity.Model.*;
import com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.AuthorizedScope;
import com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.OnlyUnauthorizedScope;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(LoginConfiguration.LOGIN_CONTROLLER_PATH)
@RequiredArgsConstructor
public class AccountController {

    private final LoginFacade loginFacade;

    @OnlyUnauthorizedScope
    @PostMapping(value = LoginConfiguration.REGISTER_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void register(@RequestBody UserRegisterModel userRegisterModel) {
        loginFacade.register(userRegisterModel);
    }

    @OnlyUnauthorizedScope
    @PostMapping(value = LoginConfiguration.AUTHORIZATION_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserAuthenticatedModel> login(@RequestBody UserLoginModel userLoginModel) {
        return ResponseEntity.ok(loginFacade.login(userLoginModel));
    }

    @AuthorizedScope
    @PutMapping(value = LoginConfiguration.CHANGE_PASSWORD_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@RequestBody UserChangePasswordModel userChangePasswordModel) {
        loginFacade.changePassword(userChangePasswordModel);
    }

    @OnlyUnauthorizedScope
    @GetMapping(value = LoginConfiguration.SEND_RESET_LINK_PATH)
    public void sendResetPasswordLink(@RequestHeader String username) {
        loginFacade.initResetPassword(username);
    }

    @OnlyUnauthorizedScope
    @GetMapping(value = LoginConfiguration.RESET_PASSWORD_PATH)
    public void resetPassword(@RequestBody UserResetPasswordModel userResetPasswordModel) {
        loginFacade.resetPassword(userResetPasswordModel);
    }

    @AuthorizedScope
    @DeleteMapping(value = LoginConfiguration.DELETE_ACCOUNT_PATH, consumes = MediaType.APPLICATION_JSON_VALUE)
    public void delete(@RequestBody UserDeleteModel userDeleteModel) {
        loginFacade.deleteAccount(userDeleteModel);
    }

    @OnlyUnauthorizedScope
    @GetMapping(value = LoginConfiguration.ACTIVATION_PATH + "/{activationLink}")
    public void activate(@PathVariable String activationLink) {
        loginFacade.activateAccount(activationLink);
    }
}
