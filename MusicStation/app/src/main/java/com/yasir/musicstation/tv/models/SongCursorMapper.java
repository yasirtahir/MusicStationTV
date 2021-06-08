package com.yasir.musicstation.tv.models;

import android.database.Cursor;
import androidx.leanback.database.CursorMapper;

/**
 * SongCursorMapper maps a database Cursor to a Song object.
 */
public final class SongCursorMapper extends CursorMapper {

    private static int idIndex;
    private static int categoryNameIndex;
    private static int songNameIndex;
    private static int artistNameIndex;
    private static int imageUrlIndex;
    private static int songUrlIndex;

    @Override
    protected void bindColumns(Cursor cursor) {
        idIndex = cursor.getColumnIndex(SongContract.SongEntry._ID);
        categoryNameIndex = cursor.getColumnIndex(SongContract.SongEntry.COLUMN_CATEGORY_NAME);
        songNameIndex = cursor.getColumnIndex(SongContract.SongEntry.COLUMN_SONG_NAME);
        artistNameIndex = cursor.getColumnIndex(SongContract.SongEntry.COLUMN_ARTIST_NAME);
        imageUrlIndex = cursor.getColumnIndex(SongContract.SongEntry.COLUMN_IMAGE_URL);
        songUrlIndex = cursor.getColumnIndex(SongContract.SongEntry.COLUMN_SONG_URL);
    }

    @Override
    protected Object bind(Cursor cursor) {
        long id = cursor.getLong(idIndex);
        String categoryName = cursor.getString(categoryNameIndex);
        String songName = cursor.getString(songNameIndex);
        String artistName = cursor.getString(artistNameIndex);
        String imageUrl = cursor.getString(imageUrlIndex);
        String songUrl = cursor.getString(songUrlIndex);

        return new Song.SongBuilder()
                .id(id)
                .categoryName(categoryName)
                .songName(songName)
                .artistName(artistName)
                .imageURL(imageUrl)
                .songURL(songUrl)
                .build();
    }
}
