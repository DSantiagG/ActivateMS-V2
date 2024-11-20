package com.activate.ActivateMSV1.recommendation_ms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.activate.ActivateMSV1.recommendation_ms.infra.DTO.EventInfoDTO;
import com.activate.ActivateMSV1.recommendation_ms.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/activate/recommendation")
public class RecommendationController {

    @Autowired
    RecommendationService recommendationService;

    Logger logger = LoggerFactory.getLogger(RecommendationController.class);

    @GetMapping("/{userId}")
    public ResponseEntity<?> recommendateEventsToUser(@PathVariable Long userId) {
        ArrayList<EventInfoDTO> pairedEvents;
        logger.info("Received request to recommend events to user with id: " + userId);
        pairedEvents = recommendationService.recommendEventsToUser(userId);

        if(pairedEvents == null){
            logger.info("Events not found for user with id: " + userId);
            return ResponseEntity.notFound().build();
        }
        logger.info("Events recommended to user with id: " + userId);
        return ResponseEntity.ok(pairedEvents);
    }

}
