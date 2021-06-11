package com.yasir.musicstation.tv.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.yasir.musicstation.tv.models.CategoryModel;
import com.yasir.musicstation.tv.models.DataModel;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class DataUtil {

    public static final String SONG_DETAIL = "SongDetails";
    private static ArrayList<CategoryModel> categoryModels;

    public static ArrayList<CategoryModel> getData(Context context) {
        if (categoryModels == null) {

            PreferenceHelper preferenceHelper = new PreferenceHelper(context);

            categoryModels = new ArrayList<>();

            if (preferenceHelper.getAllMusicStations() != null && !preferenceHelper.getAllMusicStations().isEmpty()) {
                categoryModels.addAll(preferenceHelper.getAllMusicStations());
            } else {
                loadDataFromLocalFile(context);
            }
        }

        return categoryModels;
    }

    private static void loadDataFromLocalFile(Context context) {
        String json = "";
        try {
            InputStream is = context.getAssets().open("data.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }

        if (json != null && !json.trim().isEmpty()) {
            DataModel dataModel = new Gson().fromJson(json, DataModel.class);

            if (dataModel != null) {
                categoryModels.clear();
                categoryModels.addAll(dataModel.getData());
            }
        }
    }
}
