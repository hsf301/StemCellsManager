package com.huashengfu.StemCellsManager.adapter.dynamic;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DynamicDetailPhotoViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.iv_play)
    ImageView ivPlay;

    public DynamicDetailPhotoViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
