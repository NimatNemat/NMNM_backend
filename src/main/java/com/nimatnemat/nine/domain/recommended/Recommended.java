package com.nimatnemat.nine.domain.recommended;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "restaurant_recommend_table")
@Getter
@Setter
@NoArgsConstructor
public class Recommended {
    private ObjectId _id;
    private String userId;
    private List<String> firstRecommend;
    private List<String> secondRecommend;

    // 생성자, getter, setter
}
