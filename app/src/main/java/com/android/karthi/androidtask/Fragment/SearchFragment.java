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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.karthi.androidtask.Adapter.CategoryAdapter;
import com.android.karthi.androidtask.Adapter.PhotosAdapter;
import com.android.karthi.androidtask.Adapter.VerticalSpaceItemDecoration;
import com.android.karthi.androidtask.DataService.ApiClient;
import com.android.karthi.androidtask.DataService.ApiService;
import com.android.karthi.androidtask.POJO.Category;
import com.android.karthi.androidtask.POJO.ListCategory;
import com.android.karthi.androidtask.POJO.PhotoResponse;
import com.android.karthi.androidtask.POJO.Result;
import com.android.karthi.androidtask.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.android.karthi.androidtask.Const.Const.CLIENT_ID;
import static com.android.karthi.androidtask.Const.Const.PER_PAGE;

public class SearchFragment extends Fragment {

    private static final String TAG = "MainActivity";
    GridLayoutManager gridLayoutManager;
    private ApiService apiService;
    LinearLayout emptyPhotos;
    private CompositeDisposable disposable = new CompositeDisposable();
    PhotosAdapter photosAdapter;
    RecyclerView recyclerView;
    private ProgressBar progressBar, rounProgress;

    private boolean loading = false;
    private ArrayList<Result> itemList = new ArrayList<>();
    private int pageNumber = 1;
    private final int VISIBLE_THRESHOLD = 1;
    private int lastVisibleItem, totalItemCount;
    private String query = "baby";
    private boolean connectionFlag = false;
    public SearchFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_main, container, false);
        //initialize views and retrofit service
        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        rounProgress = (ProgressBar) view.findViewById(R.id.progressBarRound);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        emptyPhotos = (LinearLayout) view.findViewById(R.id.empty_photos);
//        floatingSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        //Grid layout manger for recyclerview with sapan 2
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //add data to adapter
        photosAdapter = new PhotosAdapter(getActivity(), itemList);
        recyclerView.setAdapter(photosAdapter);
        getListItemData(query);
        registerConnectivityNetworkMonitorForAPI21AndUp();
        setUpLoadMoreListener();
      /*  //floating On search Action
        floatingSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
            }

            @Override
            public void onSearchAction(String currentQuery) {
                pageNumber = 1;
                loading = true;
                getListItemData(currentQuery);
                itemList.clear();
                photosAdapter.removeItems();
            }
        });*/
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }  //Retrieve data from unsplash api
    private void getListItemData(String localQuery) {
        query = localQuery;
        //To run on main thread
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (rounProgress.getVisibility() != View.VISIBLE)
                    progressBar.setVisibility(View.VISIBLE);
            }
        });
        //RX java disposable to cancel the api request, whenever destory/close activty
        disposable.add(apiService
                .getPhotos(query, CLIENT_ID, pageNumber, PER_PAGE)
                .subscribeOn(Schedulers.io())                  //Seperate Thread
                .observeOn(AndroidSchedulers.mainThread())      //Main Thread
                .subscribeWith(new DisposableSingleObserver<PhotoResponse>() {
                    @Override
                    public void onSuccess(PhotoResponse photoResponse) {
                        if (photoResponse.getResults().isEmpty()) {
                            if (pageNumber == 1)
                                hideProgress();
                        } else {
                            //update the photos adapter from load more data
                            photosAdapter.addItems(photoResponse.getResults());
                            loading = false;
                            rounProgress.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                            emptyPhotos.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (pageNumber > 1) {
                            progressBar.setVisibility(View.INVISIBLE);
                            Snackbar.make(recyclerView, "No more photos found", Snackbar.LENGTH_LONG).show();
                        } else {
                            hideProgress();
                            snacbar_show("Check your internet connection");
                        }
                    }
                }));
    }

    /**
     * setting listener to get callback for load more
     */
    private void setUpLoadMoreListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView,
                                   int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = gridLayoutManager.getItemCount();
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();
                if (!loading
                        && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    pageNumber++;
                    getListItemData(query);
                    loading = true;
                }
            }
        });
    }  //unregister the disposable components

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @SuppressLint("NewApi")
    private void registerConnectivityNetworkMonitorForAPI21AndUp() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();
        connectivityManager.registerNetworkCallback(
                builder.build(),
                new ConnectivityManager.NetworkCallback() {
                    /**
                     * @param network
                     */
                    @Override
                    public void onAvailable(Network network) {
                        getConnectivityIntent(false);
                    }

                    /**
                     * @param network
                     */
                    @Override
                    public void onLost(Network network) {
                        getConnectivityIntent(true);
                    }
                }
        );
    }

    private void getConnectivityIntent(boolean noConnection) {
        if (noConnection) {
            snacbar_show("Check your internet connection");
            connectionFlag = true;
        } else {
            if (connectionFlag)
                Snackbar.make(recyclerView, "Network connection back", Snackbar.LENGTH_LONG).show();
        }
    }

    //hide progress
    public void hideProgress() {
        rounProgress.setVisibility(View.GONE);
        emptyPhotos.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void snacbar_show(String message) {
        Snackbar.make(recyclerView, message, Snackbar.LENGTH_LONG).setAction("Settings",
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //implicit intent to launch settings screen
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        startActivity(intent);
                    }
                }).show();
    }


}
