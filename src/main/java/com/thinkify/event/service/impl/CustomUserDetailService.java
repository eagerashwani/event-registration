package com.thinkify.event.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.thinkify.event.entity.UserEntity;
import com.thinkify.event.repository.UserRepository;
import com.thinkify.event.service.UserService;
import com.thinkify.event.service.VerificationService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomUserDetailService implements UserDetailsService, UserService{

    @Autowired
    private UserRepository userRepo;

    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Lazy
    @Autowired
    private VerificationServiceImpl verificationService;

    @Override
    public String userSave(UserEntity user) {
        Optional<UserEntity> isUserPresent = userRepo.findByEmail(user.getEmail());
        if(isUserPresent.isEmpty()){
            String provider = user.getProvider();
         
            if(provider != null){ // OAuth k liye
                user.setUsername(user.getEmail());
                log.info("Email is {}",user.getEmail());
                user.setPassword(passwordEncoder.encode(user.getEmail()));
            } else {
                
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            
            user.setRoles(user.getRoles());
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setEnabled(false);
            UserEntity savedUser = userRepo.save(user);
            Long id = savedUser.getId();
            String uid = savedUser.getUid();
            verificationService.saveData(user.getEmail(), id, uid);
            return "Saved";
        }
        return "Email or Username is already present";
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
          UserEntity user = userRepo.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    if (!user.isEnabled()) {
        throw new DisabledException("User account is disabled");
    }

    List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
    user.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.name()))));

    return new User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, authorityList);

    }
    
}
