package com.example.SimpleSecurity.HomeTest;

import com.example.SimpleSecurity.SimpleSecurity.Model.Role;
import com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.AuthorizedScope;
import com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.OnlyUnauthorizedScope;
import com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.SecureScopeForRole;
import com.example.SimpleSecurity.SimpleSecurity.ScopeAnnotations.SecureScopeForRoles;
import com.example.SimpleSecurity.SimpleSecurity.Utils.UserContextUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/home")
public class CustomHomeController {

    @GetMapping
    public String defaultHome() {
        return "HomePage. Welcome " +
                (UserContextUtils.getActualUser() == null ? "you" : UserContextUtils.getActualUser().getUsername());
    }

    @OnlyUnauthorizedScope
    @GetMapping(path = "/unAuth")
    public String unAuthHome() {
        return "HomePage for unauthenticated";
    }

    @AuthorizedScope
    @GetMapping(path = "/auth")
    public String authHome() {
        return "HomePage for authenticated";
    }

    @SecureScopeForRole(role = Role.USER)
    @GetMapping(path = "/user")
    public String userHome() {
        return "HomePage for user";
    }

    @SecureScopeForRole(role = Role.WORKER)
    @GetMapping(path = "/worker")
    public String workerHome() {
        return "HomePage for worker";
    }

    @SecureScopeForRole(role = Role.ADMIN)
    @GetMapping(path = "/admin")
    public String adminHome() {
        return "HomePage for admin";
    }

    @SecureScopeForRole(role = Role.SUPER_ADMIN)
    @GetMapping(path = "/superAdmin")
    public String superAdminHome() {
        return "HomePage for super admin";
    }

    @GetMapping(path = "/annotationScope")
    public String annotationScope() {
        return "HomePage for super admin";
    }

    @SecureScopeForRoles(roles = {Role.USER, Role.SUPER_ADMIN})
    @GetMapping(path = "/groupScope")
    public String groupScope() {
        return "group home page";
    }


}
