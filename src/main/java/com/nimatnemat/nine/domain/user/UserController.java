package com.nimatnemat.nine.domain.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/users")
public class UserController {

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
        User newUser = userService.register(registrationDto);
        System.out.println("test");
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * 로그인 API
     *
     * @param email 이메일
     * @param password 비밀번호
     * @return 로그인된 사용자 정보
     */
    @PostMapping("/login")
    @Operation(summary = "로그인 API", description = "이메일과 비밀번호로 로그인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 실패")
    })
    public ResponseEntity<User> login(@RequestParam @Parameter(name = "email", required = true) String email,
                                      @RequestParam @Parameter(name = "password", required = true) String password) {
        User user = userService.findByEmailAndPassword(email, password);
        System.out.println("test");
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
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

    @GetMapping("/userList")
    public ResponseEntity<List<User>> getUserList() {
        List<User> userList = userService.getUserList();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<User> edit(@RequestBody UserUpdateDto userUpdateDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            User updatedUser = userService.updateUser(user.getId(), userUpdateDto);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> withdraw() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            userService.withdraw(user.getId());
            SecurityContextHolder.clearContext();
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

}
