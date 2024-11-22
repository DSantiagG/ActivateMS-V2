package com.activate.ActivateMSV1.user_management_ms.service;

import com.activate.ActivateMSV1.user_management_ms.domain.Location;
import com.activate.ActivateMSV1.user_management_ms.domain.User;
import com.activate.ActivateMSV1.user_management_ms.infra.dto.InterestDTO;
import com.activate.ActivateMSV1.user_management_ms.infra.dto.LocationDTO;
import com.activate.ActivateMSV1.user_management_ms.infra.dto.UserKeycloakDTO;
import com.activate.ActivateMSV1.user_management_ms.infra.mappers.UserAdapter;
import com.activate.ActivateMSV1.user_management_ms.infra.repository.CredentialsRepository;
import com.activate.ActivateMSV1.user_management_ms.infra.repository.UserRepository;
import com.activate.ActivateMSV1.user_management_ms.infra.repository.model.Credentials;
import com.activate.ActivateMSV1.user_management_ms.infra.repository.model.ModUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserAdapter userAdapter;

    @Autowired
    private UserPublisherService userPublisherService;

    @Autowired
    private CredentialsRepository credentialsRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private KeycloakService keycloakService;


    public void createUser(String firstName,String lastName, int age, String email, Set<InterestDTO> interests, LocationDTO locationDTO,String userName , String password) throws Exception {
        Location location = new Location(locationDTO.getLatitude(), locationDTO.getLongitude());
        HashSet<InterestDTO> interestsSet = new HashSet<>(interests);
        User user = new User(-1L,firstName +" "+ lastName, age, email, interestsSet,location);
        ModUser userMapped = userAdapter.mapDomainUserToModel(user);
        Credentials credentials = new Credentials(null,userName,
                passwordEncoder.encode(password),
                userMapped);
        userMapped.setCredentials(credentials);
        userRepository.save(userMapped);

        UserKeycloakDTO userKeycloakDTO = new UserKeycloakDTO(userName, email, firstName, lastName, password);
        keycloakService.createUser(userKeycloakDTO);

        userPublisherService.publishUserToEvent(userMapped);
        userPublisherService.publishUserToRecommendation(userMapped);
    }

}
