package com.activate.ActivateMSV1.infra.DTO;

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
