package com.yasir.musicstation.tv.presenter;

import android.app.UiModeManager;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import static android.content.Context.UI_MODE_SERVICE;

public class TVUtils {
    public static boolean isRunningOnTV(Context context) {
        UiModeManager uiModeManager = (UiModeManager) context.getSystemService(UI_MODE_SERVICE);
        if (uiModeManager.getCurrentModeType() == Configuration.UI_MODE_TYPE_TELEVISION) {
            Log.d(TVUtils.class.getSimpleName(), "Running on a TV Device");
            return true;
        } else {
            Log.d(TVUtils.class.getSimpleName(), "Running on a non-TV Device");
            return false;
        }
    }
}
