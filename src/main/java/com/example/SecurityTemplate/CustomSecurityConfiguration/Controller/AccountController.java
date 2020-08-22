package com.example.SecurityTemplate.CustomSecurityConfiguration.Controller;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;
import com.example.SecurityTemplate.CustomSecurityConfiguration.EmailSender.ActivationLinkSender;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Facade.LoginFacade;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Model.*;
import com.example.SecurityTemplate.CustomSecurityConfiguration.ScopeAnnotations.AuthorizedScope;
import com.example.SecurityTemplate.CustomSecurityConfiguration.ScopeAnnotations.OnlyUnauthorizedScope;
import com.example.SecurityTemplate.CustomSecurityConfiguration.ScopeAnnotations.SecureScopeForRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<String> login(@RequestBody UserLoginModel userLoginModel) {
        return ResponseEntity.ok(loginFacade.loginForToken(userLoginModel));
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

    //TEST
    @GetMapping(value = "/siema")
    //@SecureScopeForRole(role = Role.USER)
    public void NoSiema() {
        log.error("error");
        log.debug("debug");
        log.warn("warn");
        log.trace("trace");
        log.info("info");
        System.out.println("siema mordy");
    }
}