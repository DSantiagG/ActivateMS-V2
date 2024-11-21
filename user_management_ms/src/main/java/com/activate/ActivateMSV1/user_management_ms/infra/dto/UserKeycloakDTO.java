package com.activate.ActivateMSV1.user_management_ms.infra.dto;


import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.Set;


@Value
@RequiredArgsConstructor
@Builder
public class UserKeycloakDTO {
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String Password;
}
