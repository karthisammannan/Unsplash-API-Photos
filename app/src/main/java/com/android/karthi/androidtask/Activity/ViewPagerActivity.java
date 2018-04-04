package com.android.karthi.androidtask.Activity;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.karthi.androidtask.Fragment.PhotoFragment;
import com.android.karthi.androidtask.POJO.Result;
import com.android.karthi.androidtask.R;
import com.android.karthi.androidtask.Service.DownloadService;

import java.util.ArrayList;
import java.util.List;

import static com.android.karthi.androidtask.Const.Const.My_BROADCAST_ACTION;
import static com.android.karthi.androidtask.Const.Const.My_INTENT;
import static com.android.karthi.androidtask.Const.Const.My_INTENT_ARG_POSITION;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        initPaging();
    }

    private void initPaging() {
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        System.out.print(itemList.get(0).getUrls().getThumb());
        for (int i = 0; i < itemList.size(); i++) {
            PhotoFragment photoFragment = new PhotoFragment();
            Bundle args = new Bundle();
            args.putString(My_INTENT_ARG_POSITION, itemList.get(i).getUrls().getSmall());
            photoFragment.setArguments(args);
            pagerAdapter.addFragment(photoFragment);
        }

        viewPager.setAdapter(pagerAdapter);
        Log.d(TAG, "CurPosition: " + position);
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

    public class PagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragments = new ArrayList<Fragment>();

        public PagerAdapter(FragmentManager manager) {
            super(manager);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    public class ResponseReceiver extends BroadcastReceiver {
        private static final String TAG = "ResponseReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            int message = intent.getIntExtra(My_INTENT_RESPONSE_DOWNLOAD, 0);
            Log.d(TAG, "onReceive: "+message);

            if(message != 0) {
                    mBuilder.setProgress(100, message, false);
                    // Displays the progress bar on notification
                    mNotifyManager.notify(0, mBuilder.build());
                } else {
                    mBuilder.setContentText("Download complete");
                    // Removes the progress bar
                    mBuilder.setProgress(0,0,false);
                    mBuilder.setSmallIcon(android.R.drawable.stat_sys_download_done);
                    mNotifyManager.notify(0, mBuilder.build());
                }
        }
    }
}
