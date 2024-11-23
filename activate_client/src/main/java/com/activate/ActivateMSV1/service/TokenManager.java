package com.activate.ActivateMSV1.service;
import com.activate.ActivateMSV1.infra.DTO.KeycloakResponse;
import java.time.Instant;

public class TokenManager {
    private String accessToken;
    private String refreshToken;
    private Instant tokenExpiryTime;

    public void setTokens(KeycloakResponse response) {
        this.accessToken = response.getAccessToken();
        this.refreshToken = response.getRefreshToken();
        this.tokenExpiryTime = Instant.now().plusSeconds(Long.parseLong(response.getExpiresIn()));
    }

    public String getAccessToken() throws Exception {
        if (Instant.now().isAfter(tokenExpiryTime)) {
            refreshAccessToken();
        }
        return accessToken;
    }

    private void refreshAccessToken() throws Exception {
        KeycloakResponse response = UserService.refreshToken(refreshToken);
        setTokens(response);
    }

    public void clearTokens() {
        this.accessToken = null;
        this.refreshToken = null;
        this.tokenExpiryTime = null;
    }
}
