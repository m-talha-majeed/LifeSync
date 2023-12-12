package com.example.demo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import javax.annotation.processing.Generated;
@Entity
public class LogInData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer UserID;
    private String username;
    private String password;
    private String name;
    private String email;
    private Integer expenses;
    private Integer budget;
    private Integer income;
    private String token;
    private Integer savings;
    private Integer investments;
    private String ip;
    private String city;
    private String region;
    private String country;
    private float latitude;
    private float longitude;
    private String postal;
    private String timezone;
    private String weatherCity;
    private String weatherRegion;
    private String weatherCountry;
    private String LastUpdated;
    private double temperatureC;
    private double temperatureF;
    private String ConditionOfWeather;
    private String ClassroomCredentials;
    private String groceryTitle;
    private String groceryDesc;
    private String groceryType;
    private int groceryAmount;
    public LogInData(){
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getUserID() {
        return UserID;
    }

    // Setter
    public void setUserID(Integer userID) {
        UserID = userID;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public Integer getExpenses() {
        return expenses;
    }

    public void setExpenses(Integer expenses) {
        this.expenses = expenses;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getSavings() {
        return savings;
    }

    public void setSavings(Integer savings) {
        this.savings = savings;
    }

    public Integer getInvestments() {
        return investments;
    }

    public void setInvestments(Integer investments) {
        this.investments = investments;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
    public String getGroceryTitle() {
        return groceryTitle;
    }

    // Setter for GroceryTitle
    public void setGroceryTitle(String groceryTitle) {
        this.groceryTitle = groceryTitle;
    }

    // Getter for GroceryDesc
    public String getGroceryDesc() {
        return groceryDesc;
    }

    // Setter for GroceryDesc
    public void setGroceryDesc(String groceryDesc) {
        this.groceryDesc = groceryDesc;
    }

    // Getter for GroceryType
    public String getGroceryType() {
        return groceryType;
    }

    // Setter for GroceryType
    public void setGroceryType(String groceryType) {
        this.groceryType = groceryType;
    }

    // Getter for GroceryAmount
    public int getGroceryAmount() {
        return groceryAmount;
    }

    // Setter for GroceryAmount
    public void setGroceryAmount(int groceryAmount) {
        this.groceryAmount = groceryAmount;
    }
    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getWeatherCity() {
        return weatherCity;
    }

    public void setWeatherCity(String weatherCity) {
        this.weatherCity = weatherCity;
    }

    public String getWeatherRegion() {
        return weatherRegion;
    }

    public void setWeatherRegion(String weatherRegion) {
        this.weatherRegion = weatherRegion;
    }

    public String getWeatherCountry() {
        return weatherCountry;
    }

    public void setWeatherCountry(String weatherCountry) {
        this.weatherCountry = weatherCountry;
    }

    public String getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        LastUpdated = lastUpdated;
    }

    public double getTemperatureC() {
        return temperatureC;
    }

    public void setTemperatureC(double temperatureC) {
        this.temperatureC = temperatureC;
    }

    public double getTemperatureF() {
        return temperatureF;
    }

    public void setTemperatureF(double temperatureF) {
        this.temperatureF = temperatureF;
    }

    public String getCondition() {
        return ConditionOfWeather;
    }

    public void setCondition(String condition) {
        ConditionOfWeather = condition;
    }

    public String getClassroomCredentials() {
        return ClassroomCredentials;
    }

    public void setClassroomCredentials(String classroomCredentials) {
        ClassroomCredentials = classroomCredentials;
    }
    public void setAllData(Integer userID, String username, String password, String name, String email,
                           Integer expenses, Integer budget, Integer income, String token, Integer savings,
                           Integer investments, String ip, String city, String region, String country,
                           float latitude, float longitude, String postal, String timezone,
                           String weatherCity, String weatherRegion, String weatherCountry,
                           String lastUpdated, double temperatureC, double temperatureF,
                           String conditionOfWeather, String classroomCredentials,int GroceryAmount,String GroceryDesc,
                           String GroceryTitle,String GroceryType) {
        setUserID(userID);
        setUsername(username);
        setPassword(password);
        setName(name);
        setEmail(email);
        setExpenses(expenses);
        setBudget(budget);
        setIncome(income);
        setToken(token);
        setSavings(savings);
        setInvestments(investments);
        setIp(ip);
        setCity(city);
        setRegion(region);
        setCountry(country);
        setLatitude(latitude);
        setLongitude(longitude);
        setPostal(postal);
        setTimezone(timezone);
        setWeatherCity(weatherCity);
        setWeatherRegion(weatherRegion);
        setWeatherCountry(weatherCountry);
        setLastUpdated(lastUpdated);
        setTemperatureC(temperatureC);
        setTemperatureF(temperatureF);
        setCondition(conditionOfWeather);
        setClassroomCredentials(classroomCredentials);
        setGroceryAmount(GroceryAmount);
        setGroceryDesc(GroceryDesc);
        setGroceryTitle(GroceryTitle);
        setGroceryType(GroceryType);
    }

}
