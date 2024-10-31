package com.masters.expensetracker.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.masters.expensetracker.R;
import com.masters.expensetracker.activity.Add_IncomeActivity;
import com.masters.expensetracker.model.UserModel;

import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {

    List<UserModel> modelList;
    private Context context;

    public IncomeAdapter(List<UserModel> modelList, Context context) {
        this.modelList = modelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.income_sample_list,
                parent,
                false
        );
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final UserModel model = modelList.get(position);
        holder.name.setText(model.getIncome_name());
        holder.amount.setText(String.format("$%,.2f", model.getIncome_amount()));

        holder.deleteButton.setOnClickListener(v -> {
            if (context instanceof Add_IncomeActivity) {
                ((Add_IncomeActivity) context).deleteIncome(model.getId());  // Call delete method from activity
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
            name = itemView.findViewById(R.id.sample_income_text);
            amount = itemView.findViewById(R.id.sample_income_amount);
            deleteButton = itemView.findViewById(R.id.delete_button);

        }
    }


}
