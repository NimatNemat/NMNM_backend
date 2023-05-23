package com.nimatnemat.nine.domain.tastePlaylist;
import com.nimatnemat.nine.domain.restaurant.Restaurant;
import com.nimatnemat.nine.domain.restaurant.RestaurantRepository;
import com.nimatnemat.nine.domain.user.UserService;
import com.nimatnemat.nine.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;
import org.bson.types.ObjectId;

@Service
public class TastePlaylistService {
    private final TastePlaylistRepository tastePlaylistRepository;
    @Autowired
    public RestaurantRepository restaurantRepository;

    public TastePlaylistService(TastePlaylistRepository tastePlaylistRepository) {
        this.tastePlaylistRepository = tastePlaylistRepository;
    }

    public void addTastePlaylist(String userId, String tastePlaylistName, String tastePlaylistDesc,int publicOrPrivate) {
        Date now = new Date();
        List<Long> playlistDetail = new ArrayList<>();
        TastePlaylist newTastePlaylist = new TastePlaylist(userId, tastePlaylistName, tastePlaylistDesc,now, publicOrPrivate, playlistDetail);
        tastePlaylistRepository.save(newTastePlaylist);
    }

    public void deleteTastePlaylist(ObjectId oid) {
        TastePlaylist tastePlaylist = tastePlaylistRepository.findBy_id(oid);
        if (tastePlaylist != null) {
            tastePlaylistRepository.delete(tastePlaylist);
        }
    }

    public void addDetail(ObjectId oid, long restaurantId) {
        TastePlaylist tastePlaylist = tastePlaylistRepository.findBy_id(oid);
        if (tastePlaylist != null) {
            List<Long> currentDetails = tastePlaylist.getPlaylistDetail();

            currentDetails.add(restaurantId);
            tastePlaylist.setPlaylistDetail(currentDetails);
            tastePlaylistRepository.save(tastePlaylist);
        }
    }

    public void deleteDetail(ObjectId oid, long restaurantId) {
        TastePlaylist tastePlaylist = tastePlaylistRepository.findBy_id(oid);
        if (tastePlaylist != null) {
            List<Long> currentDetails = tastePlaylist.getPlaylistDetail();

            if (!currentDetails.isEmpty()) {
                List<Long> updatedDetails = currentDetails.stream()
                        .filter(id -> !id.equals(restaurantId))
                        .collect(Collectors.toList());

                tastePlaylist.setPlaylistDetail(updatedDetails);
                tastePlaylistRepository.save(tastePlaylist);
            }
        }
    }

    public List<TastePlaylist> getTastePlaylist(String userId) {
        return tastePlaylistRepository.findByUserId(userId);
    }
    public List<Long> getPlaylistDetail(ObjectId oid) {
        TastePlaylist tastePlaylist = tastePlaylistRepository.findBy_id(oid);
        if (tastePlaylist != null) {
            return tastePlaylist.getPlaylistDetail();
        } else {
            return new ArrayList<>();
        }
    }
}
