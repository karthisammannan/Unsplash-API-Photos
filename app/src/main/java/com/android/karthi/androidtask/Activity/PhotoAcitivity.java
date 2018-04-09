package com.android.karthi.androidtask.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.karthi.androidtask.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;

import java.io.File;
import java.io.FilenameFilter;

import static com.android.karthi.androidtask.Const.Const.NOTIFY_OPEN_URL;
import static com.android.karthi.androidtask.Const.Const.NOTIFY_URL;

public class PhotoAcitivity extends AppCompatActivity {
    PhotoView photoView;
    private static final String TAG = "PhotoAcitivity";
    private ProgressBar rounProgress;
    String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        rounProgress = (ProgressBar) findViewById(R.id.progressBarRound);
        init();
    }
    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
        Log.d(TAG, "selectedImageURI ::  " +  getIntent().getStringExtra(NOTIFY_OPEN_URL));

        //code
    }
    private void init() {
        if (isStoragePermissionGranted()) {
            // array of supported extensions (use a List if you prefer)
            final String[] EXTENSIONS = new String[]{
                    "gif", "png", "bmp", "jpg" // and other formats you need
            };
            // filter to identify images based on their extensions
            final FilenameFilter IMAGE_FILTER = new FilenameFilter() {
                @Override
                public boolean accept(final File dir, final String name) {
                    for (final String ext : EXTENSIONS) {
                        if (name.endsWith("." + ext)) {
                            return (true);
                        }
                    }
                    return (false);
                }
            };

            photoView = (PhotoView) findViewById(R.id.imageView);
            String selectedImageURI;
            if (getIntent().getData() != null) {
                selectedImageURI = getIntent().getData().getPath();
            } else {
                selectedImageURI = getIntent().getStringExtra(NOTIFY_OPEN_URL);
            }
            Log.d(TAG, "selectedImageURI ::  " + selectedImageURI);

            path = selectedImageURI.substring(0, selectedImageURI.lastIndexOf("/"));
            Log.d(TAG, "selectedImageURI ::  " + selectedImageURI);
            File directory = new File(path);
            File[] files = directory.listFiles(IMAGE_FILTER);
            for (int i = 0; i < files.length; i++) {
                Log.d("Files", "FileName:" + files[i].getAbsolutePath());
            }

            Glide.with(this)
                    .load(selectedImageURI)
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .listener(new RequestListener<Drawable>() {
                        @Override

                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            rounProgress.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            rounProgress.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(photoView);
        }
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
            init();
        } else {
            finish();
        }
    }
}
