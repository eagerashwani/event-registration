package com.thinkify.event.email;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    
    private JavaMailSender mailSender;

    public EmailSenderService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }


    public void sendOtpMail(String email, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("eagerashwani@gmail.com");
        message.setTo(email);

        message.setSubject(subject);
        message.setText(content);

        mailSender.send(message);
    }
}
