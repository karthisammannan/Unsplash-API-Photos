package com.android.karthi.androidtask.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.karthi.androidtask.POJO.Category;
import com.android.karthi.androidtask.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<Category> itemList;
    private final int CATEGORY_THRD_TYPE = 3;
    private final int CATEGORY_SEC_TYPE = 2;
    private final int CATEGORY_FRST_TYPE = 1;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView;
        switch (viewType) {
            case CATEGORY_FRST_TYPE: {
                layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_frst_screen_item, null);
                return new FirstVH(layoutView);
            }
            case CATEGORY_SEC_TYPE: {
                layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_screen_item, null);
                return new DefaultViewHolder(layoutView);
            }
            default: {
                layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_sec_screen_item, null);
                return new DefaultViewHolder(layoutView);
            }

        }
    }

    public CategoryAdapter(Context context, ArrayList<Category> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Category category = itemList.get(position);
        if (holder.getItemViewType() == CATEGORY_FRST_TYPE) {
            FirstVH firstVH = (FirstVH) holder;
            firstVH.frstvh_tvwCategoryName.setText(category.getCategoryName());
            //Glide library to load the image
            Glide.with(context)
                    .load(category.getCategoryImages().get(0))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(firstVH.frstvh_ivwCategory1);
        } else {
            DefaultViewHolder defaultViewHolder = (DefaultViewHolder) holder;

            defaultViewHolder.tvwCategoryName.setText(category.getCategoryName());
            //Glide library to load the image
            Glide.with(context)
                    .load(category.getCategoryImages().get(0))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(defaultViewHolder.ivwCategory1);
            Glide.with(context)
                    .load(category.getCategoryImages().get(1))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(defaultViewHolder.ivwCategory2);
            Glide.with(context)
                    .load(category.getCategoryImages().get(2))
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .into(defaultViewHolder.ivwCategory3);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return CATEGORY_FRST_TYPE;
        else if (position % 2 == 0)
            return CATEGORY_THRD_TYPE;
        else
            return CATEGORY_SEC_TYPE;
    }

    public class DefaultViewHolder extends RecyclerView.ViewHolder {
        public TextView tvwCategoryName;
        public ImageView ivwCategory1, ivwCategory2, ivwCategory3;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            tvwCategoryName = (TextView) itemView.findViewById(R.id.tvw_category_name);
            ivwCategory1 = (ImageView) itemView.findViewById(R.id.ivw_category1);
            ivwCategory2 = (ImageView) itemView.findViewById(R.id.ivw_category2);
            ivwCategory3 = (ImageView) itemView.findViewById(R.id.ivw_category3);
        }
    }

    public class FirstVH extends RecyclerView.ViewHolder {
        public TextView frstvh_tvwCategoryName;
        public ImageView frstvh_ivwCategory1;

        public FirstVH(View itemView) {
            super(itemView);
            frstvh_tvwCategoryName = (TextView) itemView.findViewById(R.id.tvw_category_name);
            frstvh_ivwCategory1 = (ImageView) itemView.findViewById(R.id.ivw_category1);

        }
    }
}
