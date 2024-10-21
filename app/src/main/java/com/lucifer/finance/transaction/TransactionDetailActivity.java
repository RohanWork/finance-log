package com.lucifer.finance.transaction;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lucifer.finance.R;
import com.lucifer.finance.history.HistoryActivity;
import com.lucifer.finance.smsfunctionality.SmsReceiver;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");
        transactions.sort((t2, t1) -> {
            // Compare dates in descending order
//            int dateComparison = t1.getDate().compareTo(t2.getDate());
//            if (dateComparison == 0) {
//                // If dates are equal, compare times in descending order
//                return t1.getTime().compareTo(t2.getTime());
//            }
//            return dateComparison;

            int dateComparison = t1.getDate().compareTo(t2.getDate());
            if (dateComparison == 0) {
                try {
                    Date time1 = timeFormat.parse(t1.getTime()); // Convert time string to Date object
                    Date time2 = timeFormat.parse(t2.getTime()); // Convert time string to Date object
                    assert time2 != null;
                    return time1.compareTo(time2); // Compare in descending order
                } catch (ParseException e) {
                    e.printStackTrace(); // Handle parsing error
                    return 0; // If parsing fails, consider times as equal
                }
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




/*
public class TransactionDetailActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransactionAdapter transactionAdapter;
    private List<Transaction> transactions;
    private Button datePickerButton;
    private Toolbar toolbar;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_detail);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Transaction History");
        setSupportActionBar(toolbar);
        rootView = findViewById(android.R.id.content);

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

        // Add transactions to Firebase for the current user
        storeTransactionsInFirebase(transactions);
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

    private void storeTransactionsInFirebase(List<Transaction> transactions) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user ID
        DatabaseReference userTransactionsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("transactions");

        // Save transactions in Firebase under the user's node
        userTransactionsRef.setValue(transactions)
                .addOnSuccessListener(aVoid -> {
                    // Show success message
                    Snackbar snackbar = Snackbar.make(rootView, "Data added successfully", Snackbar.LENGTH_SHORT);
                    customizeSnackbar(snackbar);
                    snackbar.show();
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Snackbar snackbar = Snackbar.make(rootView, "Exception : "+e.getMessage(), Snackbar.LENGTH_SHORT);
                    customizeSnackbar(snackbar);
                    snackbar.show();
                });
    }

    private void customizeSnackbar(Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(TransactionDetailActivity.this, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(TransactionDetailActivity.this, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
    }
}
*/




//public class TransactionDetailActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private TransactionAdapter transactionAdapter;
//    private List<Transaction> transactions;
//    private Button datePickerButton;
//    private Toolbar toolbar;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_transaction_detail);
//
//        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
//
//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Transaction History");
//        setSupportActionBar(toolbar);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//
//        transactions = loadTransactions();
//        sortTransactionsDescending(transactions); // Sort transactions by date and time in descending order
//
//        transactionAdapter = new TransactionAdapter(transactions, this);
//        recyclerView.setAdapter(transactionAdapter);
//
//        datePickerButton = findViewById(R.id.datePickerButton);
//        GradientDrawable drawable = (GradientDrawable) datePickerButton.getBackground();
//        drawable.setColor(Color.parseColor("#FFC107"));
//        datePickerButton.setOnClickListener(v -> showDatePickerDialog());
//
//        // Add transactions to Firebase for the current user
//        storeTransactionsInFirebase(transactions);
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
//
//    private void sortTransactionsDescending(List<Transaction> transactions) {
//        transactions.sort((t2, t1) -> {
//            // Compare dates in descending order
//            int dateComparison = t1.getDate().compareTo(t2.getDate());
//            if (dateComparison == 0) {
//                // If dates are equal, compare times in descending order
//                return t1.getTime().compareTo(t2.getTime());
//            }
//            return dateComparison;
//        });
//    }
//
//    private void showDatePickerDialog() {
//        // Create a DatePickerDialog to select a date
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
//            // Format the selected date
//            String selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
//            filterTransactionsByDate(selectedDate); // Refresh the log display for the selected date
//        }, year, month, day);
//
//        datePickerDialog.show();
//    }
//
//    private void filterTransactionsByDate(String selectedDate) {
//        List<Transaction> filteredTransactions = new ArrayList<>();
//        for (Transaction transaction : transactions) {
//            if (transaction.getDate().equals(selectedDate)) {
//                filteredTransactions.add(transaction);
//            }
//        }
//        sortTransactionsDescending(filteredTransactions); // Sort filtered transactions by date and time in descending order
//        transactionAdapter = new TransactionAdapter(filteredTransactions, this);
//        recyclerView.setAdapter(transactionAdapter);
//    }
//
//    private void storeTransactionsInFirebase(List<Transaction> transactions) {
//        List<Transaction> convertedTransactions = convertTransactions(transactions);
//        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user ID
//        DatabaseReference userTransactionsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("transactions");
//
//        // Loop through transactions and push each one to Firebase with a unique key
//        for (Transaction transaction : convertedTransactions) {
//            String key = userTransactionsRef.push().getKey(); // Create a unique key for each transaction
//            userTransactionsRef.child(key).setValue(transaction)
//                    .addOnSuccessListener(aVoid -> {
//                        // Show success message
//                        Log.d("Firebase", "Transaction saved successfully: " + key);
//                    })
//                    .addOnFailureListener(e -> {
//                        // Handle the error
//                        Log.e("Firebase", "Error saving transaction: " + e.getMessage());
//                    });
//        }
//    }
//
//    private List<Transaction> convertTransactions(List<Transaction> originalTransactions) {
//        List<Transaction> convertedTransactions = new ArrayList<>();
//        for (Transaction transaction : originalTransactions) {
//
//            Transaction convertedTransaction = new Transaction(
//                    transaction.getDate(),
//                    transaction.getTime(),
//                    transaction.getAmt(transaction.getAmount()), // Convert BigDecimal to double
//                    transaction.getBankName(),
//                    transaction.getBankLogo(),
//                    transaction.isCredit() // Retain the credit status
//            );
//            convertedTransactions.add(convertedTransaction);
//        }
//        return convertedTransactions;
//    }
//
//}




/*
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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

    private void storeTransactionInFirebase(Transaction newTransaction) {
        List<Transaction> convertedTransactions = convertTransactions(Collections.singletonList(newTransaction));
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user ID
        DatabaseReference userTransactionsRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("transactions");

        String key = userTransactionsRef.push().getKey(); // Create a unique key for each transaction
        userTransactionsRef.child(key).setValue(convertedTransactions.get(0))
                .addOnSuccessListener(aVoid -> {
                    // Show success message
                    Log.d("Firebase", "Transaction saved successfully: " + key);
                })
                .addOnFailureListener(e -> {
                    // Handle the error
                    Log.e("Firebase", "Error saving transaction: " + e.getMessage());
                });
    }

    private List<Transaction> convertTransactions(List<Transaction> originalTransactions) {
        List<Transaction> convertedTransactions = new ArrayList<>();
        for (Transaction transaction : originalTransactions) {
            Transaction convertedTransaction = new Transaction(
                    transaction.getDate(),
                    transaction.getTime(),
                    transaction.getAmt(transaction.getAmount()), // Convert BigDecimal to double
                    transaction.getBankName(),
                    transaction.getBankLogo(),
                    transaction.isCredit() // Retain the credit status
            );
            convertedTransactions.add(convertedTransaction);
        }
        return convertedTransactions;
    }

    // Call this method when you need to add a transaction
    private void addTransaction(Transaction newTransaction) {
        transactions.add(newTransaction); // Add the new transaction to your local list
        storeTransactionInFirebase(newTransaction); // Store the newly added transaction in Firebase
    }
}
*/
