package com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.Aspect;

import com.example.SimpleSecurity.SimpleSecurity.Exception.UserAuthenticated;
import com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.OnlyUnauthorizedScope;
import com.example.SimpleSecurity.SimpleSecurity.Utils.UserContextUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class OnlyUnauthorizedScopeAspect {

    @Before("@annotation(scope)")
    public void validateScope(OnlyUnauthorizedScope scope) {
        if (UserContextUtils.isUserAuthenticated()) {
            throw new UserAuthenticated();
        }
    }
}
