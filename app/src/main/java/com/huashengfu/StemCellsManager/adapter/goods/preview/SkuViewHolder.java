package com.huashengfu.StemCellsManager.adapter.goods.preview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SkuViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_image)
    ImageView ivImage;

    public SkuViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
