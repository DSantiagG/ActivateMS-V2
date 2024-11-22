package com.activate.ActivateMSV1.user_management_ms.service;

import com.activate.ActivateMSV1.user_management_ms.controller.UserController;
import com.activate.ActivateMSV1.user_management_ms.domain.Location;
import com.activate.ActivateMSV1.user_management_ms.domain.User;
import com.activate.ActivateMSV1.user_management_ms.infra.dto.*;
import com.activate.ActivateMSV1.user_management_ms.infra.mappers.UserAdapter;
import com.activate.ActivateMSV1.user_management_ms.infra.repository.UserRepository;
import com.activate.ActivateMSV1.user_management_ms.infra.repository.model.ModUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAdapter userAdapter;

    @Autowired
    private UserPublisherService userPublisherService;

    Logger logger = LoggerFactory.getLogger(UserService.class);


    public UserDTO getUserById(Long id) throws Exception {
        ModUser user = userRepository.findById(id).orElseThrow(() -> new Exception("User "+id +" not found"));
        return userAdapter.mapModelUserToDTO(user);
    }

    public ArrayList<UserDTO> getUsers(){
        ArrayList<UserDTO> users = new ArrayList<>();
        for (ModUser user : userRepository.findAll()) {
            users.add(userAdapter.mapModelUserToDTO(user));
        }
        return users;
    }

    public void editProfile(Long id, String name, int age, String email) throws Exception {
        ModUser userModel = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        User userDomain = userAdapter.mapModelUserToDomain(userModel);
        userDomain.editProfile(name, age, email);
        ModUser userMapped = userAdapter.mapDomainUserToModel(userDomain);
        userRepository.save(userMapped);
        logger.info("User {} updated in database",id);
        userPublisherService.publishUserToEvent(userMapped);
        logger.info("User info {} sent to events-ms",id);
        userPublisherService.publishUserToRecommendation(userMapped);
        logger.info("User info {} sent to recommendation-ms",id);

    }

    public void addInterest(Long id, InterestDTO interest) throws Exception {
        ModUser userModel = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        User userDomain = userAdapter.mapModelUserToDomain(userModel);
        userDomain.addInterest(interest);
        ModUser userMapped = userAdapter.mapDomainUserToModel(userDomain);

        userRepository.save(userMapped);
        logger.info("User interest (add) {} updated in database",id);
        userPublisherService.publishUserToEvent(userMapped);
        logger.info("User interest (add) {} sent to events-ms",id);
        userPublisherService.publishUserToRecommendation(userMapped);
        logger.info("User interest (add) {} sent to recommendation-ms",id);
    }

    public void deleteInterest(Long id, InterestDTO interest) throws Exception {
        ModUser userModel = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        User userDomain = userAdapter.mapModelUserToDomain(userModel);
        userDomain.deleteInterest(interest);
        ModUser userMapped = userAdapter.mapDomainUserToModel(userDomain);

        userRepository.save(userMapped);
        logger.info("User interest (delete) {} updated in database",id);
        userPublisherService.publishUserToEvent(userMapped);
        logger.info("User interest (delete) {} sent to events-ms",id);
        userPublisherService.publishUserToRecommendation(userMapped);
        logger.info("User interest (delete) {} sent to recommendation-ms",id);
    }

    public void udpateLocation(Long id, LocationDTO location) throws Exception {
        ModUser userModel = userRepository.findById(id).orElseThrow(() -> new Exception("User not found"));
        User userDomain = userAdapter.mapModelUserToDomain(userModel);
        userDomain.updateLocation(new Location(location.getLatitude(), location.getLongitude()));
        ModUser userMapped = userAdapter.mapDomainUserToModel(userDomain);

        userRepository.save(userMapped);
        logger.info("User location {} updated in database",id);
        userPublisherService.publishUserToEvent(userMapped);
        logger.info("User location {} sent to events-ms",id);
        userPublisherService.publishUserToRecommendation(userMapped);
        logger.info("User location {} sent to recommendation-ms",id);
    }


}
