package com.activate.ActivateMSV1;

import com.activate.ActivateMSV1.infra.DTO.EventDTO;
import com.activate.ActivateMSV1.infra.DTO.EventInfoDTO;
import com.activate.ActivateMSV1.presentation.LoginView;
import com.activate.ActivateMSV1.service.RecommendationService;
import com.activate.ActivateMSV1.service.TokenManager;
import com.activate.ActivateMSV1.service.UserService;

import java.util.ArrayList;

public class App 
{
    public static void main( String[] args )
    {
        LoginView loginView = new LoginView();
        loginView.show();

        //TODO: BORRAR
        ArrayList<EventInfoDTO> recommendedEvents = new ArrayList<>();
        TokenManager tokenManager = new TokenManager();
        try{
            tokenManager.setTokens(UserService.login("test", "test"));
            recommendedEvents = RecommendationService.getRecommendedEvents(1L, tokenManager.getAccessToken());
        }catch (Exception e){
            System.out.println("Error getting recommended events: " + e.getMessage());
        }

        recommendedEvents.forEach(event -> {
            System.out.println(event.getName());
            System.out.println(event.getDescription());
            System.out.println(event.getInterests());
        });
    }
}