package com.android.karthi.androidtask.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import static com.android.karthi.androidtask.Const.Const.My_INTENT_ARG_POSITION;

public class PhotoFragment extends Fragment {
    PhotoView imageView;
    private ProgressBar rounProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_photo, container, false);
        imageView = (PhotoView)view.findViewById(R.id.imageView);
        rounProgress = (ProgressBar) view.findViewById(R.id.progressBarRound);

        //Glide library to load the image
        Glide.with(this)
                .load(getArguments().getString(My_INTENT_ARG_POSITION))
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
                .into(imageView);
        return view;
    }

}
