package com.example.SecurityTemplate.CustomSecurityConfiguration.ScopeAnnotations.Aspect;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Model.MyUserDetails;
import com.example.SecurityTemplate.CustomSecurityConfiguration.ScopeAnnotations.AuthorizedScope;
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
public class AuthorizedScopeAspect {

    @Before("@annotation(scope)")
    public void validateScope(AuthorizedScope scope) {
        MyUserDetails myUserDetails = UserContextUtils.getActualUser();
    }

}