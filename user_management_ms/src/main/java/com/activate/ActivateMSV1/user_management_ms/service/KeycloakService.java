package com.activate.ActivateMSV1.user_management_ms.service;

import com.activate.ActivateMSV1.user_management_ms.infra.dto.UserKeycloakDTO;
import com.activate.ActivateMSV1.user_management_ms.infra.repository.IKeycloakRepository;
import com.activate.ActivateMSV1.user_management_ms.infra.util.KeycloakProvider;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class KeycloakService implements IKeycloakRepository {
    @Override
    public List<UserRepresentation> findAllUsers() {
        return KeycloakProvider.getUserResource().list();
    }

    @Override
    public List<UserRepresentation> findUserByUsername(String userName) {
        return KeycloakProvider.getUserResource().searchByUsername(userName,true);
    }

    @Override
    public boolean createUser(UserKeycloakDTO userKC) {
        int status=0;
        UsersResource userResource = KeycloakProvider.getUserResource();
        UserRepresentation user = buildUserRepresentation(userKC);
        System.out.println("antes de crear usuario");
        Response response = userResource.create(user);
        System.out.println("despues de crear usuario");
        status=response.getStatus();

        boolean res=false;
        //Usuario creado correctamente
        if(status==201){
            String path=response.getLocation().getPath();
            String userId = path.substring(path.lastIndexOf('/') + 1);
            CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
            credentialRepresentation.setTemporary(false);
            credentialRepresentation.setType(OAuth2Constants.PASSWORD);
            credentialRepresentation.setValue(userKC.getPassword());

            userResource.get(userId).resetPassword(credentialRepresentation);

            res= true;
        }

        return res;
    }

    @Override
    public boolean updateUser(String userId, UserKeycloakDTO userKC) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setType(OAuth2Constants.PASSWORD);
        credentialRepresentation.setValue(userKC.getPassword());

        UserRepresentation user = buildUserRepresentation(userKC);
        user.setCredentials(Collections.singletonList(credentialRepresentation));

        UserResource userResource = KeycloakProvider.getUserResource().get(userId);
        userResource.update(user);
        return true;
    }

    private UserRepresentation buildUserRepresentation(UserKeycloakDTO userKC){
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userKC.getUsername());
        user.setEmail(userKC.getEmail());
        user.setFirstName(userKC.getFirstName());
        user.setLastName(userKC.getLastName());
        user.setEmailVerified(true);
        user.setEnabled(true);
        return user;
    }
}
