package com.nimatnemat.nine.domain.recommended;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RecommendedRepository extends MongoRepository<Recommended, String> {
    Optional<List<Recommended>> findByUserId(String userId);
}

