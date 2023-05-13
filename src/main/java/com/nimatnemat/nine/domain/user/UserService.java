package com.nimatnemat.nine.domain.user;

import com.mongodb.client.gridfs.GridFSBucket;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private GridFSBucket gridFsBucket;

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
                registrationDto.getBirthdate(),
                registrationDto.getGroupName()
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
    public String saveImageToGridFS(MultipartFile image) {
        try {
            // 이미지의 입력 스트림을 얻습니다.
            InputStream inputStream = image.getInputStream();
            // 이미지의 원본 파일명을 얻습니다.
            String filename = image.getOriginalFilename();

            // 이미지를 GridFS에 저장합니다.
            ObjectId objectId = gridFsBucket.uploadFromStream(filename, inputStream);

            // 저장된 이미지의 URL을 생성하고 반환합니다. 실제로는 아래 예시와 같이 도메인을 사용하여 이미지를 접근할 수 있는 URL을 생성해야 합니다.
            // 예: https://example.com/images/{objectId}
            String imageUrl = String.format("http://3.39.232.5:8080/images/%s", objectId.toString());
            return imageUrl;
        } catch (IOException e) {
            throw new RuntimeException("이미지를 GridFS에 저장하는 동안 오류가 발생했습니다.", e);
        }
    }

    public void updateProfileImage(String userId, String imageUrl) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setProfileImage(imageUrl);
            userRepository.save(user);
        }
    }
    public User updateUser(String userId, UserUpdateDto userUpdateDto) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setGender(userUpdateDto.getGender());
            user.setNickName(userUpdateDto.getNickName());
            user.setEmail(userUpdateDto.getEmail());
            user.setBirthdate(userUpdateDto.getBirthdate());
            user.setGroupName(userUpdateDto.getGroupName());
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

