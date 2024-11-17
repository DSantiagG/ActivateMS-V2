package com.activate.ActivateMSV1.gestion_evento_microservicio.application.service;

import com.activate.ActivateMSV1.gestion_evento_microservicio.domain.model.Event;
import com.activate.ActivateMSV1.gestion_evento_microservicio.domain.model.Participant;
import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.config.RabbitMQConfig;
import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.dto.communication.EventInfoDTO;
import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.dto.communication.NotificationDTO;
import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.mappers.EventAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

@Service
public class EventPublisherService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    EventAdapter eventAdapter;

    Logger logger = LoggerFactory.getLogger(EventPublisherService.class);

    /**
     * Publishes an event to the RabbitMQ event queue
     * @param event the event to be published
     */
    public void publishEvent(Event event) {
        logger.info("Publishing event: " + event.getName() + " to RabbitMQ");
        EventInfoDTO eventMappedDTO = eventAdapter.mapEventToEventInfoDTO(event);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EVENT_QUEUE, eventMappedDTO);
    }

    /**
     * Publishes notifications to all participants of an event
     * @param event the event
     * @param title the title of the notification
     * @param message the message of the notification
     */
    public void publishNotifications(Event event, String title, String message) {
        logger.info("Publishing notifications to all participants of event: " + event.getName());
        for (Participant participant : event.getParticipants()) {
            NotificationDTO notification = new NotificationDTO(participant.getUser().getId(), title,message, new Date());
            rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_QUEUE, notification);
        }
    }
}
