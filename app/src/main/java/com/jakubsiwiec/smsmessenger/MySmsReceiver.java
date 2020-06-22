package com.jakubsiwiec.smsmessenger;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

public class MySmsReceiver extends BroadcastReceiver {

    private DataBaseHelper dataBaseHelper;

    private static final String TAG =
            MySmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";
    public static final String CHANNEL_ID = "SMS received notification";

    public String myPhoneNumber;




    //database insertion
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addReceivedMessage(Context context, String phoneSender, String content){
        /*
        Save the message to the database
         */
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        boolean insertData = dataBaseHelper.addMessage(phoneSender, content, timestamp, false);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        dataBaseHelper = new DataBaseHelper(context);

        Log.i(TAG, "On receive method started");

        // Get the SMS message.
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String strMessage = "";
        String format = bundle.getString("format");
        Log.i(TAG, "Received format: " + format);
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM =
                    (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                String phone = msgs[i].getOriginatingAddress();
                String content = msgs[i].getMessageBody();

                strMessage += "SMS from " + phone;
                strMessage += " :" + content + "\n";
                // Log and display the SMS message.
                Log.d(TAG, "onReceive: " + strMessage);

                // Create an explicit intent for an Activity in your app
                Intent notifyIntent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);

                addReceivedMessage(context, phone, content);

                // Create notification for an received message
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setSmallIcon(R.drawable.ic_baseline_account_circle_24)
                        .setContentTitle(phone)
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

// notificationId is a unique int for each notification that you must define
                notificationManager.notify(1, builder.build());






            }
        }
    }
}
