package com.yasir.musicstation.tv.presenter;

import android.content.ContentValues;
import android.content.Context;

import com.google.gson.Gson;
import com.yasir.musicstation.tv.models.CategoryModel;
import com.yasir.musicstation.tv.models.DataModel;
import com.yasir.musicstation.tv.models.Song;
import com.yasir.musicstation.tv.models.SongContract;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    public static final String SONG_DETAIL = "SongDetails";
    private static ArrayList<CategoryModel> categoryModels;

    public static ArrayList<CategoryModel> getData(Context context) {
        if (categoryModels == null) {

            categoryModels = new ArrayList<>();

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
                    // Save all the data to content provider and SQL
                    saveToContentProvider(context);
                }
            }
        }

        return categoryModels;
    }

    private static void saveToContentProvider(Context context) {
        if (categoryModels != null) {
            List<ContentValues> songsToInsert = new ArrayList<>();
            for (int i = 0; i < categoryModels.size(); i++) {
                for (Song song : categoryModels.get(i).getCategorySongs()) {
                    ContentValues songValues = new ContentValues();
                    songValues.put(SongContract.SongEntry.COLUMN_CATEGORY_NAME, song.getCategoryName());
                    songValues.put(SongContract.SongEntry.COLUMN_SONG_NAME, song.getSongName());
                    songValues.put(SongContract.SongEntry.COLUMN_ARTIST_NAME, song.getArtistName());
                    songValues.put(SongContract.SongEntry.COLUMN_IMAGE_URL, song.getImageURL());
                    songValues.put(SongContract.SongEntry.COLUMN_SONG_URL, song.getSongURL());
                    songsToInsert.add(songValues);
                }
            }

            try {
                ContentValues[] songContentValues = songsToInsert.toArray(new ContentValues[categoryModels.size()]);
                context.getContentResolver().bulkInsert(SongContract.SongEntry.CONTENT_URI, songContentValues);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }
}
