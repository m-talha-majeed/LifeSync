package com.example.demo;

import org.aspectj.weaver.ast.Not;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LoginInfo extends CrudRepository<LogInData, Integer> {
    List<LogInData> findAllByUsernameAndPassword(String username, String Password);

    // Retrieve the LogInData by userID
    Optional<LogInData> findById(Integer userId);

    // Example method to update the username by userID
    default void updateLocationByUserId(Integer userId, String ip, String city, String region, String country,
                                        float latitude, float longitude, String postal, String timeZone) {
        // Step 1: Retrieve the LogInData by userID
        Optional<LogInData> optionalLogInData = findById(userId);

        // Step 2: Update the fields if the LogInData exists
        optionalLogInData.ifPresent(logInData -> {
            logInData.setIp(ip);
            logInData.setCity(city);
            logInData.setRegion(region);
            logInData.setCountry(country);
            logInData.setLatitude(latitude);
            logInData.setLongitude(longitude);
            logInData.setPostal(postal);
            logInData.setTimezone(timeZone);
            // Step 3: Save the updated LogInData
            save(logInData);
        });
    }
    default void updateGroceryByUserId(Integer userId, String Description, String Type, String Title,int amount) {
        // Step 1: Retrieve the LogInData by userID
        Optional<LogInData> optionalLogInData = findById(userId);

        // Step 2: Update the fields if the LogInData exists
        optionalLogInData.ifPresent(logInData -> {
            logInData.setGroceryDesc(Description);
            logInData.setGroceryAmount(amount);
            logInData.setGroceryTitle(Title);
            logInData.setGroceryType(Type);
            // Step 3: Save the updated LogInData
            save(logInData);
        });
    }
    default void updateExpensesByUserId(Integer userId,Integer Expenses) {
        // Step 1: Retrieve the LogInData by userID
        Optional<LogInData> optionalLogInData = findById(userId);

        // Step 2: Update the fields if the LogInData exists
        optionalLogInData.ifPresent(logInData -> {
            logInData.setExpenses(Expenses);
            // Step 3: Save the updated LogInData
            save(logInData);
        });
    }
    default void updateClassroomByUserId(Integer userId,String ClassroomCredentials) {
        // Step 1: Retrieve the LogInData by userID
        Optional<LogInData> optionalLogInData = findById(userId);

        // Step 2: Update the fields if the LogInData exists
        optionalLogInData.ifPresent(logInData -> {
            logInData.setClassroomCredentials(ClassroomCredentials);
            // Step 3: Save the updated LogInData
            save(logInData);
        });
    }
    default void updateWeatherByUserId(Integer userId, String city, String region, String country, String LastUpdated, double temperatureC,
             String ConditionW,double temperatureF) {
        // Step 1: Retrieve the LogInData by userID
        Optional<LogInData> optionalLogInData = findById(userId);

        // Step 2: Update the fields if the LogInData exists
        optionalLogInData.ifPresent(logInData -> {
            logInData.setTemperatureC(temperatureC);
            logInData.setWeatherRegion(region);
            logInData.setWeatherCity(city);
            logInData.setWeatherCountry(country);
            logInData.setTemperatureF(temperatureF);
            logInData.setLastUpdated(LastUpdated);
            logInData.setCondition(ConditionW);
            // Step 3: Save the updated LogInData
            save(logInData);
        });
    }
    default void updateFinancesByUserId(Integer userId,int savings,int expenses,int investment,int budget,int income) {
        // Step 1: Retrieve the LogInData by userID
        Optional<LogInData> optionalLogInData = findById(userId);

        // Step 2: Update the fields if the LogInData exists
        optionalLogInData.ifPresent(logInData -> {
            logInData.setInvestments(investment);
            logInData.setIncome(income);
            logInData.setBudget(budget);
            logInData.setSavings(savings);
            logInData.setExpenses(expenses);
            // Step 3: Save the updated LogInData
            save(logInData);
        });
    }
    default void updateTokenByUserId(Integer userId,String Token) {
        // Step 1: Retrieve the LogInData by userID
        Optional<LogInData> optionalLogInData = findById(userId);
        // Step 2: Update the fields if the LogInData exists
        optionalLogInData.ifPresent(logInData -> {
           logInData.setToken(Token);
           // Step 3: Save the updated LogInData
            save(logInData);
        });
    }
}
