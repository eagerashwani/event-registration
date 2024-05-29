package com.thinkify.event.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "verification")
@Builder
public class Verification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String userUid;
    private String email;
    private Integer otp;
    private String link;
    @CreationTimestamp
    private Timestamp otpCreatedAt;
    private Timestamp otpExpiredAt;
    @CreationTimestamp
    private Timestamp linkCreatedAt;
    private Timestamp linkExpiredAt;
    private Integer revisions;


    @PrePersist
    protected void onCreate() {
        if (this.otpCreatedAt == null) {
            this.otpCreatedAt = Timestamp.valueOf(LocalDateTime.now());
        }
        this.otpExpiredAt = Timestamp.valueOf(this.otpCreatedAt.toLocalDateTime().plusMinutes(5));
    }
}
