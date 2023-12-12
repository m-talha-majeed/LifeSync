package com.example.demo;
public class Grocery {
    private NoteData n1;
    private String type;
    private int amount;


    public NoteData getN1() {
        return n1;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    public void setN1(NoteData n1) {
        this.n1 = n1;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Grocery()
    {
        n1=new NoteData();

    }

    public Grocery(NoteData n1,String type,int amount)
    {
        this.n1=n1;
        this.type=type;
        this.amount=amount;
    }

    public void addGrocery(NoteData n1, String type, int amount)
    {

        this.n1=n1;
        this.type=type;
        this.amount=amount;

    }

    public void displayGrocery()
    {

        if(n1!=null) {
            System.out.println("Title: " + n1.getTitle() + "\n" + "Descrription: " + n1.getDescription());
            System.out.println("Type: " + type + "\n" + "Amount: " + amount + "\n");
        }
        else{
            System.out.println("No list found!");
        }
    }

    public int deductFinances(int expenses)
    {
        if (expenses>=0){
            return(expenses + amount);
        }
        else {
            return 0;
        }
    }

    public NoteData removeGrocery()
    {
        NoteData n2=n1;
        setN1(null);
        setType(null);
        setAmount(0);
        return n2;
    }

    public void EditGrocery(String title,String desc,String type, int amount)
    {
        if(n1!=null)
        {
            n1.setTitle(title);
            n1.setDescription(desc);
            this.type=type;
            this.amount=amount;
        }
    }

}
