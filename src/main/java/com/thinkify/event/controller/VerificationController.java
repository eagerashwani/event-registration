package com.thinkify.event.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinkify.event.dto.ChangePasswordDto;
import com.thinkify.event.dto.VerificationDTO;
import com.thinkify.event.service.VerificationService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/verify")
public class VerificationController {

    @Autowired
    VerificationService verificationService;
    
    @PostMapping("/otp")
    public String emailVerifyViaOtp(@RequestBody VerificationDTO verificationDTO) {    
        return verificationService.getOtp(verificationDTO.getEmail(), verificationDTO.getOtp());
    }

    @PostMapping("/change-password")
    public String changePassword(HttpServletRequest request, @RequestBody ChangePasswordDto changePasswordDto) {   
        return verificationService.changePassword(request, changePasswordDto);
    }
    
}
