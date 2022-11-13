package com.example.myapplication.tools;

import com.example.myapplication.models.DriverRide;

import java.util.ArrayList;

public class RideMockup {

    public static ArrayList<DriverRide> getMockupRides() {
        ArrayList<DriverRide> ret = new ArrayList<>();

        for (int i = 0; i < 5; i++)
            ret.add(new DriverRide("3.23", "comment asdasd asdsdfs fsd fsdf sdf", "25.5.2022. 15:32", "25.5.2022. 18:52", "3", "80", "500.32", "Strazilovska 16, 21000 Novi Sad", "Baje Malog Knindze 22, 21000 Novi Sad"));

        return ret;
    }
}
