package com.yasir.musicstation.tv.models;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public final class SongContract {

    // The name for the entire content provider.
    public static final String CONTENT_AUTHORITY = "com.yasir.musicstation.tv";

    // Base of all URIs that will be used to contact the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // The content paths.
    public static final String PATH_SONG = "song";

    public static final class SongEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_SONG).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "." + PATH_SONG;

        public static final String TABLE_NAME = "song";

        public static final String COLUMN_CATEGORY_NAME = "categoryName";

        public static final String COLUMN_SONG_NAME = SearchManager.SUGGEST_COLUMN_TEXT_1;

        public static final String COLUMN_ARTIST_NAME = SearchManager.SUGGEST_COLUMN_TEXT_2;

        public static final String COLUMN_IMAGE_URL = "image_url";

        public static final String COLUMN_SONG_URL = "song_url";

        public static final String COLUMN_CONTENT_TYPE = SearchManager.SUGGEST_COLUMN_CONTENT_TYPE;

        public static final String COLUMN_IS_LIVE = SearchManager.SUGGEST_COLUMN_IS_LIVE;

        public static final String COLUMN_VIDEO_WIDTH = SearchManager.SUGGEST_COLUMN_VIDEO_WIDTH;

        public static final String COLUMN_VIDEO_HEIGHT = SearchManager.SUGGEST_COLUMN_VIDEO_HEIGHT;

        public static final String COLUMN_AUDIO_CHANNEL_CONFIG = SearchManager.SUGGEST_COLUMN_AUDIO_CHANNEL_CONFIG;

        public static final String COLUMN_PURCHASE_PRICE = SearchManager.SUGGEST_COLUMN_PURCHASE_PRICE;

        public static final String COLUMN_RENTAL_PRICE = SearchManager.SUGGEST_COLUMN_RENTAL_PRICE;

        public static final String COLUMN_RATING_STYLE = SearchManager.SUGGEST_COLUMN_RATING_STYLE;

        public static final String COLUMN_RATING_SCORE = SearchManager.SUGGEST_COLUMN_RATING_SCORE;

        public static final String COLUMN_PRODUCTION_YEAR = SearchManager.SUGGEST_COLUMN_PRODUCTION_YEAR;

        public static final String COLUMN_DURATION = SearchManager.SUGGEST_COLUMN_DURATION;

        public static final String COLUMN_ACTION = SearchManager.SUGGEST_COLUMN_INTENT_ACTION;

        public static Uri buildSongUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
