package com.example.myapplication.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.activities.PassengerInboxActivity;
import com.example.myapplication.service.PassengerInboxService;

public class PassengerInboxReceiver extends BroadcastReceiver {

    private static int NOTIFICATION_ID = 1;
    private static String CHANNEL_ID = "Zero channel";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID);
        mBuilder.setContentTitle("Notification in passenger inbox");
        mBuilder.setContentText("With help of receiver, from where code is called");
        mBuilder.setSmallIcon(R.drawable.ic_message_icon);
        notificationManager.notify(NOTIFICATION_ID, mBuilder.build());

        if (intent.getAction().equals(PassengerInboxActivity.SYNC_DATA)) {
            Log.i("P.Inbox Sync Receiver", "Receiver work here");
        }
    }
}
