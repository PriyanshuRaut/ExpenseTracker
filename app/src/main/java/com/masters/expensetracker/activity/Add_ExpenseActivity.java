package com.masters.expensetracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.masters.expensetracker.R;
import com.masters.expensetracker.adapter.ExpenseAdapter;
import com.masters.expensetracker.database.AppDatabase;
import com.masters.expensetracker.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Add_ExpenseActivity extends AppCompatActivity {

    RecyclerView expenseList;
    EditText expenseEdittext;
    EditText expenseAmountEdittext;
    MaterialButton addExpense;
    List<UserModel> modelList;
    ExpenseAdapter adapter;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expenseEdittext = findViewById(R.id.expense_add_text);
        expenseAmountEdittext = findViewById(R.id.expense_add_amount);
        expenseList = findViewById(R.id.expense_list);
        addExpense = findViewById(R.id.expense_add_button);

        modelList = new ArrayList<>();
        adapter = new ExpenseAdapter(modelList,this);

        db = AppDatabase.getDatabase(this);

        // Load existing expenses from the database
        loadExpenseData();

        // Set up RecyclerView
        expenseList.setLayoutManager(new LinearLayoutManager(this));
        expenseList.setAdapter(adapter);

        addExpense.setOnClickListener(v -> {
            String expenseName = expenseEdittext.getText().toString();
            String expenseAmountStr = expenseAmountEdittext.getText().toString();

            if (!TextUtils.isEmpty(expenseName) && !TextUtils.isEmpty(expenseAmountStr)) {
                //String formattedExpenseAmount = String.format("$%,.2f", Double.parseDouble(expenseAmountStr));
                double expenseAmount = Double.parseDouble(expenseAmountStr);


                // Create a new UserModel instance for the expense
                UserModel expenseModel = new UserModel(expenseAmount, expenseName,"expense",false);

                expenseModel.setNet_expense(expenseModel.getNet_expense()+Double.parseDouble(expenseAmountStr));


                // Add the expense to the list and update the adapter
                modelList.add(expenseModel);
                adapter.notifyDataSetChanged();

                // Save the expense to the database
                saveExpenseToDatabase(expenseModel);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Add_ExpenseActivity.this, MainActivity.class));
        finish();
    }

    // Method to load expense data from the database
    private void loadExpenseData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<UserModel> savedExpenses = db.userDao().getAllExpenses();  // Only load expenses
            runOnUiThread(() -> {
                modelList.clear();
                modelList.addAll(savedExpenses);
                adapter.notifyDataSetChanged();

                // Update and display the net expense when loading data
            });
        });
    }

    // Method to save expense data to the database
    private void saveExpenseToDatabase(UserModel expense) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.userDao().insert(expense);
        });
    }

    public void deleteExpense(int expenseId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.userDao().deleteById(expenseId);  // Delete from database by ID

            // Reload the updated data and UI after deletion
            runOnUiThread(() -> {
                loadExpenseData();  // Reload the expense data after deletion
            });
        });
    }


}
