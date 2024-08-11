package com.lucifer.finance.transaction;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import com.lucifer.finance.R;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<Transaction> transactions;
    private Context context;

    public TransactionAdapter(List<Transaction> transactions, Context context) {
        this.transactions = transactions;
        this.context = context;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactions.get(position);

        holder.dateTextView.setText(transaction.getDate());

        // Convert time from 24-hour format to 12-hour format
        String time12HourFormat = convertTo12HourFormat(transaction.getTime());
        holder.timeTextView.setText(time12HourFormat);

        holder.bankNameTextView.setText(transaction.getBankName());
        holder.bankLogoImageView.setImageResource(transaction.getBankLogo()); // Set the bank logo image

        BigDecimal amount = transaction.getAmount();
        String amountStr = "₹" + amount;
        if (transaction.isCredit()) {
            holder.amountTextView.setTextColor(ContextCompat.getColor(context, R.color.green)); // Set green color for credited amount
        } else {
            holder.amountTextView.setTextColor(ContextCompat.getColor(context, R.color.red)); // Set red color for debited amount
        }
        holder.amountTextView.setText(amountStr);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    static class TransactionViewHolder extends RecyclerView.ViewHolder {

        TextView dateTextView;
        TextView timeTextView;
        TextView amountTextView;
        TextView bankNameTextView;
        ImageView bankLogoImageView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            bankNameTextView = itemView.findViewById(R.id.bankNameTextView);
            bankLogoImageView = itemView.findViewById(R.id.bankLogoImageView);
        }
    }

    // Helper method to convert 12-hour format to 12-hour format (reformat)
    private String convertTo12HourFormat(String time12) {
        SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        try {
            // Try to parse the time string in the format "hh:mm a"
            Date date = sdf12.parse(time12);

            // If parsing succeeds, reformat to the same format
            return sdf12.format(date);
        } catch (ParseException e) {
            // If parsing fails, try to parse in the format "HH:mm"
            SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
            try {
                Date date = sdf24.parse(time12);

                // If parsing succeeds, reformat to the format "hh:mm a"
                return sdf12.format(date);
            } catch (ParseException ex) {
                // If parsing fails again, return the original time string
                return time12;
            }
        }
    }

}
