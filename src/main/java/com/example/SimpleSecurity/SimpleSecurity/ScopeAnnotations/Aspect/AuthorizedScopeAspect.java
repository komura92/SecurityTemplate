package com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.Aspect;

import com.example.SimpleSecurity.SimpleSecurity.Exception.UserNotAuthenticated;
import com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.AuthorizedScope;
import com.example.SimpleSecurity.SimpleSecurity.Utils.UserContextUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class AuthorizedScopeAspect {

    @Before("@annotation(scope)")
    public void validateScope(AuthorizedScope scope) {
        if (!UserContextUtils.isUserAuthenticated()) {
            throw new UserNotAuthenticated();
        }
    }

}
