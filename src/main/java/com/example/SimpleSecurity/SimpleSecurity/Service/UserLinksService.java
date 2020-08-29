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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLinksService {
    private final UserRepository userRepository;
    private final UserActivationLinkRepository userActivationLinkRepository;
    private final UserResetPasswordLinkRepository userResetPasswordLinkRepository;
    private final ActivationLinkValidator activationLinkValidator;
    private final ResetPasswordLinkValidator resetPasswordLinkValidator;

    @Transactional
    public void activate(String activationLink) {
        Optional<UserActivationLink> userActivationLink = userActivationLinkRepository.findByActivationLink(activationLink);

        if (userActivationLink.isEmpty()) {
            log.debug("User activation link doesn't exist");
            return;
        }

        UserActivationLink activationLinkEntity = userActivationLink.get();

        if (activationLinkValidator.isExpired(activationLinkEntity)) {
            log.debug("Activation link expired");
            return;
        }

        if (activationLinkEntity.getUser().isActivated()) {
            log.debug("User already activated");
            return;
        }

        if (activationLinkEntity.getUser().isBlocked()) {
            log.debug("User blocked");
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
            log.debug("Reset password link not found");
            return;
        }

        UserResetPasswordLink resetPasswordLinkEntity = userResetPasswordLink.get();

        if (!Objects.equals(user, resetPasswordLinkEntity.getUser())) {
            log.debug("Bad credentials");
            throw new UserNoMatchLinkException(user.getId(), resetPasswordLinkEntity.getUser().getId());
        }

        if (resetPasswordLinkValidator.isExpired(resetPasswordLinkEntity)) {
            log.debug("Reset password link expired");
            return;
        }

        if (LoginConfiguration.REQUIRED_ACTIVATION && !resetPasswordLinkEntity.getUser().isActivated()) {
            log.debug("User not activated yet");
            return;
        }

        if (resetPasswordLinkEntity.getUser().isBlocked()) {
            log.debug("User blocked");
            return;
        }

        if (!UserNewPasswordValidator.validate(userResetPasswordModel.getPassword())) {
            log.debug("Password is too weak");
            throw new PasswordTooWeak();
        }

        resetPasswordLinkEntity.setUsedDate(LocalDateTime.now());
        resetPasswordLinkEntity.setExpired(true);
        userResetPasswordLinkRepository.save(resetPasswordLinkEntity);

        user.setPassword(PasswordUtils.getNewPasswordHash(userResetPasswordModel.getPassword()));
        userRepository.save(user);
    }
}
