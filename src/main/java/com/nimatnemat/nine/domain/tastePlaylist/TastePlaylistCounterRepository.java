package com.nimatnemat.nine.domain.tastePlaylist;

import com.nimatnemat.nine.domain.review.ReviewCounter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TastePlaylistCounterRepository extends MongoRepository<TastePlaylistCounter, String> {
}
