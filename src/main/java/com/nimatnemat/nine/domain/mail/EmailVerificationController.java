package com.nimatnemat.nine.domain.mail;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    @PostMapping("/api/mail/send")
    @Operation(summary = "이메일 인증 코드 발송 API", description = "이메일 인증 코드를 발송합니다.")
    public ResponseEntity<Void> sendMailConfirm(@RequestParam String identifier) throws Exception {
        emailVerificationService.sendSimpleMessage(identifier);
        log.info("인증코드가 발송되었습니다.");
        return ResponseEntity.ok().build();
    }

    @PostMapping("/api/mail/verify")
    @Operation(summary = "이메일 인증 API", description = "이메일 인증을 수행합니다.")
    public ResponseEntity<Boolean> verifyEmail(@RequestParam String identifier, @RequestParam String code) {
        boolean isVerified = emailVerificationService.verifyEmail(identifier, code);
        log.info("이메일 인증 결과: " + (isVerified ? "성공" : "실패"));
        return ResponseEntity.ok(isVerified);
    }
}
