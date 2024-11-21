package com.activate.ActivateMSV1.user_management_ms.infra.repository;

import org.keycloak.representations.idm.UserRepresentation;
import com.activate.ActivateMSV1.user_management_ms.infra.dto.UserKeycloakDTO;

import java.util.List;

public interface IKeycloakRepository {

    List<UserRepresentation> findAllUsers();
    List<UserRepresentation> findUserByUsername(String userName);
    boolean createUser (UserKeycloakDTO userKC);
    boolean updateUser (String userId,UserKeycloakDTO userKC);


}
