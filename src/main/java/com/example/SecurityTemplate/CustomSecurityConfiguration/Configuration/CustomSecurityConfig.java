package com.example.SecurityTemplate.CustomSecurityConfiguration.Configuration;

import com.example.SecurityTemplate.CustomSecurityConfiguration.Model.Role;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Token.BearerConfig;
import com.example.SecurityTemplate.CustomSecurityConfiguration.Token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    private final TokenProvider tokenProvider;

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
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .and()
                .csrf().disable()
                .cors()
                .and()
                .formLogin().disable()
                .httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().permitAll()
                .and()
                .apply(new BearerConfig(tokenProvider));
    }
}
