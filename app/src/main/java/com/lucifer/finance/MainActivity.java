package com.lucifer.finance;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.lucifer.finance.auth.LoginActivity;
import com.lucifer.finance.auth.UserProfileActivity;
import com.lucifer.finance.auxilary.DeveloperInfo;
import com.lucifer.finance.auxilary.Policy;
import com.lucifer.finance.build.LoadBuildConfig;
import com.lucifer.finance.history.HistoryActivity;
import com.lucifer.finance.intro.AppGuideActivity;
import com.lucifer.finance.smsfunctionality.SmsListenerService;
import com.lucifer.finance.smsfunctionality.SmsReceiver;
import com.lucifer.finance.transaction.Transaction;
import com.lucifer.finance.transaction.TransactionDetailActivity;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_REQUEST_CODE = 1;
    private Map<String, SmsReceiver.DailyAmount> dailyLog;
    private List<Transaction> transactions;
    private SharedPreferences sharedPreferences;
    private TextView totalAmountTextView, creditedAmountTextView, debitedAmountTextView, timerTextView, greetingTextView;
    private String selectedDate;
    private View mainView;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView nav;
    private Toolbar toolbar;
    private Handler handler;
    private Runnable runnable;
    private FloatingActionButton addTransactionButton;
    private boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getProviderId();

        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.yellow));
        initializeUI();
        Snackbar snackbar = Snackbar.make(mainView, "Loading Context", Snackbar.LENGTH_SHORT);
        customizeSnackbar(snackbar);
        snackbar.show();
        setupDrawer();
        handlePermissionAndService();

        selectedDate = getCurrentDate();
        displayLog();
        updateGreeting();

        findViewById(R.id.dateFilterButton).setOnClickListener(v -> showDatePicker());
        addTransactionButton.setOnClickListener(v -> showAddTransactionDialog());
        addTransactionButton.setOnLongClickListener(v -> {
            showEasterBox();
            return true; // Indicate that the long click event is handled
        });

        handler = new Handler();
        startTimer();

        getNavItems();
    }

    private void showEasterBox() {
        Snackbar snackbar = Snackbar.make(mainView, "You found an easter 🐰", Snackbar.LENGTH_LONG);

        snackbar.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.white));
        snackbar.setBackgroundTint(ContextCompat.getColor(MainActivity.this, R.color.black));

        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);

        snackbar.show();
    }


    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity(); // Closes all activities in the stack, including the current one
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        }
    }


    private void initializeUI() {
        try {
            mainView = findViewById(android.R.id.content);
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            nav = findViewById(R.id.nav);
            drawer = findViewById(R.id.drawer);

            totalAmountTextView = findViewById(R.id.totalAmountTextView);
            creditedAmountTextView = findViewById(R.id.creditedAmountTextView);
            debitedAmountTextView = findViewById(R.id.debitedAmountTextView);
            timerTextView = findViewById(R.id.timerTextView);
            greetingTextView = findViewById(R.id.titleTextView);
            addTransactionButton = findViewById(R.id.addTransactionButton);

            sharedPreferences = getSharedPreferences("financeApp", MODE_PRIVATE);
        } catch (Exception e) {
            Log.e("MainActivity", "Error initializing UI components", e);
        }
    }


    private void setupDrawer() {
        try {
            toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        } catch (Exception e) {
            Log.e("MainActivity", "Error setting up drawer", e);
        }
    }


    private void handlePermissionAndService() {
        Dexter.withContext(MainActivity.this)
                .withPermission(Manifest.permission.RECEIVE_SMS)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        loadLog();
                        displayLog();
                        Snackbar snackbar = Snackbar.make(mainView, "SMS permission granted by user", Snackbar.LENGTH_SHORT);
                        customizeSnackbar(snackbar);
                        snackbar.show();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        if (permissionDeniedResponse.isPermanentlyDenied()) {
                            showPermissionExplanation();
                        } else {
                            Snackbar snackbar = Snackbar.make(mainView, "Permission required for SMS service.\nClick on it to allow grant permission.", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Allow", v -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_PERMISSION_REQUEST_CODE));
                            customizeSnackbar(snackbar);
                            snackbar.show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        Snackbar snackbar = Snackbar.make(mainView, "Permission required for SMS service.\nClick on it to allow grant permission.", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Allow", v -> ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, SMS_PERMISSION_REQUEST_CODE));
                        customizeSnackbar(snackbar);
                        snackbar.show();
                    }
                }).check();
    }


    private void customizeSnackbar(Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(MainActivity.this, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.black));
    }


    @SuppressLint("NonConstantResourceId")
    private void getNavItems() {
        nav.setNavigationItemSelectedListener(item -> {
            try {
                if (item.getItemId() == R.id.historyButton) {
                    startActivity(new Intent(MainActivity.this, HistoryActivity.class));
                    return true;
                }
                if (item.getItemId() == R.id.viewTransactionButton) {
                    startActivity(new Intent(MainActivity.this, TransactionDetailActivity.class));
                    return true;
                }
                //logot option disabled because its implemented in userprofileactivity
                /*if (item.getItemId() == R.id.logout) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Logout")
                            .setMessage("Do you want to logout from your account?")
                            .setIcon(R.drawable.signout)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mAuth.signOut();
                                    Intent login = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(login);
                                    finish();
//                                    Snackbar snackbar = Snackbar.make(mainView, "Thank you for using our services", Snackbar.LENGTH_SHORT);
//                                    customizeSnackbar(snackbar);
//                                    snackbar.show();
                                    Toast.makeText(MainActivity.this, "Thank you for using our services", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();

                }*/
                if (item.getItemId() == R.id.profile) {
                    Intent userProfile = new Intent(MainActivity.this, UserProfileActivity.class);
                    startActivity(userProfile);
                    return true;
                }
                if (item.getItemId() == R.id.option_contact) {
                    Intent devContact = new Intent(MainActivity.this, DeveloperInfo.class);
                    startActivity(devContact);
                    return true;
                }
                if (item.getItemId() == R.id.appVersion) {
                    String verName = LoadBuildConfig.VERSION_NAME;
                    Snackbar snackbar = Snackbar.make(mainView, "Finance Log for Android v" + verName + " [Pilot Build]", Snackbar.LENGTH_SHORT);
                    customizeSnackbar(snackbar);
                    snackbar.show();
                    return true;
                }
                if (item.getItemId() == R.id.option_report) {
                    String versionName = LoadBuildConfig.VERSION_NAME;

                    // Generate a unique report ID
                    String reportId = UUID.randomUUID().toString();

                    // Create the email intent
                    Intent email = new Intent(Intent.ACTION_SENDTO);
                    email.setData(Uri.parse("mailto:")); // Only email apps should handle this
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"pubdata.ltd@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, "Reporting bug/error of finance-log v" + versionName);
                    email.putExtra(Intent.EXTRA_TEXT, "Report ID: \n" + reportId + "\n\nNOTE: \nPlease attach the screenshot or any valid image of design/component bug.\n\n ");

                    // Check if there is an email app available before starting the activity
                    PackageManager packageManager = getPackageManager();
                    if (email.resolveActivity(packageManager) != null) {
                        startActivity(email);
                    } else {
                        Toast.makeText(MainActivity.this, "No email app found", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                //temporary disabled option due to non functional components
                /*if (item.getItemId() == R.id.option_share) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "Your app is here, download it and join us!\n https://play.google.com/store/apps/details?id=" + getPackageName());
                    Intent share = Intent.createChooser(sharingIntent, "Share Via");
                    startActivity(share);
                    return true;
                }
                if (item.getItemId() == R.id.option_rate) {
                    Intent rate = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName()));
                    startActivity(rate);
                    return true;
                }*/
                if (item.getItemId() == R.id.privacy_policy) {
                    Intent policy = new Intent(getApplicationContext(), Policy.class);
                    startActivity(policy);
                    return true;
                }
                if (item.getItemId() == R.id.option_guide) {
                    Intent intro = new Intent(getApplicationContext(), AppGuideActivity.class);
                    startActivity(intro);
                    return true;
                } else {
                    return false;
                }
            } catch (Exception exception) {
                System.out.println(exception.getMessage());
                Snackbar snackbar = Snackbar.make(mainView, "Something went wrong", Snackbar.LENGTH_SHORT);
                customizeSnackbar(snackbar);
                snackbar.show();
            }
            return false;
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        displayLog();
    }


    @SuppressLint("SetTextI18n")
    private void showPermissionExplanation() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_layout, null);

        TextView titleView = dialogView.findViewById(R.id.alert_dialog_title);
        titleView.setText("Permission Required");

        TextView messageView = dialogView.findViewById(R.id.alert_dialog_message);
        messageView.setText("\nThis app needs SMS permissions to function properly. Please enable it in the popup.\n");

        new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogTheme))
                .setView(dialogView)
                .setPositiveButton("OK", (dialog, which) -> ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.RECEIVE_SMS}, SMS_PERMISSION_REQUEST_CODE))
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null); // Clean up handler callbacks
        Intent serviceIntent = new Intent(this, SmsListenerService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent); // Required for Android O and above
        } else {
            startService(serviceIntent);
        }

    }


    private void displayLog() {
        // Initialize the daily log display with the current date
        loadLog();

        SmsReceiver.DailyAmount dailyAmount = dailyLog.getOrDefault(selectedDate, new SmsReceiver.DailyAmount());

        if (dailyLog.isEmpty() && transactions.isEmpty()) {
            totalAmountTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            totalAmountTextView.setText("\n\nNo transactions logged");
            creditedAmountTextView.setText("");
            debitedAmountTextView.setText("");
        } else {
            // Display the total amount flow
            totalAmountTextView.setText("Total Amount Flow\n  ₹ " + formatAmount(dailyAmount.total()));
            totalAmountTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            // Display the credited amount
            creditedAmountTextView.setText("Credited Amount\n  ₹ " + formatAmount(dailyAmount.credited));
            creditedAmountTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            // Display the debited amount
            debitedAmountTextView.setText("Debited Amount\n  ₹ " + formatAmount(dailyAmount.debited));
            debitedAmountTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
    }


    // Helper method to format BigDecimal with commas and two decimal places
    private String formatAmount(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(amount.doubleValue());
    }


    private void startTimer() {
        runnable = new Runnable() {
            @Override
            public void run() {
                // Get the current time and format it
                long currentTimeMillis = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("  dd/MM/yyyy \n\n hh:mm:ss a", Locale.getDefault());
                String formattedTime = sdf.format(new Date(currentTimeMillis));
//                updateGreeting();
                timerTextView.setText(formattedTime);
                timerTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Repeat the runnable task every second
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 0); // Start the timer immediately
    }


    private void showDatePicker() {
        // Create a DatePickerDialog to select a date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, selectedYear, selectedMonth, selectedDay) -> {
            // Format the selected date
            selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
            displayLog(); // Refresh the log display for the selected date
        }, year, month, day);

        datePickerDialog.show();
    }


    private String getCurrentDate() {
        // Get the current date in the format YYYY-MM-DD
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(Locale.getDefault(), "%d-%02d-%02d", year, month, day);
    }


    private void loadLog() {
        try {
            String json = sharedPreferences.getString("dailyLog", "{}");
            Type type = new TypeToken<HashMap<String, SmsReceiver.DailyAmount>>() {
            }.getType();
            dailyLog = new Gson().fromJson(json, type);
            if (dailyLog == null) {
                dailyLog = new HashMap<>();
            }

            String transactionJson = sharedPreferences.getString("transactions", "[]");
            Type transactionType = new TypeToken<ArrayList<Transaction>>() {
            }.getType();
            transactions = new Gson().fromJson(transactionJson, transactionType);
            if (transactions == null) {
                transactions = new ArrayList<>();
            }
        } catch (Exception e) {
            Log.e("MainActivity", "Error loading log", e);
        }
    }


//    private void updateGreeting() {
//        String userId1 = Objects.requireNonNull(Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());
//        String userId = Objects.requireNonNull(String.valueOf(mAuth.getInstance().getCurrentUser()));
//
//        databaseReference = FirebaseDatabase.getInstance().getReference("users");
//        DatabaseReference userRef = databaseReference.child(userId1).child("firstName");
//        System.out.println("UserID : "+userId);
//        System.out.println("databaseReference : "+databaseReference.getDatabase());
//        System.out.println("databaseReference : "+databaseReference.getDatabase().getReference().getKey());
//        System.out.println("databaseReference : "+databaseReference.getDatabase().getReference());
//        System.out.println("databaseReference : "+userRef.toString());
//        userRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                try {
//                    // Check if the data exists
//                    if (dataSnapshot.exists()) {
//                        String firstName = dataSnapshot.getValue(String.class);
//
//                        Calendar calendar = Calendar.getInstance();
//                        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//
//                        String greeting;
//                        if (hourOfDay >= 5 && hourOfDay < 12) {
//                            greeting = "Good Morning, " + (firstName != null ? firstName : "User");
//                        } else if (hourOfDay >= 12 && hourOfDay < 17) {
//                            greeting = "Good Afternoon, " + (firstName != null ? firstName : "User");
//                        } else if (hourOfDay >= 17 && hourOfDay < 21) {
//                            greeting = "Good Evening, " + (firstName != null ? firstName : "User");
//                        } else {
//                            greeting = "Good Night, " + (firstName != null ? firstName : "User");
//                        }
//
//                        greetingTextView.setText(greeting);
//                    } else {
//                        // Handle the case where data does not exist
//                        Log.e("MainActivity", "No data found for userId: " + userId);
//                        Snackbar.make(mainView, "User data not found", Snackbar.LENGTH_SHORT)
//                                .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black))
//                                .setBackgroundTint(getColor(R.color.yellow))
//                                .show();
//
//                        // Fallback greeting
//                        Calendar calendar = Calendar.getInstance();
//                        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//
//                        String fallbackGreeting;
//                        if (hourOfDay >= 5 && hourOfDay < 12) {
//                            fallbackGreeting = "Good Morning, User";
//                        } else if (hourOfDay >= 12 && hourOfDay < 17) {
//                            fallbackGreeting = "Good Afternoon, User";
//                        } else if (hourOfDay >= 17 && hourOfDay < 21) {
//                            fallbackGreeting = "Good Evening, User";
//                        } else {
//                            fallbackGreeting = "Good Night, User";
//                        }
//
//                        greetingTextView.setText(fallbackGreeting);
//                    }
//                } catch (Exception exception) {
//                    Log.e("MainActivity", "Error processing greeting data", exception);
//                    Snackbar.make(mainView, "Error retrieving greeting data", Snackbar.LENGTH_SHORT)
//                            .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black))
//                            .setBackgroundTint(getColor(R.color.yellow))
//                            .show();
//
//                    // Fallback greeting in case of an error
//                    Calendar calendar = Calendar.getInstance();
//                    int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//
//                    String fallbackGreeting;
//                    if (hourOfDay >= 5 && hourOfDay < 12) {
//                        fallbackGreeting = "Good Morning, User";
//                    } else if (hourOfDay >= 12 && hourOfDay < 17) {
//                        fallbackGreeting = "Good Afternoon, User";
//                    } else if (hourOfDay >= 17 && hourOfDay < 21) {
//                        fallbackGreeting = "Good Evening, User";
//                    } else {
//                        fallbackGreeting = "Good Night, User";
//                    }
//
//                    greetingTextView.setText(fallbackGreeting);
//                }
//            }


    private void updateGreeting() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String userMail = currentUser.getEmail();

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId).child("firstName");

//            System.out.println("CurrentUser  : "+currentUser.getDisplayName());
//            System.out.println("UserID  : "+userId);
//            System.out.println("UserMail  : "+userMail);
//            System.out.println("userRef : "+userRef.toString());
//            System.out.println("database : "+databaseReference.getDatabase());
//            System.out.println("databaseReference : "+databaseReference.getDatabase().getReference());
//            System.out.println("databaseReferenceKey : "+databaseReference.getDatabase().getReference().getKey());
            userRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        // Check if the data exists
                        if (dataSnapshot.exists()) {
                            String firstName = dataSnapshot.getValue(String.class);

                            if (firstName != null) {
                                firstName = firstName.toLowerCase(); // Convert to lowercase
                                firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1); // Capitalize first letter
                            }

                            Calendar calendar = Calendar.getInstance();
                            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

                            String greeting;
                            if (hourOfDay >= 5 && hourOfDay < 12) {
                                greeting = "Good Morning, " + (firstName != null ? firstName : "User");
                            } else if (hourOfDay >= 12 && hourOfDay < 17) {
                                greeting = "Good Afternoon, " + (firstName != null ? firstName : "User");
                            } else if (hourOfDay >= 17 && hourOfDay < 21) {
                                greeting = "Good Evening, " + (firstName != null ? firstName : "User");
                            } else {
                                greeting = "Good Night, " + (firstName != null ? firstName : "User");
                            }

                            greetingTextView.setText(greeting);
                        } else {
                            // Handle the case where data does not exist
                            Log.e("MainActivity", "No data found for userId: " + userId);
                            Snackbar.make(mainView, "User data not found", Snackbar.LENGTH_SHORT)
                                    .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black))
                                    .setBackgroundTint(getColor(R.color.yellow))
                                    .show();

                            // Fallback greeting
                            Calendar calendar = Calendar.getInstance();
                            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

                            String fallbackGreeting;
                            if (hourOfDay >= 5 && hourOfDay < 12) {
                                fallbackGreeting = "Good Morning, User";
                            } else if (hourOfDay >= 12 && hourOfDay < 17) {
                                fallbackGreeting = "Good Afternoon, User";
                            } else if (hourOfDay >= 17 && hourOfDay < 21) {
                                fallbackGreeting = "Good Evening, User";
                            } else {
                                fallbackGreeting = "Good Night, User";
                            }

                            greetingTextView.setText(fallbackGreeting);
                        }
                    } catch (Exception exception) {
                        Log.e("MainActivity", "Error processing greeting data", exception);
                        Snackbar.make(mainView, "Error retrieving greeting data", Snackbar.LENGTH_SHORT)
                                .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black))
                                .setBackgroundTint(getColor(R.color.yellow))
                                .show();

                        // Fallback greeting in case of an error
                        Calendar calendar = Calendar.getInstance();
                        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

                        String fallbackGreeting;
                        if (hourOfDay >= 5 && hourOfDay < 12) {
                            fallbackGreeting = "Good Morning, User";
                        } else if (hourOfDay >= 12 && hourOfDay < 17) {
                            fallbackGreeting = "Good Afternoon, User";
                        } else if (hourOfDay >= 17 && hourOfDay < 21) {
                            fallbackGreeting = "Good Evening, User";
                        } else {
                            fallbackGreeting = "Good Night, User";
                        }

                        greetingTextView.setText(fallbackGreeting);
                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {
                    Log.e("MainActivity", "Error retrieving user data", error.toException());
                }

//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) /*{
//                    Log.e("MainActivity", "Error retrieving user data", databaseError.toException());
//                    Snackbar.make(mainView, "Database error: " + databaseError.getMessage(), Snackbar.LENGTH_SHORT)
//                            .setTextColor(ContextCompat.getColor(MainActivity.this, R.color.black))
//                            .setBackgroundTint(getColor(R.color.yellow))
//                            .show();
//
//                    // Fallback greeting in case of an error
//                    Calendar calendar = Calendar.getInstance();
//                    int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//
//                    String fallbackGreeting;
//                    if (hourOfDay >= 5 && hourOfDay < 12) {
//                        fallbackGreeting = "Good Morning, User";
//                    } else if (hourOfDay >= 12 && hourOfDay < 17) {
//                        fallbackGreeting = "Good Afternoon, User";
//                    } else if (hourOfDay >= 17 && hourOfDay < 21) {
//                        fallbackGreeting = "Good Evening, User";
//                    } else {
//                        fallbackGreeting = "Good Night, User";
//                    }
//
//                    greetingTextView.setText(fallbackGreeting);
//                }*/
            });
        }
    }

    private void showAddTransactionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Transaction");

        // Set up the input fields
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_transaction, null);
        EditText etAmount = dialogView.findViewById(R.id.et_amount);
        EditText etBankName = dialogView.findViewById(R.id.et_bank_name);
        TextView tvDate = dialogView.findViewById(R.id.tv_date);
        TextView tvTime = dialogView.findViewById(R.id.tv_time);
        Button btnPickDate = dialogView.findViewById(R.id.btn_pick_date);
        Button btnPickTime = dialogView.findViewById(R.id.btn_pick_time);
        RadioGroup radioGroupTransactionType = dialogView.findViewById(R.id.radio_group_transaction_type);

        builder.setView(dialogView);

        // Date picker
        btnPickDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
                String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
                tvDate.setText(selectedDate);
            }, year, month, day).show();
        });

        // Time picker
        btnPickTime.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            new TimePickerDialog(this, (view, hourOfDay, minute1) -> {
                // Convert time to 12-hour format
                String time12HourFormat = convertTo12HourFormat(hourOfDay, minute1);
                tvTime.setText(time12HourFormat);
            }, hour, minute, false).show(); // Use false to indicate 24-hour format for internal usage
        });

        builder.setPositiveButton("Add", (dialog, which) -> {
            String amountStr = etAmount.getText().toString();
            String bankName = etBankName.getText().toString().toUpperCase();
            String date = tvDate.getText().toString();
            String time = tvTime.getText().toString();

            // Determine if the transaction is credit or debit
            int selectedRadioButtonId = radioGroupTransactionType.getCheckedRadioButtonId();
            boolean isCredit = selectedRadioButtonId == R.id.radio_credit;

            if (amountStr.isEmpty() || bankName.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Snackbar snackbar = Snackbar.make(mainView, "Please fill all fields in add transaction window", Snackbar.LENGTH_LONG);
                customizeSnackbar(snackbar);
                snackbar.show();
//                Toast.makeText(this, "Please fill all fields in add transaction window", Toast.LENGTH_LONG).show();
                return;
            }

            try {
                BigDecimal amount = new BigDecimal(amountStr);
                amount = amount.setScale(2, RoundingMode.HALF_UP); // Limit decimal points to 2

                // Extract integer part before the decimal point
                String integerPart = amount.toBigInteger().toString();

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    Snackbar snackbar = Snackbar.make(mainView, "Amount should be greater than zero", Snackbar.LENGTH_LONG);
                    customizeSnackbar(snackbar);
                    snackbar.show();
                    return;
                } else if (integerPart.length() > 7) {
                    Snackbar snackbar1 = Snackbar.make(mainView, "Amount should be up to 7 digits before the decimal point", Snackbar.LENGTH_LONG);
                    customizeSnackbar(snackbar1);
                    snackbar1.show();
                    return;
                }

                int bankLogo = getBankLogo(bankName); // Fetch appropriate logo if needed

                storeTransaction(amount, isCredit, bankName, bankLogo, date, time);
                updateDailyLog(amount, isCredit, bankName); // Update dailyLog

                // Refresh the display after adding
                displayLog();

//                Toast.makeText(this, "Transaction added successfully", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(mainView, "Transaction added successfully", Snackbar.LENGTH_LONG);
                customizeSnackbar(snackbar);
                snackbar.show();
            } catch (NumberFormatException e) {
//                Toast.makeText(this, "Invalid amount format", Toast.LENGTH_SHORT).show();
                Snackbar snackbar = Snackbar.make(mainView, "Invalid amount format", Snackbar.LENGTH_LONG);
                customizeSnackbar(snackbar);
                snackbar.show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.getContext().setTheme(R.style.RoundedCornerDialog);
        builder.show();
    }


    // Helper method to convert time from 24-hour format to 12-hour format
    private String convertTo12HourFormat(int hour, int minute) {
        // Create a SimpleDateFormat object for the 12-hour format
        SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        // Format the time to 12-hour format
        return sdf12.format(calendar.getTime());
    }


    private void updateDailyLog(BigDecimal amount, boolean isCredit, String bankName) {
        SharedPreferences sharedPreferences = getSharedPreferences("financeApp", MODE_PRIVATE);
        String json = sharedPreferences.getString("dailyLog", "{}");
        Type type = new TypeToken<HashMap<String, SmsReceiver.DailyAmount>>() {}.getType();
        Map<String, SmsReceiver.DailyAmount> dailyLog = new Gson().fromJson(json, type);

        if (dailyLog == null) {
            dailyLog = new HashMap<>();
        }

        String currentDate = getCurrentDate(); // Format: yyyy-MM-dd
        SmsReceiver.DailyAmount dailyAmount = dailyLog.getOrDefault(currentDate, new SmsReceiver.DailyAmount());

        if (isCredit) {
            dailyAmount.credited = dailyAmount.credited.add(amount);
        } else {
            dailyAmount.debited = dailyAmount.debited.add(amount);
        }

        // Update bank transaction count
        dailyAmount.bankTransactionCount.put(bankName, dailyAmount.bankTransactionCount.getOrDefault(bankName, 0) + 1);

        dailyLog.put(currentDate, dailyAmount);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dailyLog", new Gson().toJson(dailyLog));
        editor.apply();
    }


    private void storeTransaction(BigDecimal amount, boolean isCredit, String bankName, int bankLogo, String date, String time) {
        SharedPreferences sharedPreferences = getSharedPreferences("financeApp", MODE_PRIVATE);
        String json = sharedPreferences.getString("transactions", "[]");
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        List<Transaction> transactions = new Gson().fromJson(json, type);

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        Transaction transaction = new Transaction(date, time, amount, bankName, bankLogo, isCredit);
        transactions.add(transaction);

        String newJson = new Gson().toJson(transactions);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("transactions", newJson);
        editor.apply();
    }


    private int getBankLogo(String bankName) {
        return switch (bankName.toUpperCase()) {
            case "HDFC"  -> R.drawable.hdfc;
            case "ICICI" -> R.drawable.icici;
            case "YES"   -> R.drawable.yes;
            case "PNB"   -> R.drawable.pnb;
            case "AXIS"  -> R.drawable.axis;
            case "SBI"   -> R.drawable.sbi;
            case "INR"   -> R.drawable.sbi;
            default      -> R.drawable.bank_def;
        };
    }

    //end of the code
}