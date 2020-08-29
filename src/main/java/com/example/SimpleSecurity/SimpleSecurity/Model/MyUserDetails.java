package com.example.SimpleSecurity.SimpleSecurity.Model;

import com.example.SimpleSecurity.SimpleSecurity.Entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

@AllArgsConstructor
public class MyUserDetails extends User implements UserDetails {

    public MyUserDetails(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.activated = user.isActivated();
        this.blocked = user.isBlocked();
        this.roles = user.getRoles();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.getRoles()
                .stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.isActivated();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !this.isBlocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.isActivated() && !this.isBlocked();
    }
}
