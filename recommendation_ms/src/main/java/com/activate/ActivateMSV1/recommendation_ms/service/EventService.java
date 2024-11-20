package com.activate.ActivateMSV1.recommendation_ms.service;

import com.activate.ActivateMSV1.recommendation_ms.infra.model.Event;
import com.activate.ActivateMSV1.recommendation_ms.infra.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    Logger logger = LoggerFactory.getLogger(EventService.class);

    public Event getEvent(Long id) {
        logger.info("Received request to get event with id: " + id);
        return eventRepository.findById(id.toString()).orElse(null);
    }

    public List<Event> getEvents() {
        logger.info("Received request to get all events");
        return eventRepository.findAll();
    }

    public Event saveEvent(Event event) {
        logger.info("Event {} with id {} saved", event.getName(), event.getId());
        return eventRepository.save(event);
    }
}
