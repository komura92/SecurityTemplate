package com.example.SecurityTemplate.CustomSecurityConfiguration.ScopeAnnotations;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Model.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@AuthorizedScope
public @interface SecureScopeForRoles {
    Role[] roles() default {};
}
