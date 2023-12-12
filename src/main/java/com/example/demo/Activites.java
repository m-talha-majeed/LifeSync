package com.example.demo;
import java.util.ArrayList;
import java.util.Scanner;

public class Activites {
    private ArrayList<ActivityData> activity;

    public Activites()
    {
        activity=new ArrayList<>();
    }

    public Activites(ArrayList<ActivityData> activity)
    {
        this.activity=activity;
    }
    public void addacc(String name,String desc,String date, String duration)
    {
        ActivityData a1=new ActivityData();

        a1.setName(name);
        a1.setDescription(desc);
        a1.setDate(date);
        a1.setDuration(duration);

        activity.add(a1);
    }

    public void removeacc(int id)
    {
        for(ActivityData a1: activity)
         {
            if(a1.getActivityID()==id)
           {
               activity.remove(a1);
               System.out.println("Activity Successfully Deleted! ");
               return;
           }
        }

        System.out.println("Invalid ActivityID!");
    }

    public void EditActivity(int id,String title,String desc,String date,String duration)
    {
        for(ActivityData a1: activity)
        {
            if(a1.getActivityID()==id)
            {
                a1.setName(title);
                a1.setDescription(desc);
                a1.setDate(date);
                a1.setDuration(duration);
                System.out.println("Activity Successfully Edited! ");
                return;
            }
        }
    }

    public void Search(String title)
    {
        for(ActivityData a1:activity)
        {
            if(a1.getName().equals(title))
            {
                System.out.println("ActivityID: " + a1.getActivityID() + "\n" + "Title: " + a1.getName() + "\n"+ "Decription: " +a1.getDescription() +"\n" + "Date: " + a1.getDate() + "\n" + "Duration: " + a1.getDuration() + "\n");
                return;
            }
        }

        System.out.println("Activity Not Found");
    }

}

