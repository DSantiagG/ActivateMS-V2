package com.activate.ActivateMSV1.gestion_evento_microservicio.ui.controller;

import com.activate.ActivateMSV1.gestion_evento_microservicio.application.service.OrganizerService;
import com.activate.ActivateMSV1.gestion_evento_microservicio.domain.model.Location;
import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.dto.request.EventRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activate/event/organizer")
public class OrganizerController {

    @Autowired
    private OrganizerService organizerService;

    Logger logger = LoggerFactory.getLogger(OrganizerController.class);

    /**
     * Create an event
     * @param eventRequest Event data
     * @return 201 Created if the event was created successfully otherwise 400 Bad Request
     */
    @PostMapping
    public ResponseEntity<?> createEvent(@RequestBody EventRequest eventRequest) {
        logger.info("Received request to create event: " + eventRequest.getName());
        Location location = new Location(eventRequest.getLatitude(), eventRequest.getLongitude());
        organizerService.createEvent(eventRequest.getMaxCapacity(), eventRequest.getDuration(), eventRequest.getName(), eventRequest.getDescription(), eventRequest.getDate(), location, eventRequest.getType(), eventRequest.getOrganizerId(), eventRequest.getInterests());
        return ResponseEntity.created(null).build();
    }

    /**
     *  Cancel an event
     * @param organizerId Event organizer id
     * @param eventId Event id to cancel
     * @return 200 OK if the event was canceled successfully otherwise 400 Bad Request
     */
    @PutMapping("/{organizerId}/cancel/{eventId}")
    public ResponseEntity cancelEvent(@PathVariable Long organizerId, @PathVariable Long eventId) {
        logger.info("Received request to cancel event with id: " + eventId);
        organizerService.cancelEvent(eventId, organizerId);
        return ResponseEntity.noContent().build();
    }
}