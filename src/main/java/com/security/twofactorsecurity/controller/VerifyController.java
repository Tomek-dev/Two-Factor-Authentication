package com.security.twofactorsecurity.controller;

import com.security.twofactorsecurity.verification.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class VerifyController {

    private VerificationService authenticationService;

    @Autowired
    public VerifyController(VerificationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/verify")
    @ResponseBody
    public String getVerify(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "verify";
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String code, Authentication authentication){
        if(authenticationService.verify(authentication, code)){
            return "redirect:/";
        }
        return "redirect:/verify?error";
    }
}
