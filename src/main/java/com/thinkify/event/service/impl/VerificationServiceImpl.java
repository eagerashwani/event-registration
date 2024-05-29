package com.thinkify.event.service.impl;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Optional;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thinkify.event.dto.ChangePasswordDto;
import com.thinkify.event.dto.VerificationDTO;
import com.thinkify.event.email.EmailSenderService;
import com.thinkify.event.entity.UserEntity;
import com.thinkify.event.entity.Verification;
import com.thinkify.event.jwt.JwtUtils;
import com.thinkify.event.repository.UserRepository;
import com.thinkify.event.repository.VerificationRepo;
import com.thinkify.event.service.VerificationService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class VerificationServiceImpl implements VerificationService{

    private VerificationRepo verificationRepo;
    private UserRepository userRepository;
    private ModelMapper mapper;
    private EmailSenderService emailSenderService;
    private JwtUtils jwtUtils;
    private PasswordEncoder passwordEncoder;

    public void saveData(String email, Long id, String uid) {
        Integer otp = otpGenrator();
        Verification verification = Verification.builder()
                                        .email(email)
                                        .userId(id)
                                        .userUid(uid)
                                        .otp(otp)
                                        .revisions(0)
                                        .build();
        verificationRepo.save(verification);
        String subject = "Saviour Email Verification";
        String content = "<p>Hello,</p><p>Saviour OTP verification " + otp + "</p>";
        emailSenderService.sendOtpMail(email, subject, content);
        
    }

    private Verification dtoToEntity(VerificationDTO verificationDTO) {
        return mapper.map(verificationDTO, Verification.class);
    }

    private Integer otpGenrator(){
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000);
        return randomNumber;
    }

    @Override
    public String getOtp(String email, Integer otp) {
        Verification verified = verificationRepo.findByEmail(email).orElseThrow(()-> new RuntimeException("OTP not found with this mail " + email));
        Timestamp expireAt = verified.getOtpExpiredAt();
     
        LocalDateTime currentDateTime = LocalDateTime.now();
        LocalDateTime currentExpity = convertTimestampToLocalDateTime(expireAt);
        if(currentDateTime.isBefore(currentExpity)){
            if(verified.getOtp().equals(otp)){
                Long id = verified.getUserId();
                UserEntity user = userRepository.findById(id).orElseThrow(()-> new UsernameNotFoundException("user not found in getOtp()"));
                user.setEnabled(true);
                userRepository.save(user);
                return "Verified";
            } else {
                return "Incorrect OTP";
            }
        }
        return "OTP verification time expires";
    }

   private LocalDateTime convertTimestampToLocalDateTime(Timestamp timestamp) {
        Instant instant = timestamp.toInstant();
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    @Override
    public String changePassword(HttpServletRequest request, ChangePasswordDto changePasswordDto) {
        String token = jwtUtils.getJwtFromHeader(request);
        Long userId = jwtUtils.extractClaim(token, claims -> claims.get("uid", Long.class));
        String password = changePasswordDto.getPassword();
        log.info(password);
        String confirmPassword = changePasswordDto.getConfirmPassword();
        log.info(confirmPassword);

       
        
        if(password.equals(confirmPassword)){
            UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User not found changePassword()"));
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return "Password Changed";
        }
        return "Mismatch password";  
    }

    
}
