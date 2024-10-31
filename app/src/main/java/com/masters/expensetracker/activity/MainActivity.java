package com.masters.expensetracker.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.masters.expensetracker.R;
import com.masters.expensetracker.database.AppDatabase;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    MaterialButton addIncome,addExpense,analytics;
    AppDatabase db;
    TextView incomeAmount,expense_amount;
    double Profit_amount = 0;
    ImageView income_profit,expense_profit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        //Focus Areas: Data storage, Graphing/Visualization, List management.
        //Features:
        //Users can add, edit, and delete expenses.
        //Show expenses by categories (food, travel, etc.) and date.
        //Store expenses in SQLite or Room database.
        //Improvements:
        //Add pie charts or bar charts to visualize expenses (using MPAndroidChart).
        //Implement filtering by time period (daily, weekly, monthly).
        hideSystemUI();

        incomeAmount = findViewById(R.id.income_amount);
        expense_amount = findViewById(R.id.expense_amount);
        income_profit = findViewById(R.id.income_profit);
        expense_profit = findViewById(R.id.expense_profit);
        db = AppDatabase.getDatabase(this);
        addIncome = findViewById(R.id.add_income);
        addExpense = findViewById(R.id.add_expense);
        analytics = findViewById(R.id.detailed_analytics);
        loadTotalIncomeAndExpense();

        addIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Add_IncomeActivity.class));
                finish();
            }
        });

        addExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Add_ExpenseActivity.class));
                finish();
            }
        });

        analytics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AnalyticsActivity.class));
                finish();
            }
        });

    }

    private void hideSystemUI() {
        // This is for full-screen immersive mode
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        );
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();  // Re-hide UI when returning to the app
        }
    }

    private void loadTotalIncomeAndExpense() {
        Executors.newSingleThreadExecutor().execute(() -> {

            double NetIncome = db.userDao().getTotalIncome();
            double NetExpense = db.userDao().getTotalExpense();
            Profit_amount = NetIncome - NetExpense;



            // Update UI on the main thread
            runOnUiThread(() -> {

                incomeAmount.setText(String.format("$%,.2f", NetIncome));
                expense_amount.setText(String.format("$%,.2f",NetExpense));

                if (NetIncome > NetExpense && Profit_amount > 2 * NetExpense) {
                    income_profit.setImageResource(R.drawable.profit_level2);
                    expense_profit.setImageResource(R.drawable.loss_level2);
                    animateImageView(income_profit, true, 2);  // Animate profit level 2
                    animateImageView(expense_profit, false, 2);  // Animate loss level 2
                }
                else if (NetIncome > NetExpense) {
                    income_profit.setImageResource(R.drawable.profit_level1);
                    expense_profit.setImageResource(R.drawable.loss_level1);
                    animateImageView(income_profit, true, 1);  // Animate profit level 1
                    animateImageView(expense_profit, false, 1);  // Animate loss level 1
                }
                else if (NetExpense > NetIncome && Profit_amount < 2 * NetExpense) {
                    income_profit.setImageResource(R.drawable.loss_level2);
                    expense_profit.setImageResource(R.drawable.profit_level2);
                    animateImageView(expense_profit, true, 2);  // Animate profit level 2
                    animateImageView(income_profit, false, 2);  // Animate loss level 2
                }
                else if (NetExpense > NetIncome) {
                    income_profit.setImageResource(R.drawable.loss_level1);
                    expense_profit.setImageResource(R.drawable.profit_level1);
                    animateImageView(expense_profit, true, 1);  // Animate profit level 1
                    animateImageView(income_profit, false, 1);  // Animate loss level 1
                }
                else {
                    income_profit.setImageResource(R.drawable.profit_level1);
                    expense_profit.setImageResource(R.drawable.loss_level1);
                    animateImageView(income_profit, true, 1);  // Animate profit level 1
                    animateImageView(expense_profit, false, 1);  // Animate loss level 1
                }

            });
        });
    }

    private void animateImageView(ImageView imageView, boolean isProfit, int level) {
        // Load the correct drawable based on whether it's profit or loss and the level
        int drawableId;
        if (isProfit) {
            drawableId = (level == 1) ? R.drawable.profit_level1 : R.drawable.profit_level2;
        } else {
            drawableId = (level == 1) ? R.drawable.loss_level1 : R.drawable.loss_level2;
        }

        // Set the image resource
        imageView.setImageResource(drawableId);

        // Define the animation (for example, a simple scale or translation animation)
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationY", 0f, -20f, 0f);
        animator.setDuration(500); // 500ms for one up-down animation cycle
        animator.setRepeatMode(ValueAnimator.RESTART); // Restart the animation when it ends
        animator.setRepeatCount(ValueAnimator.INFINITE); // Loop the animation infinitely
        animator.start(); // Start the animation
    }


}