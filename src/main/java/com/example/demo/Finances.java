package com.example.demo;
public class Finances {
    private int expenses;
    private int budget;
    private int income;
    private int savings;
    private int investments;

    public void setExpenses(int expenses) {
        this.expenses = expenses;
    }

    public void setBudget(int budget) {
        this.budget = budget;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public void setSavings(int savings) {
        this.savings = savings;
    }

    public void setInvestments(int investments) {
        this.investments = investments;
    }

    public int getExpenses() {
        return expenses;
    }

    public int getBudget() {
        return budget;
    }

    public int getIncome() {
        return income;
    }

    public int getSavings() {
        return savings;
    }

    public int getInvestments() {
        return investments;
    }

    public Finances()
    {

    }

    public Finances(int expenses, int budget, int income, int savings, int investments) {
        this.expenses = expenses;
        this.budget = budget;
        this.income = income;
        this.savings = savings;
        this.investments = investments;
    }

    public void addFinances(int expenses,int budget,int income,int savings,int investments)
    {
        this.expenses=expenses;
        this.budget=budget;
        this.income=income;
        this.savings=savings;
        this.investments=investments;
    }

    public void print()
    {
        System.out.println("Expenses: " + expenses + "\n" + "Budget: " + budget + "\n"+ "Income: " +income +"\n" + "Savings: " + savings +"\n" + "Investments: " + investments + "\n" );
    }

    public boolean budgetExceeded()
    {
        if (expenses>budget)
        {
            return true;
        }

        return false;
    }

    public int getTotal()
    {
        int total=expenses+budget+income+savings+investments;
        return total;
    }

    public void editFinances(int expenses,int budget,int income,int savings,int investments)
    {
        this.expenses=expenses;
        this.budget=budget;
        this.income=income;
        this.savings=savings;
        this.investments=investments;
    }
}
