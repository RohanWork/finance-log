package com.lucifer.finance;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];

                for (int i = 0; i < pdus.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                    } else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                }

                for (SmsMessage message : messages) {
                    String msgBody = message.getMessageBody();
                    processMessage(context, msgBody);
                }
            }
        }

        // Ensure addDummyData is called outside of the if-block
//        addDummyData(context);
    }

    private void processMessage(Context context, String msgBody) {
        double amount = extractAmount(msgBody);
        if (amount != 0) {
            boolean isCredit = msgBody.toLowerCase().contains("credited") || msgBody.toLowerCase().contains("credit");
            updateLog(context, amount, isCredit);
        }
    }

    private double extractAmount(String msgBody) {
        // Extract the amount from the message body using regex or any other method
        // This implementation specifically looks for "Rs."
        String[] words = msgBody.split(" ");
        for (String word : words) {
            try {
                if (word.startsWith("Rs.")) {
                    return Double.parseDouble(word.replace("Rs.", "").replace(",", ""));
                }
            } catch (NumberFormatException e) {
                // Ignore
            }
        }
        return 0;
    }

    private void updateLog(Context context, double amount, boolean isCredit) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("financeApp", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("dailyLog", "{}");
        Type type = new TypeToken<HashMap<String, DailyAmount>>() {}.getType();
        Map<String, DailyAmount> dailyLog = new Gson().fromJson(json, type);

        if (dailyLog == null) {
            dailyLog = new HashMap<>();
        }

        String currentDate = getCurrentDate();
        DailyAmount dailyAmount = dailyLog.getOrDefault(currentDate, new DailyAmount());

        if (isCredit) {
            dailyAmount.credited += amount;
        } else {
            dailyAmount.debited += amount;
        }

        dailyLog.put(currentDate, dailyAmount);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dailyLog", new Gson().toJson(dailyLog));
        editor.apply();

        Toast.makeText(context, "Transaction logged: " + (isCredit ? "Credit" : "Debit") + " Rs." + amount, Toast.LENGTH_SHORT).show();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private void addDummyData(Context context) {
        String[] dummyMessages = {
                "credited Rs.100.00",
                "debited Rs.50.00",
                "credited Rs.200.00",
                "debited Rs.75.00"
        };

        for (String message : dummyMessages) {
            processMessage(context, message);
        }

        Toast.makeText(context, "Dummy data added.", Toast.LENGTH_SHORT).show();
    }

    public static class DailyAmount {
        double credited;
        double debited;

        double total() {
            return credited + debited;
        }
    }
}
