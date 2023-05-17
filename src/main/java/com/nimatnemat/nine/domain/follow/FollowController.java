package com.nimatnemat.nine.domain.follow;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nimatnemat.nine.domain.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/follows")
public class FollowController {
    @Autowired
    private FollowService followService;


    @PostMapping("/follow")
    @Operation(summary = "팔로우 추가 API",description = "선택한 유저를 팔로우 목록에 추가합니다")
    public ResponseEntity<?> addFollow(Authentication authentication,@RequestParam String targetId){
        try{
            followService.addFollow(authentication.getName(),targetId);
            return new ResponseEntity<>( authentication.getName() +"님이 "+ targetId + "님을 팔로우했습니다!", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/unfollow")
    @Operation(summary = "언팔로우 API",description = "선택한 유저를 팔로우 목록에서 제거합니다")
    public ResponseEntity<?> unfollow(Authentication authentication,@RequestParam String targetId){
        try{
            followService.unfollow(authentication.getName(),targetId);
            return new ResponseEntity<>( authentication.getName() + "님이 " + targetId + "님을 언팔로우했습니다!", HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getMyfollows")
    @Operation(summary = "팔로우 확인 API",description = "내가 팔로우한 사용자 정보를 제공합니다")
    public ResponseEntity<?> getMyfollows(@RequestParam String userId){
        try{
            List<UserSummaryDto> userSummaries = followService.getMyFollows(userId);

            return new ResponseEntity<>(userSummaries, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/getMyfollowers")
    @Operation(summary = "팔로워 확인 API",description = "나를 팔로우한 사용자 정보를 제공합니다")
    public ResponseEntity<?> getMyfollowers(@RequestParam String userId){
        try{
            List<UserSummaryDto> userSummaries = followService.getMyFollowers(userId);

            return new ResponseEntity<>(userSummaries, HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
