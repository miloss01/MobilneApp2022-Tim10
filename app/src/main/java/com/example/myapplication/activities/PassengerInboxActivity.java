package com.example.myapplication.activities;


import static com.google.android.material.internal.ContextUtils.getActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PassengerInboxAdapter;
import com.example.myapplication.fragments.PassengerInboxFragment;
import com.example.myapplication.tools.FragmentTransition;

import java.util.ArrayList;

public class PassengerInboxActivity extends AppCompatActivity{
    ListView simpleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_inbox);
        /*simpleList = (ListView) findViewById(R.id.messageListView);
        PassengerInboxAdapter customAdapter = new PassengerInboxAdapter(this);
        simpleList.setAdapter(customAdapter);*/
        FragmentTransition.to(PassengerInboxFragment.newInstance(), PassengerInboxActivity.this, false, R.id.upView);
        Toast.makeText(this, "AAAA", Toast.LENGTH_SHORT).show();

    }


}
