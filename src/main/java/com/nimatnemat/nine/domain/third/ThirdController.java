package com.nimatnemat.nine.domain.third;

import io.swagger.v3.oas.annotations.Operation;
import org.json.JSONArray; // 이렇게 변경하세요
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api")
public class ThirdController {

    @PostMapping("/thirdRecommend")
    @Operation(summary = "세 번째 추천 API", description = "클라이언트에서 문자열 배열을 받아서 해당 데이터를 Flask 서버로 POST하고, 그 결과를 반환합니다.")
    public String thirdRecommend(@RequestBody String[] values) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://15.165.161.104:5000/thirdRecommend";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // POST 요청에 필요한 데이터 설정
        JSONArray jsonArray = new JSONArray();
        for (String value : values) {
            jsonArray.put(value);
        }

        HttpEntity<String> request = new HttpEntity<>(jsonArray.toString(), headers);

        // POST 요청 보내고 결과 받기
        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        // 받은 결과 확인하기
        return response.getBody();
    }
}