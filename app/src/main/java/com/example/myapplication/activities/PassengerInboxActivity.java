package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
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
import com.example.myapplication.tools.FragmentTransition;
import com.google.android.material.snackbar.Snackbar;

public class PassengerInboxActivity extends Activity {
    ListView simpleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout);
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
    }
}