package com.lucifer.finance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;
    private Map<String, SmsReceiver.DailyAmount> dailyLog;
    private SharedPreferences sharedPreferences;
    private TextView totalAmountTextView, creditedAmountTextView, debitedAmountTextView, logTextView, timerTextView, greetingTextView;
    private Button dateFilterButton;
    private String selectedDate;
    private Handler handler;
    private Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        totalAmountTextView = findViewById(R.id.totalAmountTextView);
        creditedAmountTextView = findViewById(R.id.creditedAmountTextView);
        debitedAmountTextView = findViewById(R.id.debitedAmountTextView);
        logTextView = findViewById(R.id.logTextView);
        timerTextView = findViewById(R.id.timerTextView); // Add a TextView for the timer in your layout XML
        greetingTextView = findViewById(R.id.titleTextView); // Add a TextView for the greeting in your layout XML
        dateFilterButton = findViewById(R.id.dateFilterButton);

        dateFilterButton.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow));
        dateFilterButton.setTextColor(ContextCompat.getColor(this, R.color.black));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        sharedPreferences = getSharedPreferences("financeApp", MODE_PRIVATE);

        requestSmsPermissions();

        selectedDate = getCurrentDate(); // Set the initial date to today

        displayLog();

        dateFilterButton.setOnClickListener(v -> showDatePicker());

        // Initialize and start the timer
        handler = new Handler();
        startTimer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayLog(); // Refresh the log whenever the activity is resumed
    }

    private void requestSmsPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)) {

                showPermissionExplanation();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS},
                        SMS_PERMISSION_REQUEST_CODE);
            }
        } else {
            loadLog();
            displayLog();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NotNull String[] permissions, @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadLog();
                displayLog();
            } else {
                Toast.makeText(this, "SMS permissions denied. Functionality may be limited.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showPermissionExplanation() {
        new AlertDialog.Builder(this)
                .setTitle("Permission Required")
                .setMessage("This app needs SMS permissions to function properly. Please grant the permissions.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    private void loadLog() {
        String json = sharedPreferences.getString("dailyLog", "{}");
        Type type = new TypeToken<HashMap<String, SmsReceiver.DailyAmount>>() {}.getType();
        dailyLog = new Gson().fromJson(json, type);
        if (dailyLog == null) {
            dailyLog = new HashMap<>();
        }
    }

    @SuppressLint("SetTextI18n")
    private void displayLog() {
        loadLog(); // Ensure log is loaded

        SmsReceiver.DailyAmount dailyAmount = dailyLog.getOrDefault(selectedDate, new SmsReceiver.DailyAmount());

        assert dailyAmount != null;
        totalAmountTextView.setText("Total Amount Flow :  ₹" + dailyAmount.total());
        creditedAmountTextView.setText("Credited Amount    :  ₹" + dailyAmount.credited);
        debitedAmountTextView.setText("Debited Amount     :  ₹" + dailyAmount.debited);

        StringBuilder logBuilder = new StringBuilder();
        for (Map.Entry<String, SmsReceiver.DailyAmount> entry : dailyLog.entrySet()) {
            logBuilder.append(entry.getKey())
                    .append("\n\t\tTotal       :  ₹").append(entry.getValue().total())
                    .append("\n\t\tCredited :  ₹").append(entry.getValue().credited)
                    .append("\n\t\tDebited  :  ₹").append(entry.getValue().debited)
                    .append("\n");
        }

        logTextView.setText(logBuilder.toString());
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            selectedDate = String.format("%d-%02d-%02d", year, month + 1, dayOfMonth);
            displayLog();
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void startTimer() {
        runnable = new Runnable() {
            @Override
            public void run() {
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
                String currentDateAndTime = sdf.format(new Date());
                timerTextView.setText(currentDateAndTime);
                updateGreeting();

                handler.postDelayed(this, 1000); // Update every second
            }
        };

        handler.post(runnable);
    }

    private void updateGreeting() {
        String visitor = "User.";
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        String greeting;
        if (hourOfDay >= 5 && hourOfDay < 12) {
            greeting = "Good Morning, "+visitor;
        } else if (hourOfDay >= 12 && hourOfDay < 17) {
            greeting = "Good Afternoon, "+visitor;
        } else if (hourOfDay >= 17 && hourOfDay < 21) {
            greeting = "Good Evening, "+visitor;
        } else {
            greeting = "Good Night, "+visitor;
        }

        greetingTextView.setText(greeting);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Stop the timer when the activity is destroyed
    }
}
