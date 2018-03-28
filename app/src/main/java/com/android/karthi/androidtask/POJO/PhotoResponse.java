package com.android.karthi.androidtask.POJO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Karthi on 27/3/2018.
 */

public class PhotoResponse  {

    @SerializedName("results")
    @Expose
    private List<Result> results = null;


    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

}