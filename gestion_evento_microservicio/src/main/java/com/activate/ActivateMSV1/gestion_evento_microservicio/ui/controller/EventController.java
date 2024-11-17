package com.activate.ActivateMSV1.gestion_evento_microservicio.ui.controller;

import com.activate.ActivateMSV1.gestion_evento_microservicio.application.service.EventService;
import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.dto.request.EvaluationRequest;
import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.repository.event.query.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/activate/event")
public class EventController {

    @Autowired
    private EventService eventService;

    Logger logger = LoggerFactory.getLogger(EventController.class);

    /**
     * Get an event by id
     * @param eventId Event id
     * @return Event data if found otherwise 404 Not Found
     * @throws Exception
     */
    @GetMapping("/{eventId}")
    public ResponseEntity<?> getEvent(@PathVariable Long eventId) {
        logger.info("Received request to get event with id: " + eventId);
        Event event = eventService.getEvent(eventId);
        return ResponseEntity.ok(event);
    }

    /**
     * Get all events
     * @return List of events if found otherwise 404 Not Found
     */
    @GetMapping
    public ResponseEntity<?> getEvents() {
        ArrayList<Event> events = eventService.getEvents();
        if (events.size() == 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(events);
    }

    /**
     * Create a new event
     * @param eventId Event id
     * @return 204 No Content if the event is updated successfully or 400 Bad Request if the event can not be updated
     */
    @PutMapping("/{eventId}/type")
    public ResponseEntity<?> updateType(@PathVariable Long eventId) {
        logger.info("Received request to update type of event with id: " + eventId);
        eventService.updateType(eventId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update the maximum capacity of an event
     * @param eventId Event id
     * @param maxCapacity New maximum capacity
     * @return 204 No Content if the event is updated successfully or 400 Bad Request if the event can not be updated
     */
    @PutMapping("/{eventId}/maxCapacity")
    public ResponseEntity<?> updateMaxCapacity(@PathVariable Long eventId, @RequestParam int maxCapacity) {
        logger.info("Received request to update max capacity of event with id: " + eventId + " to " + maxCapacity);
        eventService.updateMaxCapacity(eventId, maxCapacity);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update the date of an event
     * @param eventId Event id
     * @param date New date
     * @return 204 No Content if the event is updated successfully or 400 Bad Request if the event can not be updated
     */
    @PutMapping("/{eventId}/date")
    public ResponseEntity<?> updateDate(@PathVariable Long eventId, @RequestParam LocalDateTime date) {
        logger.info("Received request to update date of event with id: " + eventId + " to " + date);
        eventService.updateDate(eventId, date);
        return ResponseEntity.noContent().build();
    }

    /**
     * Add an evaluation to an event
     * @param eventId Event id
     * @param request Evaluation data
     * @return 201 Created if the evaluation is added successfully or 400 Bad Request if the evaluation can not be added
     */
    @PostMapping("/{eventId}/evaluation")
    public ResponseEntity<?> addEvaluation(@PathVariable Long eventId, @RequestBody EvaluationRequest request) {
        logger.info("Received request to add evaluation to event with id: " + eventId);
        eventService.addEvaluation(eventId, request.getComment(), request.getScore(), request.getParticipantId());
        return ResponseEntity.created(null).build();
    }

    /**
     * Add a participant to an event
     * @param eventId Event id
     * @param participantId Participant id
     * @return 204 No Content if the participant is added successfully or 400 Bad Request if the participant can not be added
     */
    @PutMapping("/{eventId}/participant/{participantId}")
    public ResponseEntity<?> addParticipant(@PathVariable Long eventId, @PathVariable Long participantId){
        logger.info("Received request to add participant with id: " + participantId + " to event with id: " + eventId);
        eventService.addParticipant(eventId, participantId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Remove a participant from an event
     * @param eventId Event id
     * @param participantId Participant id
     * @return 204 No Content if the participant is removed successfully or 400 Bad Request if the participant can not be removed
     */
    @DeleteMapping("/{eventId}/participant/{participantId}")
    public ResponseEntity<?> removeParticipant(@PathVariable Long eventId, @PathVariable Long participantId) {
        logger.info("Received request to remove participant with id: " + participantId + " from event with id: " + eventId);
        eventService.removeParticipant(eventId, participantId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Start an event
     * @param eventId Event id
     * @return 204 No Content if the event is started successfully or 400 Bad Request if the event can not be started
     */
    @PutMapping("/{eventId}/start")
    public ResponseEntity<?> startEvent(@PathVariable Long eventId) {
        logger.info("Received request to start event with id: " + eventId);
        eventService.startEvent(eventId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Finish an event
     * @param eventId Event id
     * @return 204 No Content if the event is finished successfully or 400 Bad Request if the event can not be finished
     */
    @PutMapping("/{eventId}/finish")
    public ResponseEntity <?> finishEvent(@PathVariable Long eventId) {
        logger.info("Received request to finish event with id: " + eventId);
        eventService.finishEvent(eventId);
        return ResponseEntity.noContent().build();
    }
}