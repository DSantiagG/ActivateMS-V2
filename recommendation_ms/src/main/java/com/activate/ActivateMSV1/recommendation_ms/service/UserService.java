package com.activate.ActivateMSV1.recommendation_ms.service;

import com.activate.ActivateMSV1.recommendation_ms.infra.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.activate.ActivateMSV1.recommendation_ms.infra.model.User;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(UserService.class);

    public User getUser(Long id) {
        logger.info("Received request to get user with id: " + id);
        User user = userRepository.findById(id.toString()).orElse(null);
        if (user == null) {
            return user;
        }
        logger.info("User {} with id {} found", user.getName(), user.getId());
        return user;
    }

    public List<User> getAllUsers() {
        logger.info("Received request to get all users");
        return userRepository.findAll();
    }

}
