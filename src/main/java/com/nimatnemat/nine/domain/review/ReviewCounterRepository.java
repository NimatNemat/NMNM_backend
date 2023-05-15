package com.nimatnemat.nine.domain.review;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewCounterRepository extends MongoRepository<ReviewCounter, String> {
}