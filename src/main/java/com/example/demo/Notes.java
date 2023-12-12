package com.example.demo;
import java.util.ArrayList;
import java.util.Scanner;
public class Notes {
    private ArrayList<NoteData> note;
    public Notes() {
        this.note=new ArrayList<>();
    }

    public Notes(ArrayList<NoteData> note) {
        this.note = note;
    }

    public void addNote(NoteData n1) {
        note.add(n1);
    }

    public void addn(String title, String description)
    {
        NoteData n1=new NoteData();
        n1.setTitle(title);
        n1.setDescription(description);
        note.add(n1);
    }
    public void removen(int id)
    {
        for(NoteData n1: note)
        {
            if(n1.getNoteID()==id)
            {
                note.remove(n1);
                System.out.println("Note Successfully Deleted! ");
                return;
            }
        }

        System.out.println("Invalid NoteID!");
    }

    public void removeno(NoteData n1)
    {
        note.remove(n1);
    }

    public void EditNote(int id,String title, String description)
    {
        for(NoteData n1: note)
        {
            if(n1.getNoteID()==id)
            {
                n1.setTitle(title);
                n1.setDescription(description);
                System.out.println("Note Successfully Edited! ");
                return;
            }
        }
    }

    public void print()
    {
        for(NoteData n1: note)
        {
            System.out.println("NoteID: " + n1.getNoteID()+ "\n" + "Title: " + n1.getTitle() + "\n"+ "Decription: " +n1.getDescription() +"\n");

        }
    }

    public void search(String title)
    {
        for(NoteData n1: note)
        {
            if(n1.getTitle().equals(title))
            {
                System.out.println("NoteID: " + n1.getNoteID()+ "\n" + "Title: " + n1.getTitle() +  "\n" + "Description: " + n1.getDescription()+ "\n");
                return;
            }
        }
        System.out.println("Note Not Found");
    }
}
