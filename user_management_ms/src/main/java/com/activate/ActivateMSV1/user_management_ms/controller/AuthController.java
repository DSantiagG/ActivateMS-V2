package com.activate.ActivateMSV1.user_management_ms.controller;

import com.activate.ActivateMSV1.user_management_ms.infra.dto.RequestRegisterDTO;
import com.activate.ActivateMSV1.user_management_ms.infra.dto.UserDTO;
import com.activate.ActivateMSV1.user_management_ms.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    Logger logger = LoggerFactory.getLogger(AuthController.class);
    /**
     * Creates a new user.
     *
     * Accepts a `UserDTO` in the request body and uses `UserService` to create and save the user.
     * Returns a `201 Created` status on success, or `400 Bad Request` with an error message on failure.
     *
     * @param userDTO The user's data (name, age, email, interests, location).
     * @return `201 Created` on success, or `400 Bad Request` on error.
     */
    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody RequestRegisterDTO userDTO) {
        String msg;
        try {
            authService.createUser(userDTO.getFirstName(),userDTO.getLastName(), userDTO.getAge(), userDTO.getEmail(), userDTO.getInterests(), userDTO.getLocation(),userDTO.getUsername(), userDTO.getPassword());
            msg="User "+userDTO.getUsername()+" created successfully";
            logger.info(msg);
            return ResponseEntity.status(HttpStatus.CREATED).body(msg);
        } catch (Exception e) {
            msg="Error creating user: "+ userDTO.getUsername()+" ---- \n"+e.getMessage();
            logger.error(msg);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

}
