package com.example.demo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class Weather {
    private final String apiKey;
    private static WeatherDetails wDetail;

    public Weather() {
        apiKey = "937a856871934ff6ab9195906230211";
    }

    public void fetchWeatherData(String location) {
        try {
            String apiUrl = "https://api.weatherapi.com/v1/current.json?key=" + apiKey + "&q=" + location;

            // Create a URL object from the API URL
            URL url = new URL(apiUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // If the response code is OK, read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                // Parse the weather data
                parseWeatherData(response.toString());
            } else {
                // If the response code is not OK, print an error message
                System.out.println("Error: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void PrintWeatherData() {
        System.out.println("City: " + wDetail.city());
        System.out.println("Region: " + wDetail.region());
        System.out.println("Country: " + wDetail.country());
        System.out.println("\nCurrent Weather:");
        System.out.println("Last Updated: " + wDetail.LastUpdated());
        System.out.println("Temperature (C): " + wDetail.temperatureC());
        System.out.println("Temperature (F): " + wDetail.temperatureF());
        System.out.println("Condition: " + wDetail.Condition());
    }

    private static void parseWeatherData(String jsonData) {
        JSONParser parser = new JSONParser();
        try {
            // Parse JSON data into a JSONObject
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);

            // Extract data from the JSONObject
            JSONObject location = (JSONObject) jsonObject.get("location");
            String cityName = (String) location.get("name");
            String region = (String) location.get("region");
            String country = (String) location.get("country");
            double latitude = (double) location.get("lat");
            double longitude = (double) location.get("lon");
            JSONObject current = (JSONObject) jsonObject.get("current");
            long lastUpdatedEpoch = (long) current.get("last_updated_epoch");
            String lastUpdated = (String) current.get("last_updated");
            double temperatureC = (double) current.get("temp_c");
            double temperatureF = (double) current.get("temp_f");
            boolean isDay = (long) current.get("is_day") == 1;
            String conditionText = (String) ((JSONObject) current.get("condition")).get("text");
            wDetail = new WeatherDetails(cityName, region, country, lastUpdated, temperatureC, temperatureF, conditionText);
            // Print the extracted information
            PrintWeatherData();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getCity() {
        return wDetail.city();
    }

    public String getRegion() {
        return wDetail.region();
    }

    public String getCountry() {
        return wDetail.country();
    }

    public String getLastUpdated() {
        return wDetail.LastUpdated();
    }

    public double getTemperatureC() {
        return wDetail.temperatureC();
    }

    public double getTemperatureF() {
        return wDetail.temperatureF();
    }

    public String getCondition() {
        return wDetail.Condition();
    }
}

record WeatherDetails(String city, String region, String country, String LastUpdated, double temperatureC,
                      double temperatureF, String Condition) {

}