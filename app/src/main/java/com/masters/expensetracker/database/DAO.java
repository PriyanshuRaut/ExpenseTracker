package com.masters.expensetracker.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.masters.expensetracker.model.UserModel;

import java.util.List;

@Dao
public interface DAO {

    @Insert
    void insert(UserModel modelDatabase);

    // Query to get all income records
    @Query("SELECT * FROM userExpense_Info WHERE type = 'income'")
    List<UserModel> getAllIncomes();

    // Query to get all expense records
    @Query("SELECT * FROM userExpense_Info WHERE type = 'expense'")
    List<UserModel> getAllExpenses();

    @Query("DELETE FROM userExpense_Info WHERE id = :userId")
    void deleteById(int userId);

    @Query("SELECT SUM(net_income) FROM userExpense_Info WHERE type = 'income' ")
    double getTotalIncome();

    // Calculate total expense (negative amounts)
    @Query("SELECT SUM(net_expense) FROM userExpense_Info WHERE type = 'expense' ")
    double getTotalExpense();

    @Query("SELECT amount FROM userExpense_Info")
    double getTotalAmount();
}
