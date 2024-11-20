package com.activate.ActivateMSV1.recommendation_ms.service;

import com.activate.ActivateMSV1.recommendation_ms.infra.DTO.EventInfoDTO;
import com.activate.ActivateMSV1.recommendation_ms.infra.DTO.NotificationDTO;
import com.activate.ActivateMSV1.recommendation_ms.infra.DTO.UserDTO;
import com.activate.ActivateMSV1.recommendation_ms.infra.config.RabbitMQConfig;
import com.activate.ActivateMSV1.recommendation_ms.infra.mappers.EventMapper;
import com.activate.ActivateMSV1.recommendation_ms.infra.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

@Service
public class EventConsumerService {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RecommendationService recommendationService;

    Logger logger = LoggerFactory.getLogger(EventConsumerService.class);

    @RabbitListener(queues = RabbitMQConfig.EVENT_QUEUE)
    public void receiveMessage(EventInfoDTO event) {
        boolean eventAlreadyExists;
        logger.info("Entering receiveMessage");
        if(event == null) {
            logger.error("Received null event");
            return;
        }
        logger.info("Received event: {}", event.getName());
        eventAlreadyExists = eventRepository.findById(event.getId().toString()).orElse(null) != null;

        eventRepository.save(EventMapper.INSTANCE.toRepoModelEvent(event));
        logger.info("Event {} with id {} saved", event.getName(), event.getId());
        if(eventAlreadyExists) return;

        logger.info("Starting recommendation service to recommend event {}", event.getName());
        recommendationService.recommendUsersToEvent(event.getId()).forEach(
                user -> sendNotification(event, user)
        );
    }

    private void sendNotification(EventInfoDTO event, UserDTO user) {
        NotificationDTO notification = createNotification(event, user);
        logger.info("Sending notification about event {} to user {}", user.getName(), event.getName());
        rabbitTemplate.convertAndSend(RabbitMQConfig.NOTIFICATION_QUEUE, notification);
    }

    private NotificationDTO createNotification(EventInfoDTO event, UserDTO user) {
        NotificationDTO notification = new NotificationDTO();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'a las' H ': 'M", new Locale("es", "ES"));
        String formattedDate = event.getDate().format(formatter);

        String title = "¡Nuevo evento recomendado!";
        String description = "¡Hola, " + user.getName() + "! Activate te recomienda el evento '" + event.getName() + "' que se llevará a cabo cerca de ti el " + formattedDate + ". ¡No te lo pierdas <3!";

        logger.info("Creating notification with title: {} and description: {}", title, description);

        notification.setUserId(user.getId());
        notification.setTitle(title);
        notification.setDescription(description);
        Date date = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        notification.setDate(date);
        return notification;
    }
}
