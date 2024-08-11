package com.lucifer.finance.transaction;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lucifer.finance.R;

import java.lang.reflect.Type;
import java.util.*;

//public class TransactionDetailActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private TransactionAdapter transactionAdapter;
//    private List<Transaction> transactions;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_transaction_detail);
//
//        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        transactions = loadTransactions();
//
//        transactionAdapter = new TransactionAdapter(transactions, this);
//        recyclerView.setAdapter(transactionAdapter);
//    }
//
//    private List<Transaction> loadTransactions() {
//        SharedPreferences sharedPreferences = getSharedPreferences("financeApp", MODE_PRIVATE);
//        String transactionJson = sharedPreferences.getString("transactions", "[]");
//        Log.d("Transaction Details", transactionJson); // Log the JSON string for debugging
//
//        Type transactionType = new TypeToken<ArrayList<Transaction>>() {}.getType();
//        List<Transaction> transactions = new Gson().fromJson(transactionJson, transactionType);
//
//        if (transactions == null) {
//            Log.e("Transaction loading", "Failed to load transactions from JSON", new Throwable("Failed to load transactions")); // Log the error
//            transactions = new ArrayList<>(); // Return an empty list if deserialization fails
//        }
//
//        return transactions;
//    }
//}



public class TransactionDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactions;
    private Button datePickerButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Transaction History");
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        transactions = loadTransactions();
        sortTransactionsDescending(transactions); // Sort transactions by date and time in descending order

        transactionAdapter = new TransactionAdapter(transactions, this);
        recyclerView.setAdapter(transactionAdapter);

        datePickerButton = findViewById(R.id.datePickerButton);
        GradientDrawable drawable = (GradientDrawable) datePickerButton.getBackground();
        drawable.setColor(Color.parseColor("#FFC107"));
        datePickerButton.setOnClickListener(v -> showDatePickerDialog());
    }

    private List<Transaction> loadTransactions() {
        SharedPreferences sharedPreferences = getSharedPreferences("financeApp", MODE_PRIVATE);
        String transactionJson = sharedPreferences.getString("transactions", "[]");
        Log.d("Transaction Details", transactionJson); // Log the JSON string for debugging

        Type transactionType = new TypeToken<ArrayList<Transaction>>() {}.getType();
        List<Transaction> transactions = new Gson().fromJson(transactionJson, transactionType);

        if (transactions == null) {
            Log.e("Transaction loading", "Failed to load transactions from JSON", new Throwable("Failed to load transactions")); // Log the error
            transactions = new ArrayList<>(); // Return an empty list if deserialization fails
        }

        return transactions;
    }

    private void sortTransactionsDescending(List<Transaction> transactions) {
        transactions.sort((t2, t1) -> {
            // Compare dates in descending order
            int dateComparison = t1.getDate().compareTo(t2.getDate());
            if (dateComparison == 0) {
                // If dates are equal, compare times in descending order
                return t1.getTime().compareTo(t2.getTime());
            }
            return dateComparison;
        });
    }

    private void showDatePickerDialog() {
        // Create a DatePickerDialog to select a date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // Format the selected date
            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            filterTransactionsByDate(selectedDate); // Refresh the log display for the selected date
        }, year, month, day);

        datePickerDialog.show();
    }

    private void filterTransactionsByDate(String selectedDate) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getDate().equals(selectedDate)) {
                filteredTransactions.add(transaction);
            }
        }
        sortTransactionsDescending(filteredTransactions); // Sort filtered transactions by date and time in descending order
        transactionAdapter = new TransactionAdapter(filteredTransactions, this);
        recyclerView.setAdapter(transactionAdapter);
    }
}

