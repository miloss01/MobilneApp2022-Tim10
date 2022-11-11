package com.example.myapplication.tools;

import android.hardware.lights.LightsManager;

import com.example.myapplication.models.Message;

import java.sql.Date;
import java.util.ArrayList;

public class InboxMokap {

    public static ArrayList<Message> getMessages() {
        ArrayList<Message> messages = new ArrayList<Message>();
        messages.add(new Message(new Date(2022, 11, 11), "jdjwaie", "qqqqqq", Message.MessageType.Drive, 1));
        messages.add(new Message(new Date(2022, 11, 11), "aaa", "aaa", Message.MessageType.Panic, 2));
        messages.add(new Message(new Date(2022, 11, 11), "", "bb", Message.MessageType.Support, 3));
        messages.add(new Message(new Date(2022, 11, 11), "", "cc", Message.MessageType.Drive, 1));
        messages.add(new Message(new Date(2022, 11, 11), "", "vv", Message.MessageType.Drive, 1));

        return messages;
    }
}
