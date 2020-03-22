package com.security.twofactorsecurity.security;

import com.security.twofactorsecurity.dao.UserDao;
import com.security.twofactorsecurity.model.User;
import com.security.twofactorsecurity.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Optional;

@Component
public class VerificationHandler implements AuthenticationSuccessHandler {

    private static final String VERIFICATION_URL = "/verify";
    private static final String INDEX_URL = "/";
    private static final String COOKIE_NAME = "Authorization";

    private VerificationService verificationService;
    private UserDao userDao;

    @Autowired
    public VerificationHandler(@Qualifier("GoogleAuthenticatorService") VerificationService verificationService, UserDao userDao) {
        this.verificationService = verificationService;
        this.userDao = userDao;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        Optional<User> userOptional = userDao.findByUsername(authentication.getName());
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if(user.getUsing2FA()){
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
            String value = Base64.getEncoder().encodeToString(user.getUsername().getBytes());
            Cookie cookie = new Cookie(COOKIE_NAME, value);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(5 * 60);
            httpServletResponse.addCookie(cookie);
            new DefaultRedirectStrategy().sendRedirect(httpServletRequest, httpServletResponse, VERIFICATION_URL);
        }
        else{
            new DefaultRedirectStrategy().sendRedirect(httpServletRequest, httpServletResponse, INDEX_URL);
        }
    }
}
