package com.security.twofactorsecurity.controller;

import com.security.twofactorsecurity.service.GoogleAuthenticatorService;
import com.security.twofactorsecurity.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VerifyController {

    private VerificationService verificationService;

    @Autowired
    public VerifyController(@Qualifier("GoogleAuthenticatorService") VerificationService verificationService) {
        this.verificationService = verificationService;
    }

    @GetMapping("/verify")
    @ResponseBody
    public String getVerify(){
        return "verify";
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String code, Authentication authentication){
        if(verificationService.verify(authentication, code)){
            return "redirect:/";
        }
        return "redirect:/verify?error";
    }
}
