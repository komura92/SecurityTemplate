package com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.Aspect;

import com.example.SimpleSecurity.SimpleSecurity.Entity.User;
import com.example.SimpleSecurity.SimpleSecurity.Entity.UserRole;
import com.example.SimpleSecurity.SimpleSecurity.Exception.ForbiddenScopeForThisRole;
import com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.SecureScopeForRoles;
import com.example.SimpleSecurity.SimpleSecurity.Utils.UserContextUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Objects;

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
                        .noneMatch(role ->
                                Arrays.stream(scope.roles())
                                        .map(Enum::name)
                                        .anyMatch(o -> Objects.equals(o, role))
                        )) {

            throw new ForbiddenScopeForThisRole();
        }
    }

}
