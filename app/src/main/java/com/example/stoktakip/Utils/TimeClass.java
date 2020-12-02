package com.example.stoktakip.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimeClass {

    public static String getClock(){

        String saveCurrentTime;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        saveCurrentTime = currentTime.format(calendar.getTime());

        return saveCurrentTime;


    }

    public static String getDate(){

        String saveCurrentDate;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        return saveCurrentDate;

    }

}
