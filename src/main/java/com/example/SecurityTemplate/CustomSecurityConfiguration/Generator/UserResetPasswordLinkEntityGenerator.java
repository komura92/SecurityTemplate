package com.example.SecurityTemplate.CustomSecurityConfiguration.Generator;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.User;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.UserResetPasswordLink;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Exception.UserBlockedException;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Exception.UserNotFoundException;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Repository.UserRepository;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Repository.UserResetPasswordLinkRepository;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Validator.ResetPasswordLinkValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
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
