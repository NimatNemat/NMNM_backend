package com.nimatnemat.nine.domain.like;

import com.nimatnemat.nine.domain.restaurant.Restaurant;
import com.nimatnemat.nine.domain.restaurant.RestaurantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;
    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/like")
    @Operation(summary = "좋아요 API", description = "좋아요를 POST합니다.")
    public ResponseEntity<?> likeRestaurant(Authentication authentication, @RequestParam Long restaurantId) {
        try {
            likeService.likeRestaurant(authentication.getName(), restaurantId);
            return new ResponseEntity<>(authentication.getName() +"님의 좋아요가 수행되었습니다.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/unlike")
    @Operation(summary = "싫어요 API", description = "좋아요를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "수행 성공"),
            @ApiResponse(responseCode = "403", description = "로그인을 먼저 해주세요")
    })
    public ResponseEntity<?> unlikeRestaurant(Authentication authentication, @RequestParam Long restaurantId) {
        try {
            likeService.unlikeRestaurant(authentication.getName(), restaurantId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("/restaurant/{restaurantId}")
//    @Operation(summary = "레스토랑 좋아요 수 API", description = "레스토랑별 좋아요 수를 보여줍니다")
//    public ResponseEntity<Long> getLikesForRestaurant(@PathVariable Long restaurantId) {
//        long likeCount = likeService.getLikesForRestaurant(restaurantId);
//        return new ResponseEntity<>(likeCount, HttpStatus.OK);
//    }
    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자가 좋아요한 가게 API", description = "해당 사용자가 좋아요한 식당들을 보여줍니다.")
    public ResponseEntity<List<Long>> getLikedRestaurantsForUser(Authentication authentication) {
        List<Long> restaurantIds = likeService.getLikedRestaurantIdsForUser(authentication.getName());
        return new ResponseEntity<>(restaurantIds, HttpStatus.OK);
    }
}
