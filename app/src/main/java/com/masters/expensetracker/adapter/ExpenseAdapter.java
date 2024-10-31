package com.masters.expensetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masters.expensetracker.R;
import com.masters.expensetracker.activity.Add_ExpenseActivity;
import com.masters.expensetracker.activity.Add_IncomeActivity;
import com.masters.expensetracker.model.UserModel;

import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    List<UserModel> modelList;
    private Context context;

    public ExpenseAdapter(List<UserModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.income_sample_list, // Make sure this layout exists
                parent,
                false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserModel model = modelList.get(position);
        holder.name.setText(model.getExpense_name());
        holder.amount.setText(String.format("$%,.2f",model.getExpense_amount()));

        holder.deleteButton.setOnClickListener(v -> {
            if (context instanceof Add_ExpenseActivity) {
                ((Add_ExpenseActivity) context).deleteExpense(model.getId());  // Call delete method from activity
            }
        });
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView amount;
        ImageView deleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.sample_income_text); // Change to expense TextView IDs
            amount = itemView.findViewById(R.id.sample_income_amount); // Change to expense TextView IDs
            deleteButton = itemView.findViewById(R.id.delete_button);

        }
    }
}
