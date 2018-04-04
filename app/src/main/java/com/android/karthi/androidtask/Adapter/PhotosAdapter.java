package com.android.karthi.androidtask.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.karthi.androidtask.Activity.MainActivity;
import com.android.karthi.androidtask.Activity.ViewPagerActivity;
import com.android.karthi.androidtask.POJO.Result;
import com.android.karthi.androidtask.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.android.karthi.androidtask.Const.Const.My_INTENT;
import static com.android.karthi.androidtask.Const.Const.My_INTENT_POSITION;

/**
 * Created by Karthi on 27/3/2018.
 */


public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private ArrayList<Result> itemList;
    private Context context;
    private OnFragmentInteractionListener mListener;
    private static final String TAG = "PhotosAdapter";
    public PhotosAdapter(Context context, ArrayList<Result> itemList,OnFragmentInteractionListener mListener) {
        this.context = context;
        this.itemList = itemList;
        this. mListener = mListener;
    }
    //append photos to list
    public void addItems(List<Result> itemList) {
        for (Result result:itemList) {
            this.itemList.add(result);
            notifyDataSetChanged();
        }
    }
    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.photos_list_item, null);
        PhotoViewHolder rcv = new PhotoViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        Result result = itemList.get(position);
        holder.thumbnail.getLayoutParams().height = 350;
        //Glide library to load the image
        Glide.with(context)
                .load(result.getUrls().getSmall())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.thumbnail);
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ViewPagerActivity.class);
                Log.d(TAG, "onClick: "+itemList.size());
                intent.putExtra(My_INTENT,itemList);
                intent.putExtra(My_INTENT_POSITION,position);
                context.startActivity(intent);
            //    mListener.openFragment(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
    // clear photos from photos list
    public void removeItems() {
        this.itemList.clear();
        notifyDataSetChanged();
    }
    //initialize the adapter item views
    class PhotoViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnail;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.item_photo);
        }
    }
}