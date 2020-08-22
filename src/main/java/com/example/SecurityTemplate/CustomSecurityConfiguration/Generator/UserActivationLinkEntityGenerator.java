package com.example.SecurityTemplate.CustomSecurityConfiguration.Generator;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.User;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.UserActivationLink;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Exception.UserBlockedException;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Exception.UserNotFoundException;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Repository.UserActivationLinkRepository;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Repository.UserRepository;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Validator.ActivationLinkValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserActivationLinkEntityGenerator extends Generator {

    private final ActivationLinkValidator activationLinkValidator;
    private final UserRepository userRepository;
    private final UserActivationLinkRepository userActivationLinkRepository;


    @Transactional
    public UserActivationLink getNewActivationLinkForUser(String username) {
        String activationLink;

        Optional<User> user = userRepository.findUserByUsername(username);
        if (user.isPresent()) {
            if (user.get().isBlocked()) {
                throw new UserBlockedException();
            }
            Optional<UserActivationLink> userActivationLink = userActivationLinkRepository.findByUserAndExpired(user.get(), false);
            if (userActivationLink.isPresent()) {
                if (!activationLinkValidator.isExpired(userActivationLink.get())) {
                    return userActivationLink.get();
                } else {
                    UserActivationLink link = userActivationLink.get();
                    link.setExpired(true);
                    userActivationLinkRepository.save(link);
                }
            }
        } else {
            throw new UserNotFoundException(username);
        }

        do {
            activationLink = getRandomString(LoginConfiguration.ACTIVATION_LINK_LENGTH);
        } while (!activationLinkValidator.isNewActivationLinkNotExistsInDB(activationLink));


        return userActivationLinkRepository.save(UserActivationLink.builder()
                .id(null)
                .user(userRepository.findUserByUsername(username)
                        .orElseThrow(() -> new UserNotFoundException(username)))
                .activationLink(activationLink)
                .generatedDate(LocalDateTime.now())
                .activationDate(null)
                .expired(false)
                .build());
    }
}
