package com.example.SimpleSecurity.SimpleSecurity.Validator;

import com.example.SimpleSecurity.SimpleSecurity.Configuration.LoginConfiguration;
import com.example.SimpleSecurity.SimpleSecurity.Entity.UserActivationLink;
import com.example.SimpleSecurity.SimpleSecurity.Repository.UserActivationLinkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ActivationLinkValidator {

    private final UserActivationLinkRepository userActivationLinkRepository;


    public boolean isNewActivationLinkNotExistsInDB(String activationLink) {
        return userActivationLinkRepository
                .findByActivationLinkAndExpired(activationLink, false)
                .isEmpty();
    }

    public boolean isExistingActivationLinkCorrect(UserActivationLink userActivationLink) {
        return userActivationLink != null &&
                !isExpired(userActivationLink);
    }

    public boolean isExpired(UserActivationLink userActivationLink) {
        return userActivationLink.getActivationDate() != null &&
                LocalDateTime.now()
                        .isBefore(userActivationLink.getGeneratedDate()
                                .plusDays(LoginConfiguration.ACTIVATION_LINK_EXPIRED_DAYS)
                                .plusHours(LoginConfiguration.ACTIVATION_LINK_EXPIRED_HOURS)
                                .plusMinutes(LoginConfiguration.ACTIVATION_LINK_EXPIRED_MINUTES)
                        );
    }
}
