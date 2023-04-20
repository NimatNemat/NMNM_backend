package com.nimatnemat.nine.domain.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.jwt.expiration}")
    private int jwtExpirationInMs;

//    private SecretKey secretKey;

    private final UserDetailsService userDetailsService;
    public JwtTokenProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

//    @PostConstruct
//    public void init() {
//        secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
//    }

//    public String createToken(String userEmail, List<String> roleList) {
//        Claims claims = Jwts.claims().setSubject(userEmail); // JWT payload 에 저장되는 정보단위
//        claims.put("roles", roleList); // 정보는 key / value 쌍으로 저장된다.
//        Date now = new Date();
//        return Jwts.builder()
//                .setClaims(claims) // 정보 저장
//                .setIssuedAt(now) // 토큰 발행 시간 정보
//                .setExpiration(new Date(now.getTime() + expireTime)) // set Expire Time
//                .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
//                // signature 에 들어갈 secret값 세팅
//                .compact();
//    }
public String createToken(String userId) {
    Claims claims = Jwts.claims(); // JWT payload 에 저장되는 정보단위
        claims.put("userId", userId); // 정보는 key / value 쌍으로 저장된다.
        Date now = new Date(); // 오늘 날짜
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // 헤더 설정
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발행 시간 정보
                .setExpiration(new Date(now.getTime() + jwtExpirationInMs)) // set Expire Time
                .signWith(SignatureAlgorithm.HS256, jwtSecret)  // 사용할 암호화 알고리즘과
                // signature 에 들어갈 secret값 세팅
                .compact();
}

    // JWT 토큰에서 인증 정보 조회
//    public Authentication getAuthentication(String token) {
//        String email = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
//        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }
    public Authentication getAuthentication(String token) {
        String userId = Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token).getBody().getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // Request의 Header에서 token 값을 가져옵니다. "X-AUTH-TOKEN" : "TOKEN값'
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("X-AUTH-TOKEN");
    }

//    // 토큰의 유효성 + 만료일자 확인
//    public boolean validateToken(String jwtToken) {
//        try {
//            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
//            return claims.getBody().getExpiration().before(new Date()) == false;
//        } catch (Exception e) {
//            return false;
//        }
//    }
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(jwtSecret).build().parseClaimsJws(token);
            return true;
        } catch (Exception ex) {
            log.error("Error validating token: {}", ex.getMessage());
        }
        return false;
    }

}

//    public String generateToken(String userEmail) {
//        Claims claims = Jwts.claims().setSubject(userEmail);
////        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//        Date now = new Date();
////        Date expiryDate = new Date(now.getTime() + jwtExpirationInMs);
//    return Jwts.builder()
//            .setSubject(claims.getSubject())
//            .setIssuedAt(now)
//            .setExpiration(new Date(now.getTime() + jwtExpirationInMs))
//            .signWith(secretKey, SignatureAlgorithm.HS512)
//            .compact();
//    }