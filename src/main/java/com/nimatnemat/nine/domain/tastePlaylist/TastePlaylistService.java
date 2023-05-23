package com.nimatnemat.nine.domain.tastePlaylist;
import com.nimatnemat.nine.domain.restaurant.Restaurant;
import com.nimatnemat.nine.domain.restaurant.RestaurantRepository;
import com.nimatnemat.nine.domain.review.ReviewCounter;
import com.nimatnemat.nine.domain.review.ReviewCounterRepository;
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
    @Autowired
    private TastePlaylistCounterRepository tastePlaylistCounterRepository;

    public TastePlaylistService(TastePlaylistRepository tastePlaylistRepository) {
        this.tastePlaylistRepository = tastePlaylistRepository;
    }

    public void addTastePlaylist(String userId, String tastePlaylistName, String tastePlaylistDesc, int publicOrPrivate) {
        Date now = new Date();
        List<Long> playlistDetail = new ArrayList<>();
        Long tastePlaylistId = generateTastePlaylistId(); // unique tastePlaylistId generation needed
        TastePlaylist newTastePlaylist = new TastePlaylist(tastePlaylistId, userId, tastePlaylistName, tastePlaylistDesc, now, publicOrPrivate, playlistDetail);
        tastePlaylistRepository.save(newTastePlaylist);
    }

    private Long generateTastePlaylistId() {
        // generate unique tastePlaylistId
        TastePlaylistCounter tastePlaylistCounter = tastePlaylistCounterRepository.findById("unique").orElseGet(TastePlaylistCounter::new);
        Long tastePlaylistId = tastePlaylistCounter.incrementAndGet();
        tastePlaylistCounterRepository.save(tastePlaylistCounter);
        // Here's a simple generation logic, but it could be replaced by more complex logic if needed
        return tastePlaylistId;
    }

    public void deleteTastePlaylist(Long tastePlaylistId) {
        TastePlaylist tastePlaylist = tastePlaylistRepository.findByTastePlaylistId(tastePlaylistId);
        if (tastePlaylist != null) {
            tastePlaylistRepository.delete(tastePlaylist);
        }
    }

    public void addDetail(Long tastePlaylistId, long restaurantId) {
        TastePlaylist tastePlaylist = tastePlaylistRepository.findByTastePlaylistId(tastePlaylistId);
        if (tastePlaylist != null) {
            List<Long> currentDetails = tastePlaylist.getPlaylistDetail();
            currentDetails.add(restaurantId);
            tastePlaylist.setPlaylistDetail(currentDetails);
            tastePlaylistRepository.save(tastePlaylist);
        }
    }

    public void deleteDetail(Long tastePlaylistId, long restaurantId) {
        TastePlaylist tastePlaylist = tastePlaylistRepository.findByTastePlaylistId(tastePlaylistId);
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

    public List<Long> getPlaylistDetail(Long tastePlaylistId) {
        TastePlaylist tastePlaylist = tastePlaylistRepository.findByTastePlaylistId(tastePlaylistId);
        if (tastePlaylist != null) {
            return tastePlaylist.getPlaylistDetail();
        } else {
            return new ArrayList<>();
        }
    }
}
