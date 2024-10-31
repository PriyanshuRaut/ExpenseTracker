package com.masters.expensetracker.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;
import com.masters.expensetracker.R;
import com.masters.expensetracker.database.AppDatabase;
import com.masters.expensetracker.model.UserModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class AnalyticsActivity extends AppCompatActivity {

    PieChart pieChart;
    AppDatabase db;
    double Profit_amount;
    MaterialButton incomeSwitch;
    MaterialButton expenseSwitch;
    boolean isShowingIncome = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        db = AppDatabase.getDatabase(this);
        pieChart = findViewById(R.id.pieChart);
        incomeSwitch = findViewById(R.id.income_switch);
        expenseSwitch = findViewById(R.id.expense_switch);

        incomeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadIncomeData();
            }
        });
        expenseSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadExpenseData();
            }
        });

        loadIncomeData();

        loadTotalIncomeAndExpense();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(AnalyticsActivity.this,MainActivity.class));
        finish();
    }

    private void loadTotalIncomeAndExpense() {
        Executors.newSingleThreadExecutor().execute(() -> {

            double NetIncome = db.userDao().getTotalIncome();
            double NetExpense = db.userDao().getTotalExpense();
            Profit_amount = NetIncome - NetExpense;
            runOnUiThread(() -> {

            });
        });
    }

    // Load income data from the database and display in the PieChart
    private void loadIncomeData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<UserModel> incomeList = db.userDao().getAllIncomes(); // Fetch all income data

            runOnUiThread(() -> {
                // Set PieChart data
                setPieChartData(incomeList, true); // true indicates income data
            });
        });
    }

    // Load expense data from the database and display in the PieChart
    private void loadExpenseData() {
        Executors.newSingleThreadExecutor().execute(() -> {
            List<UserModel> expenseList = db.userDao().getAllExpenses(); // Fetch all expense data

            runOnUiThread(() -> {
                // Set PieChart data
                setPieChartData(expenseList, false); // false indicates expense data
            });
        });
    }

    // Method to set data into the PieChart (for both income and expense)
    private void setPieChartData(List<UserModel> dataList, boolean isIncome) {
        // Create a list of PieEntries (for income or expense)
        List<PieEntry> pieEntries = new ArrayList<>();
        if (isIncome) {
            for (UserModel userModel : dataList) {
                pieEntries.add(new PieEntry((float) userModel.getIncome_amount(), userModel.getIncome_name()));
            }
        } else {
            for (UserModel userModel : dataList) {
                pieEntries.add(new PieEntry((float) userModel.getExpense_amount(), userModel.getExpense_name()));
            }
        }

        // Create a PieDataSet
        PieDataSet pieDataSet = new PieDataSet(pieEntries, isIncome ? "Income" : "Expense");
        //pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS); // Set the color template
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.rgb(180, 227, 183));  // Lighter Green
        colors.add(Color.rgb(255, 213, 128));   // Lighter Orange
        colors.add(Color.rgb(159,206, 255));   // Lighter Blue
        colors.add(Color.rgb(255, 152, 147));    // Lighter Red
        colors.add(Color.rgb(229, 153, 230));  // Lighter Purple

// Apply custom colors to the dataset
        pieDataSet.setColors(colors);

        // Create a PieData object with the dataset
        PieData pieData = new PieData(pieDataSet);

        // Set data to PieChart
        pieChart.setData(pieData);
        pieChart.setCenterText(isIncome ? "Income Sources" : "Expenses");
        pieChart.animateXY(1000, 1000); // Animation duration
        pieChart.invalidate(); // Refresh the chart
    }


}