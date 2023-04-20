package com.nimatnemat.nine.domain.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User register(UserRegistrationDto registrationDto, byte[] profileImage) {
        Optional<User> userOptional = userRepository.findByEmail(registrationDto.getEmail());

        if (userOptional.isPresent()) {
            throw new RuntimeException("이미 등록된 이메일입니다.");
        }

        User newUser = new User(
//                UUID.randomUUID().toString(),
                registrationDto.getUserId(),
                registrationDto.getGender(),
                passwordEncoder.encode(registrationDto.getPassword()),
                registrationDto.getNickName(),
                profileImage,
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
    public User findById(String id) {
        return userRepository.findById(id).orElse(null);
    }

    public boolean deleteById(String id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
    public User updateUser(String id, UserUpdateDto updatedUser) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // Update the user's properties with the updatedUser properties
            // For example:
            user.setNickName(user.getNickName());
//            user.setAge(user.getAge());
            // ... (update other properties as needed)

            return userRepository.save(user);
        }

        return null;
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
    public byte[] getProfileImageByUserId(String userId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));
        User user = mongoTemplate.findOne(query, User.class);

        if (user != null) {
            return user.getProfileImage();
        } else {
            // Handle the case when the user is not found, e.g., log a warning, return a default value or throw a custom exception
            // For example, you can return null or an empty byte array:
            return null;
            // OR
            // return new byte[0];
        }
    }
}
