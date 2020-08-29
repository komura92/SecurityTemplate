package com.example.SimpleSecurity.SimpleSecurity.Utils;

import com.example.SimpleSecurity.SimpleSecurity.Entity.User;
import com.example.SimpleSecurity.SimpleSecurity.Exception.UserNotAuthenticated;
import com.example.SimpleSecurity.SimpleSecurity.Model.MyUserDetails;
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
