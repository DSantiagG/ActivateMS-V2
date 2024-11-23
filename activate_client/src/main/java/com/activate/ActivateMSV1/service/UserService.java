package com.activate.ActivateMSV1.service;

import com.activate.ActivateMSV1.infra.DTO.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class UserService {

    private static String apiUrl = "http://localhost:8084/api/activate/user";
    private static String loginUrl = "http://localhost:8085/realms/Activate-realm/protocol/openid-connect/token";
    private static String client_id = "activate-gateway-client";
    private static String client_secret = "c8AyYWdC6MIoqzxv6xhWa8HLKVWt2y3T";

    public static KeycloakResponse login(String username, String password) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(loginUrl);
        postRequest.addHeader("content-type", "application/x-www-form-urlencoded");

        StringEntity params = new StringEntity("client_id="+client_id+"&client_secret="+client_secret+"&username=" + username + "&password=" + password + "&grant_type=password");
        postRequest.setEntity(params);

        HttpResponse response = httpClient.execute(postRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            KeycloakResponse keycloakResponse = mapper.readValue(jsonResponse, KeycloakResponse.class);
            httpClient.close();
            return keycloakResponse;
        } else {
            String responseBody = EntityUtils.toString(response.getEntity());
            httpClient.close();
            throw new Exception(responseBody);
        }
    }

    public static KeycloakResponse refreshToken(String refreshToken) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost postRequest = new HttpPost(loginUrl);
        postRequest.addHeader("content-type", "application/x-www-form-urlencoded");

        StringEntity params = new StringEntity("client_id=" + client_id + "&client_secret=" + client_secret + "&refresh_token=" + refreshToken + "&grant_type=refresh_token");
        postRequest.setEntity(params);

        HttpResponse response = httpClient.execute(postRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            ObjectMapper mapper = new ObjectMapper();
            KeycloakResponse keycloakResponse = mapper.readValue(jsonResponse, KeycloakResponse.class);
            httpClient.close();
            return keycloakResponse;
        } else {
            String responseBody = EntityUtils.toString(response.getEntity());
            httpClient.close();
            throw new Exception(responseBody);
        }
    }

    public static boolean registerUser(RequestRegisterDTO user) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(user);
        String url = "http://localhost:8082/auth/register";
        HttpPost postRequest = new HttpPost(url);
        postRequest.addHeader("content-type", "application/json");
        postRequest.setEntity(new StringEntity(jsonString));

        HttpResponse response = httpClient.execute(postRequest);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 201) {
            httpClient.close();
            return true;
        } else {
            String responseBody = EntityUtils.toString(response.getEntity());
            httpClient.close();
            throw new Exception(responseBody);
        }
    }

    public static UserDTO getUser(String username, String token) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        String url = apiUrl + "/username/" + username;
        HttpGet getRequest = new HttpGet(url);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        getRequest.addHeader("Authorization", "Bearer " + token);

        HttpResponse response = httpClient.execute(getRequest);
        int statusCode = response.getStatusLine().getStatusCode();


        if (statusCode == 200) {
            String jsonResponse = EntityUtils.toString(response.getEntity());
            UserDTO user = mapper.readValue(jsonResponse, UserDTO.class);
            httpClient.close();

            return user;
        } else {

            String responseBody = EntityUtils.toString(response.getEntity());
            ErrorResponse errorResponse = mapper.readValue(responseBody, ErrorResponse.class);
            httpClient.close();
            throw new Exception(errorResponse.getMessage());
        }
    }

    public static boolean updateProfile(UserDTO user, String token) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(user);
        String url = apiUrl+"/"+user.getId()+"/profile";
        HttpPut putRequest = new HttpPut(url);
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(new StringEntity(jsonString));
        putRequest.addHeader("Authorization", "Bearer " + token);

        HttpResponse response = httpClient.execute(putRequest);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            httpClient.close();
            return true;
        } else {
            String responseBody = EntityUtils.toString(response.getEntity());
            httpClient.close();
            throw new Exception(responseBody);
        }
    }

    public static boolean addInterest(Long userId, InterestRequestDTO interest, String token) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(interest);
        String url = apiUrl+"/"+userId+"/interests/add";
        HttpPut putRequest = new HttpPut(url);
        putRequest.addHeader("Authorization", "Bearer " + token);
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(new StringEntity(jsonString));

        HttpResponse response = httpClient.execute(putRequest);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            httpClient.close();
            return true;
        } else {
            String responseBody = EntityUtils.toString(response.getEntity());
            httpClient.close();
            throw new Exception(responseBody);
        }
    }

    public static boolean removeInterest(Long userId, InterestRequestDTO interest, String token) throws Exception {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(interest);
        String url = apiUrl+"/"+userId+"/interests/remove";
        HttpPut putRequest = new HttpPut(url);
        putRequest.addHeader("Authorization", "Bearer " + token);
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(new StringEntity(jsonString));

        HttpResponse response = httpClient.execute(putRequest);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            httpClient.close();
            return true;
        } else {
            String responseBody = EntityUtils.toString(response.getEntity());
            httpClient.close();
            throw new Exception(responseBody);
        }
    }

    public static boolean updateLocation(Long userId, LocationDTO location, String token) throws Exception{
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(location);
        String url = apiUrl+"/"+userId+"/location";
        HttpPut putRequest = new HttpPut(url);
        putRequest.addHeader("Authorization", "Bearer " + token);
        putRequest.addHeader("content-type", "application/json");
        putRequest.setEntity(new StringEntity(jsonString));

        HttpResponse response = httpClient.execute(putRequest);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 200) {
            httpClient.close();
            return true;
        } else {
            String responseBody = EntityUtils.toString(response.getEntity());
            httpClient.close();
            throw new Exception(responseBody);
        }

    }
}


