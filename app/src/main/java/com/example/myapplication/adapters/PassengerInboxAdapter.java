package com.example.myapplication.adapters;

import android.app.Activity;
import android.content.Context;
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
        View vi = view;
        Message message = InboxMokap.getMessages().get(i);

        if(view == null)
            vi = activity.getLayoutInflater().inflate(R.layout.message_list, null);
        //vi = inflter.inflate(R.layout.message_list, null);

        TextView name = (TextView)vi.findViewById(R.id.name);
        TextView description = (TextView)vi.findViewById(R.id.description);
        ImageView image = (ImageView)vi.findViewById(R.id.item_icon);

        name.setText(message.getHeader());
        description.setText(message.getDate().toString());

        if (message.getAvatar()==1){
            vi.setBackgroundColor(500005);
        }
        if (message.getAvatar()==2){
            vi.setBackgroundColor(500002);
        }
        if (message.getAvatar()==3){
            vi.setBackgroundColor(500004);
        }
        return vi;
    }
}
