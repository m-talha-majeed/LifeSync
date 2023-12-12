package com.example.demo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.print.attribute.standard.PrinterLocation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Location {
    private final String apiKey;
    private static LocationInfo LInfo;

    public Location() {
        this.apiKey = "bde8ae92a04727";
    }

    public String getCity() {
        return LInfo.city();
    }

    public void FetchLocation() {
        try {
            // Replace "YOUR_API_TOKEN" with your actual API token
            String apiUrl = "https://ipinfo.io?token=" + apiKey;

            // Create a URL object with the API URL
            URL url = new URL(apiUrl);

            // Open a connection to the URL
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set the request method to GET
            connection.setRequestMethod("GET");

            // Get the response code
            int responseCode = connection.getResponseCode();

            // Check if the request was successful (status code 200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Create a BufferedReader to read the response
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                // Read the response line by line
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                // Close the BufferedReader
                reader.close();
                extractData(response.toString());

            } else {
                System.out.println("Error: Unable to fetch data. Response code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void extractData(String jsonData) {
        try {
            // Create a JSONParser
            JSONParser parser = new JSONParser();

            // Parse JSON data into a JSONObject
            JSONObject jsonObject = (JSONObject) parser.parse(jsonData);

            // Extract values from the JSONObject
            String ip = (String) jsonObject.get("ip");
            String city = (String) jsonObject.get("city");
            String region = (String) jsonObject.get("region");
            String country = (String) jsonObject.get("country");
            String loc = (String) jsonObject.get("loc");
            String postal = (String) jsonObject.get("postal");
            String timezone = (String) jsonObject.get("timezone");

            // Split the 'loc' field into latitude and longitude
            String[] locArray = loc.split(",");
            float latitude = Float.parseFloat(locArray[0].trim());
            float longitude = Float.parseFloat(locArray[1].trim());
            LInfo = new LocationInfo(ip, city, region, country, latitude, longitude, postal, timezone);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIp() {
        return LInfo.ip();
    }

    public String getRegion() {
        return LInfo.region();
    }

    public String getCountry() {
        return LInfo.country();
    }


    public float getLatitude() {
        return LInfo.latitude();
    }

    public float getLongitude() {
        return LInfo.longitude();
    }
    public String getPostal() {
        return LInfo.postal();
    }
    public String getTimezone() {
        return LInfo.timezone();
    }
}

record LocationInfo(String ip, String city, String region, String country, float latitude, float longitude,
                    String postal, String timezone) {
    // Empty body, as records automatically provide toString(), equals(), and hashCode() implementations
}