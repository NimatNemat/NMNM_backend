package com.nimatnemat.nine.domain.tastePlaylist;
import org.bson.types.ObjectId;
import com.nimatnemat.nine.domain.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

import javax.swing.text.html.Option;
public interface TastePlaylistRepository extends MongoRepository<TastePlaylist, ObjectId>  {
    TastePlaylist findBy_id(ObjectId _id);

    List<TastePlaylist> findByUserId(String userId);
}
