package com.android.karthi.androidtask.Activity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.karthi.androidtask.Fragment.CategoryFragment;
import com.android.karthi.androidtask.Fragment.DownloadFragment;
import com.android.karthi.androidtask.Fragment.FavoriteFragment;
import com.android.karthi.androidtask.Fragment.SearchFragment;
import com.android.karthi.androidtask.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "HomeActivity";
    Toolbar toolbar;
    View reveal_view, reveal_bg;
    String color;
    @BindView(R.id.title)
    TextView tvwTitle;
    @BindView(R.id.tvw_category)
    TextView tvwCategory;
    @BindView(R.id.tvw_search)
    TextView tvwSearch;
    @BindView(R.id.tvw_downloads)
    TextView tvwDownloads;
    @BindView(R.id.tvw_favorites)
    TextView tvwFavorites;
    @BindView(R.id.ico_category)
    ImageView icoCategory;
    @BindView(R.id.ico_search)
    ImageView icoSearch;
    @BindView(R.id.ico_downloads)
    ImageView icoDownloads;
    @BindView(R.id.ico_favorite)
    ImageView icoFavorite;
    @BindView(R.id.action_category)
    LinearLayout mActionCategory;
    @BindView(R.id.action_search)
    LinearLayout mActionSearch;
    @BindView(R.id.action_downloads)
    LinearLayout mActionDownloads;
    @BindView(R.id.action_favorite)
    LinearLayout mActionFavorite;
    int defaultColor = Color.parseColor("#455A64");
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        reveal_view = (View) findViewById(R.id.reveal);
        reveal_bg = (View) findViewById(R.id.reveal_bg);

        mActionCategory.setOnClickListener(this);
        mActionSearch.setOnClickListener(this);
        mActionDownloads.setOnClickListener(this);
        mActionFavorite.setOnClickListener(this);

        // Retrieve the AppCompact Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setBackgroundColor(Color.parseColor("#00000000"));
        reveal_view.setBackgroundColor(defaultColor);
        icoCategory.setColorFilter(defaultColor);
        tvwCategory.setTextColor(defaultColor);
        reveal_bg.setBackgroundColor(defaultColor);

        int stausbarHeight = getStatusBarHeight();
        // Set the padding to match the Status Bar height
        reveal_bg.setPadding(0, stausbarHeight, 0, 0);
        //Set toolbar height programatically
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) reveal_bg.getLayoutParams();
        params.height = getActionbarHeight() + stausbarHeight;
        reveal_bg.setLayoutParams(params);
        // Set the padding to match the Status Bar height
        toolbar.setPadding(0, stausbarHeight, 0, 0);
        //Set toolbar height programatically
        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) toolbar.getLayoutParams();
        params2.height = getActionbarHeight() + stausbarHeight;
        toolbar.setLayoutParams(params2);

        // Set the padding to match the Status Bar height
        reveal_view.setPadding(0, stausbarHeight, 0, 0);
        //Set toolbar height programatically
        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) reveal_view.getLayoutParams();
        params1.height = getActionbarHeight() + stausbarHeight;
        reveal_view.setLayoutParams(params1);
        CategoryFragment categoryFragment = new CategoryFragment();
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, categoryFragment).commit();
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

    @SuppressLint("Range")
    @Override
    public void onClick(View view) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        int id = view.getId();
        ImageView imageView = null;
        TextView textView = null;
        setColor();
        switch (id) {
            case R.id.action_category:
                color = "#455A64";
                imageView = icoCategory;
                textView = tvwCategory;
                title = "Category";
                ft.replace(R.id.container, new CategoryFragment());
                break;
            case R.id.action_search:
                imageView = icoSearch;
                textView = tvwSearch;
                title = "Search";
                color = "#0091EA";
                ft.replace(R.id.container, new SearchFragment());
                break;
            case R.id.action_downloads:
                imageView = icoDownloads;
                textView = tvwDownloads;
                title = "Downloads";
                color = "#4A148C";
                ft.replace(R.id.container, new DownloadFragment());

                break;
            case R.id.action_favorite:
                imageView = icoFavorite;
                textView = tvwFavorites;
                color = "#00AA8D";
                title = "Favorite";
                ft.replace(R.id.container, new FavoriteFragment());

                break;
            default:
                break;
        }

        ft.commit();
        setIconColor(color, imageView);
        setTextColor(color, textView);
        reveal_view.setBackgroundColor(Color.parseColor(color));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startAnim();
        }
        tvwTitle.setText(title);
    }

    void setIconColor(String color, ImageView view) {
        view.setColorFilter(Color.parseColor(color));
    }

    void setTextColor(String color, TextView view) {
        view.setTextColor(Color.parseColor(color));
    }

    void setColor() {
        int color = Color.parseColor("#747474");
        icoCategory.setColorFilter(color);
        icoSearch.setColorFilter(color);
        icoDownloads.setColorFilter(color);
        icoFavorite.setColorFilter(color);
        tvwCategory.setTextColor(color);
        tvwSearch.setTextColor(color);
        tvwDownloads.setTextColor(color);
        tvwFavorites.setTextColor(color);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    void startAnim() {
        Animator reveal = ViewAnimationUtils.createCircularReveal(reveal_view,
                reveal_view.getWidth() / 2,
                reveal_view.getHeight() / 2,
                reveal_view.getWidth() / 5,
                reveal_view.getWidth());
        reveal.setDuration(200).start();
        reveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                reveal_bg.setBackgroundColor(Color.parseColor(color));
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                reveal_bg.setBackgroundColor(Color.parseColor(color));
            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

    }

}
