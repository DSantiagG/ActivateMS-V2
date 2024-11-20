package com.activate.ActivateMSV1.recommendation_ms.service;

import com.activate.ActivateMSV1.recommendation_ms.domain.Event;
import com.activate.ActivateMSV1.recommendation_ms.domain.Recommendation;
import com.activate.ActivateMSV1.recommendation_ms.domain.User;
import com.activate.ActivateMSV1.recommendation_ms.infra.DTO.UserDTO;
import com.activate.ActivateMSV1.recommendation_ms.infra.exceptions.ServiceValidationException;
import com.activate.ActivateMSV1.recommendation_ms.infra.mappers.EventMapper;
import com.activate.ActivateMSV1.recommendation_ms.infra.DTO.EventInfoDTO;
import com.activate.ActivateMSV1.recommendation_ms.infra.mappers.UserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class RecommendationService {
    @Autowired
    EventService eventService;
    @Autowired
    UserService userService;

    Logger logger = LoggerFactory.getLogger(RecommendationService.class);
    Recommendation recommendationDomain = new Recommendation();

    public ArrayList<EventInfoDTO> recommendEventsToUser(Long userId) {
        ArrayList<Event> domainEvents = new ArrayList<>();
        ArrayList<EventInfoDTO> eventsDTO = new ArrayList<>();

        User user = UserMapper.INSTANCE.toDomainUser(userService.getUser(userId));

        logger.info("Received request to recommend events to user with id: " + userId);

        if(user == null) {
            logger.error("User {} not found", userId);
            throw new ServiceValidationException("User not found");
        }

        eventService.getEvents()
                .forEach(
                        event -> domainEvents.add(EventMapper.INSTANCE.toDomainEvent(event))
                );

        logger.info("Starting recommendation service to recommend events to user {}", user.getName());
        recommendationDomain.recommendateEventsToUser(user, domainEvents)
                .forEach(
                    event -> {
                        eventsDTO.add(EventMapper.INSTANCE.toEventInfoDTO(event));
                        logger.info("Event {} recommended to user {}", event.getName(), user.getName());
                    }
                );

        return eventsDTO;
    }

    public ArrayList<UserDTO> recommendUsersToEvent(Long eventId){
        Event event = EventMapper.INSTANCE.toDomainEvent(eventService.getEvent(eventId));
        ArrayList<User> users = new ArrayList<>();
        ArrayList<UserDTO> usersRecommended = new ArrayList<>();

        logger.info("Received request to recommend users to event with id: " + eventId);

        userService.getAllUsers()
                .forEach(
                        user -> users.add(UserMapper.INSTANCE.toDomainUser(user))
                );

        logger.info("Starting recommendation service to recommend users to event {}", event.getName());
        recommendationDomain.recommendUsersToEvent(event, users)
                .forEach(
                        user -> {
                            usersRecommended.add(UserMapper.INSTANCE.toUserDTO(user));
                            logger.info("User {} recommended to event {}", user.getName(), event.getName());
                        }
                );

        return usersRecommended;
    }
}
