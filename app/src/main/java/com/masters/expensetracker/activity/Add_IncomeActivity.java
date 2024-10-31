package com.masters.expensetracker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.masters.expensetracker.R;
import com.masters.expensetracker.adapter.IncomeAdapter;
import com.masters.expensetracker.database.AppDatabase;
import com.masters.expensetracker.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class Add_IncomeActivity extends AppCompatActivity {

    RecyclerView incomeList;
    EditText incomeEdittext;
    EditText incomeAmountEdittext;
    MaterialButton addIncome;
    List<UserModel> modelList;
    IncomeAdapter adapter;
    AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);

        db = AppDatabase.getDatabase(this);
        incomeEdittext = findViewById(R.id.income_add_text);
        incomeAmountEdittext = findViewById(R.id.income_add_amount);
        incomeList = findViewById(R.id.income_list);
        addIncome = findViewById(R.id.income_add_button);

        modelList = new ArrayList<>();
        adapter = new IncomeAdapter(modelList,this);

        loadIncomeData();

        // Set up RecyclerView
        incomeList.setLayoutManager(new LinearLayoutManager(this));
        incomeList.setAdapter(adapter);

        addIncome.setOnClickListener(v -> {
            String incomeName = incomeEdittext.getText().toString();
            String incomeAmountStr = incomeAmountEdittext.getText().toString();

            if (!TextUtils.isEmpty(incomeName) && !TextUtils.isEmpty(incomeAmountStr)) {
                double incomeAmount = Double.parseDouble(incomeAmountStr);

                // Create a new UserModel instance for the income
                UserModel incomeModel = new UserModel(incomeAmount, incomeName, "income",true);

                incomeModel.setNet_income(incomeModel.getNet_income()+incomeAmount);

                // Add the income to the list and update the adapter
                modelList.add(incomeModel);
                adapter.notifyDataSetChanged();

                // Save the income to the database
                saveIncomeToDatabase(incomeModel);

            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(Add_IncomeActivity.this, MainActivity.class));
        finish();
    }

    private void loadIncomeData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<UserModel> savedIncome = db.userDao().getAllIncomes();  // Only load income
            runOnUiThread(() -> {
                modelList.clear();
                modelList.addAll(savedIncome);
                adapter.notifyDataSetChanged();

                // Update and display the net income when loading data
            });
        });
    }

    // Method to save income data to the database
    private void saveIncomeToDatabase(UserModel income) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.userDao().insert(income);
        });
    }

    public void deleteIncome(int incomeId) {
        Executors.newSingleThreadExecutor().execute(() -> {
            db.userDao().deleteById(incomeId);  // Delete from database by ID

            // Reload the updated data and UI after deletion
            runOnUiThread(() -> {
                loadIncomeData();  // Reload the income data after deletion

            });
        });
    }

}
