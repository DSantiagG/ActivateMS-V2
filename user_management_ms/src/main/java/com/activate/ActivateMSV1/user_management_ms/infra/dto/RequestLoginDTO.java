package com.activate.ActivateMSV1.user_management_ms.infra.dto;

import lombok.Data;

@Data
public class RequestLoginDTO {
    private String username;
    private String password;
}
