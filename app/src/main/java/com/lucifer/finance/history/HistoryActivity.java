//package com.lucifer.finance;
//
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.TextView;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.content.ContextCompat;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import java.lang.reflect.Type;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//public class HistoryActivity extends AppCompatActivity {
//
//    private Map<String, SmsReceiver.DailyAmount> dailyLog;
//    private TextView logTextView;
//    private SharedPreferences sharedPreferences;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_history);
//
//        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
//        logTextView = findViewById(R.id.logTextView);
//        sharedPreferences = getSharedPreferences("financeApp", MODE_PRIVATE);
//
//        loadLog();
//        displayLog();
//    }
//
//    private void loadLog() {
//        String json = sharedPreferences.getString("dailyLog", "{}");
//        Type type = new TypeToken<HashMap<String, SmsReceiver.DailyAmount>>() {}.getType();
//        dailyLog = new Gson().fromJson(json, type);
//        if (dailyLog == null) {
//            dailyLog = new HashMap<>();
//        }
//        Log.d("DailyLog Details", dailyLog.toString()); // Log the JSON string for debugging
//
//    }
//
//    private void displayLog() {
//        StringBuilder logBuilder = new StringBuilder();
//
//        // Convert the map to a list of entries and sort by date
//        List<Map.Entry<String, SmsReceiver.DailyAmount>> entries = new ArrayList<>(dailyLog.entrySet());
//        Collections.sort(entries, (e1, e2) -> e2.getKey().compareTo(e1.getKey())); // Sort in descending order by date
//
//        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
//
//        for (Map.Entry<String, SmsReceiver.DailyAmount> entry : entries) {
//            String date = entry.getKey();
//            SmsReceiver.DailyAmount amount = entry.getValue();
//
//            try {
//                Date parsedDate = inputFormat.parse(date);
//                if (parsedDate != null) {
//                    String formattedDate = outputFormat.format(parsedDate);
//                    logBuilder.append(formattedDate).append(":\n")
//                            .append("\t\tTotal Amount Flow  : ₹").append(amount.total()).append("\n")
//                            .append("\t\tCredited Amount     : ₹").append(amount.credited).append("\n")
//                            .append("\t\tDebited Amount      : ₹").append(amount.debited).append("\n")
//                            .append("\t\tBank Transactions  :\n");
//
//                    for (Map.Entry<String, Integer> bankEntry : amount.bankTransactionCount.entrySet()) {
//                        logBuilder.append("\t\t\t\t")
//                                .append(" trans : ")
//                                .append(bankEntry.getValue()+" ")
//                                .append(bankEntry.getKey())
//                                .append("\n");
//                    }
//
//                    logBuilder.append("----------------------------------------------------\n\n");
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        logTextView.setText(logBuilder.toString());
//    }
//}


package com.lucifer.finance.history;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ExpandableListView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lucifer.finance.R;
import com.lucifer.finance.smsfunctionality.SmsReceiver;

import java.lang.reflect.Type;
import java.util.*;

public class HistoryActivity extends AppCompatActivity {

    private Map<String, SmsReceiver.DailyAmount> dailyLog;
    private ExpandableListView logListView;
    private SharedPreferences sharedPreferences;
    private HistoryAdapter adapter;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Transaction Log");
        setSupportActionBar(toolbar);
        logListView = findViewById(R.id.logListView);
        sharedPreferences = getSharedPreferences("financeApp", MODE_PRIVATE);

//        ExpandableListView logListView = (ExpandableListView) findViewById(R.id.logListView);
//        logListView.setGroupIndicator(getDrawable(R.drawable.custom_group_indicator));
//        logListView.setGroupIndicator(getResources().getDrawable(R.drawable.custom_group_indicator));

        loadLog();
        displayLog();
    }

    private void loadLog() {
        String json = sharedPreferences.getString("dailyLog", "{}");
        Type type = new TypeToken<HashMap<String, SmsReceiver.DailyAmount>>() {}.getType();
        dailyLog = new Gson().fromJson(json, type);
        if (dailyLog == null) {
            dailyLog = new HashMap<>();
        }
//        Log.d("DailyLog Details", dailyLog.toString()); // Log the JSON string for debugging

    }

    private void displayLog() {
        // Convert the map to a list of entries and sort by date
        List<Map.Entry<String, SmsReceiver.DailyAmount>> entries = new ArrayList<>(dailyLog.entrySet());
        Collections.sort(entries, (e1, e2) -> e2.getKey().compareTo(e1.getKey())); // Sort in descending order by date

        adapter = new HistoryAdapter(this, entries);
        logListView.setAdapter(adapter);
    }
}



/*
package com.lucifer.finance.history;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lucifer.finance.R;
import com.lucifer.finance.auth.RegistrationActivity;
import com.lucifer.finance.smsfunctionality.SmsReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoryActivity extends AppCompatActivity {

    private Map<String, SmsReceiver.DailyAmount> dailyLog;
    private ExpandableListView logListView;
    private HistoryAdapter adapter;
    private Toolbar toolbar;
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Transaction Log");
        setSupportActionBar(toolbar);
        rootView = findViewById(android.R.id.content);

        logListView = findViewById(R.id.logListView);

        // Load transaction logs from Firebase
        loadLog();
    }

    // Method to load dailyLog from Firebase
    private void loadLog() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // you're using FirebaseAuth
        DatabaseReference dailyLogRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("dailyLogs");

        dailyLogRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dailyLog = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String date = snapshot.getKey();
                    SmsReceiver.DailyAmount dailyAmount = snapshot.getValue(SmsReceiver.DailyAmount.class);
                    dailyLog.put(date, dailyAmount);
                }

                // Once data is loaded from Firebase, display it
                displayLog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle possible errors here
                Snackbar snackbar = Snackbar.make(rootView, "Exception : "+databaseError.getMessage(), Snackbar.LENGTH_SHORT);
                customizeSnackbar(snackbar);
                snackbar.show();
            }
        });
    }

    // Method to display the transaction logs in the ExpandableListView
    private void displayLog() {
        // Convert the map to a list of entries and sort by date
        List<Map.Entry<String, SmsReceiver.DailyAmount>> entries = new ArrayList<>(dailyLog.entrySet());
        Collections.sort(entries, (e1, e2) -> e2.getKey().compareTo(e1.getKey())); // Sort in descending order by date

        adapter = new HistoryAdapter(this, entries);
        logListView.setAdapter(adapter);
    }

    // Method to store dailyLog in Firebase
    private void storeDailyLogInFirebase() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid(); // Get current user ID from FirebaseAuth
        DatabaseReference dailyLogRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("dailyLogs");

        dailyLogRef.setValue(dailyLog)
                .addOnSuccessListener(aVoid -> {
                    // Data successfully saved, you can show a Snackbar or Toast here if needed
                    Snackbar snackbar = Snackbar.make(rootView, "Data added successfully", Snackbar.LENGTH_SHORT);
                    customizeSnackbar(snackbar);
                    snackbar.show();
                })
                .addOnFailureListener(e -> {
                    // Handle the error, e.g., show an error message to the user
                    Snackbar snackbar = Snackbar.make(rootView, "Exception : "+e.getMessage(), Snackbar.LENGTH_SHORT);
                    customizeSnackbar(snackbar);
                    snackbar.show();
                });
    }

    // Example method that might update dailyLog and then save it to Firebase
    private void updateDailyLog(String date, SmsReceiver.DailyAmount updatedAmount) {
        dailyLog.put(date, updatedAmount); // Update the log
        storeDailyLogInFirebase(); // Store the updated log in Firebase
    }

    private void customizeSnackbar(Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(HistoryActivity.this, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(HistoryActivity.this, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
    }
}
*/
