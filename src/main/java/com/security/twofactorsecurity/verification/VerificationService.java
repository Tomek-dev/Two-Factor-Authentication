package com.security.twofactorsecurity.verification;

import com.security.twofactorsecurity.dao.UserDao;
import com.security.twofactorsecurity.enums.Role;
import com.security.twofactorsecurity.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class VerificationService {

    private UserDao userDao;

    @Autowired
    public VerificationService(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean verify(Authentication authentication, String phoneCode){
        String code = retrieveCodeFromApi("");
        if(code.equals(phoneCode)){
            updateAuthentication(authentication.getName());
            return true;
        }
        return false;
    }

    public void allowVerification(Authentication authentication){
        Authentication allowAuthentication = new UsernamePasswordAuthenticationToken(
                authentication.getName(), null, Collections.singleton(Role.PRE_VERIFICATION));
        SecurityContextHolder.getContext().setAuthentication(allowAuthentication);
    }

    private String retrieveCodeFromApi(String username){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> exchange = restTemplate.exchange(
                "",
                HttpMethod.GET,
                HttpEntity.EMPTY,
                String.class);
        return exchange.getBody();
    }

    private void updateAuthentication(String username){
        Optional<User> optionalUser = userDao.findByUsername(username);
        User user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getRoles());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
