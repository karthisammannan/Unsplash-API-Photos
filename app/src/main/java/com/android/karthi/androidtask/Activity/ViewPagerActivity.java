package com.android.karthi.androidtask.Activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.android.karthi.androidtask.POJO.Result;
import com.android.karthi.androidtask.R;
import com.android.karthi.androidtask.Service.DownloadService;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;

import static android.R.attr.uiOptions;

import java.util.ArrayList;

import static com.android.karthi.androidtask.Const.Const.My_BROADCAST_ACTION;
import static com.android.karthi.androidtask.Const.Const.My_INTENT;
import static com.android.karthi.androidtask.Const.Const.My_INTENT_DOWNLOAD;
import static com.android.karthi.androidtask.Const.Const.My_INTENT_POSITION;
import static com.android.karthi.androidtask.Const.Const.My_INTENT_RESPONSE_DOWNLOAD;

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
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  //No title bar is set for the activity
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_view_pager);



        viewPager = (ViewPager) findViewById(R.id.viewPager);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
                //Start the Service
                Log.d(TAG, "intent_service_started");
                //show notificaion
                mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                mBuilder = new NotificationCompat.Builder(ViewPagerActivity.this);
                mBuilder.setContentTitle("File Download")
                        .setContentText("Download in progress")
                        .setSmallIcon(android.R.drawable.stat_sys_download)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                R.mipmap.ic_launcher));
                Intent intent = new Intent(ViewPagerActivity.this, DownloadService.class);
                intent.putExtra(My_INTENT_DOWNLOAD, itemList.get(viewPager.getCurrentItem()).getUrls().getRegular());
                startService(intent);
            }
        });
        itemList = getIntent().getParcelableArrayListExtra(My_INTENT);
        position = getIntent().getIntExtra(My_INTENT_POSITION, 0);
        viewPager.setAdapter(new SamplePagerAdapter());
        viewPager.setCurrentItem(position);
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
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager localBroadcastManager = LocalBroadcastManager.getInstance(this);
        localBroadcastManager.unregisterReceiver(responseReceiver);
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
            if (flag) {
                // Hide status bar
                hideSystemUI();
                flag = false;
            } else {
                // Show status bar
                showSystemUI();
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                flag = true;
            }
        }
        private void hideSystemUI() {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        private void showSystemUI() {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
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
            Log.d(TAG, "onReceive: " + message);

            if (message != 0) {
                mBuilder.setProgress(100, message, false);
                // Displays the progress bar on notification
                mNotifyManager.notify(0, mBuilder.build());
            } else {
                mBuilder.setContentText("Download complete");
                // Removes the progress bar
                mBuilder.setProgress(0, 0, false);
                mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                mNotifyManager.notify(0, mBuilder.build());
            }
        }
    }
}
