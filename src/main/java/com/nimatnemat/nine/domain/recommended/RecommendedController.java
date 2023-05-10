package com.nimatnemat.nine.domain.recommended;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/recommended")
public class RecommendedController {

        @Autowired
        private RecommendedService recommendedService;

    @GetMapping("/{userId}/first")
    @Operation(summary = "사용자별 첫 번째 추천 레스토랑 조회 API", description = "사용자 ID에 따라 첫 번째 추천 레스토랑 목록을 반환합니다.")
    public ResponseEntity<List<String>> getFirstRecommendationsByUserId(@PathVariable String userId) {
        Optional<List<Recommended>> recommendations = recommendedService.findByUserId(userId);

        if (recommendations.isPresent() && !recommendations.get().isEmpty()) {
            List<String> firstRecommend = recommendations.get().get(0).getFirstRecommend();
            return new ResponseEntity<>(firstRecommend, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }
    @GetMapping("/second")
    @Operation(summary = "사용자별 두 번째 추천 레스토랑 조회 API", description = "사용자 ID에 따라 두 번째 추천 레스토랑 목록을 반환합니다.")
    public ResponseEntity<List<String>> getSecondRecommendationsByUserId(Authentication authentication) {
        Optional<List<Recommended>> recommendations = recommendedService.findByUserId(authentication.getName());

        if (recommendations.isPresent() && !recommendations.get().isEmpty()) {
            List<String> secondRecommend = recommendations.get().get(0).getSecondRecommend();
            return new ResponseEntity<>(secondRecommend, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
    }
}