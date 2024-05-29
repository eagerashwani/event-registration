package com.thinkify.event.dto;

import java.util.List;
import java.util.Set;

import com.thinkify.event.constants.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    private String jwtToken;
    private String email;
    private Set<Role> roles;
}
