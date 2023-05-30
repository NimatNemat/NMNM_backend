package com.nimatnemat.nine.domain.user;

import com.mongodb.client.gridfs.GridFSBucket;
import com.nimatnemat.nine.domain.restaurant.Restaurant;
import com.nimatnemat.nine.domain.restaurant.RestaurantRepository;
import com.nimatnemat.nine.domain.userRating.UserRating;
import com.nimatnemat.nine.domain.userRating.UserRatingRepository;
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
    @Autowired
    private RestaurantRepository restaurantRepository;  // 모든 레스토랑을 가져오기 위한 repository
    @Autowired
    private UserRatingRepository userRatingRepository;  // user_rating_table에 접근하기 위한 repository

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
            String imageUrl = String.format("/images/%s", objectId.toString());
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
            user.setNickName(userUpdateDto.getNickName());
//            user.setGroupName(userUpdateDto.getGroupName());
            user.setInfoMessage(userUpdateDto.getInfoMessage());
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
            allUsers.add(new UserSearchResultDto(user.getUserId(), user.getProfileImage(), user.getNickName(), user.getEmail()));
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
    public User findByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElse(null);
    }
    public boolean updateEmail(String userId, String newEmail) {
        User user = findByUserId(userId);
        if (user == null) {
            return false;
        }

        user.setEmail(newEmail);

        try {
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            // 이 부분에서 어떤 오류가 발생하는지 로그로 확인할 수 있도록 추가하는 것이 좋습니다.
            e.printStackTrace();
            return false;
        }
    }
    //비밀번호 변경을 위한 코드
    public boolean updatePassword(String userId, String newPassword) {
        User user = findByUserId(userId);
        if (user == null) {
            return false;
        }

        String hashedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(hashedPassword);
        userRepository.save(user);
        return true;
    }

    public void initializeUserRatings(String userId) {
        List<Restaurant> allRestaurants = restaurantRepository.findAll();  // 모든 레스토랑을 가져옵니다.
        for (Restaurant restaurant : allRestaurants) {
            UserRating userRating = new UserRating();
            userRating.setUserId(userId);
            userRating.setRestaurantId(restaurant.getRestaurantId());
            userRating.setRating(0);
            userRatingRepository.save(userRating);  // 각 레스토랑에 대한 초기 사용자 평점을 데이터베이스에 저장합니다.
        }
    }
    public String getUserNickName(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getNickName();
        } else {
            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }
    }
    public String getUserProfileImage(String userId) {
        Optional<User> userOptional = userRepository.findByUserId(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return user.getProfileImage();
        } else {
            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }
    }
}

