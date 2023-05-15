package com.nimatnemat.nine.domain.user;

import com.nimatnemat.nine.domain.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.Collections;
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

//        if (userService.isNicknameDuplicated(null, registrationDto.getNickName())) {
//            return new ResponseEntity<>("이미 사용 중인 닉네임입니다.", HttpStatus.BAD_REQUEST);
//        }

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
    @GetMapping("/userId")
    @Operation(summary = "회원정보 조회 API", description = "신규 사용자를 등록합니다.")
    public ResponseEntity<UserDto> getUserById(@RequestParam("userId") String userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            UserDto userDto = new UserDto(userOptional.get());
            return new ResponseEntity<>(userDto, HttpStatus.OK);
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

    @PostMapping(value = "/upload-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이미지 업로드 API", description = "사용자 프로필 이미지를 업로드합니다.")
    public ResponseEntity<?> uploadImage(@RequestParam String userId, @RequestPart(value = "image") MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return new ResponseEntity<>("이미지 파일이 필요합니다.", HttpStatus.BAD_REQUEST);
        }

        try {
            String imageUrl = userService.saveImageToGridFS(image);
            userService.updateProfileImage(userId, imageUrl);
            return new ResponseEntity<>(Collections.singletonMap("imageUrl", imageUrl), HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("이미지 업로드 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
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
    @PutMapping("/change-password")
    @Operation(summary = "비밀번호 변경 API", description = "사용자의 비밀번호를 변경합니다.")
    public ResponseEntity<?> changePassword(@RequestParam String userId,
                                            @RequestParam String newPassword) {
        User user = userService.findByUserId(userId);
        if (user == null) {
            return new ResponseEntity<>("존재하지 않는 사용자입니다.", HttpStatus.NOT_FOUND);
        }

        boolean isUpdated = userService.updatePassword(userId, newPassword);
        if (isUpdated) {
            return new ResponseEntity<>("비밀번호가 변경되었습니다.", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("비밀번호 변경에 실패했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
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
