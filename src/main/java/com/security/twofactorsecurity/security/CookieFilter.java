package com.security.twofactorsecurity.security;

import com.security.twofactorsecurity.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;

public class CookieFilter extends BasicAuthenticationFilter {

    private static final String COOKIE_NAME = "Authorization";

    public CookieFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if(cookie.getName().equals(COOKIE_NAME)){
                UsernamePasswordAuthenticationToken token = getAuthenticationToken(cookie);
                SecurityContextHolder.getContext().setAuthentication(token);
                chain.doFilter(request, response);
                return;
            }
        }
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthenticationToken(Cookie cookie) {
        byte[] bytes = Base64.getDecoder().decode(cookie.getValue());
        String username = new String(bytes);
        Collection<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(Role.PRE_VERIFICATION.getAuthority()));
        return new UsernamePasswordAuthenticationToken(username, null, authorities);
    }
}
