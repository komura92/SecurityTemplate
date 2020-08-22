package com.example.SecurityTemplate.CustomSecurityConfiguration.Validator;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.UserResetPasswordLink;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Repository.UserResetPasswordLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ResetPasswordLinkValidator {

    private final UserResetPasswordLinkRepository userResetPasswordLinkRepository;

    public boolean isNewResetPasswordLinkCorrect(String resetPasswordLink) {
        return userResetPasswordLinkRepository
                .findByResetPasswordLinkAndExpired(resetPasswordLink, false)
                .isEmpty();
    }

    public boolean isExistingResetPasswordLinkCorrect(UserResetPasswordLink userResetPasswordLink) {
        return userResetPasswordLink != null &&
                !isExpired(userResetPasswordLink);
    }

    public boolean isExpired(UserResetPasswordLink userResetPasswordLink) {
        return userResetPasswordLink.getUsedDate() == null &&
                LocalDateTime.now()
                        .isBefore(userResetPasswordLink.getGeneratedDate()
                                .plusDays(LoginConfiguration.RESET_PASSWORD_LINK_EXPIRED_DAYS)
                                .plusHours(LoginConfiguration.RESET_PASSWORD_LINK_EXPIRED_HOURS)
                                .plusMinutes(LoginConfiguration.RESET_PASSWORD_EXPIRED_MINUTES)
                        );
    }
}
