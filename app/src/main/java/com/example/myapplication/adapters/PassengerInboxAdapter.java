package com.example.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.models.Message;
import com.example.myapplication.tools.InboxMokap;

public class PassengerInboxAdapter extends BaseAdapter {

    private Activity activity;
    LayoutInflater inflter;

    public  PassengerInboxAdapter(Activity activity) {this.activity = activity;}
    @Override
    public int getCount() {
        return InboxMokap.getMessages().size();
    }

    @Override
    public Object getItem(int i) {
        return InboxMokap.getMessages().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        Message message = InboxMokap.getMessages().get(i);

        if(view == null)
            view = activity.getLayoutInflater().inflate(R.layout.message_list, null);
        //view = inflter.inflate(R.layout.message_list, null);

        TextView name = (TextView)view.findViewById(R.id.name);
        TextView description = (TextView)view.findViewById(R.id.description);
        TextView type = (TextView)view.findViewById(R.id.type);
        ImageView image = (ImageView)view.findViewById(R.id.item_icon);
        View layout = view.findViewById(R.id.list_item_pass_inbox);


        name.setText(message.getHeader());
        description.setText(message.getDate().toString());
        type.setText(message.getMType().toString());

        if (message.getAvatar()==1){
            layout.setBackgroundColor(Color.LTGRAY);
        }
        if (message.getAvatar()==2){
            layout.setBackgroundColor(Color.GRAY);
        }
        if (message.getAvatar()==3){
            layout.setBackgroundColor(Color.red(1));
        }
        return view;
    }
}
