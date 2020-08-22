package com.example.SecurityTemplate.CustomSecurityConfiguration.ScopeAnnotations.Aspect;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.User;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.UserRole;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Exception.ForbiddenScopeForThisRole;
import com.example.SecurityTemplate.CustomSecurityConfiguration.ScopeAnnotations.SecureScopeForRoles;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Utils.UserContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class SecureScopeForRolesAspect {

    @Before("@annotation(scope)")
    public void validateScope(SecureScopeForRoles scope) {
        User actualUser = UserContextUtils.getActualUser();
        if (actualUser == null ||
                actualUser.getRoles().stream()
                        .map(UserRole::getRole)
                        .noneMatch(
                                role ->
                                        Arrays.stream(scope.roles())
                                                .map(Enum::name).anyMatch(o -> o.equals(role))
                        )) {

            throw new ForbiddenScopeForThisRole();
        }
    }

}