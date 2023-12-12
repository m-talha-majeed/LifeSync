package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ActivityData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ActID;
    private Integer userID;
    private static int id=0;
    private int activityID;
    private String name;
    private String description;
    private String date;
    private String duration;
    public int getUserId(){
        return userID;
    }
    public void setUserId(int user){
        userID=user;
    }
    public ActivityData() {
        this.activityID = id++;
    }

    public int getActivityID() {
        return activityID;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getDuration() {
        return duration;
    }

    public void setActivityID(int activityID) {
        this.activityID = activityID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
