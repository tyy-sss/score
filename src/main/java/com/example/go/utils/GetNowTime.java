package com.example.go.utils;


import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

public class GetNowTime {

    public static Timestamp getNowTime() throws ParseException {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }
}
