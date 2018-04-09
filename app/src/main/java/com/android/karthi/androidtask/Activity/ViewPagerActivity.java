package com.android.karthi.androidtask.Activity;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.karthi.androidtask.POJO.Result;
import com.android.karthi.androidtask.R;
import com.android.karthi.androidtask.Service.DownloadService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;

import static com.android.karthi.androidtask.Const.Const.My_BROADCAST_ACTION;
import static com.android.karthi.androidtask.Const.Const.My_INTENT;
import static com.android.karthi.androidtask.Const.Const.My_INTENT_DOWNLOAD;
import static com.android.karthi.androidtask.Const.Const.My_INTENT_POSITION;
import static com.android.karthi.androidtask.Const.Const.My_INTENT_RESPONSE_DOWNLOAD;
import static com.android.karthi.androidtask.Const.Const.NOTIFY_OPEN_URL;
import static com.android.karthi.androidtask.Const.Const.NOTIFY_URL;

public class ViewPagerActivity extends AppCompatActivity {
    private ArrayList<Result> itemList;
    private int position;
    private static final String TAG = "ViewPagerActivity";
    private Toolbar toolbar;
    ViewPager viewPager;
    private ImageView icoBack, icoDownload;
    ResponseReceiver responseReceiver;
    NotificationManager mNotifyManager;
    NotificationCompat.Builder mBuilder;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        public void run() {
            hideSystemUI();
        }
    };
    int mLastSystemUIVisibility = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        // Retrieve the AppCompact Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int stausbarHeight = getStatusBarHeight();

        // Set the padding to match the Status Bar height
        toolbar.setPadding(0, stausbarHeight, 0, 0);
        //Set toolbar height programatically
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) toolbar.getLayoutParams();
        params.height = getActionbarHeight() + stausbarHeight;
        toolbar.setLayoutParams(params);

        viewPager = (ViewPager) findViewById(R.id.viewPager);
        icoBack = (ImageView) findViewById(R.id.ico_back);
        icoDownload = (ImageView) findViewById(R.id.ico_download);
        icoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        icoDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isStoragePermissionGranted()) {
                    download();
                }
            }
        });
        itemList = getIntent().getParcelableArrayListExtra(My_INTENT);
        position = getIntent().getIntExtra(My_INTENT_POSITION, 0);
        viewPager.setAdapter(new SamplePagerAdapter());
        viewPager.setCurrentItem(position);
        startHandler();
        View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if ((visibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0) {
                    startHandler();
                }
                mLastSystemUIVisibility = visibility;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(My_BROADCAST_ACTION);
        responseReceiver = new ResponseReceiver();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.registerReceiver(responseReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(responseReceiver);
    }

    private void download() {
        String url =  itemList.get(viewPager.getCurrentItem()).getUrls().getRegular();
        //Start the Service & Notificaion

        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(ViewPagerActivity.this,"channel_id");
        mBuilder.setContentTitle("File Download")
                .setContentText("Download in progress")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher));
        Intent intent = new Intent(ViewPagerActivity.this, DownloadService.class);
        intent.putExtra(My_INTENT_DOWNLOAD,url);
        startService(intent);
    }

    public boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.v(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
            //resume tasks needing this permission
            download();
        } else {
            Snackbar.make(toolbar,"Permission Failed",Snackbar.LENGTH_SHORT).show();
        }
    }

    private void startHandler() {
        //remove previous callbacks
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 3000);
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

    class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(container.getContext());
            //Glide library to load the image
            Glide.with(ViewPagerActivity.this)
                    .load(itemList.get(position).getUrls().getThumb())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(photoView);
            // Now just add PhotoView to ViewPager and return it
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            photoView.setOnPhotoTapListener(new OnPhotoTapListener() {
                @Override
                public void onPhotoTap(ImageView view, float x, float y) {
                    fullScreen();
                }
            });
            return photoView;
        }

        public void fullScreen() {
            if ((mLastSystemUIVisibility & View.SYSTEM_UI_FLAG_HIDE_NAVIGATION) == 0)
                hideSystemUI();
            else
                showSystemUI();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    public class ResponseReceiver extends BroadcastReceiver {
        private static final String TAG = "ResponseReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            int message = intent.getIntExtra(My_INTENT_RESPONSE_DOWNLOAD, 0);
            if (message != 0) {
                mBuilder.setProgress(100, message, false);
                // Displays the progress bar on notification
                mNotifyManager.notify(0, mBuilder.build());
            } else {
                Intent notificationIntent = new Intent(ViewPagerActivity.this, PhotoAcitivity.class);
                Log.d(TAG, "NOTIFY_URL: "+intent.getStringExtra(NOTIFY_URL));
                notificationIntent.putExtra(NOTIFY_OPEN_URL,intent.getStringExtra(NOTIFY_URL));
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(ViewPagerActivity.this, 0,
                        notificationIntent, 0);
                mBuilder.setContentIntent(pendingIntent);
                mBuilder.setAutoCancel(true);
                mBuilder.setContentText("Download complete");
                // Removes the progress bar
                mBuilder.setProgress(0, 0, false);
                mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                mNotifyManager.notify(0, mBuilder.build());
            }
        }
    }

    public void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_IMMERSIVE);
        getSupportActionBar().hide();
    }

    public void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getSupportActionBar().show();
    }

}
