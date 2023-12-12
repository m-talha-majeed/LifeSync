package com.example.demo;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class NewsApiService {
    private final String apiKey;
    private List<NewsArticle> Articles;

    public NewsApiService() {
        apiKey = "dff97ace2dc348c5bd4ae5ab815bf549";
    }

    public void searchTopHeadlines(String country, String category) {
        try {
            String apiUrl = "https://newsapi.org/v2/top-headlines?apiKey="+apiKey;
            if (country != null&&!(country.isEmpty())) {
                apiUrl=apiUrl+"&country="+country;
            }
            if ((category != null) && !(category.isEmpty())) {
                apiUrl=apiUrl+"&category="+category;
            }else{
                apiUrl=apiUrl+"&language=en";
            }
            JSONObject jsonResponse = fetchDataFromAPI(apiUrl);
            Articles = extractArticles(jsonResponse);

        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private JSONObject fetchDataFromAPI(String apiUrl) throws IOException, ParseException {
        URL urlApi = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) urlApi.openConnection();
        conn.setRequestMethod("GET");
        conn.connect();

        int responseCode = conn.getResponseCode();

        if (responseCode != 200) {
            throw new RuntimeException("HTTP response Code: " + responseCode);
        }

        StringBuilder txt = new StringBuilder();
        Scanner scanner = new Scanner(urlApi.openStream());
        while (scanner.hasNext()) {
            txt.append(scanner.nextLine());
        }
        scanner.close();

        JSONParser parser = new JSONParser();
        return (JSONObject) parser.parse(txt.toString());
    }

    private List<NewsArticle> extractArticles(JSONObject jsonResponse) {
        List<NewsArticle> articlesList = new ArrayList<>();

        if (jsonResponse.containsKey("articles")) {
            Object articlesObj = jsonResponse.get("articles");
            if (articlesObj instanceof List) {
                List<JSONObject> jsonArticlesList = (List<JSONObject>) articlesObj;
                for (JSONObject jsonArticle : jsonArticlesList) {
                    String title = (String) jsonArticle.get("title");
                    String author = (String) jsonArticle.get("author");
                    String description = (String) jsonArticle.get("description");
                    String url = (String) jsonArticle.get("url");
                    NewsArticle article = new NewsArticle(title, author, description, url);
                    articlesList.add(article);
                }
            }
        }
        return articlesList;
    }

    public List<NewsArticle> getTopHeadlines() {
        return Articles;
    }
}
