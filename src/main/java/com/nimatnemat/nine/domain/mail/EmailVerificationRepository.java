package com.nimatnemat.nine.domain.mail;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailVerificationRepository extends MongoRepository<EmailVerification, String> {
    Optional<EmailVerification> findFirstByEmailOrderByCreatedAtDesc(String email);

    Optional<EmailVerification> findFirstByEmailAndUserIdOrderByCreatedAtDesc(String email, String userId);
}