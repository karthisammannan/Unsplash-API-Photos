package com.android.karthi.androidtask.Fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.karthi.androidtask.Adapter.CategoryAdapter;
import com.android.karthi.androidtask.Adapter.VerticalSpaceItemDecoration;
import com.android.karthi.androidtask.POJO.Category;
import com.android.karthi.androidtask.POJO.ListCategory;
import com.android.karthi.androidtask.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class CategoryFragment extends Fragment {
    RecyclerView mRecyclerCategory;
    LinearLayoutManager mLayoutManager;
    CategoryAdapter mCategoryAaptor;
    private static final String TAG = "CategoryFragment";

    public CategoryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_category, container, false);
        mRecyclerCategory = (RecyclerView) view.findViewById(R.id.recycler_category);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerCategory.setLayoutManager(mLayoutManager);
        mRecyclerCategory.addItemDecoration(new VerticalSpaceItemDecoration(30));
        mCategoryAaptor = new CategoryAdapter(getActivity(), getItems());
        mRecyclerCategory.setAdapter(mCategoryAaptor);
        return view;
    }

    private ArrayList<Category> getItems() {
        String json = null;
        try {
            InputStream inputStream = getActivity().getAssets().open("Category.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, "UTF-8");

        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "getItems: " + json);
        ListCategory category = new Gson().fromJson(json, ListCategory.class);
        return category.getCategory();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
