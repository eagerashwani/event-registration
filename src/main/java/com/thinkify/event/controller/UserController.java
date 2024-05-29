package com.thinkify.event.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.thinkify.event.constants.Role;
import com.thinkify.event.dto.LoginRequest;
import com.thinkify.event.dto.LoginResponse;
import com.thinkify.event.entity.UserEntity;
import com.thinkify.event.jwt.JwtUtils;
import com.thinkify.event.service.UserService;
import com.thinkify.event.service.impl.CustomUserDetailService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
public class UserController {

    private CustomUserDetailService customUserDetailService;
    private UserService userService;
    private AuthenticationManager manager;
    AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;

    public UserController(CustomUserDetailService customUserDetailService, UserService userService,
            AuthenticationManager manager, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.customUserDetailService = customUserDetailService;
        this.userService = userService;
        this.manager = manager;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        this.doAuthenticate(request.getEmail(), request.getPassword());

        
        UserDetails userDetails = customUserDetailService.loadUserByUsername(request.getEmail());
        String token = jwtUtils.generateTokenFromUsername(userDetails);
        
        LoginResponse response = LoginResponse.builder()
                .jwtToken(token)
                // .roles(roles) 
                .email(request.getEmail()).build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    

    @PostMapping("/signup")
    public String postMethodName(@RequestBody UserEntity user) {
        return customUserDetailService.userSave(user);
    }

   
    
    
    private void doAuthenticate(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
            
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Credentials Invalid !!");
        }
    }
}
