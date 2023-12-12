package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class NoteData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int ActID;
    private Integer userID;
    private static int id=0;
    private int noteID;
    private String title;
    private String description;
    public  NoteData()
    {
        this.noteID=id++;
    }
    public NoteData(String title, String desc,int UserID) {
        this.noteID = id++;
        this.title = title;
        this.description = desc;
        this.userID=UserID;
    }
    public int getStaicID()
    {
        return id;
    }
    public int getNoteID() {
        return noteID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
    public void setUserID(int USERID){
        userID=USERID;
    }
    public int getUserID(){
        return userID;
    }
}
