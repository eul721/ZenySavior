package com.example.ZenySavior;

import android.util.Log;

/**
 * Created by Jacky on 7/24/2014.
 */
public class Logger {
    public static void logWithTag(String tag, String message){
        Log.d(tag,message);
    }

    public static void logWithTag(String tag, int value){
        Log.d(tag,String.valueOf(value));
    }
    public static void logWithTag(String tag, long value){
        Log.d(tag,String.valueOf(value));
    }

}
