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
    }
}