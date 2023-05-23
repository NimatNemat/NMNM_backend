package com.nimatnemat.nine.domain.tastePlaylist;
import com.nimatnemat.nine.domain.restaurant.Restaurant;
import com.nimatnemat.nine.domain.restaurant.RestaurantRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.nimatnemat.nine.domain.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;


import org.springframework.web.bind.annotation.*;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/tastePlaylist")
public class TastePlaylistController {
    @Autowired
    private TastePlaylistService tastePlaylistService;
    @Autowired
    public RestaurantRepository restaurantRepository;


    @PostMapping("/addTastePlaylist")
    @Operation(summary = "맛플리 추가 API", description = "새로운 맛플리를 추가합니다")
    public ResponseEntity<?> addTastePlaylist(@RequestParam String userId,@RequestParam String tastePlaylistName, @RequestParam String tastePlaylistDesc,@RequestParam int publicOrPrivate){
        try{
            tastePlaylistService.addTastePlaylist(userId, tastePlaylistName, tastePlaylistDesc,publicOrPrivate);
            return new ResponseEntity<>(userId+"님이 "+tastePlaylistName+"을 추가하였습니다",HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deleteTastePlaylist")
    @Operation(summary = "맛플리 삭제 API", description = "맛플리를 삭제합니다")
    public ResponseEntity<?> deleteTastePlaylist(@RequestParam String playlistId){
        try{
            tastePlaylistService.deleteTastePlaylist(new ObjectId(playlistId));
            return new ResponseEntity<>("맛플리를 삭제하였습니다",HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/addDetail")
    @Operation(summary = "맛플리 세부사항 추가 API", description = "맛플리 세부사항을 추가합니다")
    public ResponseEntity<?> addDetail(@RequestParam String playlistId, @RequestParam long restaurantId){
        try{
            tastePlaylistService.addDetail(new ObjectId(playlistId), restaurantId);
            return new ResponseEntity<>("해당 맛플리에서 "+restaurantId+ "를 추가하였습니다",HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/deleteDetail")
    @Operation(summary = "맛플리 세부사항 삭제 API",description = "맛플리 세부사항을 삭제합니다")
    public ResponseEntity<?> deleteDetail(@RequestParam String playlistId, @RequestParam long restaurantId){
        try{
            tastePlaylistService.deleteDetail(new ObjectId(playlistId), restaurantId);
            return new ResponseEntity<>("해당 맛플리에서 "+restaurantId+ "를 삭제하였습니다",HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getTastePlaylist")
    @Operation(summary = "유저 맛플리 정보 제공 API", description = "해당 유저의 모든 맛플리 정보를 제공합니다")
    public ResponseEntity<?> getTastePlaylist(@RequestParam String userId){
        try{
            List<?> T = tastePlaylistService.getTastePlaylist(userId);
            return new ResponseEntity<>(T,HttpStatus.OK);
        }
        catch(Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getDetail/{playlistId}")
    @Operation(summary = "맛플리 세부정보의 레스토랑 객체 반환 API", description = "맛플리 세부정보에 있는 레스토랑 아이디를 확인하여 해당 아이디에 맞는 레스토랑 객체를 제공합니다")
    public ResponseEntity<?> getPlaylistDetail(@PathVariable String playlistId) {
        try {
            List<Long> detail = tastePlaylistService.getPlaylistDetail(new ObjectId(playlistId));
            List<Restaurant> restaurantList = getRestaurantList(detail);

            restaurantList.forEach(restaurant -> {
                if (restaurant.getImageFile() != null) {
                    String imageUrl = String.format("/images/%s", restaurant.getImageFile().toHexString());
                    restaurant.setImageUrl(imageUrl);
                } else {
                    restaurant.setImageUrl(null);
                }
            });

            return new ResponseEntity<>(restaurantList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    private List<Restaurant> getRestaurantList(List<Long> detail) {
        List<Restaurant> restaurantList = new ArrayList<>();

        for (long pd : detail) {
            Optional<Restaurant> restaurant = restaurantRepository.findByRestaurantId(pd);
            restaurant.ifPresent(restaurantList::add);
        }

        return restaurantList;
    }
}
