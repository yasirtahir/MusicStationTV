package com.yasir.musicstation.tv.presenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.yasir.musicstation.tv.models.SongContract;

public class SongDbHelper extends SQLiteOpenHelper {

    // Change this when you change the database schema.
    private static final int DATABASE_VERSION = 4;

    // The name of our database.
    private static final String DATABASE_NAME = "MusicStation.db";

    public SongDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold songs.
        final String SQL_CREATE_SONG_TABLE = "CREATE TABLE " + SongContract.SongEntry.TABLE_NAME + " (" +
                SongContract.SongEntry._ID + " INTEGER PRIMARY KEY," +
                SongContract.SongEntry.COLUMN_CATEGORY_NAME + " TEXT NOT NULL, " +
                SongContract.SongEntry.COLUMN_SONG_NAME + " TEXT UNIQUE NOT NULL, " +
                SongContract.SongEntry.COLUMN_ARTIST_NAME + " TEXT NOT NULL, " +
                SongContract.SongEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL, " +
                SongContract.SongEntry.COLUMN_SONG_URL + " TEXT NOT NULL " +
                " );";

        // Do the creating of the databases.
        db.execSQL(SQL_CREATE_SONG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Simply discard all old data and start over when upgrading.
        db.execSQL("DROP TABLE IF EXISTS " +  SongContract.SongEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Do the same thing as upgrading...
        onUpgrade(db, oldVersion, newVersion);
    }
}
