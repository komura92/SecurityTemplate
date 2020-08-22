package com.example.SecurityTemplate.CustomSecurityConfiguration.Facade;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.User;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Model.*;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Service.UserLoginService;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Service.UserLinksService;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Utils.UserContextUtils;
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

    public String loginForToken(UserLoginModel userLoginModel) {
        String JWT = userLoginService.login(userLoginModel);
        log.info("User logged in [username={}]", userLoginModel.getUsername());
        return JWT;
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

    //TODO in controller ? logoutSuccessHandler
    public void logout() {
        UserContextUtils.disableAuthentication();
    }
}
