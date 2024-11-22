package com.activate.ActivateMSV1.user_management_ms.infra.dto;

import lombok.Data;

import java.util.Set;

@Data
public class RequestRegisterDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
    private String email;
    private Set<InterestDTO> interests;
    private LocationDTO location;
    private String username;
    private String password;
}
