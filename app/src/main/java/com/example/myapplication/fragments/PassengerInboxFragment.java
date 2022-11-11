package com.example.myapplication.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.adapters.PassengerInboxAdapter;
import com.example.myapplication.models.Message;
import com.example.myapplication.tools.InboxMokap;

public class PassengerInboxFragment extends ListFragment {

    public static PassengerInboxFragment newInstance() {
        return new PassengerInboxFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        //setHasOptionsMenu(true);
        return inflater.inflate(R.layout.map_layout, vg, false);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Message cinema = InboxMokap.getMessages().get(position);


        /*Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("name", cinema.getHeader());
        intent.putExtra("descr", cinema.getBody());
        startActivity(intent);*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getActivity(), "onActivityCreated()", Toast.LENGTH_SHORT).show();

        //Dodaje se adapter
        PassengerInboxAdapter adapter = new PassengerInboxAdapter(getActivity());
        setListAdapter(adapter);
    }

}