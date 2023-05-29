package com.nimatnemat.nine.domain.group;
import com.nimatnemat.nine.domain.user.User;
import com.nimatnemat.nine.domain.user.UserService;
import com.nimatnemat.nine.domain.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.assertions.Assertions.assertFalse;

//@Service
//public class GroupService {
//    private final GroupRepository groupRepository;
//
//}

