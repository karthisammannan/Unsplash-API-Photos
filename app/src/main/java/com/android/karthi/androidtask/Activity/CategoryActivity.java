package com.android.karthi.androidtask.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.karthi.androidtask.Adapter.CategoryAdapter;
import com.android.karthi.androidtask.Adapter.VerticalSpaceItemDecoration;
import com.android.karthi.androidtask.POJO.Category;
import com.android.karthi.androidtask.POJO.ListCategory;
import com.android.karthi.androidtask.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class CategoryActivity extends AppCompatActivity {
    RecyclerView mRecyclerCategory;
    LinearLayoutManager mLayoutManager;
    CategoryAdapter mCategoryAaptor;
    private static final String TAG = "CategoryActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        mRecyclerCategory = (RecyclerView) findViewById(R.id.recycler_category);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerCategory.setLayoutManager(mLayoutManager);
          mRecyclerCategory.addItemDecoration(new VerticalSpaceItemDecoration(30));
        mCategoryAaptor = new CategoryAdapter(this, getItems());
        mRecyclerCategory.setAdapter(mCategoryAaptor);
    }

    private ArrayList<Category> getItems() {
        String json = null;
        try {
            InputStream inputStream = getAssets().open("Category.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getItems: "+json);
        ListCategory category = new Gson().fromJson(json, ListCategory.class);
        return category.getCategory();
    }
}
