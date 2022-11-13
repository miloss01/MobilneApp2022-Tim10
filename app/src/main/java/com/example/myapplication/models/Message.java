package com.example.myapplication.models;

import java.util.Date;

public class Message {
    private Date date;
    private String body;
    private String header;
    private MessageType type;
    private int avatar;

    public enum MessageType{
        Panic,
        Support,
        Drive
    }

    public Message(){}

    public Message(Date date, String body, String header, MessageType type, int avatar) {
        this.date = date;
        this.body = body;
        this.header = header;
        this.type = type;
        this.avatar = avatar;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public MessageType getMType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }
}
