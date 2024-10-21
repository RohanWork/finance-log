//package com.lucifer.finance.smsfunctionality;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Build;
//import android.os.IBinder;
//import android.util.Log;
//import androidx.core.app.NotificationCompat;
//import com.lucifer.finance.R;
//
//public class SmsListenerService extends Service {
//    private static final String CHANNEL_ID = "SMS_Permission_Channel";
//    private SmsReceiver smsReceiver;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        smsReceiver = new SmsReceiver();
//        Log.d("SmsListenerService", "SmsListenerService created");
//
//        // Register the SMS receiver
//        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
//
//        // Create notification channel if necessary
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "SMS Permission Channel",
//                    NotificationManager.IMPORTANCE_DEFAULT // Adjust importance based on need
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            manager.createNotificationChannel(channel);
//        }
//
//        // Create an intent for the notification, but not pointing to the service
//        Intent notificationIntent = new Intent(this, SmsListenerService.class); // Or another activity if needed
//        PendingIntent pendingIntent;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            pendingIntent = PendingIntent.getActivity(
//                    this,
//                    0,
//                    notificationIntent,
//                    PendingIntent.FLAG_IMMUTABLE
//            );
//        } else {
//            pendingIntent = PendingIntent.getActivity(
//                    this,
//                    0,
//                    notificationIntent,
//                    PendingIntent.FLAG_UPDATE_CURRENT
//            );
//        }
//
//        // Build the notification
//        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("SMS Service Running")
//                .setContentText("Listening for SMS messages...")
//                .setSmallIcon(R.drawable.logo)
//                .setContentIntent(pendingIntent)
//                .setOngoing(true) // Keeps the notification persistent
//                .build();
//
//        Log.d("SmsListenerService", "Starting foreground service");
//        startForeground(1, notification);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        Log.d("SmsListenerService", "onBind called");
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("SmsListenerService", "onStartCommand called");
//        return START_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d("SmsListenerService", "onDestroy called");
//        unregisterReceiver(smsReceiver);
//    }
//}




//package com.lucifer.finance.smsfunctionality;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Build;
//import android.os.IBinder;
//import android.util.Log;
//import androidx.core.app.NotificationCompat;
//import com.lucifer.finance.MainActivity;
//import com.lucifer.finance.R;
//
//public class SmsListenerService extends Service {
//    private static final String CHANNEL_ID = "SMS_Service_Channel";
//    private SmsReceiver smsReceiver;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        smsReceiver = new SmsReceiver();
//        Log.d("SmsListenerService", "Service created");
//
//        // Register the SMS receiver dynamically
//        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
//
//        // Create a notification channel for foreground service
//        createNotificationChannel();
//
//        // Start the service as a foreground service to keep it running
//        startForeground(1, buildNotification());
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d("SmsListenerService", "onStartCommand called");
//        return START_STICKY; // Ensures service is restarted if killed by the system
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        Log.d("SmsListenerService", "onDestroy called");
//        // Unregister the SMS receiver to avoid leaks
//        unregisterReceiver(smsReceiver);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        // No binding provided for this service
//        return null;
//    }
//
//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "SMS Service Channel",
//                    NotificationManager.IMPORTANCE_LOW
//            );
//            NotificationManager manager = getSystemService(NotificationManager.class);
//            if (manager != null) {
//                manager.createNotificationChannel(channel);
//            }
//        }
//    }
//
//    private Notification buildNotification() {
//        Intent notificationIntent = new Intent(this, MainActivity.class); // Or any activity you want to open
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
//
//        return new NotificationCompat.Builder(this, CHANNEL_ID)
//                .setContentTitle("SMS Listener Service")
//                .setContentText("Listening for SMS messages...")
//                .setSmallIcon(R.drawable.logo)
//                .setContentIntent(pendingIntent)
//                .setOngoing(true) // Makes notification persistent
//                .build();
//    }
//}






package com.lucifer.finance.smsfunctionality;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.lucifer.finance.R;

public class SmsListenerService extends Service {
    private static final String CHANNEL_ID = "SMS_Permission_Channel";
    private SmsReceiver smsReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        smsReceiver = new SmsReceiver();
        Log.d("SmsListenerService", "SmsListenerService created");

        // Register the SMS receiver
        registerReceiver(smsReceiver, new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));

        // Create notification channel if necessary
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "SMS Permission Channel",
                    NotificationManager.IMPORTANCE_DEFAULT // Adjust importance based on need
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        // Create an intent for the notification, but not pointing to the service
        Intent notificationIntent = new Intent(this, SmsListenerService.class); // Or another activity if needed
        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE
            );
        } else {
            pendingIntent = PendingIntent.getActivity(
                    this,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
        }

        // Build the notification
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("SMS Service Running")
                .setContentText("Listening for SMS messages...")
                .setSmallIcon(R.drawable.logo)
                .setContentIntent(pendingIntent)
                .setOngoing(true) // Keeps the notification persistent
                .build();

        Log.d("SmsListenerService", "Starting foreground service");
        startForeground(1, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SmsListenerService", "onBind called");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SmsListenerService", "onStartCommand called");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SmsListenerService", "onDestroy called");
        unregisterReceiver(smsReceiver);
    }
}
