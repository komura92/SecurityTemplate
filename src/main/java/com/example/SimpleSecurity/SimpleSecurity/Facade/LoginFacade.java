package com.example.SimpleSecurity.SimpleSecurity.Facade;

import com.example.SimpleSecurity.SimpleSecurity.Configuration.LoginConfiguration;
import com.example.SimpleSecurity.SimpleSecurity.Model.*;
import com.example.SimpleSecurity.SimpleSecurity.Service.UserLoginService;
import com.example.SimpleSecurity.SimpleSecurity.Service.UserLinksService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFacade {

    private final UserLoginService userLoginService;
    private final UserLinksService userLinksService;

    public void register(UserRegisterModel userRegisterModel) {
        userLoginService.register(userRegisterModel);

        if (LoginConfiguration.REQUIRED_ACTIVATION && LoginConfiguration.activationLinkSender != null) {
            String activationLink = userLoginService.getNewActivationLinkString(userRegisterModel.getUsername());
            LoginConfiguration.activationLinkSender.send(activationLink, userRegisterModel.getEmail());
        }

        log.info("New user registered [username={}]", userRegisterModel.getUsername());
    }

    public UserAuthenticatedModel login(UserLoginModel userLoginModel) {
        UserAuthenticatedModel userAuthenticatedModel = userLoginService.login(userLoginModel);
        log.info("User logged in [username={}]", userLoginModel.getUsername());
        return userAuthenticatedModel;
    }

    public void deleteAccount(UserDeleteModel userDeleteModel) {
        userLoginService.delete(userDeleteModel);
        log.info("User deleted [username={}]", userDeleteModel.getUsername());
    }

    public void activateAccount(String activationLink) {
        if (!LoginConfiguration.REQUIRED_ACTIVATION) {
            log.info("Server not required activations");
            return;
        }

        userLinksService.activate(activationLink);
    }

    public void changePassword(UserChangePasswordModel userChangePasswordModel) {
        userLoginService.updateUserPassword(userChangePasswordModel);
        log.info("User changed password [username={}]", userChangePasswordModel.getUsername());
    }

    public void initResetPassword(String username) {
        if (LoginConfiguration.resetPasswordLinkSender == null) {
            log.error("ResetPasswordLinkSender is null");
            return;
        }
        String newResetPasswordLink = userLoginService.getNewResetPasswordLink(username);
        LoginConfiguration.resetPasswordLinkSender.send(newResetPasswordLink, userLoginService.getUserEmail(username));
    }

    public void resetPassword(UserResetPasswordModel userResetPasswordModel) {
        userLinksService.resetPassword(userResetPasswordModel);
    }
}
