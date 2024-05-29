package com.thinkify.event.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.thinkify.event.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long>{

    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByEmail(String email);
    Optional<UserEntity> findByUsernameOrEmail(String username, String email);

}
