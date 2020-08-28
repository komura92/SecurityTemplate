package com.example.SecurityTemplate.CustomSecurityConfiguration.ScopeAnnotations.Aspect;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Exception.UserAuthenticated;
import com.example.SecurityTemplate.CustomSecurityConfiguration.ScopeAnnotations.OnlyUnauthorizedScope;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Utils.UserContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class OnlyUnauthorizedScopeAspect {

    @Before("@annotation(scope)")
    public void validateScope(OnlyUnauthorizedScope scope) {
        if (UserContextUtils.isUserAuthenticated()) {
            throw new UserAuthenticated();
        }
    }
}
