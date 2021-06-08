package com.yasir.musicstation.tv.models;

import java.util.ArrayList;

public class CategoryModel {

    private String categoryName;
    private ArrayList<Song> categorySongs;

    public String getCategoryName() {
        return categoryName == null ? "" : categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<Song> getCategorySongs() {
        return categorySongs == null ? new ArrayList<Song>() : categorySongs;
    }

    public void setCategorySongs(ArrayList<Song> categorySongs) {
        this.categorySongs = categorySongs;
    }
}
