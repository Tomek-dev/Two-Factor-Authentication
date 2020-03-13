package com.security.twofactorsecurity.config;

import com.security.twofactorsecurity.service.UserDetailsServiceImpl;
import com.security.twofactorsecurity.security.VerificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsServiceImpl userDetailsService;
    private VerificationHandler verificationHandler;

    @Autowired
    public WebSecurityConfig(UserDetailsServiceImpl userDetailsService, VerificationHandler verificationHandler) {
        this.userDetailsService = userDetailsService;
        this.verificationHandler = verificationHandler;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/secured").hasRole("USER")
                .antMatchers("/verify").hasRole("PRE_VERIFICATION")
                .antMatchers("/authenticated").authenticated()
                .and()
                .formLogin().permitAll()
                .successHandler(verificationHandler);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
