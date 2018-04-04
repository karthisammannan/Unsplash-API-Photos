package com.android.karthi.androidtask.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
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

public class PhotoAcitivity extends AppCompatActivity {
    PhotoView photoView;
    private static final String TAG = "PhotoAcitivity";
    private ProgressBar rounProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        rounProgress = (ProgressBar) findViewById(R.id.progressBarRound);

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
        Uri selectedImageURI = getIntent().getData();
        String path = selectedImageURI.getPath().substring(0, selectedImageURI.getPath().lastIndexOf("/"));
        File directory = new File(path);
        File[] files = directory.listFiles(IMAGE_FILTER);
        for (int i = 0; i < files.length; i++) {
            Log.d("Files", "FileName:" + files[i].getAbsolutePath());
        }

        Log.d(TAG, "onCreate: " + path);
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
