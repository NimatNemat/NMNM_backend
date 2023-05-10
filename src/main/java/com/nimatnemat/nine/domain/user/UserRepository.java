package com.nimatnemat.nine.domain.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUserId(String userId);

    Optional<User> findByEmail(String email);
    Optional<User> findByNickName(String nickName);
}