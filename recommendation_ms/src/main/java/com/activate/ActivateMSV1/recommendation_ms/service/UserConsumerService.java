package com.activate.ActivateMSV1.recommendation_ms.service;

import com.activate.ActivateMSV1.recommendation_ms.infra.DTO.UserDTO;
import com.activate.ActivateMSV1.recommendation_ms.infra.config.RabbitMQConfig;
import com.activate.ActivateMSV1.recommendation_ms.infra.mappers.UserMapper;
import com.activate.ActivateMSV1.recommendation_ms.infra.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserConsumerService {

    @Autowired
    private UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(UserConsumerService.class);

    @RabbitListener(queues = RabbitMQConfig.USER_QUEUE)
    public void receiveMessage(UserDTO user) {
        logger.info("Entering receiveMessage");
        if(user == null) {
            logger.error("Received null user");
            return;
        }
        logger.info("Received user: {}", user.getName());
        userRepository.save(UserMapper.INSTANCE.toRepoModelUser(user));
        logger.info("User {} with id {} saved", user.getName(), user.getId());
    }
}
