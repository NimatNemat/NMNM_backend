package com.nimatnemat.nine.domain.group;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface GroupRepository extends MongoRepository<Group, ObjectId> {
    Optional<Group> findByGroupName(String groupName);

}
