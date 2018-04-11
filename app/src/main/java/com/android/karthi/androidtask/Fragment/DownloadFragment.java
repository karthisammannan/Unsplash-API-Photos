package com.android.karthi.androidtask.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.karthi.androidtask.Adapter.PhotosAdapter;
import com.android.karthi.androidtask.DataService.ApiClient;
import com.android.karthi.androidtask.DataService.ApiService;
import com.android.karthi.androidtask.POJO.PhotoResponse;
import com.android.karthi.androidtask.POJO.Result;
import com.android.karthi.androidtask.R;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.android.karthi.androidtask.Const.Const.CLIENT_ID;
import static com.android.karthi.androidtask.Const.Const.PER_PAGE;

public class DownloadFragment extends Fragment {


    public DownloadFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }  //Retrieve data from unsplash api

}
