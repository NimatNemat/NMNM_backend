package com.nimatnemat.nine.domain.user;

import com.nimatnemat.nine.domain.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;


import java.util.Base64;
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
    @Operation(summary = "사용자 등록 API", description = "신규 사용자를 등록합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "등록 성공입니다잇"),
            @ApiResponse(responseCode = "400", description = "등록 실패")
    })
    public ResponseEntity<User> register(@RequestBody UserRegistrationDto registrationDto) {

//        String base64ProfileImage = registrationDto.getProfileImage();
//        byte[] decodedProfileImage = Base64.getDecoder().decode(base64ProfileImage);

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
    @GetMapping("/home")
    public ResponseEntity<User> home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") String userId) {
        Optional<User> userOptional = userService.getUserById(userId);
        if (userOptional.isPresent()) {
            return new ResponseEntity<>(userOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    @PutMapping("/update")
//    public ResponseEntity<User> edit(@RequestBody UserUpdateDto userUpdateDto) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof User) {
//            User user = (User) authentication.getPrincipal();
//            User updatedUser = userService.updateUser(user.getId(), userUpdateDto);
//            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//    }

//    @DeleteMapping("/delete")
//    public ResponseEntity<Void> withdraw() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof User) {
//            User user = (User) authentication.getPrincipal();
//            userService.withdraw(user.getId());
//            SecurityContextHolder.clearContext();
//            return new ResponseEntity<>(HttpStatus.OK);
//        } else {
//            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        }
//    }
//    @GetMapping("/profileImage/{userId}")
//    public ResponseEntity<byte[]> getProfileImage(@PathVariable String userId) {
//        byte[] profileImage = userService.getProfileImageByUserId(userId);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.IMAGE_PNG);
//        return new ResponseEntity<>(profileImage, headers, HttpStatus.OK);
//    }

}
