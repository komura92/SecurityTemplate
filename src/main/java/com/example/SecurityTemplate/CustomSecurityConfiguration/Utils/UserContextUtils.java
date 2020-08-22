package com.example.SecurityTemplate.CustomSecurityConfiguration.Utils;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.User;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Exception.UserNotAuthenticated;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Model.MyUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserContextUtils {

    public static MyUserDetails getActualUser() {
        if (!isUserAuthenticated() ||
                !(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User)) {
            throw new UserNotAuthenticated();
        }

        return (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    //TODO NOT WORKING SO WELL
    private static boolean isUserAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    public static void disableAuthentication() {
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
    }
}
