package com.example.stoktakip.Models;

public class AdditionalExpense {

    private String expenseKey, expenseType, whatExpense, expenseQuantity, expenseDate;

    public AdditionalExpense() {
    }

    public AdditionalExpense(String expenseKey, String expenseType, String whatExpense, String expenseQuantity, String expenseDate) {
        this.expenseKey = expenseKey;
        this.expenseType = expenseType;
        this.whatExpense = whatExpense;
        this.expenseQuantity = expenseQuantity;
        this.expenseDate = expenseDate;
    }

    public String getExpenseKey() {
        return expenseKey;
    }

    public void setExpenseKey(String expenseKey) {
        this.expenseKey = expenseKey;
    }

    public String getExpenseType() {
        return expenseType;
    }

    public void setExpenseType(String expenseType) {
        this.expenseType = expenseType;
    }

    public String getWhatExpense() {
        return whatExpense;
    }

    public void setWhatExpense(String whatExpense) {
        this.whatExpense = whatExpense;
    }

    public String getExpenseQuantity() {
        return expenseQuantity;
    }

    public void setExpenseQuantity(String expenseQuantity) {
        this.expenseQuantity = expenseQuantity;
    }

    public String getExpenseDate() {
        return expenseDate;
    }

    public void setExpenseDate(String expenseDate) {
        this.expenseDate = expenseDate;
    }

}
