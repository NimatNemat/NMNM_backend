package com.nimatnemat.nine.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    private MongoTemplate mongoTemplate;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    //회원가입을 위한 코드
    public User register(UserRegistrationDto registrationDto) {
        User newUser = new User(
//                UUID.randomUUID().toString(),
                registrationDto.getUserId(),
                registrationDto.getGender(),
                passwordEncoder.encode(registrationDto.getPassword()),
                registrationDto.getNickName(),
                registrationDto.getProfileImage(),
                registrationDto.getEmail(),
                registrationDto.getBirthdate()
        );

        return userRepository.save(newUser);
    }

    //로그인을 위한 코드
    public User findByUserIdAndPassword(String userId, String password) {
        Optional<User> userOptional = userRepository.findByUserId(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }

        return null;
    }

    public User updateUser(String userId, UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setGender(userUpdateDto.getGender());
            user.setNickName(userUpdateDto.getNickName());
            user.setEmail(userUpdateDto.getEmail());
            user.setBirthdate(userUpdateDto.getBirthdate());
            return userRepository.save(user);
        } else {
            return null;
        }
    }

    public boolean deleteUser(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return true;
        } else {
            return false;
        }
    }

    // Add this method to get a list of all users
    public Optional<User> getUserById(String userId) {
        return userRepository.findByUserId(userId);
    }

    public boolean withdraw(String id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return true;
        }

        return false;
    }

    public List<UserSearchResultDto> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserSearchResultDto> allUsers = new ArrayList<>();

        for (User user : users) {
            allUsers.add(new UserSearchResultDto(user.getUserId(), user.getProfileImage(), user.getNickName()));
        }

        return allUsers;
    }

    //중복검사를 위한 메소드 들
    public boolean isUserIdDuplicated(String userId) {
        Optional<User> existingUser = userRepository.findByUserId(userId);
        return existingUser.isPresent();
    }

    public boolean isEmailDuplicated(String userId, String email) {
        User currentUser = userRepository.findByUserId(userId).orElse(null);
        if (currentUser != null && currentUser.getEmail().equals(email)) {
            return false;
        }
        Optional<User> existingUser = userRepository.findByEmail(email);
        return existingUser.isPresent();
    }

    public boolean isNicknameDuplicated(String userId, String nickname) {
        User currentUser = userRepository.findByUserId(userId).orElse(null);
        if (currentUser != null && currentUser.getNickName().equals(nickname)) {
            return false;
        }
        Optional<User> existingUser = userRepository.findByNickName(nickname);
        return existingUser.isPresent();
    }
}

