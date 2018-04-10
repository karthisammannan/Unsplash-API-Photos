package com.android.karthi.androidtask.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ListCategory {
    @SerializedName("categories")
    @Expose
    private ArrayList<Category> category;

    public ArrayList<Category> getCategory() {
        return category;
    }

}
