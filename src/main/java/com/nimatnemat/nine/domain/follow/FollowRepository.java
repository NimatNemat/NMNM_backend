package com.nimatnemat.nine.domain.follow;
import org.bson.types.ObjectId;
import com.nimatnemat.nine.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;


public interface FollowRepository extends MongoRepository<Follow, ObjectId> {
    Follow findByFollowIdAndFollowingId(String followId, String followingId);

    List<FollowingIdOnlyDto> findFollowingIdByFollowId(String followId);
    List<FollowIdOnlyDto> findFollowIdByFollowingId(String followingId);
}
