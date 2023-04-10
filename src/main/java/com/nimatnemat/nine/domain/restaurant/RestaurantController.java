//package com.nimatnemat.nine.domain.restaurant;
//
//import io.swagger.v3.oas.annotations.Parameter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.*;
//
//@RequiredArgsConstructor
//@RestController
//@CrossOrigin(origins = "*", allowedHeaders = "*")
//
//public class RestaurantController {
//    private final RestaurantService restaurantService;
//
//    @GetMapping("api/restaurant/{address}")
//    //이 Spring 주석은 경로 변수를 사용하여 지정된 경로에서 메서드를 GET 요청에 매핑
//    public RestaurantNameSearchResponseDto findByAddress(@RequestParam @Parameter(name = "address", required = true) String address){
//        //findByAddress 메서드는 api/restaurant/{name} 경로에서 GET 요청에 매핑
//        //여기서 {name}는 레스토랑 검색에 사용되는 이름을 나타내는 경로 변수
//        System.out.println("test" + address);
//        //이 메서드는 서비스에서 찾은 레스토랑 목록이 포함된 'RestaurantNameSearchResponseDto' 개체를 반환
//        return restaurantService.getRestaurantList(address);
//    }
//}
package com.nimatnemat.nine.domain.restaurant;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api/restaurant")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @GetMapping("/all")
    public List<Restaurant> getAllRestaurants() {
        return restaurantService.getAllRestaurants();
    }
}
