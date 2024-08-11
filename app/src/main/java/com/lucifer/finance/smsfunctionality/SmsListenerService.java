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
