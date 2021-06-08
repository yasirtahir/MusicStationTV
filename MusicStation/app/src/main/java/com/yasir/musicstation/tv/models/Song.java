package com.yasir.musicstation.tv.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

public class Song implements Parcelable {

    public final long id;
    private String categoryName;
    private String songName;
    private String artistName;
    private String imageURL;
    private String songURL;

    public Song(long id, String categoryName, String songName, String artistName, String imageURL, String songURL) {
        this.id = id;
        this.categoryName = categoryName;
        this.songName = songName;
        this.artistName = artistName;
        this.imageURL = imageURL;
        this.songURL = songURL;
    }

    protected Song(Parcel in) {
        id = in.readLong();
        categoryName = in.readString();
        songName = in.readString();
        artistName = in.readString();
        imageURL = in.readString();
        songURL = in.readString();
    }

    public String getCategoryName() {
        return categoryName == null ? "" : categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSongName() {
        return songName == null ? "" : songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName  == null ? "" : artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getImageURL() {
        return imageURL  == null ? "" : imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getSongURL() {
        return songURL  == null ? "" : songURL;
    }

    public void setSongURL(String songURL) {
        this.songURL = songURL;
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public boolean equals(Object m) {
        return m instanceof Song && id == ((Song) m).id;
    }

    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(categoryName);
        dest.writeString(songName);
        dest.writeString(artistName);
        dest.writeString(imageURL);
        dest.writeString(songURL);
    }

    @NotNull
    @Override
    public String toString() {
        String s = "Song{";
        s += "id=" + id;
        s += ", categoryName='" + categoryName + "'";
        s += ", songName='" + songName + "'";
        s += ", artistName='" + artistName + "'";
        s += ", imageURL='" + imageURL + "'";
        s += ", songURL='" + songURL + "'";
        s += "}";
        return s;
    }

    // Builder for Song object.
    public static class SongBuilder {
        private long id;
        private String categoryName;
        private String songName;
        private String artistName;
        private String imageURL;
        private String songURL;

        public SongBuilder id(long id) {
            this.id = id;
            return this;
        }

        public SongBuilder categoryName(String categoryName) {
            this.categoryName = categoryName;
            return this;
        }

        public SongBuilder songName(String songName) {
            this.songName = songName;
            return this;
        }

        public SongBuilder artistName(String artistName) {
            this.artistName = artistName;
            return this;
        }

        public SongBuilder imageURL(String imageURL) {
            this.imageURL = imageURL;
            return this;
        }

        public SongBuilder songURL(String songURL) {
            this.songURL = songURL;
            return this;
        }

        public Song build() {
            return new Song(
                    id,
                    categoryName,
                    songName,
                    artistName,
                    imageURL,
                    songURL
            );
        }
    }

}
