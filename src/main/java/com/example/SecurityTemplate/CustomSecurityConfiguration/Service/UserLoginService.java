package com.example.SecurityTemplate.CustomSecurityConfiguration.Service;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration.LoginConfiguration;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.User;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.UserActivationLink;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.UserResetPasswordLink;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Entity.UserRole;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Exception.*;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Generator.UserActivationLinkEntityGenerator;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Generator.UserResetPasswordLinkEntityGenerator;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Model.*;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Repository.UserRepository;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Repository.UserRoleRepository;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Token.TokenProvider;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Utils.PasswordUtils;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Utils.UserContextUtils;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Validator.UserActualPasswordValidator;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Validator.UserEmailValidator;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Validator.UserNewPasswordValidator;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Validator.UsernameValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder encoder;

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

    public String login(UserLoginModel userLoginModel) {
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

            return tokenProvider.createToken(userLoginModel.getUsername());
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
        return LoginConfiguration.SERVER_ADDRESS +
                ":" +
                LoginConfiguration.SERVER_PORT +
                LoginConfiguration.LOGIN_CONTROLLER_PATH +
                LoginConfiguration.ACTIVATION_PATH +
                "/" +
                userActivationLink.getActivationLink();
    }

    public String getNewResetPasswordLink(String username) {
        UserResetPasswordLink userActivationLink = userResetPasswordLinkEntityGenerator.getResetPasswordLink(username);
        log.debug("New reset password link generated with [id={}] for user with [id={}]", userActivationLink.getId(), userActivationLink.getUser().getId());
        return LoginConfiguration.SERVER_ADDRESS +
                ":" +
                LoginConfiguration.SERVER_PORT +
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
