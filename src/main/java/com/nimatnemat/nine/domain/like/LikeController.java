package com.nimatnemat.nine.domain.like;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/likes")
public class LikeController {
    @Autowired
    private LikeService likeService;

    @PostMapping("/like")
    public ResponseEntity<?> likeRestaurant(@RequestParam String userId, @RequestParam Long restaurantId) {
        try {
            likeService.likeRestaurant(userId, restaurantId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/unlike")
    public ResponseEntity<?> unlikeRestaurant(@RequestParam String userId, @RequestParam Long restaurantId) {
        try {
            likeService.unlikeRestaurant(userId, restaurantId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<Long> getLikesForRestaurant(@PathVariable Long restaurantId) {
        long likeCount = likeService.getLikesForRestaurant(restaurantId);
        return new ResponseEntity<>(likeCount, HttpStatus.OK);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Long>> getLikedRestaurantsForUser(@PathVariable String userId) {
        List<Long> restaurantIds = likeService.getLikedRestaurantIdsForUser(userId);
        return new ResponseEntity<>(restaurantIds, HttpStatus.OK);
    }

}
