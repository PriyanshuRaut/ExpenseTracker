package com.masters.expensetracker.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "userExpense_Info")
public class UserModel {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;

    @ColumnInfo(name = "net_income")
    double net_income = 0;
    @ColumnInfo(name = "net_expense")
    double net_expense = 0;

    @ColumnInfo(name = "income_amount")
    private double income_amount;

    @ColumnInfo(name = "income_name")
    private String income_name;

    @ColumnInfo(name = "expense_amount")
    private double expense_amount;
    @ColumnInfo(name = "expense_name")
    private String expense_name;

    @ColumnInfo(name = "amount")
    private double amount = 0;

    private String description;


    private String type;

    private Date date;

    public Date date_Data(Date date){

        return date;
    }



//    public UserModel(double income_amount, String income_name, String type) {
//        this.income_amount = income_amount;
//        this.income_name = income_name;
//        this.type = type;
//
//    }
//
//    public UserModel(String expense_amount, String expense_name,String type) {
//        this.expense_amount = expense_amount;
//        this.expense_name = expense_name;
//        this.type = type;
//    }

    public UserModel(double amount, String name, String type, boolean isIncome) {
        if (isIncome) {
            this.income_amount = amount;
            this.income_name = name;
        } else {
            this.expense_amount = amount;
            this.expense_name = name;
        }
        this.type = type;
    }


    public UserModel() {

    }

    public int getId() {
        return id;
    }

    public double getIncome_amount() {
        return income_amount;
    }

    public void setIncome_amount(double income_amount) {
        this.income_amount = income_amount;
    }

    public String getIncome_name() {
        return income_name;
    }

    public void setIncome_name(String income_name) {
        this.income_name = income_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNet_income() {
        return net_income;
    }

    public void setNet_income(double net_income) {
        this.net_income = net_income;
    }

    public double getNet_expense() {
        return net_expense;
    }

    public void setNet_expense(double net_expense) {
        this.net_expense = net_expense;
    }

    public double getExpense_amount() {
        return expense_amount;
    }

    public void setExpense_amount(double expense_amount) {
        this.expense_amount = expense_amount;
    }

    public String getExpense_name() {
        return expense_name;
    }

    public void setExpense_name(String expense_name) {
        this.expense_name = expense_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
