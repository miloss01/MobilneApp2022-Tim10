package com.example.myapplication.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.dto.MessageReceivedDTO;
import com.example.myapplication.dto.PassengerDTO;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DriverInboxAdapter extends ArrayAdapter<MessageReceivedDTO> {

    private List<MessageReceivedDTO> messagesList;
    private final HashMap<Long, MessageReceivedDTO> filteredRecentPerUser = new HashMap<>();
    private HashMap<Long, PassengerDTO> users = new HashMap<>();  // users interacted with
    private final Long driverId;

    private static final String RIDE_COLOR = "#D3ADC3C6";
    private static final String PANIC_COLOR = "#FFFFE8C9";
    private static final String SUPPORT_COLOR = "#FFE7EFD8";
    private static final String DEFAULT_COLOR = "#FFFFFFFF";

    public DriverInboxAdapter(Context context, int resource, ArrayList<MessageReceivedDTO> messages,
                              HashMap<Long, PassengerDTO> users,
                              Long driverId) {
        super(context, resource, messages);
        this.messagesList = messages;
        this.driverId = driverId;
        this.users = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MessageReceivedDTO message = messagesList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.driver_inbox_cell,
                    parent, false);
        }
        Log.i("TAG", String.valueOf(message == null));
        Log.i("TAG", message.getId().toString());
        Log.i("TAG", message.getType());

        RelativeLayout background = convertView.findViewById(R.id.background_mesagecell);
        background.setBackgroundColor(Color.parseColor(getColorByMessageType(message.getType())));

        TextView tvTime = convertView.findViewById(R.id.textview_messagecell_time);
        tvTime.setText(message.getTimeOfSending());

        TextView tvSender = convertView.findViewById(R.id.textview_messagecell_sendername);
        Long userId = Objects.equals(driverId, message.getSenderId()) ? message.getReceiverId() : message.getSenderId();
        tvSender.setText(users.get(userId).getName() + " " + users.get(userId).getSurname());

        PassengerDTO user = this.users.get(userId);

        TextView tvPreview = convertView.findViewById(R.id.textview_messagecell_preview);
        String prefix = Objects.equals(userId, message.getSenderId()) ? (user.getName() + " said: ") : "You: ";
        if (message.getMessage().length() > 35) tvPreview.setText(prefix + message.getMessage().substring(0, 30) + "...");
        else tvPreview.setText(prefix + message.getMessage());

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_messagecell_senderphoto);
        if (user != null && user.getProfilePicture() != null ) {
            if (!user.getProfilePicture().startsWith("data")) new RidePassengersAdapter.DownloadImageTask(imageView).execute(user.getProfilePicture());
            else {
                final String encodedString = user.getProfilePicture();
                final String pureBase64Encoded = encodedString.substring(encodedString.indexOf(",")  + 1);
                final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                imageView.setImageBitmap(decodedBitmap);
            }
        }

        return convertView;
    }

    private String getColorByMessageType(String type) {
        switch (type) {
            case "ride":
                return RIDE_COLOR;
            case "panic":
                return PANIC_COLOR;
            case "support":
                return SUPPORT_COLOR;
            default: return DEFAULT_COLOR;
        }

    }

}
