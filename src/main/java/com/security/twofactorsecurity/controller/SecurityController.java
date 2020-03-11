package com.security.twofactorsecurity.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecurityController {

    @GetMapping("/public")
    public String getPublicEndPoint() {
        return "public";
    }

    @GetMapping("/secured")
    public String getSecuredEndPoint(){
        return "secured";
    }
}
