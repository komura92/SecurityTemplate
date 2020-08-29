package com.example.SimpleSecurity.SimpleSecurity.Service;

import com.example.SimpleSecurity.SimpleSecurity.Configuration.LoginConfiguration;
import com.example.SimpleSecurity.SimpleSecurity.Entity.User;
import com.example.SimpleSecurity.SimpleSecurity.Entity.UserActivationLink;
import com.example.SimpleSecurity.SimpleSecurity.Entity.UserResetPasswordLink;
import com.example.SimpleSecurity.SimpleSecurity.Entity.UserRole;
import com.example.SimpleSecurity.SimpleSecurity.Exception.*;
import com.example.SimpleSecurity.SimpleSecurity.Generator.UserActivationLinkEntityGenerator;
import com.example.SimpleSecurity.SimpleSecurity.Generator.UserResetPasswordLinkEntityGenerator;
import com.example.SimpleSecurity.SimpleSecurity.Model.*;
import com.example.SimpleSecurity.SimpleSecurity.Repository.UserRepository;
import com.example.SimpleSecurity.SimpleSecurity.Repository.UserRoleRepository;
import com.example.SimpleSecurity.SimpleSecurity.Token.TokenProvider;
import com.example.SimpleSecurity.SimpleSecurity.Utils.PasswordUtils;
import com.example.SimpleSecurity.SimpleSecurity.Utils.UserContextUtils;
import com.example.SimpleSecurity.SimpleSecurity.Validator.UserActualPasswordValidator;
import com.example.SimpleSecurity.SimpleSecurity.Validator.UserEmailValidator;
import com.example.SimpleSecurity.SimpleSecurity.Validator.UserNewPasswordValidator;
import com.example.SimpleSecurity.SimpleSecurity.Validator.UsernameValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserLoginService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserActivationLinkEntityGenerator userActivationLinkEntityGenerator;
    private final UserResetPasswordLinkEntityGenerator userResetPasswordLinkEntityGenerator;

    @Value("${server.address}")
    private String SERVER_ADDRESS;

    @Value("${server.port}")
    private String SERVER_PORT;

    @Transactional
    public void register(UserRegisterModel userRegisterModel) {
        if (userRepository.findUserByUsername(userRegisterModel.getUsername()).isPresent()
                || Objects.equals(userRegisterModel.getUsername(), UserContextUtils.ANONYMOUS_USERNAME))
            throw new UserAlreadyExistsException(userRegisterModel.getUsername());

        if (!UsernameValidator.validate(userRegisterModel.getUsername())) {
            throw new BadUsernameException();
        }

        if (!UserNewPasswordValidator.validate(userRegisterModel.getPassword())) {
            throw new PasswordTooWeak();
        }

        if (!UserEmailValidator.validate(userRegisterModel.getEmail())) {
            throw new BadEmailException();
        }

        List<UserRole> roles = new ArrayList<>();
        User user = new User(
                null,
                userRegisterModel.getUsername(),
                PasswordUtils.getNewPasswordHash(userRegisterModel.getPassword()),
                userRegisterModel.getEmail(),
                roles,
                !LoginConfiguration.REQUIRED_ACTIVATION,
                false
        );

        User newUser = userRepository.save(user);
        LoginConfiguration.DEFAULT_NEW_USER_ROLES.forEach(role -> {
            UserRole userRole = new UserRole(null, newUser, role.name());
            roles.add(userRoleRepository.save(userRole));
        });

        newUser.setRoles(roles);
        userRepository.save(newUser);
    }

    public UserAuthenticatedModel login(UserLoginModel userLoginModel) {
        User user = userRepository
                .findUserByUsername(userLoginModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userLoginModel.getUsername()));

        if (user.isBlocked() || !user.isActivated()) {
            log.debug("User is not activated or blocked [id={}]", user.getId());
            return null;
        }

        if (UserActualPasswordValidator.validate(userLoginModel.getPassword(), user.getPassword())) {

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userLoginModel.getUsername(), userLoginModel.getPassword());

            authenticationManager.authenticate(authenticationToken);
            String JWT = tokenProvider.createToken(userLoginModel.getUsername());

            return UserAuthenticatedModel.builder()
                    .JWT(JWT)
                    .roles(user.getRoles().stream()
                            .map(UserRole::getRole)
                            .collect(Collectors.toList()))
                    .build();
        } else {
            log.debug("Incorrect password given for user id={}, username={}", user.getId(), user.getUsername());
            throw new BadPasswordException(userLoginModel.getUsername());
        }
    }

    @Transactional
    public void delete(UserDeleteModel userDeleteModel) {
        User userFromCredentials = userRepository
                .findUserByUsername(userDeleteModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userDeleteModel.getUsername()));

        User actualUser = UserContextUtils.getActualUser();

        if (Objects.equals(userFromCredentials, actualUser) &&
                UserActualPasswordValidator.validate(userDeleteModel.getPassword(), actualUser.getPassword())) {
            userFromCredentials.getRoles()
                    .forEach(userRoleRepository::delete);
            userRepository.delete(userFromCredentials);
        } else {
            throw (new BadPasswordException(userDeleteModel.getUsername()));
        }
    }

    @Transactional
    public void updateUserPassword(UserChangePasswordModel userChangePasswordModel) {
        User userFromCredentials = userRepository
                .findUserByUsername(userChangePasswordModel.getUsername())
                .orElseThrow(() -> new UserNotFoundException(userChangePasswordModel.getUsername()));

        User actualUser = UserContextUtils.getActualUser();

        if (Objects.equals(userFromCredentials, actualUser) &&
                UserActualPasswordValidator.validate(userChangePasswordModel.getPassword(), userFromCredentials.getPassword())) {

            if (!UserNewPasswordValidator.validate(userChangePasswordModel.getNewPassword())) {
                throw new PasswordTooWeak();
            }

            userFromCredentials.setPassword(PasswordUtils.getNewPasswordHash(userChangePasswordModel.getNewPassword()));
            userRepository.save(userFromCredentials);
        } else {
            throw new BadPasswordException(userChangePasswordModel.getUsername());
        }
    }

    public String getNewActivationLinkString(String username) {
        UserActivationLink userActivationLink = userActivationLinkEntityGenerator.getNewActivationLinkForUser(username);
        log.debug("New activation link generated with [id={}] for user with [id={}]", userActivationLink.getId(), userActivationLink.getUser().getId());
        return this.SERVER_ADDRESS +
                ":" +
                this.SERVER_PORT +
                LoginConfiguration.LOGIN_CONTROLLER_PATH +
                LoginConfiguration.ACTIVATION_PATH +
                "/" +
                userActivationLink.getActivationLink();
    }

    public String getNewResetPasswordLink(String username) {
        UserResetPasswordLink userActivationLink = userResetPasswordLinkEntityGenerator.getResetPasswordLink(username);
        log.debug("New reset password link generated with [id={}] for user with [id={}]", userActivationLink.getId(), userActivationLink.getUser().getId());
        return this.SERVER_ADDRESS +
                ":" +
                this.SERVER_PORT +
                LoginConfiguration.LOGIN_CONTROLLER_PATH +
                LoginConfiguration.SEND_RESET_LINK_PATH +
                "/" +
                userActivationLink.getResetPasswordLink();
    }

    public String getUserEmail(String username) {
        User user = userRepository.findUserByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        return user.getEmail();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return new MyUserDetails(
                userRepository.findUserByUsername(s)
                        .orElseThrow(() -> new UserNotFoundException(s)));
    }
}
