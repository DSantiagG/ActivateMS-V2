package com.activate.ActivateMSV1.gestion_evento_microservicio.ui.controller;

import com.activate.ActivateMSV1.gestion_evento_microservicio.application.service.ParticipantService;
import com.activate.ActivateMSV1.gestion_evento_microservicio.domain.model.EventInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/activate/event/participant")
public class ParticipantController {

    @Autowired
    private ParticipantService participantService;

    Logger logger = LoggerFactory.getLogger(ParticipantController.class);

    /**
     * Get all events of a participant
     * @param participantId Participant id
     * @return List of events if found otherwise 404 Not Found
     */
    @GetMapping("/{participantId}")
    public ResponseEntity<?> getParticipantEvents(@PathVariable Long participantId) {
        logger.info("Received request to get events of participant with id: " + participantId);
        ArrayList<EventInfo> events = participantService.getParticipantEvents(participantId);
        if (events.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(events);
    }
}