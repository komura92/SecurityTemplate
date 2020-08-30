package com.example.SimpleSecurity.SimpleSecurity.Service;

import com.example.SimpleSecurity.SimpleSecurity.Configuration.LoginConfiguration;
import com.example.SimpleSecurity.SimpleSecurity.Entity.User;
import com.example.SimpleSecurity.SimpleSecurity.Entity.UserActivationLink;
import com.example.SimpleSecurity.SimpleSecurity.Entity.UserResetPasswordLink;
import com.example.SimpleSecurity.SimpleSecurity.Exception.PasswordTooWeak;
import com.example.SimpleSecurity.SimpleSecurity.Exception.UserNoMatchLinkException;
import com.example.SimpleSecurity.SimpleSecurity.Exception.UserNotFoundException;
import com.example.SimpleSecurity.SimpleSecurity.Model.UserResetPasswordModel;
import com.example.SimpleSecurity.SimpleSecurity.Repository.UserActivationLinkRepository;
import com.example.SimpleSecurity.SimpleSecurity.Repository.UserRepository;
import com.example.SimpleSecurity.SimpleSecurity.Repository.UserResetPasswordLinkRepository;
import com.example.SimpleSecurity.SimpleSecurity.Utils.PasswordUtils;
import com.example.SimpleSecurity.SimpleSecurity.Validator.ActivationLinkValidator;
import com.example.SimpleSecurity.SimpleSecurity.Validator.ResetPasswordLinkValidator;
import com.example.SimpleSecurity.SimpleSecurity.Validator.UserNewPasswordValidator;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserLinksService {
    private final UserRepository userRepository;
    private final UserActivationLinkRepository userActivationLinkRepository;
    private final UserResetPasswordLinkRepository userResetPasswordLinkRepository;
    private final ActivationLinkValidator activationLinkValidator;
    private final ResetPasswordLinkValidator resetPasswordLinkValidator;

    private Logger logger = Logger.getLogger(UserLinksService.class);

    @Transactional
    public void activate(String activationLink) {
        Optional<UserActivationLink> userActivationLink = userActivationLinkRepository.findByActivationLink(activationLink);

        if (userActivationLink.isEmpty()) {
            return;
        }

        UserActivationLink activationLinkEntity = userActivationLink.get();

        if (activationLinkValidator.isExpired(activationLinkEntity)) {
            logger.trace("Activation link expired [linkId=" + activationLinkEntity.getId() + "]");
            return;
        }

        if (activationLinkEntity.getUser().isActivated()) {
            logger.trace("User already activated [username=" + activationLinkEntity.getUser().getUsername() + "]");
            return;
        }

        if (activationLinkEntity.getUser().isBlocked()) {
            logger.trace("User blocked [username=" + activationLinkEntity.getUser().getUsername() + "]");
            return;
        }

        activationLinkEntity.setActivationDate(LocalDateTime.now());
        activationLinkEntity.setExpired(true);
        userActivationLinkRepository.save(activationLinkEntity);

        activationLinkEntity.getUser().setActivated(true);
        userRepository.save(activationLinkEntity.getUser());
    }

    @Transactional
    public void resetPassword(UserResetPasswordModel userResetPasswordModel) {
        Optional<UserResetPasswordLink> userResetPasswordLink = userResetPasswordLinkRepository
                .findByResetPasswordLink(userResetPasswordModel.getResetPasswordLink());

        User user = userRepository.findUserByUsername(userResetPasswordModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userResetPasswordModel.getUsername()));

        if (userResetPasswordLink.isEmpty()) {
            return;
        }

        UserResetPasswordLink resetPasswordLinkEntity = userResetPasswordLink.get();

        if (!Objects.equals(user, resetPasswordLinkEntity.getUser())) {
            logger.trace("Bad credentials [username=" + user.getUsername() + "]");
            throw new UserNoMatchLinkException(user.getId(), resetPasswordLinkEntity.getUser().getId());
        }

        if (resetPasswordLinkValidator.isExpired(resetPasswordLinkEntity)) {
            logger.trace("Reset password link expired [linkId=" + resetPasswordLinkEntity.getId() + "]");
            return;
        }

        if (LoginConfiguration.REQUIRED_ACTIVATION && !resetPasswordLinkEntity.getUser().isActivated()) {
            logger.trace("User not activated yet [username=" + user.getUsername() + "]");
            return;
        }

        if (resetPasswordLinkEntity.getUser().isBlocked()) {
            logger.trace("User blocked [username=" + user.getUsername() + "]");
            return;
        }

        if (!UserNewPasswordValidator.validate(userResetPasswordModel.getPassword())) {
            throw new PasswordTooWeak();
        }

        resetPasswordLinkEntity.setUsedDate(LocalDateTime.now());
        resetPasswordLinkEntity.setExpired(true);
        userResetPasswordLinkRepository.save(resetPasswordLinkEntity);

        user.setPassword(PasswordUtils.getNewPasswordHash(userResetPasswordModel.getPassword()));
        userRepository.save(user);
    }
}
