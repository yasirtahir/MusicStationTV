package com.yasir.musicstation.tv.models;

import java.util.ArrayList;

public class DataModel {

    private ArrayList<CategoryModel> data;

    public ArrayList<CategoryModel> getData() {
        return data == null ? new ArrayList<CategoryModel>() : data;
    }

    public void setData(ArrayList<CategoryModel> data) {
        this.data = data;
    }
}
