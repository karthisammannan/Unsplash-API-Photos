package com.android.karthi.androidtask;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.karthi.androidtask.Adapter.PhotosAdapter;
import com.android.karthi.androidtask.DataService.ApiClient;
import com.android.karthi.androidtask.DataService.ApiService;
import com.android.karthi.androidtask.POJO.PhotoResponse;
import com.android.karthi.androidtask.POJO.Result;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static com.android.karthi.androidtask.Const.Const.CLIENT_ID;
import static com.android.karthi.androidtask.Const.Const.PER_PAGE;

/**
 * Created by Karthi on 27/3/2018.
 */

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    GridLayoutManager gridLayoutManager;
    private ApiService apiService;
    LinearLayout emptyPhotos;
    private CompositeDisposable disposable = new CompositeDisposable();
    PhotosAdapter photosAdapter;
    RecyclerView recyclerView;
    private ProgressBar progressBar, rounProgress;
    FloatingSearchView floatingSearchView;

    private boolean loading = false;
    private List<Result> itemList = new ArrayList<>();
    private int pageNumber = 1;
    private final int VISIBLE_THRESHOLD = 1;
    private int lastVisibleItem, totalItemCount;
    private String query = "baby";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initialize views and retrofit service
        apiService = ApiClient.getClient().create(ApiService.class);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        rounProgress = (ProgressBar) findViewById(R.id.progressBarRound);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyPhotos = (LinearLayout) findViewById(R.id.empty_photos);
        floatingSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        //Grid layout manger for recyclerview with sapan 2
        recyclerView.setHasFixedSize(true);
        gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        //add data to adapter
        photosAdapter = new PhotosAdapter(this, itemList);
        recyclerView.setAdapter(photosAdapter);
        getListItemData(query);
        setUpLoadMoreListener();
        //floating On search Action
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
        });
    }

    //register the network connection callback
    @Override
    protected void onResume() {
        super.onResume();
        registerConnectivityNetworkMonitorForAPI21AndUp();
    }

    //Retrieve data from unsplash api
    private void getListItemData(String localQuery) {
        query = localQuery;
        //To run on main thread
        runOnUiThread(new Runnable() {
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
                            snacbar_show("Network Connection Error");
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
    }

    //unregister the disposable components
    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }

    @SuppressLint("NewApi")
    private void registerConnectivityNetworkMonitorForAPI21AndUp() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
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
        if (!noConnection) {
            getListItemData(query);
        } else {
            snacbar_show("Network Connection Error");
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
