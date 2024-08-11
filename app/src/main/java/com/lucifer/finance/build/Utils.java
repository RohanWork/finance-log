package com.lucifer.finance.build;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lucifer.finance.smsfunctionality.SmsReceiver;
import com.lucifer.finance.transaction.Transaction;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import com.lucifer.finance.R;

public class Utils {

    public static void customizeSnackbar(Context context, Snackbar snackbar) {
        snackbar.setTextColor(ContextCompat.getColor(context, R.color.black));
        snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.yellow));
        View snackbarView = snackbar.getView();
        TextView textView = snackbarView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextSize(15);
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.black));
    }

    public static String formatAmount(BigDecimal amount) {
        NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        return formatter.format(amount.doubleValue());
    }

    public static String convertTo12HourFormat(int hour, int minute) {
        SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm", Locale.getDefault());
        SimpleDateFormat sdf12 = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        return sdf12.format(calendar.getTime());
    }

    public static void showAddTransactionDialog(Context context, View mainView, SharedPreferences sharedPreferences, Runnable onTransactionAdded) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Add Transaction");

        View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_transaction, null);
        EditText etAmount = dialogView.findViewById(R.id.et_amount);
        EditText etBankName = dialogView.findViewById(R.id.et_bank_name);
        TextView tvDate = dialogView.findViewById(R.id.tv_date);
        TextView tvTime = dialogView.findViewById(R.id.tv_time);
        Button btnPickDate = dialogView.findViewById(R.id.btn_pick_date);
        Button btnPickTime = dialogView.findViewById(R.id.btn_pick_time);
        RadioGroup radioGroupTransactionType = dialogView.findViewById(R.id.radio_group_transaction_type);

        builder.setView(dialogView);

        btnPickDate.setOnClickListener(v -> showDatePicker(context, tvDate));
        btnPickTime.setOnClickListener(v -> showTimePicker(context, tvTime));

        builder.setPositiveButton("Add", (dialog, which) -> {
            String amountStr = etAmount.getText().toString();
            String bankName = etBankName.getText().toString();
            String date = tvDate.getText().toString();
            String time = tvTime.getText().toString();

            int selectedRadioButtonId = radioGroupTransactionType.getCheckedRadioButtonId();
            boolean isCredit = selectedRadioButtonId == R.id.radio_credit;

            if (amountStr.isEmpty() || bankName.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Snackbar snackbar = Snackbar.make(mainView, "Please fill all fields in add transaction window", Snackbar.LENGTH_LONG);
                customizeSnackbar(context, snackbar);
                snackbar.show();
                return;
            }

            try {
                BigDecimal amount = new BigDecimal(amountStr);
                amount = amount.setScale(2, RoundingMode.HALF_UP);
                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    Snackbar snackbar = Snackbar.make(mainView, "Amount should be greater than zero", Snackbar.LENGTH_LONG);
                    customizeSnackbar(context, snackbar);
                    snackbar.show();
                    return;
                }

                int bankLogo = getBankLogo(context, bankName);
                storeTransaction(context, sharedPreferences, amount, isCredit, bankName, bankLogo, date, time);
                updateDailyLog(context, sharedPreferences, amount, isCredit, bankName);

                Snackbar snackbar = Snackbar.make(mainView, "Transaction added successfully", Snackbar.LENGTH_LONG);
                customizeSnackbar(context, snackbar);
                snackbar.show();
                if (onTransactionAdded != null) {
                    onTransactionAdded.run();
                }
            } catch (NumberFormatException e) {
                Snackbar snackbar = Snackbar.make(mainView, "Invalid amount format", Snackbar.LENGTH_LONG);
                customizeSnackbar(context, snackbar);
                snackbar.show();
            }
        });

        builder.setNegativeButton("Cancel", null);
        builder.getContext().setTheme(R.style.RoundedCornerDialog);
        builder.show();
    }

    private static void showDatePicker(Context context, TextView tvDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(context, (view, year1, month1, dayOfMonth) -> {
            String selectedDate = String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth);
            tvDate.setText(selectedDate);
        }, year, month, day).show();
    }

    private static void showTimePicker(Context context, TextView tvTime) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(context, (view, hourOfDay, minute1) -> {
            String time12HourFormat = convertTo12HourFormat(hourOfDay, minute1);
            tvTime.setText(time12HourFormat);
        }, hour, minute, false).show();
    }

    private static void storeTransaction(Context context, SharedPreferences sharedPreferences, BigDecimal amount, boolean isCredit, String bankName, int bankLogo, String date, String time) {
        String json = sharedPreferences.getString("transactions", "[]");
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        List<Transaction> transactions = new Gson().fromJson(json, type);

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        Transaction transaction = new Transaction(date, time, amount, bankName, bankLogo, isCredit);
        transactions.add(transaction);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("transactions", new Gson().toJson(transactions));
        editor.apply();
    }

    private static void updateDailyLog(Context context, SharedPreferences sharedPreferences, BigDecimal amount, boolean isCredit, String bankName) {
        String json = sharedPreferences.getString("dailyLog", "{}");
        Type type = new TypeToken<HashMap<String, SmsReceiver.DailyAmount>>() {}.getType();
        Map<String, SmsReceiver.DailyAmount> dailyLog = new Gson().fromJson(json, type);

        if (dailyLog == null) {
            dailyLog = new HashMap<>();
        }

        String currentDate = getCurrentDate();
        SmsReceiver.DailyAmount dailyAmount = dailyLog.getOrDefault(currentDate, new SmsReceiver.DailyAmount());

        if (isCredit) {
            dailyAmount.credited = dailyAmount.credited.add(amount);
        } else {
            dailyAmount.debited = dailyAmount.debited.add(amount);
        }

        dailyAmount.bankTransactionCount.put(bankName, dailyAmount.bankTransactionCount.getOrDefault(bankName, 0) + 1);
        dailyLog.put(currentDate, dailyAmount);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dailyLog", new Gson().toJson(dailyLog));
        editor.apply();
    }

    private static int getBankLogo(Context context, String bankName) {
        return switch (bankName.toUpperCase()) {
            case "HDFC"  -> R.drawable.hdfc;
            case "ICICI" -> R.drawable.icici;
            case "YES"   -> R.drawable.yes;
            case "PNB"   -> R.drawable.pnb;
            case "AXIS"  -> R.drawable.axis;
            case "SBI"   -> R.drawable.sbi;
            case "INR"   -> R.drawable.sbi;
            default      -> R.drawable.bank;
        };
    }

    private static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Months are 0-based
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return String.format(Locale.getDefault(), "%d-%02d-%02d", year, month, day);
    }
}
