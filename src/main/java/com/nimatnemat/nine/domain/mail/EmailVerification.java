package com.nimatnemat.nine.domain.mail;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "email_verification_table")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmailVerification {

    @Id
    private String id;

    @Indexed(unique = true)
    private String email;

    private String verificationCode;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private boolean verified;

    public EmailVerification(String email, String verificationCode, LocalDateTime createdAt, LocalDateTime expiresAt) {
        this.email = email;
        this.verificationCode = verificationCode;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.verified = false;
    }
}

