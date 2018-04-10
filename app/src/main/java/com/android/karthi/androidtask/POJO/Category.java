package com.android.karthi.androidtask.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Category {
    @SerializedName("category_name")
    @Expose
    private String categoryName;
    @SerializedName("category_images")
    @Expose
    private ArrayList<String> categoryImages;

    public Category(String categoryName, ArrayList<String> categoryImages) {
        this.categoryName = categoryName;
        this.categoryImages = categoryImages;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public ArrayList<String> getCategoryImages() {
        return categoryImages;
    }


}
