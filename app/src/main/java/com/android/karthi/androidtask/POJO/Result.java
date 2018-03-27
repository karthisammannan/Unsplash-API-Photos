package com.android.karthi.androidtask.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Raghuvarma on 27/3/2018.
 */

public class Result {


    @SerializedName("urls")
    @Expose
    private Urls urls;

    public Urls getUrls() {
        return urls;
    }

    public void setUrls(Urls urls) {
        this.urls = urls;
    }


}