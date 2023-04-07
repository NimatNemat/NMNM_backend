package com.nimatnemat.nine.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    //회원가입을 위한 코드
    public User register(UserRegistrationDto registrationDto) {
        Optional<User> userOptional = userRepository.findByEmail(registrationDto.getEmail());

        if (userOptional.isPresent()) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }

        User newUser = new User(
//                UUID.randomUUID().toString(),
                registrationDto.getUserId(),
                registrationDto.getAge(),
                registrationDto.getGender(),
                passwordEncoder.encode(registrationDto.getPassword()),
                registrationDto.getNickName(),
                registrationDto.getEmail(),
                registrationDto.getProfileImage()
        );

        return userRepository.save(newUser);
    }
    //로그인을 위한 코드
    public User findByEmailAndPassword(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }

        return null;
    }
}
