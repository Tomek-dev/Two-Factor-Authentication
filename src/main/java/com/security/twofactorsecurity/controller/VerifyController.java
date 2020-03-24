package com.security.twofactorsecurity.controller;

import com.security.twofactorsecurity.exceptions.SecretKeyAlreadyExistException;
import com.security.twofactorsecurity.service.GoogleAuthenticatorService;
import com.security.twofactorsecurity.service.VerificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
    @PreAuthorize("hasRole('ROLE_PRE_VERIFICATION')")
    public String getVerify(){
        return "verify";
    }

    @PostMapping("/verify")
    @PreAuthorize("hasRole('ROLE_PRE_VERIFICATION')")
    public String verify(@RequestParam String code, Authentication authentication){
        if(verificationService.verify(authentication, code)){
            return "redirect:/";
        }
        return "redirect:/verify?error";
    }


    @GetMapping("/generate")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String generate(Authentication authentication, Model model){
        try {
            String code = verificationService.generateKey(authentication.getName());
            model.addAttribute("code", code);
            return "generate";
        } catch (SecretKeyAlreadyExistException e) {
            return "redirect:/verify";
        }
    }
}
