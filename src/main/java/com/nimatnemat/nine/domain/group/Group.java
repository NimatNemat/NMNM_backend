package com.nimatnemat.nine.domain.group;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "groups_table")
@Getter
@Setter
@NoArgsConstructor
public class Group {
    private String groupName;
    private String groupInfo;
    private List<Long> firstRecommend;
}
