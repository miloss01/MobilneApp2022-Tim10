package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.myapplication.activities.SplashScreenActivity;
import com.example.myapplication.tools.Retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;

import androidx.navigation.ui.AppBarConfiguration;

import com.example.myapplication.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        createNotificationChannel();

//        StompClient stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, Constants.websocketBaseUrl);
        Retrofit.stompClient.connect();
        Retrofit.stompClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {

                case OPENED:
                    Log.d("TAG", "Stomp connection opened");
                    break;

                case ERROR:
                    Log.e("TAG", "Error", lifecycleEvent.getException());
                    break;

                case CLOSED:
                    Log.d("TAG", "Stomp connection closed");
                    break;
            }
        });

        startActivity(new Intent(this, SplashScreenActivity.class));


    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NOTIFICATION_CHANNEL";
            String description = "CHANNEL FOR NOTIFICATIONS";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("NOTIFICATION_CHANNEL", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }



}