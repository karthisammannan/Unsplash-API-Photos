package com.android.karthi.androidtask.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.karthi.androidtask.POJO.Result;
import com.android.karthi.androidtask.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

/**
 * Created by Raghuvarma on 27/3/2018.
 */

public class PhotosAdapter extends RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder> {

    private List<Result> itemList;
    private Context context;

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public PhotosAdapter(Context context, List<Result> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

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
    public void onBindViewHolder(final PhotoViewHolder holder, int position) {
        Result result = itemList.get(position);
        holder.thumbnail.getLayoutParams().height = 350;

        Glide.with(context)
                .load(result.getUrls().getSmall())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public void removeItems() {
        this.itemList.clear();
        notifyDataSetChanged();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView thumbnail;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            thumbnail = (ImageView) itemView.findViewById(R.id.item_photo);
        }

        @Override
        public void onClick(View view) {
            //  Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}