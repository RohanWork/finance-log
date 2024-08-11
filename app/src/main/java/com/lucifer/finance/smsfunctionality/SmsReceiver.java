////package com.lucifer.finance;
////
////import android.content.BroadcastReceiver;
////import android.content.Context;
////import android.content.Intent;
////import android.content.SharedPreferences;
////import android.os.Build;
////import android.os.Bundle;
////import android.telephony.SmsMessage;
////import android.widget.Toast;
////import com.google.gson.Gson;
////import com.google.gson.reflect.TypeToken;
////
////import java.lang.reflect.Type;
////import java.text.SimpleDateFormat;
////import java.util.Date;
////import java.util.HashMap;
////import java.util.Locale;
////import java.util.Map;
////import java.util.regex.Matcher;
////import java.util.regex.Pattern;
////
////public class SmsReceiver extends BroadcastReceiver {
////
////    @Override
////    public void onReceive(Context context, Intent intent) {
////        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
////            Bundle bundle = intent.getExtras();
////            if (bundle != null) {
////                Object[] pdus = (Object[]) bundle.get("pdus");
////                SmsMessage[] messages = new SmsMessage[pdus.length];
////
////                for (int i = 0; i < pdus.length; i++) {
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                        String format = bundle.getString("format");
////                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
////                    } else {
////                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
////                    }
////                }
////
////                for (SmsMessage message : messages) {
////                    String msgBody = message.getMessageBody();
////                    processMessage(context, msgBody);
////                }
////            }
////        }
////    }
////
////    private void processMessage(Context context, String msgBody) {
////        boolean[] isCreditDebit = new boolean[2];
////        double amount = extractAmount(msgBody, isCreditDebit);
////
////        if (amount != 0) {
////            boolean isCredit = isCreditDebit[0];
////            boolean isDebit = isCreditDebit[1];
////            updateLog(context, amount, isCredit, isDebit);
////        }
////    }
////
////    private double extractAmount(String msgBody, boolean[] isCreditDebit) {
////        // Regex pattern to match amounts (including those with commas and decimals)
////        Pattern amountPattern = Pattern.compile("((?<!\\w)\\d{1,3}(?:,\\d{3})*(?:\\.\\d{1,2})?)");
////        Matcher matcher = amountPattern.matcher(msgBody);
////
////        boolean isCredit = msgBody.toLowerCase().contains("credited");
////        boolean isDebit = msgBody.toLowerCase().contains("debited") || msgBody.toLowerCase().contains("debited for INR");
////
////        // Set the isCreditDebit flag to indicate if it's a credit or debit
////        isCreditDebit[0] = isCredit;
////        isCreditDebit[1] = isDebit;
////
////        if (isCredit || isDebit) {
////            // Find the first numerical value in the message
////            if (matcher.find()) {
////                String amountStr = matcher.group(1).replace(",", "");
////                try {
////                    return Double.parseDouble(amountStr);
////                } catch (NumberFormatException e) {
////                    // Ignore and return 0 if parsing fails
////                }
////            }
////        }
////        return 0;
////    }
////
////    private void updateLog(Context context, double amount, boolean isCredit, boolean isDebit) {
////        SharedPreferences sharedPreferences = context.getSharedPreferences("financeApp", Context.MODE_PRIVATE);
////        String json = sharedPreferences.getString("dailyLog", "{}");
////        Type type = new TypeToken<HashMap<String, DailyAmount>>() {}.getType();
////        Map<String, DailyAmount> dailyLog = new Gson().fromJson(json, type);
////
////        if (dailyLog == null) {
////            dailyLog = new HashMap<>();
////        }
////
////        String currentDate = getCurrentDate();
////        DailyAmount dailyAmount = dailyLog.getOrDefault(currentDate, new DailyAmount());
////
////        if (isCredit) {
////            assert dailyAmount != null;
////            dailyAmount.credited += amount;
////        } else if (isDebit) {
////            assert dailyAmount != null;
////            dailyAmount.debited += amount;
////        }
////
////        dailyLog.put(currentDate, dailyAmount);
////
////        SharedPreferences.Editor editor = sharedPreferences.edit();
////        editor.putString("dailyLog", new Gson().toJson(dailyLog));
////        editor.apply();
////
////        Toast.makeText(context, "Transaction logged: " + (isCredit ? "Credited" : "Debited") + " Rs." + amount, Toast.LENGTH_SHORT).show();
////    }
////
////    private String getCurrentDate() {
////        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
////        return dateFormat.format(new Date());
////    }
////
////    public static class DailyAmount {
////        double credited;
////        double debited;
////
////        double total() {
////            return credited + debited;
////        }
////    }
////}
//
//
////package com.lucifer.finance;
////
////import android.content.BroadcastReceiver;
////import android.content.Context;
////import android.content.Intent;
////import android.content.SharedPreferences;
////import android.os.Build;
////import android.os.Bundle;
////import android.telephony.SmsMessage;
////import android.widget.Toast;
////import com.google.gson.Gson;
////import com.google.gson.reflect.TypeToken;
////
////import java.lang.reflect.Type;
////import java.text.SimpleDateFormat;
////import java.util.Date;
////import java.util.HashMap;
////import java.util.Locale;
////import java.util.Map;
////import java.util.regex.Matcher;
////import java.util.regex.Pattern;
////
////public class SmsReceiver extends BroadcastReceiver {
////
////    @Override
////    public void onReceive(Context context, Intent intent) {
////        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
////            Bundle bundle = intent.getExtras();
////            if (bundle != null) {
////                Object[] pdus = (Object[]) bundle.get("pdus");
////                SmsMessage[] messages = new SmsMessage[pdus.length];
////
////                for (int i = 0; i < pdus.length; i++) {
////                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////                        String format = bundle.getString("format");
////                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
////                    } else {
////                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
////                    }
////                }
////
////                for (SmsMessage message : messages) {
////                    String msgBody = message.getMessageBody();
////                    processMessage(context, msgBody);
////                }
////            }
////        }
////    }
////
////    private void processMessage(Context context, String msgBody) {
////        boolean[] isCreditDebit = new boolean[2];
////        double amount = extractAmount(msgBody, isCreditDebit);
////
////        if (amount != 0) {
////            boolean isCredit = isCreditDebit[0];
////            boolean isDebit = isCreditDebit[1];
////            String bankName = extractBankName(msgBody);
////            updateLog(context, amount, isCredit, isDebit, bankName);
////        }
////    }
////
////    private double extractAmount(String msgBody, boolean[] isCreditDebit) {
////        // Regex pattern to match amounts (including those with commas and decimals)
////        Pattern amountPattern = Pattern.compile("((?<!\\w)\\d{1,3}(?:,\\d{3})*(?:\\.\\d{1,2})?)");
////        Matcher matcher = amountPattern.matcher(msgBody);
////
////        boolean isCredit = msgBody.toLowerCase().contains("credited");
////        boolean isDebit = msgBody.toLowerCase().contains("debited") || msgBody.toLowerCase().contains("debited for") || msgBody.toLowerCase().contains("debited by") || msgBody.toLowerCase().contains("debited by Rs.");
////
////        // Set the isCreditDebit flag to indicate if it's a credit or debit
////        isCreditDebit[0] = isCredit;
////        isCreditDebit[1] = isDebit;
////
////        if (isCredit || isDebit) {
////            // Find the first numerical value in the message
////            if (matcher.find()) {
////                String amountStr = matcher.group(1).replace(",", "");
////                try {
////                    return Double.parseDouble(amountStr);
////                } catch (NumberFormatException e) {
////                    // Ignore and return 0 if parsing fails
////                }
////            }
////        }
////        return 0;
////    }
////
////    private String extractBankName(String msgBody) {
////        // Check for known bank names at the end of the message
////        Pattern bankPattern = Pattern.compile("-\\s*(\\w+\\s*\\w+)$");
////        Matcher matcher = bankPattern.matcher(msgBody);
////
////        if (matcher.find()) {
////            return matcher.group(1).toUpperCase().trim();
////        }
////
////        // Fallback to checking for capitalized words within the message
////        bankPattern = Pattern.compile("(\\b[A-Z]{2,}(?:\\s+[A-Z]{2,})*\\b)");
////        matcher = bankPattern.matcher(msgBody);
////        if (matcher.find()) {
////            return matcher.group(1).toUpperCase().trim();
////        }
////
////        return "Unknown";
////    }
////
////    private void updateLog(Context context, double amount, boolean isCredit, boolean isDebit, String bankName) {
////        SharedPreferences sharedPreferences = context.getSharedPreferences("financeApp", Context.MODE_PRIVATE);
////        String json = sharedPreferences.getString("dailyLog", "{}");
////        Type type = new TypeToken<HashMap<String, DailyAmount>>() {}.getType();
////        Map<String, DailyAmount> dailyLog = new Gson().fromJson(json, type);
////
////        if (dailyLog == null) {
////            dailyLog = new HashMap<>();
////        }
////
////        String currentDate = getCurrentDate();
////        DailyAmount dailyAmount = dailyLog.getOrDefault(currentDate, new DailyAmount());
////
////        if (isCredit) {
////            dailyAmount.credited += amount;
////        } else if (isDebit) {
////            dailyAmount.debited += amount;
////        }
////
////        // Update bank transaction count
////        dailyAmount.bankTransactionCount.put(bankName, dailyAmount.bankTransactionCount.getOrDefault(bankName, 0) + 1);
////
////        dailyLog.put(currentDate, dailyAmount);
////
////        SharedPreferences.Editor editor = sharedPreferences.edit();
////        editor.putString("dailyLog", new Gson().toJson(dailyLog));
////        editor.apply();
////
////        Toast.makeText(context, "Transaction logged: " + (isCredit ? "Credited" : "Debited") + " Rs." + amount + " from " + bankName, Toast.LENGTH_SHORT).show();
////    }
////
////    private String getCurrentDate() {
////        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
////        return dateFormat.format(new Date());
////    }
////
////    public static class DailyAmount {
////        double credited;
////        double debited;
////        Map<String, Integer> bankTransactionCount = new HashMap<>();
////
////        double total() {
////            return credited + debited;
////        }
////    }
////}
//
//
//
//
//package com.lucifer.finance;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.Bundle;
//import android.telephony.SmsMessage;
//import android.widget.Toast;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import java.lang.reflect.Type;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class SmsReceiver extends BroadcastReceiver {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
//            Bundle bundle = intent.getExtras();
//            if (bundle != null) {
//                Object[] pdus = (Object[]) bundle.get("pdus");
//                SmsMessage[] messages = new SmsMessage[pdus.length];
//
//                for (int i = 0; i < pdus.length; i++) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        String format = bundle.getString("format");
//                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
//                    } else {
//                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                    }
//                }
//
//                for (SmsMessage message : messages) {
//                    String msgBody = message.getMessageBody();
//                    processMessage(context, msgBody);
//                }
//            }
//        }
//    }
//
//    private void processMessage(Context context, String msgBody) {
//        boolean[] isCreditDebit = new boolean[2];
//        double amount = extractAmount(msgBody, isCreditDebit);
//
//        if (amount != 0) {
//            boolean isCredit = isCreditDebit[0];
//            boolean isDebit = isCreditDebit[1];
//            String bankName = extractBankName(msgBody);
//            updateLog(context, amount, isCredit, isDebit, bankName);
//        }
//    }
//
//    private double extractAmount(String msgBody, boolean[] isCreditDebit) {
//        // Regex patterns to match specific transaction patterns
//        Pattern creditPattern = Pattern.compile("(?:credited(?:\\s*for)?\\s*INR|credited\\s*Rs\\.?)\\s*(\\d{1,7}(?:,\\d{3})*(?:\\.\\d{1,2})?)");
//        Pattern debitPattern = Pattern.compile("(?:debited(?:\\s*for)?\\s*INR|debited\\s*by\\s*Rs\\.?|debited\\s*by)\\s*(\\d{1,7}(?:,\\d{3})*(?:\\.\\d{1,2})?)");
//
//        Matcher creditMatcher = creditPattern.matcher(msgBody);
//        Matcher debitMatcher = debitPattern.matcher(msgBody);
//
//        boolean isCredit = creditMatcher.find();
//        boolean isDebit = debitMatcher.find();
//
//        // Set the isCreditDebit flag to indicate if it's a credit or debit
//        isCreditDebit[0] = isCredit;
//        isCreditDebit[1] = isDebit;
//
//        String amountStr = null;
//        if (isCredit) {
//            amountStr = creditMatcher.group(1).replace(",", "");
//        } else if (isDebit) {
//            amountStr = debitMatcher.group(1).replace(",", "");
//        }
//
//        if (amountStr != null) {
//            try {
//                return Double.parseDouble(amountStr);
//            } catch (NumberFormatException e) {
//                // Ignore and return 0 if parsing fails
//            }
//        }
//
//        return 0;
//    }
//
//    private String extractBankName(String msgBody) {
//        // Check for known bank names at the end of the message
//        Pattern bankPattern = Pattern.compile("-\\s*(\\w+(?:\\s+\\w+)*)$");
//        Matcher matcher = bankPattern.matcher(msgBody);
//
//        if (matcher.find()) {
//            return matcher.group(1).toUpperCase().trim();
//        }
//
//        // Fallback to checking for capitalized words within the message
//        bankPattern = Pattern.compile("(\\b[A-Z]{2,}(?:\\s+[A-Z]{2,})*\\b)");
//        matcher = bankPattern.matcher(msgBody);
//        if (matcher.find()) {
//            return matcher.group(1).toUpperCase().trim();
//        }
//
//        return "Unknown";
//    }
//
//    private void updateLog(Context context, double amount, boolean isCredit, boolean isDebit, String bankName) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("financeApp", Context.MODE_PRIVATE);
//        String json = sharedPreferences.getString("dailyLog", "{}");
//        Type type = new TypeToken<HashMap<String, DailyAmount>>() {}.getType();
//        Map<String, DailyAmount> dailyLog = new Gson().fromJson(json, type);
//
//        if (dailyLog == null) {
//            dailyLog = new HashMap<>();
//        }
//
//        String currentDate = getCurrentDate();
//        DailyAmount dailyAmount = dailyLog.getOrDefault(currentDate, new DailyAmount());
//
//        if (isCredit) {
//            dailyAmount.credited += amount;
//        } else if (isDebit) {
//            dailyAmount.debited += amount;
//        }
//
//        // Update bank transaction count
//        dailyAmount.bankTransactionCount.put(bankName, dailyAmount.bankTransactionCount.getOrDefault(bankName, 0) + 1);
//
//        dailyLog.put(currentDate, dailyAmount);
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putString("dailyLog", new Gson().toJson(dailyLog));
//        editor.apply();
//
//        Toast.makeText(context, "Transaction logged: " + (isCredit ? "Credited" : "Debited") + " Rs." + amount + " from " + bankName, Toast.LENGTH_SHORT).show();
//    }
//
//    private String getCurrentDate() {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        return dateFormat.format(new Date());
//    }
//
//    public static class DailyAmount {
//        double credited;
//        double debited;
//        Map<String, Integer> bankTransactionCount = new HashMap<>();
//
//        double total() {
//            return credited + debited;
//        }
//    }
//}


/*
package com.lucifer.finance.smsfunctionality;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lucifer.finance.R;
import com.lucifer.finance.transaction.Transaction;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];

                for (int i = 0; i < pdus.length; i++) {
                    String format = bundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                }

                for (SmsMessage message : messages) {
                    String msgBody = message.getMessageBody();
                    processMessage(context, msgBody);
                }
            }
        }
    }

    private void processMessage(Context context, String msgBody) {
        boolean[] isCreditDebit = new boolean[2];
        BigDecimal amount = extractAmount(msgBody, isCreditDebit);

        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            boolean isCredit = isCreditDebit[0];
            boolean isDebit = isCreditDebit[1];
            String bankName = extractBankName(msgBody);
            int bankLogo = getBankLogo(bankName); // Assuming you have a method to get the bank logo
            updateLog(context, amount, isCredit, isDebit, bankName);
            storeTransaction(context, amount, isCredit, bankName, bankLogo);
        }
    }

    private BigDecimal extractAmount(String msgBody, boolean[] isCreditDebit) {
        // Updated regex patterns to match credit and debit transaction patterns
        Pattern creditPattern = Pattern.compile("(?i)\\b(credited|CR|deposit|received|added|inflow|funds added|credited to your account|amount credited|credited with|credit to)\\b.*?\\b(\\d{1,3}(?:,\\d{3})*(?:\\.\\d{2})?)\\b");
        Pattern debitPattern = Pattern.compile("(?i)\\b(debited|debit|DR|withdrawal|spent|deducted|outflow|funds withdrawn|amount debited|debited from your account|deducted from|debit to)\\b.*?\\b(\\d{1,3}(?:,\\d{3})*(?:\\.\\d{2})?)\\b");

        Matcher creditMatcher = creditPattern.matcher(msgBody);
        Matcher debitMatcher = debitPattern.matcher(msgBody);

        boolean isCredit = creditMatcher.find();
        boolean isDebit = debitMatcher.find();

        // Set the isCreditDebit flag to indicate if it's a credit or debit
        isCreditDebit[0] = isCredit;
        isCreditDebit[1] = isDebit;

        String amountStr = null;
        if (isCredit) {
            amountStr = creditMatcher.group(2).replace(",", "");
        } else if (isDebit) {
            amountStr = debitMatcher.group(2).replace(",", "");
        }

        if (amountStr != null) {
            try {
                return new BigDecimal(amountStr);
            } catch (NumberFormatException e) {
                // Log or handle the exception if needed
            }
        }

        return BigDecimal.ZERO;
    }



//    private BigDecimal extractAmount(String msgBody, boolean[] isCreditDebit) {
//        // Regex patterns to match specific transaction patterns
//        Pattern creditPattern = Pattern.compile(
//                "(?:credited|deposit|added|received|credited\\s*to|credited\\s*for|credited\\s*by)" +
//                        "\\s*(?:INR|Rs\\.?|Rs\\.|inr|rs\\.?|rs\\.|\\$|USD|usd)?" +
//                        "\\s*([\\d,\\.]+)",
//                Pattern.CASE_INSENSITIVE
//        );
//        Pattern debitPattern = Pattern.compile(
//                "(?:debited|withdrawn|deducted|paid|debit\\s*from|debited\\s*for|debited\\s*by|debit by\\s*of Rs)" +
//                        "\\s*(?:INR|Rs\\.?|Rs\\.|inr|rs\\.?|rs\\.|\\$|USD|usd)?" +
//                        "\\s*([\\d,\\.]+)",
//                Pattern.CASE_INSENSITIVE
//        );
//
//        Matcher creditMatcher = creditPattern.matcher(msgBody);
//        Matcher debitMatcher = debitPattern.matcher(msgBody);
//
//        boolean isCredit = creditMatcher.find();
//        boolean isDebit = debitMatcher.find();
//
//        // Set the isCreditDebit flag to indicate if it's a credit or debit
//        isCreditDebit[0] = isCredit;
//        isCreditDebit[1] = isDebit;
//
//        String amountStr = null;
//        if (isCredit) {
//            amountStr = creditMatcher.group(1).replace(",", "");
//        } else if (isDebit) {
//            amountStr = debitMatcher.group(1).replace(",", "");
//        }
//
//        if (amountStr != null) {
//            try {
//                return new BigDecimal(amountStr);
//            } catch (NumberFormatException e) {
//                e.getLocalizedMessage();
//            }
//        }
//
//        return BigDecimal.ZERO;
//    }


    private String extractBankName(String msgBody) {
        // Check for known bank names at the end of the message
        Pattern bankPattern = Pattern.compile("-\\s*(\\w+(?:\\s+\\w+)*)$");
        Matcher matcher = bankPattern.matcher(msgBody);

        if (matcher.find()) {
            if (matcher.group(1).toUpperCase().trim().equals("INR")) {
                return "SBI";
            }
            else {
                return matcher.group(1).toUpperCase().trim();
            }
        }

        // Fallback to checking for capitalized words within the message
        bankPattern = Pattern.compile("(\\b[A-Z]{2,}(?:\\s+[A-Z]{2,})*\\b)");
        matcher = bankPattern.matcher(msgBody);
        if (matcher.find()) {
            if (matcher.group(1).toUpperCase().trim().equals("INR")) {
                return "SBI";
            }
            else {
                return matcher.group(1).toUpperCase().trim();
            }
        }

        return "Unknown";
    }

    private int getBankLogo(String bankName) {
        // Implement this method to return the correct bank logo based on the bank name
        // For example:
        return switch (bankName) {
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

    private void updateLog(Context context, BigDecimal amount, boolean isCredit, boolean isDebit, String bankName) {
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
            if (dailyAmount != null) {
                dailyAmount.credited = dailyAmount.credited.add(amount);
            }
        } else if (isDebit) {
            if (dailyAmount != null) {
                dailyAmount.debited = dailyAmount.debited.add(amount);
            }
        }

        // Update bank transaction count
        assert dailyAmount != null;
        dailyAmount.bankTransactionCount.put(bankName, dailyAmount.bankTransactionCount.getOrDefault(bankName, 0) + 1);

        dailyLog.put(currentDate, dailyAmount);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dailyLog", new Gson().toJson(dailyLog));
        editor.apply();

        Toast.makeText(context, "Transaction logged: \n" + (isCredit ? "Credited" : "Debited") + " Rs." + amount + " from " + bankName, Toast.LENGTH_SHORT).show();
    }

    private void storeTransaction(Context context, BigDecimal amount, boolean isCredit, String bankName, int bankLogo) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("financeApp", Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("transactions", "[]");
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        List<Transaction> transactions = new Gson().fromJson(json, type);

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        String currentDate = getCurrentDate();
        String currentTime = getCurrentTime();
        Transaction transaction = new Transaction(currentDate, currentTime, amount, bankName, bankLogo, isCredit);
        transactions.add(transaction);

        String newJson = new Gson().toJson(transactions);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("transactions", newJson);
        editor.apply();
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return timeFormat.format(new Date());
    }

    public static class DailyAmount {
        public BigDecimal credited = BigDecimal.ZERO;
        public BigDecimal debited = BigDecimal.ZERO;
        public Map<String, Integer> bankTransactionCount = new HashMap<>();

        public BigDecimal total() {
            return credited.add(debited);
        }
    }
}
*/







/*package com.lucifer.finance.smsfunctionality;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lucifer.finance.R;
import com.lucifer.finance.transaction.Transaction;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SHARED_PREFS_NAME = "financeApp";
    private static final String PROCESSED_TRANSACTIONS_KEY = "processedTransactions";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SmsReceiver", "onReceive called");
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];

                for (int i = 0; i < pdus.length; i++) {
                    String format = bundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                }

                for (SmsMessage message : messages) {
                    String msgBody = message.getMessageBody();
                    processMessage(context, msgBody);
                }
            }
        }
    }

    private void processMessage(Context context, String msgBody) {
        boolean[] isCreditDebit = new boolean[2];
        BigDecimal amount = extractAmount(msgBody, isCreditDebit);

        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            boolean isCredit = isCreditDebit[0];
            boolean isDebit = isCreditDebit[1];
            String bankName = extractBankName(msgBody);
            int bankLogo = getBankLogo(bankName);

            String uniqueId = generateUniqueId(msgBody);  // Generate a unique ID for this transaction
            if (isTransactionProcessed(context, uniqueId)) {
                return;  // Transaction already processed
            }

            updateLog(context, amount, isCredit, isDebit, bankName);
            storeTransaction(context, amount, isCredit, bankName, bankLogo, uniqueId);
        }
    }

    private String generateUniqueId(String msgBody) {
        // Create a unique identifier based on the message body and timestamp
        return String.valueOf(msgBody.hashCode() + getCurrentTimestamp());
    }

    private boolean isTransactionProcessed(Context context, String uniqueId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> processedTransactions = sharedPreferences.getStringSet(PROCESSED_TRANSACTIONS_KEY, new HashSet<>());
        return processedTransactions.contains(uniqueId);
    }

    private void markTransactionAsProcessed(Context context, String uniqueId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> processedTransactions = sharedPreferences.getStringSet(PROCESSED_TRANSACTIONS_KEY, new HashSet<>());
        processedTransactions.add(uniqueId);
        editor.putStringSet(PROCESSED_TRANSACTIONS_KEY, processedTransactions);
        editor.apply();
    }

    private void updateLog(Context context, BigDecimal amount, boolean isCredit, boolean isDebit, String bankName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("dailyLog", "{}");
        Type type = new TypeToken<HashMap<String, DailyAmount>>() {}.getType();
        Map<String, DailyAmount> dailyLog = new Gson().fromJson(json, type);

        if (dailyLog == null) {
            dailyLog = new HashMap<>();
        }

        String currentDate = getCurrentDate();
        DailyAmount dailyAmount = dailyLog.getOrDefault(currentDate, new DailyAmount());

        if (isCredit) {
            if (dailyAmount != null) {
                dailyAmount.credited = dailyAmount.credited.add(amount);
            }
        } else if (isDebit) {
            if (dailyAmount != null) {
                dailyAmount.debited = dailyAmount.debited.add(amount);
            }
        }

        // Update bank transaction count
        assert dailyAmount != null;
        dailyAmount.bankTransactionCount.put(bankName, dailyAmount.bankTransactionCount.getOrDefault(bankName, 0) + 1);

        dailyLog.put(currentDate, dailyAmount);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dailyLog", new Gson().toJson(dailyLog));
        editor.apply();

        Toast.makeText(context, "Transaction logged: \n" + (isCredit ? "Credited" : "Debited") + " Rs." + amount + " from " + bankName, Toast.LENGTH_SHORT).show();
    }

    private void storeTransaction(Context context, BigDecimal amount, boolean isCredit, String bankName, int bankLogo, String uniqueId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("transactions", "[]");
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        List<Transaction> transactions = new Gson().fromJson(json, type);

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        String currentDate = getCurrentDate();
        String currentTime = getCurrentTime();
        Transaction transaction = new Transaction(currentDate, currentTime, amount, bankName, bankLogo, isCredit);
        transactions.add(transaction);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("transactions", new Gson().toJson(transactions));
        editor.apply();

        markTransactionAsProcessed(context, uniqueId);
    }

    private BigDecimal extractAmount(String msgBody, boolean[] isCreditDebit) {
        Pattern creditPattern = Pattern.compile("(?i)\\b(credited|CR|deposit|received|added|inflow|funds added|credited to your account|amount credited|credited with|credit to)\\b.*?\\b(\\d{1,7}(?:,\\d{3})*(?:\\.\\d{2})?)\\b");
        Pattern debitPattern = Pattern.compile("(?i)\\b(debited|debit|DR|withdrawal|spent|deducted|outflow|funds withdrawn|amount debited|debited from your account|deducted from|debit to)\\b.*?\\b(\\d{1,7}(?:,\\d{3})*(?:\\.\\d{2})?)\\b");

        Matcher creditMatcher = creditPattern.matcher(msgBody);
        Matcher debitMatcher = debitPattern.matcher(msgBody);

        boolean isCredit = creditMatcher.find();
        boolean isDebit = debitMatcher.find();

        // Set the isCreditDebit flag to indicate if it's a credit or debit
        isCreditDebit[0] = isCredit;
        isCreditDebit[1] = isDebit;

        String amountStr = null;
        if (isCredit) {
            amountStr = creditMatcher.group(2).replace(",", "");
        } else if (isDebit) {
            amountStr = debitMatcher.group(2).replace(",", "");
        }

        if (amountStr != null) {
            try {
                return new BigDecimal(amountStr);
            } catch (NumberFormatException e) {
                // Log or handle the exception if needed
            }
        }

        return BigDecimal.ZERO;
    }

    private String extractBankName(String msgBody) {
        // Check for known bank names at the end of the message
        Pattern bankPattern = Pattern.compile("-\\s*(\\w+(?:\\s+\\w+)*)$");
        Matcher matcher = bankPattern.matcher(msgBody);

        if (matcher.find()) {
            if (matcher.group(1).toUpperCase().trim().equals("INR")) {
                return "SBI";
            }
            else {
                return matcher.group(1).toUpperCase().trim();
            }
        }

        // Fallback to checking for capitalized words within the message
        bankPattern = Pattern.compile("(\\b[A-Z]{2,}(?:\\s+[A-Z]{2,})*\\b)");
        matcher = bankPattern.matcher(msgBody);
        if (matcher.find()) {
            if (matcher.group(1).toUpperCase().trim().equals("INR")) {
                return "SBI";
            }
            else {
                return matcher.group(1).toUpperCase().trim();
            }
        }

        return "Unknown";
    }

    private int getBankLogo(String bankName) {
        // Implement this method to return the correct bank logo based on the bank name
        // For example:
        return switch (bankName) {
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

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return timeFormat.format(new Date());
    }

    private long getCurrentTimestamp() {
        return new Date().getTime();
    }

    // Inner class to hold daily amount data
    public static class DailyAmount {
        public BigDecimal credited = BigDecimal.ZERO;
        public BigDecimal debited = BigDecimal.ZERO;
        public Map<String, Integer> bankTransactionCount = new HashMap<>();

        public BigDecimal total() {
            return credited.add(debited);
        }
    }
}*/








package com.lucifer.finance.smsfunctionality;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lucifer.finance.R;
import com.lucifer.finance.transaction.Transaction;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    private static final String SHARED_PREFS_NAME = "financeApp";
    private static final String PROCESSED_TRANSACTIONS_KEY = "processedTransactions";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SmsReceiver", "SmsReceiver called from SmsReceiver (onReceive)");
        if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdus.length];

                for (int i = 0; i < pdus.length; i++) {
                    String format = bundle.getString("format");
                    messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                }

                for (SmsMessage message : messages) {
                    String msgBody = message.getMessageBody();
                    processMessage(context, msgBody);
                }
            }
        }
    }

    private void processMessage(Context context, String msgBody) {
        boolean[] isCreditDebit = new boolean[2];
        BigDecimal amount = extractAmount(msgBody, isCreditDebit);

        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            boolean isCredit = isCreditDebit[0];
            boolean isDebit = isCreditDebit[1];
            String bankName = extractBankName(msgBody);
            int bankLogo = getBankLogo(bankName);

            String uniqueId = generateUniqueId(msgBody);  // Generate a unique ID for this transaction
            if (isTransactionProcessed(context, uniqueId)) {
                return;  // Transaction already processed
            }

            updateLog(context, amount, isCredit, isDebit, bankName);
            storeTransaction(context, amount, isCredit, bankName, bankLogo, uniqueId);
        }
    }

    private String generateUniqueId(String msgBody) {
        // Create a unique identifier based on the message body and timestamp
        return String.valueOf(msgBody.hashCode() + getCurrentTimestamp());
    }

    private boolean isTransactionProcessed(Context context, String uniqueId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        Set<String> processedTransactions = sharedPreferences.getStringSet(PROCESSED_TRANSACTIONS_KEY, new HashSet<>());
        return processedTransactions.contains(uniqueId);
    }

    private void markTransactionAsProcessed(Context context, String uniqueId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> processedTransactions = sharedPreferences.getStringSet(PROCESSED_TRANSACTIONS_KEY, new HashSet<>());
        processedTransactions.add(uniqueId);
        editor.putStringSet(PROCESSED_TRANSACTIONS_KEY, processedTransactions);
        editor.apply();
    }

    private void updateLog(Context context, BigDecimal amount, boolean isCredit, boolean isDebit, String bankName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("dailyLog", "{}");
        Type type = new TypeToken<HashMap<String, DailyAmount>>() {}.getType();
        Map<String, DailyAmount> dailyLog = new Gson().fromJson(json, type);

        if (dailyLog == null) {
            dailyLog = new HashMap<>();
        }

        String currentDate = getCurrentDate();
        DailyAmount dailyAmount = dailyLog.getOrDefault(currentDate, new DailyAmount());

        if (isCredit) {
            if (dailyAmount != null) {
                dailyAmount.credited = dailyAmount.credited.add(amount);
            }
        } else if (isDebit) {
            if (dailyAmount != null) {
                dailyAmount.debited = dailyAmount.debited.add(amount);
            }
        }

        // Update bank transaction count
        assert dailyAmount != null;
        dailyAmount.bankTransactionCount.put(bankName, dailyAmount.bankTransactionCount.getOrDefault(bankName, 0) + 1);

        dailyLog.put(currentDate, dailyAmount);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dailyLog", new Gson().toJson(dailyLog));
        editor.apply();

        Toast.makeText(context, "Transaction logged: \n" + (isCredit ? "Credited" : "Debited") + " Rs." + amount + " from " + bankName, Toast.LENGTH_SHORT).show();
    }

    private void storeTransaction(Context context, BigDecimal amount, boolean isCredit, String bankName, int bankLogo, String uniqueId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("transactions", "[]");
        Type type = new TypeToken<ArrayList<Transaction>>() {}.getType();
        List<Transaction> transactions = new Gson().fromJson(json, type);

        if (transactions == null) {
            transactions = new ArrayList<>();
        }

        String currentDate = getCurrentDate();
        String currentTime = getCurrentTime();
        Transaction transaction = new Transaction(currentDate, currentTime, amount, bankName, bankLogo, isCredit);
        transactions.add(transaction);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("transactions", new Gson().toJson(transactions));
        editor.apply();

        markTransactionAsProcessed(context, uniqueId);
    }

    private BigDecimal extractAmount(String msgBody, boolean[] isCreditDebit) {
        // Updated regex patterns to match credit and debit transaction patterns
        Pattern creditPattern = Pattern.compile("(?i)\\b(credited|CR|deposit|received|added|inflow|funds added|credited to your account|amount credited|credited with|credit to)\\b.*?\\b(\\d{1,7}(?:,\\d{3})*(?:\\.\\d{2})?)\\b");
        Pattern debitPattern = Pattern.compile("(?i)\\b(debited|debit|DR|withdrawal|spent|deducted|outflow|funds withdrawn|amount debited|debited from your account|deducted from|debit to)\\b.*?\\b(\\d{1,7}(?:,\\d{3})*(?:\\.\\d{2})?)\\b");

        Matcher creditMatcher = creditPattern.matcher(msgBody);
        Matcher debitMatcher = debitPattern.matcher(msgBody);

        boolean isCredit = creditMatcher.find();
        boolean isDebit = debitMatcher.find();

        // Set the isCreditDebit flag to indicate if it's a credit or debit
        isCreditDebit[0] = isCredit;
        isCreditDebit[1] = isDebit;

        String amountStr = null;
        if (isCredit) {
            amountStr = creditMatcher.group(2).replace(",", "");
        } else if (isDebit) {
            amountStr = debitMatcher.group(2).replace(",", "");
        }

        if (amountStr != null) {
            try {
                return new BigDecimal(amountStr);
            } catch (NumberFormatException e) {
                // Log or handle the exception if needed
            }
        }

        return BigDecimal.ZERO;
    }

    private String extractBankName(String msgBody) {
        // Check for known bank names at the end of the message
        Pattern bankPattern = Pattern.compile("-\\s*(\\w+(?:\\s+\\w+)*)$");
        Matcher matcher = bankPattern.matcher(msgBody);

        if (matcher.find()) {
            if (matcher.group(1).toUpperCase().trim().equals("INR")) {
                return "SBI";
            }
            else {
                return matcher.group(1).toUpperCase().trim();
            }
        }

        // Fallback to checking for capitalized words within the message
        bankPattern = Pattern.compile("(\\b[A-Z]{2,}(?:\\s+[A-Z]{2,})*\\b)");
        matcher = bankPattern.matcher(msgBody);
        if (matcher.find()) {
            if (matcher.group(1).toUpperCase().trim().equals("INR")) {
                return "SBI";
            }
            else {
                return matcher.group(1).toUpperCase().trim();
            }
        }

        return "Unknown";
    }

    private int getBankLogo(String bankName) {
        // Implement this method to return the correct bank logo based on the bank name
        // For example:
        return switch (bankName) {
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

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return timeFormat.format(new Date());
    }

    private long getCurrentTimestamp() {
        return new Date().getTime();
    }

    // Inner class to hold daily amount data
    public static class DailyAmount {
        public BigDecimal credited = BigDecimal.ZERO;
        public BigDecimal debited = BigDecimal.ZERO;
        public Map<String, Integer> bankTransactionCount = new HashMap<>();

        public BigDecimal total() {
            return credited.add(debited);
        }
    }
}

