package com.nimatnemat.nine.domain.user;

import com.nimatnemat.nine.domain.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;



import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/users")
public class UserController {

    @Autowired // Add this line
    private JwtTokenProvider JwtTokenProvider;
    @Autowired
    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자 등록 API
     *
     * @param registrationDto 등록할 사용자 정보
     * @return 등록된 사용자 정보
     */
    @PostMapping("/register")
    @Operation(summary = "회원가입 API", description = "신규 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공입니다잇"),
            @ApiResponse(responseCode = "400", description = "등록 실패")
    })
    public ResponseEntity<?> register(@RequestBody UserRegistrationDto registrationDto) {
        if (userService.isUserIdDuplicated(registrationDto.getUserId())) {
            return new ResponseEntity<>("이미 사용 중인 아이디입니다.", HttpStatus.BAD_REQUEST);
        }
        if (userService.isEmailDuplicated(null, registrationDto.getEmail())) {
            return new ResponseEntity<>("이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST);
        }

        if (userService.isNicknameDuplicated(null, registrationDto.getNickName())) {
            return new ResponseEntity<>("이미 사용 중인 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }

        User newUser = userService.register(registrationDto);
        System.out.println("test");
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * 로그인 API
     *
     * @param userId 아이디
     * @param password 비밀번호
     * @return 로그인된 사용자 정보
     */
    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "아이디와 비밀번호로 로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패")
    })
//    public ResponseEntity<User> login(@RequestParam @Parameter(name = "email", required = true) String email,
//                                      @RequestParam @Parameter(name = "password", required = true) String password) {
//        User user = userService.findByEmailAndPassword(email, password);
//        System.out.println("test");
//        if (user != null) {
//            return new ResponseEntity<>(user, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//    }
    public ResponseEntity<String> login(@RequestParam @Parameter(name = "userId", required = true) String userId,
                                        @RequestParam @Parameter(name = "password", required = true) String password) {
        User user = userService.findByUserIdAndPassword(userId, password);
        System.out.println("test");
        if (user != null) {
            String token = JwtTokenProvider.createToken(user.getUserId());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
//    @GetMapping("/authentication-status")
//    @Operation(summary = "사용자 인증 상태 확인 API", description = "토큰을 이용하여 사용자 인증 상태를 확인합니다.")
//    public ResponseEntity<String> checkAuthenticationStatus(@RequestHeader("Authorization") String token) {
//        if (JwtTokenProvider.validateToken(token)) {
//            return new ResponseEntity<>("인증되었습니다.", HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>("인증되지 않았습니다.", HttpStatus.UNAUTHORIZED);
//        }
//    }

    @GetMapping("/userId")
    @Operation(summary = "회원정보 조회 API", description = "신규 사용자를 등록합니다.")
    public ResponseEntity<User> getUserById(Authentication authentication) {
        Optional<User> userOptional = userService.getUserById(authentication.getName());
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/all")
    @Operation(summary = "전체 유저 정보 조회 API", description = "전체 유저의 아이디, 프로필 이미지, 닉네임을 반환합니다.")
    public ResponseEntity<List<UserSearchResultDto>> getAllUsers() {
        List<UserSearchResultDto> allUsers = userService.getAllUsers();
        if (!allUsers.isEmpty()) {
            return new ResponseEntity<>(allUsers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping("/refresh-token")
    @Operation(summary = "토큰 갱신 API", description = "기존 토큰을 이용하여 새로운 토큰을 발급합니다.")
    public ResponseEntity<String> refreshToken(@RequestHeader("X-AUTH-TOKEN") String token) {
        try {
            if (JwtTokenProvider.validateToken(token)) {
                String newToken = JwtTokenProvider.refreshToken(token);
                return ResponseEntity.ok(newToken);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token");
        }
    }

    @PutMapping("/update")
    @Operation(summary = "사용자 정보 업데이트 API", description = "사용자 정보를 업데이트합니다.")
    public ResponseEntity<?> updateUser(@RequestParam String userId, @RequestBody UserUpdateDto userUpdateDto) {
//        String userId = authentication.getName();

        if (userService.isEmailDuplicated(userId, userUpdateDto.getEmail())) {
            return new ResponseEntity<>("이미 사용 중인 이메일입니다.", HttpStatus.BAD_REQUEST);
        }
        if (userService.isNicknameDuplicated(userId, userUpdateDto.getNickName())) {
            return new ResponseEntity<>("이미 사용 중인 닉네임입니다.", HttpStatus.BAD_REQUEST);
        }

        User updatedUser = userService.updateUser(userId, userUpdateDto);
        if (updatedUser != null) {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @DeleteMapping("/delete")
    @Operation(summary = "사용자 정보 삭제 API", description = "사용자 정보를 삭제합니다.")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        boolean isDeleted = userService.deleteUser(authentication.getName());
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
//    @GetMapping("/profileImage/{userId}")
//    public ResponseEntity<byte[]> getProfileImage(@PathVariable String userId) {
//        byte[] profileImage = userService.getProfileImageByUserId(userId);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        return new ResponseEntity<>(profileImage, headers, HttpStatus.OK);
//    }

}
