package com.thinkify.event.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thinkify.event.entity.Verification;

public interface VerificationRepo extends JpaRepository<Verification, Long>{
    Optional<Verification> findByEmail(String email);
}
