package com.example.myapplication.tools;

import com.example.myapplication.models.User;

import java.util.ArrayList;

public class UsersMokap {

    public static ArrayList<User> getUsers() {
        ArrayList<User> users = new ArrayList<User>();
        users.add(new User("Sally", "Doe", "examplemail@sally.com"));
        users.add(new User("Sandy", "Doe", "examplemail@sandy.com"));
        users.add(new User("Terence", "Doe", "examplemail@terence.com"));
        return users;
    }

    public static User getUserByEmail(String email) {
        for (User u : getUsers()) {
            if (u.getEmail().equals(email)) return u;
        }
        return null;
    }

}
