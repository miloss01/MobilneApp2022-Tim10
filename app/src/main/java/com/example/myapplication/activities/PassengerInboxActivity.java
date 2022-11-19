package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PassengerInboxAdapter;
import com.example.myapplication.fragments.PassengerInboxFragment;
import com.example.myapplication.receiver.PassengerInboxReceiver;
import com.example.myapplication.service.PassengerInboxService;
import com.example.myapplication.tools.FragmentTransition;
import com.example.myapplication.tools.ReceiverTools;
import com.google.android.material.snackbar.Snackbar;

public class PassengerInboxActivity extends AppCompatActivity {
    ListView simpleList;

    private PendingIntent pendingIntent;
    private AlarmManager manager;
    private PassengerInboxReceiver receiver;
    public static String SYNC_DATA = "SYNC_DATA";


    @Override
    protected void onResume() {
        super.onResume();
        //Za slucaj da referenca nije postavljena da se izbegne problem sa androidom!
        if (manager == null) {
            setUpReceiver();
        }

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ReceiverTools.calculateTimeTillNextSync(3), pendingIntent);
        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();

        IntentFilter filter = new IntentFilter();
        filter.addAction(SYNC_DATA);
        registerReceiver(receiver, filter);
    }

    private static String CHANNEL_ID = "Zero channel";

    private void createNotificationChannel() {
        CharSequence name = "Notification channel";
        String description = "Description";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);

        createNotificationChannel();

        PassengerInboxActivity hmm = this;
        simpleList = (ListView) findViewById(R.id.messageListView);
        PassengerInboxAdapter customAdapter = new PassengerInboxAdapter(this);
        simpleList.setAdapter(customAdapter);
        //setListAdapter(adapter);
        //SimpleAdapter simpleAdapter=new SimpleAdapter(this,arrayList,R.layout.list_view_items,from,to);//Create object and set the parameters for simpleAdapter
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(hmm, OneMessageActivity.class);
//                intent.putExtra("name", cinema.getName());
//                intent.putExtra("descr", cinema.getDescription());
                startActivity(intent);}
        });

        Spinner spiner = findViewById(R.id.spiner_pass_inbox);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.spiner_pass_inbox_options));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spiner.setAdapter(adapter);

        Toolbar toolbar = findViewById(R.id.passenger_inbox_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Inbox");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setUpReceiver(){
        receiver = new PassengerInboxReceiver();

        //definisemo manager i kazemo kada je potrebno da se ponavlja
        /*
        parametri:
            context: this - u komkontekstu zelimo da se intent izvrsava
            requestCode: 0 - nas jedinstev kod
            intent: intent koji zelimo da se izvrsi kada dodje vreme
            flags: 0 - flag koji opisuje sta da se radi sa intent-om kada se poziv desi
            detaljnije:https://developer.android.com/reference/android/app/PendingIntent.html#getService
            (android.content.Context, int, android.content.Intent, int)
        */
        Intent alarmIntent = new Intent(this, PassengerInboxService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE);

        //koristicemo sistemski AlarmManager pa je potrebno da dobijemo
        //njegovu instancu.
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
    }

}