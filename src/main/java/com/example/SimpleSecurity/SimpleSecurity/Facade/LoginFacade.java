package com.example.SimpleSecurity.SimpleSecurity.Facade;

import com.example.SimpleSecurity.SimpleSecurity.Configuration.LoginConfiguration;
import com.example.SimpleSecurity.SimpleSecurity.Model.*;
import com.example.SimpleSecurity.SimpleSecurity.Service.UserLinksService;
import com.example.SimpleSecurity.SimpleSecurity.Service.UserLoginService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LoginFacade {

    private Logger logger = Logger.getLogger(LoginFacade.class);

    private final UserLoginService userLoginService;
    private final UserLinksService userLinksService;

    public void register(UserRegisterModel userRegisterModel) {
        userLoginService.register(userRegisterModel);

        if (LoginConfiguration.REQUIRED_ACTIVATION && LoginConfiguration.activationLinkSender != null) {
            String activationLink = userLoginService.getNewActivationLinkString(userRegisterModel.getUsername());
            LoginConfiguration.activationLinkSender.send(activationLink, userRegisterModel.getEmail());
        }

        logger.trace("New user registered [username=" + userRegisterModel.getUsername() + "]");
    }

    public UserAuthenticatedModel login(UserLoginModel userLoginModel) {
        UserAuthenticatedModel userAuthenticatedModel = userLoginService.login(userLoginModel);
        logger.trace("User logged in [username=" + userLoginModel.getUsername() + "]");
        return userAuthenticatedModel;
    }

    public void deleteAccount(UserDeleteModel userDeleteModel) {
        userLoginService.delete(userDeleteModel);
        logger.trace("User deleted [username=" + userDeleteModel.getUsername() + "])");
    }

    public void activateAccount(String activationLink) {
        if (!LoginConfiguration.REQUIRED_ACTIVATION) {
            logger.trace("Server not required activations");
            return;
        }

        userLinksService.activate(activationLink);
    }

    public void changePassword(UserChangePasswordModel userChangePasswordModel) {
        userLoginService.updateUserPassword(userChangePasswordModel);
        logger.trace("User changed password [username=" + userChangePasswordModel.getUsername() + "]");
    }

    public void initResetPassword(String username) {
        if (LoginConfiguration.resetPasswordLinkSender == null) {
            logger.error("ResetPasswordLinkSender is null");
            return;
        }
        String newResetPasswordLink = userLoginService.getNewResetPasswordLink(username);
        LoginConfiguration.resetPasswordLinkSender.send(newResetPasswordLink, userLoginService.getUserEmail(username));
    }

    public void resetPassword(UserResetPasswordModel userResetPasswordModel) {
        userLinksService.resetPassword(userResetPasswordModel);
    }
}
