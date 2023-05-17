package com.nimatnemat.nine.domain.follow;
import com.nimatnemat.nine.domain.user.User;
import com.nimatnemat.nine.domain.user.UserService;
import com.nimatnemat.nine.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.assertions.Assertions.assertFalse;

@Service
public class FollowService {
    private final FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MongoTemplate mongoTemplate;

    public FollowService(FollowRepository followRepository){
        this.followRepository = followRepository;
    }

    public void addFollow(String followId, String followingId){
        // 팔로우가 이미 존재하는지 확인
        Follow existingFollow = followRepository.findByFollowIdAndFollowingId(followId, followingId);
        if(existingFollow == null) { // 팔로우가 존재하지 않으면 새로운 팔로우 저장
            Follow newFollow = new Follow(followId,followingId);
            followRepository.save(newFollow);
        } else {
            // 팔로우가 이미 존재하면 아무것도 하지 않고 메소드 종료
        }
    }

    public void unfollow(String followId, String followingId){
        Follow follow = followRepository.findByFollowIdAndFollowingId(followId, followingId);
        if(follow != null){
            followRepository.delete(follow);
        }
    }

    public List<UserSummaryDto> getMyFollows(String followId){
        List<FollowingIdOnlyDto> myFollowList = followRepository.findFollowingIdByFollowId(followId);
        List<UserSummaryDto> userSummaries = new ArrayList<>();
        for (FollowingIdOnlyDto followingIdOnlyDto : myFollowList) {
            Optional<User> optionalUser = userRepository.findByUserId(followingIdOnlyDto.getFollowingId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                UserSummaryDto userSummary = new UserSummaryDto();
                userSummary.setUserId(user.getUserId());
                userSummary.setNickName(user.getNickName());
                userSummary.setProfileImage(user.getProfileImage());
                userSummaries.add(userSummary);
            }
        }
        return userSummaries;
    }

    public List<UserSummaryDto> getMyFollowers(String followingId){
        List<FollowIdOnlyDto> myFollowingList = followRepository.findFollowIdByFollowingId(followingId);
        List<UserSummaryDto> userSummaries = new ArrayList<>();
        for (FollowIdOnlyDto followIdOnlyDto : myFollowingList) {
            Optional<User> optionalUser = userRepository.findByUserId(followIdOnlyDto.getFollowId());
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();
                UserSummaryDto userSummary = new UserSummaryDto();
                userSummary.setUserId(user.getUserId());
                userSummary.setNickName(user.getNickName());
                userSummary.setProfileImage(user.getProfileImage());
                userSummaries.add(userSummary);
            }
        }
        return userSummaries;
    }

//    public List<FollowIdOnlyDto> findFollowIdByFollowingId (String followId) {
//        List<FollowIdOnlyDto> myFollowList = followRepository.findFollowIdByFollowingId(followId);
//        return myFollowList;
//    }

}
