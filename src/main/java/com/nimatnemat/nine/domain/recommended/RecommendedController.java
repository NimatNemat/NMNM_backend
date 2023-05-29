package com.nimatnemat.nine.domain.recommended;

import com.nimatnemat.nine.domain.group.Group;
import com.nimatnemat.nine.domain.group.GroupRepository;
import com.nimatnemat.nine.domain.restaurant.Restaurant;
import com.nimatnemat.nine.domain.restaurant.RestaurantService;
import com.nimatnemat.nine.domain.user.User;
import com.nimatnemat.nine.domain.user.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/recommended")
public class RecommendedController {
    @Autowired
    private  RestaurantService restaurantService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;


    @GetMapping("/first")
    @Operation(summary = "사용자별 첫 번째 추천 레스토랑 조회 API", description = "사용자 ID에 따라 첫 번째 추천 레스토랑 목록을 반환합니다.")
    public ResponseEntity<List<Restaurant>> getFirstRecommendationsByUserId(Authentication authentication) {
        try {
            Optional<User> optionalUser = userRepository.findByUserId(authentication.getName());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                Optional<Group> optionalGroup = groupRepository.findByGroupName(user.getGroupName());
                if (optionalGroup.isPresent()) {
                    Group group = optionalGroup.get();
                    List<Long> firstRecommendIds = group.getFirstRecommend();
                    List<Restaurant> firstRecommendRestaurants = restaurantService.getRestaurantsByRestaurantIds(firstRecommendIds);

                    firstRecommendRestaurants.forEach(restaurant -> {
                        if (restaurant.getImageFile() != null) {
                            String imageUrl = String.format("/images/%s", restaurant.getImageFile().toHexString());
                            restaurant.setImageUrl(imageUrl);
                        } else {
                            restaurant.setImageUrl(null);
                        }
                    });

                    return new ResponseEntity<>(firstRecommendRestaurants, HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/second")
    @Operation(summary = "사용자별 두 번째 추천 레스토랑 조회 API", description = "사용자 ID에 따라 두 번째 추천 레스토랑 목록을 반환합니다.")
    public ResponseEntity<List<Restaurant>> getSecondRecommendationsByUserId(Authentication authentication) {
        try {
            Optional<User> optionalUser = userRepository.findByUserId(authentication.getName());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                List<Long> secondRecommendIds = user.getSecondRecommend();
                List<Restaurant> secondRecommendRestaurants = restaurantService.getRestaurantsByRestaurantIds(secondRecommendIds);

                secondRecommendRestaurants.forEach(restaurant -> {
                    if (restaurant.getImageFile() != null) {
                        String imageUrl = String.format("/images/%s", restaurant.getImageFile().toHexString());
                        restaurant.setImageUrl(imageUrl);
                    } else {
                        restaurant.setImageUrl(null);
                    }
                });

                return new ResponseEntity<>(secondRecommendRestaurants, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/groupChoice")
    @Operation(summary = "그룹 별 상위 10개 음식점 정보 제공 API", description = "회원가입 절차 중 그룹 선택 시 보여질 그룹 별 음식점 정보를 제공합니다")
    public ResponseEntity<List<Restaurant>> getGroupChoiceList() {
        try {
            List<Group> groups = groupRepository.findAll();
            List<Restaurant> groupChoiceRestaurants = new ArrayList<>();

            for (Group group : groups) {
                List<Long> firstRecommendIds = group.getFirstRecommend();
                List<Restaurant> groupRestaurants = restaurantService.getRestaurantsByRestaurantIds(firstRecommendIds.subList(0, Math.min(firstRecommendIds.size(), 10)));
                groupChoiceRestaurants.addAll(groupRestaurants);
            }

            groupChoiceRestaurants.forEach(restaurant -> {
                if (restaurant.getImageFile() != null) {
                    String imageUrl = String.format("/images/%s", restaurant.getImageFile().toHexString());
                    restaurant.setImageUrl(imageUrl);
                } else {
                    restaurant.setImageUrl(null);
                }
            });

            return new ResponseEntity<>(groupChoiceRestaurants, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
//    @GetMapping("/first")
//    @Operation(summary = "사용자별 첫 번째 추천 레스토랑 조회 API", description = "사용자 ID에 따라 첫 번째 추천 레스토랑 목록을 반환합니다.")
//    public ResponseEntity<List<String>> getFirstRecommendationsByUserId(@RequestParam("userId") String userId) {
//        Optional<List<Recommended>> recommendations = recommendedService.findByUserId(authentication.getName());
//
//        if (recommendations.isPresent() && !recommendations.get().isEmpty()) {
//            List<String> firstRecommend = recommendations.get().get(0).getFirstRecommend();
//            return new ResponseEntity<>(firstRecommend, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(null, HttpStatus.OK);
//        }
//    }
//    @GetMapping("/second")
//    @Operation(summary = "사용자별 두 번째 추천 레스토랑 조회 API", description = "사용자 ID에 따라 두 번째 추천 레스토랑 목록을 반환합니다.")
//    public ResponseEntity<List<String>> getSecondRecommendationsByUserId(Authentication authentication) {
//        Optional<List<Recommended>> recommendations = recommendedService.findByUserId(authentication.getName());
//
//        if (recommendations.isPresent() && !recommendations.get().isEmpty()) {
//            List<String> secondRecommend = recommendations.get().get(0).getSecondRecommend();
//            return new ResponseEntity<>(secondRecommend, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(null, HttpStatus.OK);
//        }
//    }