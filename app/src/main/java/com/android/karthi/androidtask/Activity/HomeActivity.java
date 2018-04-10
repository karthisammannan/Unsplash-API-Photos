package com.android.karthi.androidtask.Activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.android.karthi.androidtask.Helper.BottomNavigationViewHelper;
import com.android.karthi.androidtask.R;

import io.reactivex.annotations.NonNull;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "HomeActivity";
    Toolbar toolbar;
    LinearLayout mActionCategory, mActionSearch, mActionDownloads, mActionFavorite;
    ImageView icoCategory, icoSearch,icoDownloads,icoFavorite;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mActionCategory = (LinearLayout) findViewById(R.id.action_category);
        mActionSearch = (LinearLayout) findViewById(R.id.action_search);
        mActionDownloads = (LinearLayout) findViewById(R.id.action_downloads);
        mActionFavorite = (LinearLayout) findViewById(R.id.action_favorite);

        icoCategory =(ImageView)findViewById(R.id.ico_category);
        icoSearch =(ImageView)findViewById(R.id.ico_search);
        icoDownloads =(ImageView)findViewById(R.id.ico_downloads);
        icoFavorite =(ImageView)findViewById(R.id.ico_favorite);

        mActionCategory.setOnClickListener(this);
        mActionSearch.setOnClickListener(this);
        mActionDownloads.setOnClickListener(this);
        mActionFavorite.setOnClickListener(this);

        // FOR NAVIGATION VIEW ITEM TEXT COLOR
        final int[][] states = new int[][]{
                new int[]{-android.R.attr.state_checked},  // unchecked
                new int[]{android.R.attr.state_checked},   // checked
                new int[]{}                                // default
        };

        // Fill in color corresponding to state defined in state
        final int[] color_favorite = new int[]{
                Color.parseColor("#00AA8D"),
                Color.parseColor("#00AA8D"),
                Color.parseColor("#00AA8D"),
        };
        // Fill in color corresponding to state defined in state
        final int[] color_download = new int[]{
                Color.parseColor("#747474"),
                Color.parseColor("#455A64"),
                Color.parseColor("#747474"),
        };   // Fill in color corresponding to state defined in state
        final int[] color_category = new int[]{
                Color.parseColor("#747474"),
                Color.parseColor("#4A148C"),
                Color.parseColor("#747474"),
        };   // Fill in color corresponding to state defined in state
        final int[] color_search = new int[]{
                Color.parseColor("#747474"),
                Color.parseColor("#0091EA"),
                Color.parseColor("#747474"),
        };
        // Retrieve the AppCompact Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(Color.parseColor("#4A148C"));
        int stausbarHeight = getStatusBarHeight();
        // Set the padding to match the Status Bar height
        toolbar.setPadding(0, stausbarHeight, 0, 0);
        //Set toolbar height programatically
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        params.height = getActionbarHeight() + stausbarHeight;
        toolbar.setLayoutParams(params);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent: ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: ");
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    private int getActionbarHeight() {
        // Calculate ActionBar height
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    // A method to find height of the status bar
    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        setColor();
        switch (id) {
            case R.id.action_category:
                icoCategory.setColorFilter(Color.parseColor("#4A148C"));
                toolbar.setBackgroundColor(Color.parseColor("#4A148C"));
                break;
            case R.id.action_search:
                icoSearch.setColorFilter(Color.parseColor("#0091EA"));
                toolbar.setBackgroundColor(Color.parseColor("#0091EA"));
                break;
            case R.id.action_downloads:
                icoDownloads.setColorFilter(Color.parseColor("#455A64"));
                toolbar.setBackgroundColor(Color.parseColor("#455A64"));

                break;
            case R.id.action_favorite:
                icoFavorite.setColorFilter(Color.parseColor("#00AA8D"));
                toolbar.setBackgroundColor(Color.parseColor("#00AA8D"));
                break;
            default:
                break;
        }
    }
    void setColor(){
        icoCategory.setColorFilter(Color.parseColor("#747474"));
        icoSearch.setColorFilter(Color.parseColor("#747474"));
        icoDownloads.setColorFilter(Color.parseColor("#747474"));
        icoFavorite.setColorFilter(Color.parseColor("#747474"));
    }

}
