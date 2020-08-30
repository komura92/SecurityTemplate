package com.example.SimpleSecurity.SimpleSecurity.Generator;

import com.example.SimpleSecurity.SimpleSecurity.Configuration.LoginConfiguration;
import com.example.SimpleSecurity.SimpleSecurity.Entity.User;
import com.example.SimpleSecurity.SimpleSecurity.Entity.UserResetPasswordLink;
import com.example.SimpleSecurity.SimpleSecurity.Exception.UserBlockedException;
import com.example.SimpleSecurity.SimpleSecurity.Exception.UserNotFoundException;
import com.example.SimpleSecurity.SimpleSecurity.Repository.UserRepository;
import com.example.SimpleSecurity.SimpleSecurity.Repository.UserResetPasswordLinkRepository;
import com.example.SimpleSecurity.SimpleSecurity.Validator.ResetPasswordLinkValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserResetPasswordLinkEntityGenerator extends Generator {

    private final ResetPasswordLinkValidator resetPasswordLinkValidator;
    private final UserRepository userRepository;
    private final UserResetPasswordLinkRepository userResetPasswordLinkRepository;

    @Transactional
    public UserResetPasswordLink getResetPasswordLink(String username) {
        String resetPasswordLink;

        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            if (user.get().isBlocked()) {
                throw new UserBlockedException();
            }
            Optional<UserResetPasswordLink> userResetPasswordLink = userResetPasswordLinkRepository.findByUserAndExpired(user.get(), false);
            if (userResetPasswordLink.isPresent()) {
                if (!resetPasswordLinkValidator.isExpired(userResetPasswordLink.get())) {
                    return userResetPasswordLink.get();
                } else {
                    UserResetPasswordLink link = userResetPasswordLink.get();
                    link.setExpired(true);
                    userResetPasswordLinkRepository.save(link);
                }
            }
        } else {
            throw new UserNotFoundException(username);
        }

        do {
            resetPasswordLink = getRandomString(LoginConfiguration.RESET_PASSWORD_LINK_LENGTH);
        } while (!resetPasswordLinkValidator.isNewResetPasswordLinkCorrect(resetPasswordLink));

        return userResetPasswordLinkRepository.save(UserResetPasswordLink.builder()
                .id(null)
                .user(user.get())
                .resetPasswordLink(resetPasswordLink)
                .generatedDate(LocalDateTime.now())
                .usedDate(null)
                .expired(false)
                .build());
    }
}
