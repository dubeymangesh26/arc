package com.arcltd.staff.utility;

import android.util.Log;

public class ELog {
    public static void d(String tag, String message) {
        Log.d(tag, message);
    }

    public static void d(String tag, String message, Throwable e) {
        Log.d(tag, message, e);
    }

    public static void i(String tag, String message) {
        Log.i(tag, message);
    }

    public static void i(String tag, String message, Throwable e) {
        Log.i(tag, message, e);
    }

    public static void v(String tag, String message) {
        Log.v(tag, message);
    }

    public static void v(String tag, String message, Throwable e) {
        Log.v(tag, message, e);
    }

    public static void w(String tag, String message) {
        Log.w(tag, message);
    }

    public static void w(String tag, String message, Throwable e) {
        Log.w(tag, message, e);
    }

    public static void e(String tag, String message) {
        Log.e(tag, message);
    }

    public static void e(String tag, String message, Throwable e) {
        try {
            Log.e(tag, message, e);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}