package com.example.SecurityTemplate.CustomSecurityConfiguration.Utils;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.User;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Exception.UserNotAuthenticated;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Model.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserContextUtils {

    public static final String ANONYMOUS_USERNAME = "anonymousUser";

    public static MyUserDetails getActualUser() {
        if (!isUserAuthenticated() ||
                !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User)) {
            throw new UserNotAuthenticated();
        }

        return (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean isUserAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                !Objects.equals(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(), ANONYMOUS_USERNAME);
    }
}
