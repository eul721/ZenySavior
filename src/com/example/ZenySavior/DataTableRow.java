package com.example.ZenySavior;

import java.util.HashMap;

/**
 * Created by Jacky on 7/21/2014.
 * This file serves as the basic Data Structure for table rows
 */
public class DataTableRow {
    private int id;
    private int year;
    private int month;
    private int date;
    private double spentValue;

    public DataTableRow(int id, int year, int month, int date, double spentValue){
        this.id = id;
        this.year = year;
        this.month = month;
        this.date = date;
        this.spentValue = spentValue;
    }

    public int getId() {
        return id;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDate() {
        return date;
    }

    public double getspentValue() {
        return spentValue;
    }

    public HashMap<String,Object> getHashMap(){
        HashMap<String,Object> hashMap = new HashMap<String, Object>();
        hashMap.put("id",getId());
        hashMap.put("year",getYear());
        hashMap.put("month",getMonth());
        hashMap.put("date",getDate());
        hashMap.put("spentValue",getspentValue());
        return hashMap;

    }


}
