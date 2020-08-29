package com.example.SimpleSecurity.SimpleSecurity.Configuration;

import com.example.SimpleSecurity.SimpleSecurity.Token.BearerConfig;
import com.example.SimpleSecurity.SimpleSecurity.Token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;

    @Bean
    public LogoutSuccessHandler logoutSuccessHandler() {
        return new CustomLogoutSuccessHandler();
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler(){
        return new CustomLoginSuccessHandler();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .logout()
                .logoutUrl(LoginConfiguration.LOGIN_CONTROLLER_PATH + LoginConfiguration.LOGOUT_PATH)
                .invalidateHttpSession(true)
                .logoutSuccessHandler(logoutSuccessHandler())
                .deleteCookies("JSESSIONID")
                .and()
                .csrf().disable()
                .cors()
                .and()
                .formLogin()
                .successHandler(loginSuccessHandler())
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .apply(new BearerConfig(tokenProvider));
    }
}
