package com.example.demo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.util.ArrayList;
import java.util.List;

public class Fitness {
    private final String apiKey;
    private static List<ExerciseRecord> exerciseList;

    public Fitness() {
        apiKey = "qNen4Bpaa49UftzOcEK9zQ==QxUKFpFbMZG0i8YV";
    }
    public List<ExerciseRecord> getExcercises(){
        return exerciseList;
    }
    public void getExerciseData(String muscle, String type) throws Exception {
        StringBuilder apiUrlBuilder = new StringBuilder("https://api.api-ninjas.com/v1/exercises?X-Api-Key=" + apiKey + "&");
        if (muscle != null) {
            apiUrlBuilder.append("muscle=").append(muscle).append("&");
        }
        if (type != null) {
            apiUrlBuilder.append("type=").append(type);
        }
        String apiUrl = apiUrlBuilder.toString();
        ExtractData(FetchExercises(apiUrl));
    }

    private static void ExtractData(String jsonString) {
        System.out.println(jsonString);
        try {
            // Parse the JSON array
            JSONArray jsonArray = (JSONArray) new JSONParser().parse(jsonString);

            // Create a list to store ExerciseRecord instances
            exerciseList = new ArrayList<>();

            // Loop through each exercise in the array
            for (Object obj : jsonArray) {
                JSONObject exerciseJson = (JSONObject) obj;

                // Extract values into ExerciseRecord
                ExerciseRecord exerciseRecord = new ExerciseRecord(
                        (String) exerciseJson.get("name"),
                        (String) exerciseJson.get("type"),
                        (String) exerciseJson.get("muscle"),
                        (String) exerciseJson.get("equipment"),
                        (String) exerciseJson.get("difficulty"),
                        (String) exerciseJson.get("instructions")
                );

                // Add ExerciseRecord to the list
                exerciseList.add(exerciseRecord);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String FetchExercises(String apiUrl) throws Exception {
        StringBuilder response = new StringBuilder();

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
            } else {
                throw new RuntimeException("Failed to fetch data. Response code: " + responseCode);
            }
        } finally {
            connection.disconnect();
        }
        return response.toString();
    }
}

