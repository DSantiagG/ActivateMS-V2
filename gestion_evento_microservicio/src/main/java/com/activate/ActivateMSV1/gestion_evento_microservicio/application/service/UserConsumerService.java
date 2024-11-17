package com.activate.ActivateMSV1.gestion_evento_microservicio.application.service;

import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.config.RabbitMQConfig;
import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.dto.communication.UserDTO;
import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.mappers.UserAdapter;
import com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.repository.user.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserConsumerService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserAdapter userAdapter;

    Logger logger = LoggerFactory.getLogger(UserConsumerService.class);

    /**
     * Receive a message from the queue and save the user in the database
     * @param user user data
     */
    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void receiveMessage(UserDTO user){
        logger.info("Received user: " + user.getName());
        com.activate.ActivateMSV1.gestion_evento_microservicio.infrastructure.repository.user.model.User userMapped = userAdapter.mapUserDTOToInfrastructure(user);
        userRepository.save(userMapped);
    }
}