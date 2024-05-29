package com.thinkify.event.service;

import com.thinkify.event.dto.ChangePasswordDto;
import com.thinkify.event.dto.VerificationDTO;

import jakarta.servlet.http.HttpServletRequest;

public interface VerificationService {
    public void saveData(String email, Long id, String uid);
    public String getOtp(String email, Integer otp);
    public String changePassword(HttpServletRequest request, ChangePasswordDto changePasswordDto);
}
