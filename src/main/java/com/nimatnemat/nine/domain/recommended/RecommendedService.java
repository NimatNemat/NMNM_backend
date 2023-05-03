package com.nimatnemat.nine.domain.recommended;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RecommendedService {


        @Autowired
        private RecommendedRepository recommendedRepository;

        public Optional<List<Recommended>> findByUserId(String userId) {
            return recommendedRepository.findByUserId(userId);
        }
    }
