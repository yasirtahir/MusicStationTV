package com.yasir.musicstation.tv.utils;

import android.util.Log;

import com.yasir.musicstation.tv.BuildConfig;

public class AppLog {
    // Universal class to Log anything on the Android Monitor

    public static void Debug(String msg) {
        if (isEmptyOrNull(msg))
            return;
        if (BuildConfig.DEBUG)
            Log.d("Application", msg);
    }

    public static void Error(String msg) {
        if (isEmptyOrNull(msg))
            return;
        if (BuildConfig.DEBUG)
            Log.e("Application", msg);
    }

    private static boolean isEmptyOrNull(String string) {
        if (string == null)
            return true;

        return (string.trim().length() <= 0);
    }
}
