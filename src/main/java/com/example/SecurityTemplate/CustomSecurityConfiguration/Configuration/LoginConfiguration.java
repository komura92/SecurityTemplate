package com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration;

import com.example.SecurityTemplate.CustomSecurityConfiguration.EmailSender.ActivationLinkSender;
import com.example.SecurityTemplate.CustomSecurityConfiguration.EmailSender.ResetPasswordLinkSender;
import com.example.SecurityTemplate.CustomSecurityConfiguration.EmailSender.Sender;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Model.Role;
import org.springframework.beans.factory.annotation.Value;

import java.util.Arrays;
import java.util.List;

public class LoginConfiguration {

    @Value("server.address")
    public static String SERVER_ADDRESS;

    @Value("server.port")
    public static String SERVER_PORT;

    //BASIC TABLES NAMES
    public static final String USER_TABLE_NAME = "user";
    public static final String USER_ROLE_TABLE_NAME = "user_role";
    public static final String RESET_LINK_TABLE_NAME = "user_reset_password_link";
    public static final String ACTIVATION_LINK_TABLE_NAME = "user_activation_link";

    //LOGIN CONTROLLER CONFIGURATION
    public static final String LOGIN_CONTROLLER_PATH = "/login";
    public static final String ACTIVATION_PATH = "/activate";
    public static final String REGISTER_PATH = "/register";
    public static final String AUTHORIZATION_PATH = "/a";
    public static final String DELETE_ACCOUNT_PATH = "/d";
    public static final String CHANGE_PASSWORD_PATH = "/up";
    public static final String LOGOUT_PATH = "/lout";
    public static final String RESET_PASSWORD_PATH = "/irp";
    public static final String SEND_RESET_LINK_PATH = "/srl";

    //SENDERS CONFIGURATION
    public static final Sender activationLinkSender = new ActivationLinkSender();
    public static final Sender resetPasswordLinkSender = new ResetPasswordLinkSender();

    //PASSWORD REQUIREMENTS CONFIGURATION
    public static final boolean PASSWORD_CONTAINS_UPPERCASE = true;
    public static final boolean PASSWORD_CONTAINS_LOWERCASE = true;
    public static final boolean PASSWORD_CONTAINS_DIGIT = true;
    public static final boolean PASSWORD_CONTAINS_SPECIAL_CHARACTER = true;
    public static final List<Character> SPECIAL_CHARACTERS = Arrays.asList('$', '#', '@', '%', '&', '*', '!', '?');

    //USERNAME CONFIGURATION
    public static final boolean USERNAME_CAN_CONTAINS_UPPER_CASES = true;
    public static final boolean USERNAME_CAN_CONTAINS_LOWER_CASES = true;
    public static final boolean USERNAME_CAN_CONTAINS_DIGITS = true;
    public static final boolean USERNAME_CAN_CONTAINS_SPECIAL_CHARACTERS = false;

    //FUNCTIONAL CONFIGURATION
    public static final boolean REQUIRED_ACTIVATION = true;
    public static final List<Role> DEFAULT_NEW_USER_ROLES = Arrays.asList(Role.USER);
    public static final int RESET_PASSWORD_LINK_LENGTH = 64;
    public static final int PASSWORD_HASH_STRENGTH = 16;
    public static final int ACTIVATION_LINK_LENGTH = 32;
    public static final int ACTIVATION_LINK_EXPIRED_DAYS = 30;
    public static final int ACTIVATION_LINK_EXPIRED_HOURS = 0;
    public static final int ACTIVATION_LINK_EXPIRED_MINUTES = 0;
    public static final int RESET_PASSWORD_LINK_EXPIRED_DAYS = 2;
    public static final int RESET_PASSWORD_LINK_EXPIRED_HOURS = 0;
    public static final int RESET_PASSWORD_EXPIRED_MINUTES = 0;
}
