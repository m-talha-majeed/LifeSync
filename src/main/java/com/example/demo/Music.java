package com.example.demo;
import java.net.URI;
import java.net.http.*;
import java.util.Base64;
import java.nio.charset.StandardCharsets;
import java.awt.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.net.http.*;
import java.net.http.HttpRequest.*;
import java.net.http.HttpResponse.*;
import java.util.concurrent.*;
import java.util.Scanner;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class Music {
    private static AccessToken accTok;
    private final String clientId;
    private final String clientSecret;
    private final int localServerPort; // Set a port for your local server
    public Music(){
        clientId="23bd1f410ecf44e29237f5635a2abac6";
        clientSecret="bf894a0de7104efcb353f51cb61a852b";
        accTok=new AccessToken(null,null,null);
        localServerPort = 8000;
    }
    public Music(String AccToken){
        clientId="23bd1f410ecf44e29237f5635a2abac6";
        clientSecret="bf894a0de7104efcb353f51cb61a852b";
        accTok=new AccessToken(AccToken,null,null);
        localServerPort = 8000;
    }
    public String authenticator() throws URISyntaxException, IOException {
        // Spotify API credentials
        String redirectUri = "http://localhost:8080/LifeSync/Homepage"; // Set this to your registered redirect URI

        // Authorization endpoint
        String authorizationEndpoint = "https://accounts.spotify.com/authorize";

        // Token endpoint
        String tokenEndpoint = "https://accounts.spotify.com/api/token";

        // Spotify API scopes
        String scopes = "user-read-currently-playing user-modify-playback-state user-read-playback-state app-remote-control"; // Add the scopes your application needs

        // Construct the authorization URL
        String encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8);
        String encodedScopes = URLEncoder.encode(scopes, StandardCharsets.UTF_8);

        String authorizationUrl = String.format("%s?client_id=%s&response_type=code&redirect_uri=%s&scope=%s",
                authorizationEndpoint, clientId, encodedRedirectUri, encodedScopes);
        System.out.println("Authorization URL: " + authorizationUrl);
        return authorizationUrl;

    }
    public String GetAccessToken(String code){
        String redirectUri = "http://localhost:8080/LifeSync/Homepage"; // Set this to your registered redirect URI

        // Token endpoint
        String tokenEndpoint = "https://accounts.spotify.com/api/token";
        // Exchange authorization code for an access token
        String tokenRequestData = String.format("grant_type=authorization_code&code=%s&redirect_uri=%s",
                code,redirectUri);
        String clientCredentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes(StandardCharsets.UTF_8));
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest tokenRequest = HttpRequest.newBuilder()
                .uri(URI.create(tokenEndpoint))
                .header("Authorization", "Basic " + encodedCredentials)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(tokenRequestData))
                .build();

        try {
            HttpResponse<String> tokenResponse = httpClient.send(tokenRequest, HttpResponse.BodyHandlers.ofString());
            parseAccessToken(tokenResponse.body());
            printTokenInfo();
            return accTok.token();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static void printTokenInfo(){
        System.out.println("Access Token: " + accTok.token());
        System.out.println("Token Type: " + accTok.TokenType());
        System.out.println("Expiry Time: " + accTok.ExpiryTime());
    }
    private static boolean isPlaybackPlaying(String jsonString) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject json = (JSONObject) parser.parse(jsonString);
            return (boolean) json.get("is_playing");
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Return false if there's an exception or the key is not present
        }
    }
    private static void parseAccessToken(String jsonData) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);

            // Extract values into Java variables
            String accessToken = (String) jsonObject.get("access_token");
            String tokenType = (String) jsonObject.get("token_type");
            Long expiresIn = (Long) jsonObject.get("expires_in");
            accTok=new AccessToken(accessToken,tokenType,expiresIn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static boolean isNotInitialized(String variable) {
        // Check if the variable is null
        return variable == null;
    }
    public String refreshAccessToken(String refreshToken) {
        // Spotify API endpoint for token refresh
        String tokenRefreshEndpoint = "https://accounts.spotify.com/api/token";
        System.out.println("Refreshing Token");
        // Construct the token refresh request
        String tokenRefreshRequestData = String.format("grant_type=refresh_token&refresh_token=%s", refreshToken);
        String clientCredentials = clientId + ":" + clientSecret;
        String encodedCredentials = Base64.getEncoder().encodeToString(clientCredentials.getBytes(StandardCharsets.UTF_8));

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest tokenRefreshRequest = HttpRequest.newBuilder()
                .uri(URI.create(tokenRefreshEndpoint))
                .header("Authorization", "Basic " + encodedCredentials)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(tokenRefreshRequestData))
                .build();

        try {
            HttpResponse<String> tokenRefreshResponse = httpClient.send(tokenRefreshRequest, HttpResponse.BodyHandlers.ofString());
            parseAccessToken(tokenRefreshResponse.body());
            printTokenInfo();
            return accTok.token();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public void getUserDevices(String accessToken) {
        // Spotify API endpoint for getting user's devices
        String devicesEndpoint = "https://api.spotify.com/v1/me/player/devices";
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(devicesEndpoint))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful (HTTP status code 200)
            if (response.statusCode() == 200) {
                System.out.println("User Devices:");
                System.out.println(response.body());
            } else if (response.statusCode()==401){
                getUserDevices(refreshAccessToken(accTok.token()));
            }else {
                System.err.println("Error retrieving user devices. Status code: " + response.statusCode());
                System.err.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean getCurrentTrack(String accessToken) {
        // Spotify API endpoint for getting the user's currently playing track
        String currentTrackEndpoint = "https://api.spotify.com/v1/me/player/currently-playing";
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(currentTrackEndpoint))
                .header("Authorization", "Bearer " + accessToken)
                .build();
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful (HTTP status code 200)
            if (response.statusCode() == 200) {
                System.out.println("Currently Playing Track:");
                System.out.println(response.body());
                return isPlaybackPlaying(response.toString());
                // Process the response JSON as needed
            }else if (response.statusCode()==401){

                getCurrentTrack(refreshAccessToken(accTok.token()));
            } else {
                System.err.println("Error retrieving currently playing track. Status code: " + response.statusCode());
                System.err.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    public void pauseOrResumePlayback(String accessToken) {
        boolean resume=getCurrentTrack(accessToken);
        // Spotify API endpoint for pausing or resuming playback
        String playbackControlEndpoint = "https://api.spotify.com/v1/me/player/play";

        // If resume is false, change the endpoint to pause
        if (!resume) {
            playbackControlEndpoint = "https://api.spotify.com/v1/me/player/pause";
        }

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(playbackControlEndpoint))
                .header("Authorization", "Bearer " + accessToken)
                .PUT(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful (HTTP status code 204 for pause/resume)
            if (response.statusCode() == 204) {
                if (resume) {
                    System.out.println("Playback resumed successfully.");
                } else {
                    System.out.println("Playback paused successfully.");
                }
            } else if (response.statusCode()==401){
                pauseOrResumePlayback(refreshAccessToken(accessToken));
            }else {
                System.err.println("Error pausing/resuming playback. Status code: " + response.statusCode());
                System.err.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void skipToNextTrack(String accessToken) {
        // Spotify API endpoint for skipping to the next track
        String skipToNextEndpoint = "https://api.spotify.com/v1/me/player/next";

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(skipToNextEndpoint))
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful (HTTP status code 204 for skip to next track)
            if (response.statusCode() == 204) {
                System.out.println("Skipped to the next track successfully.");
            } else if (response.statusCode()==401){
                if (isNotInitialized(accTok.token())){
                    authenticator();
                }
                else{
                    refreshAccessToken(accTok.token());
                }
                skipToNextTrack(accTok.token());
            }else {
                System.err.println("Error skipping to the next track. Status code: " + response.statusCode());
                System.err.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void skipToPreviousTrack(String accessToken) {
        // Spotify API endpoint for skipping to the previous track
        String skipToPreviousEndpoint = "https://api.spotify.com/v1/me/player/previous";

        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(skipToPreviousEndpoint))
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful (HTTP status code 204 for skip to previous track)
            if (response.statusCode() == 204) {
                System.out.println("Skipped to the previous track successfully.");
            } else if (response.statusCode()==401){
                if (isNotInitialized(accTok.token())){
                    authenticator();
                }
                else{
                    refreshAccessToken(accTok.token());
                }
                skipToPreviousTrack(accTok.token());
            }else {
                System.err.println("Error skipping to the previous track. Status code: " + response.statusCode());
                System.err.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPlaybackVolume(String accessToken, int volumeLevel) {
        // Spotify API endpoint for setting the playback volume
        String setVolumeEndpoint = "https://api.spotify.com/v1/me/player/volume";

        HttpClient httpClient = HttpClient.newHttpClient();

        // Ensure the volume level is within the valid range (0 to 100)
        int adjustedVolume = Math.max(0, Math.min(100, volumeLevel));

        // Build the JSON payload
        String jsonPayload = String.format("{\"volume_percent\": %d}", adjustedVolume);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(setVolumeEndpoint))
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the request was successful (HTTP status code 204 for set volume)
            if (response.statusCode() == 204) {
                System.out.println("Playback volume set successfully to " + adjustedVolume + "%.");
            } else if (response.statusCode() == 400) {
                System.err.println("Error setting playback volume. Status code: " + response.statusCode());
                System.err.println("Response: " + response.body());
            } else if (response.statusCode() == 401) {
                // Handle token refresh here
                if (isNotInitialized(accTok.token())){
                    authenticator();
                }
                else{
                    refreshAccessToken(accTok.token());
                }
                setPlaybackVolume(accTok.token(), volumeLevel);
            } else {
                System.err.println("Unexpected status code: " + response.statusCode());
                System.err.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        return accTok.token();
    }
}
record AccessToken(String token,String TokenType,Long ExpiryTime){}