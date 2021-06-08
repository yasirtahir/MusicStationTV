package com.yasir.musicstation.tv.presenter;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import androidx.annotation.NonNull;

import com.yasir.musicstation.tv.models.SongContract;

import java.util.HashMap;

public class SongProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private SongDbHelper mOpenHelper;

    // These codes are returned from sUriMatcher#match when the respective Uri matches.
    private static final int AUDIO = 1;
    private static final int SEARCH_SUGGEST = 2;
    private static final int REFRESH_SHORTCUT = 3;

    private static final SQLiteQueryBuilder sVideosContainingQueryBuilder;
    private static final String[] sVideosContainingQueryColumns;
    private static final HashMap<String, String> sColumnMap = buildColumnMap();
    private ContentResolver mContentResolver;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mContentResolver = context.getContentResolver();
        mOpenHelper = new SongDbHelper(context);
        return true;
    }

    static {
        sVideosContainingQueryBuilder = new SQLiteQueryBuilder();
        sVideosContainingQueryBuilder.setTables(SongContract.SongEntry.TABLE_NAME);
        sVideosContainingQueryBuilder.setProjectionMap(sColumnMap);
        sVideosContainingQueryColumns = new String[]{
                SongContract.SongEntry._ID,
                SongContract.SongEntry.COLUMN_CATEGORY_NAME,
                SongContract.SongEntry.COLUMN_SONG_NAME,
                SongContract.SongEntry.COLUMN_ARTIST_NAME,
                SongContract.SongEntry.COLUMN_IMAGE_URL,
                SongContract.SongEntry.COLUMN_SONG_URL,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID
        };
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = SongContract.CONTENT_AUTHORITY;

        // For each type of URI to add, create a corresponding code.
        matcher.addURI(authority, SongContract.PATH_SONG, AUDIO);

        // Search related URIs.
        matcher.addURI(authority, "search/" + SearchManager.SUGGEST_URI_PATH_QUERY, SEARCH_SUGGEST);
        matcher.addURI(authority, "search/" + SearchManager.SUGGEST_URI_PATH_QUERY + "/*", SEARCH_SUGGEST);
        return matcher;
    }

    private Cursor getSuggestions(String query) {
        query = query.toLowerCase();
        return sVideosContainingQueryBuilder.query(
                mOpenHelper.getReadableDatabase(),
                sVideosContainingQueryColumns,
                SongContract.SongEntry.COLUMN_ARTIST_NAME + " LIKE ? OR " +
                        SongContract.SongEntry.COLUMN_SONG_NAME + " LIKE ?",
                new String[]{"%" + query + "%", "%" + query + "%"},
                null,
                null,
                null
        );
    }

    private static HashMap<String, String> buildColumnMap() {
        HashMap<String, String> map = new HashMap<>();
        map.put(SongContract.SongEntry._ID, SongContract.SongEntry._ID);
        map.put(SongContract.SongEntry.COLUMN_CATEGORY_NAME, SongContract.SongEntry.COLUMN_CATEGORY_NAME);
        map.put(SongContract.SongEntry.COLUMN_SONG_NAME, SongContract.SongEntry.COLUMN_SONG_NAME);
        map.put(SongContract.SongEntry.COLUMN_ARTIST_NAME, SongContract.SongEntry.COLUMN_ARTIST_NAME);
        map.put(SongContract.SongEntry.COLUMN_IMAGE_URL, SongContract.SongEntry.COLUMN_IMAGE_URL);
        map.put(SongContract.SongEntry.COLUMN_SONG_URL, SongContract.SongEntry.COLUMN_SONG_URL);
        map.put(SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID, SongContract.SongEntry._ID + " AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA_ID);
        map.put(SearchManager.SUGGEST_COLUMN_SHORTCUT_ID, SongContract.SongEntry._ID + " AS " + SearchManager.SUGGEST_COLUMN_SHORTCUT_ID);
        return map;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case SEARCH_SUGGEST: {
                String rawQuery = "";
                if (selectionArgs != null && selectionArgs.length > 0) {
                    rawQuery = selectionArgs[0];
                }
                retCursor = getSuggestions(rawQuery);
                break;
            }
            case AUDIO: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        SongContract.SongEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        retCursor.setNotificationUri(mContentResolver, uri);
        return retCursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case AUDIO:
                return SongContract.SongEntry.CONTENT_TYPE;
            // The Android TV global search is querying our app for relevant content.
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            case REFRESH_SHORTCUT:
                return SearchManager.SHORTCUT_MIME_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        final Uri returnUri;
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case AUDIO: {
                long _id = mOpenHelper.getWritableDatabase().insert(
                        SongContract.SongEntry.TABLE_NAME, null, values);
                if (_id > 0) {
                    returnUri = SongContract.SongEntry.buildSongUri(_id);
                } else {
                    throw new SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        mContentResolver.notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        final int rowsDeleted;

        if (selection == null) {
            throw new UnsupportedOperationException("Cannot delete without selection specified.");
        }

        switch (sUriMatcher.match(uri)) {
            case AUDIO: {
                rowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        SongContract.SongEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (rowsDeleted != 0) {
            mContentResolver.notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        final int rowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case AUDIO: {
                rowsUpdated = mOpenHelper.getWritableDatabase().update(
                        SongContract.SongEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (rowsUpdated != 0) {
            mContentResolver.notifyChange(uri, null);
        }

        return rowsUpdated;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
        switch (sUriMatcher.match(uri)) {
            case AUDIO: {
                final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                int returnCount = 0;

                db.beginTransaction();
                try {
                    for (ContentValues value : values) {
                        long _id = db.insertWithOnConflict(SongContract.SongEntry.TABLE_NAME,
                                null, value, SQLiteDatabase.CONFLICT_REPLACE);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                mContentResolver.notifyChange(uri, null);
                return returnCount;
            }
            default: {
                return super.bulkInsert(uri, values);
            }
        }
    }
}