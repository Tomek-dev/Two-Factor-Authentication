package com.security.twofactorsecurity.config;

import com.security.twofactorsecurity.security.JwtFiler;
import com.security.twofactorsecurity.service.UserDetailsServiceImpl;
import com.security.twofactorsecurity.security.VerificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Order(1)
    @Configuration
    class BasicAuthConfiguration extends WebSecurityConfigurerAdapter{
        private UserDetailsServiceImpl userDetailsService;
        private VerificationHandler verificationHandler;

        @Autowired
        public BasicAuthConfiguration(UserDetailsServiceImpl userDetailsService, VerificationHandler verificationHandler) {
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
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers("/public").permitAll()
                    .antMatchers("/secured").hasRole("USER")
                    .antMatchers("/authenticated").authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll()
                    .successHandler(verificationHandler)
                    .and()
                    .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"));
        }
    }

    @Configuration
    @Order(2)
    class JWTAuthConfiguration extends WebSecurityConfigurerAdapter{

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeRequests()
                    .antMatchers("/verify").hasRole("PRE_VERIFICATION")
                    .and()
                    .addFilter(new JwtFiler(authenticationManager()));
        }
    }

}
