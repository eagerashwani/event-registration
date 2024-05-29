package com.thinkify.event.jwt;


import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.thinkify.event.entity.UserEntity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class JwtUtils {
    
    @Value("${spring.app.jwtSecret}")
    private String jwtSecret;

    @Value("${spring.app.jwtExpirationInMS}")
    private int jwtExpirationInMS;

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        log.info("Authorization Token: {}",bearerToken);
        if(bearerToken != null && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return null;
    }

    public String generateTokenFromUsername(UserDetails userDetails){
        String username = userDetails.getUsername();
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        List<String> authoritiesList = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return Jwts.builder()
                .subject(username)
                .claim("roles", authoritiesList)
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime() + jwtExpirationInMS))
                .signWith(key())
                .compact();
    }

    public String getUsernameFromJwtToken(String token){
        return Jwts.parser().verifyWith((SecretKey)key()).build().parseSignedClaims(token)
                .getPayload().getSubject();
    }

 

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .verifyWith((SecretKey)key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Collection<? extends GrantedAuthority> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role))
                .collect(Collectors.toList());
    }

    
    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public boolean validateJwtToken(String authToken){
        try {
            log.info("Validating... {}",authToken);
            Jwts.parser().verifyWith((SecretKey) key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid Jwt Token: {}", e.getMessage());
        }
         catch (ExpiredJwtException e) {
            log.error("Jwt is expired {}", e.getMessage());
        }
         catch (UnsupportedJwtException e) {
            log.error("Jwt is unsupported {}", e.getMessage());
        }
         catch (IllegalArgumentException e) {
            log.error("Jwt claim string is empty {}", e.getMessage());
        }
        return false;
    }

}

