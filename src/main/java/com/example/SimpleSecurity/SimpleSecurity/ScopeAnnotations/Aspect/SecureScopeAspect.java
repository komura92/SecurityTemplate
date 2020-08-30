package com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.Aspect;


import com.example.SimpleSecurity.SimpleSecurity.Entity.User;
import com.example.SimpleSecurity.SimpleSecurity.Entity.UserRole;
import com.example.SimpleSecurity.SimpleSecurity.Exception.ForbiddenScopeForThisRole;
import com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.SecureScopeForRole;
import com.example.SimpleSecurity.SimpleSecurity.Utils.UserContextUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class SecureScopeAspect {

    @Before("@annotation(scope)")
    public void validateScope(SecureScopeForRole scope) {
        User actualUser = UserContextUtils.getActualUser();
        if (actualUser == null ||
                actualUser.getRoles().stream()
                        .map(UserRole::getRole)
                        .noneMatch(o -> Objects.equals(o, scope.role().name()))) {

            throw new ForbiddenScopeForThisRole();
        }
    }

}
