package com.huashengfu.StemCellsManager.adapter.goods;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsDetailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.ll_menu)
    LinearLayout llMenu;
    @BindView(R.id.ll_up)
    LinearLayout llUp;
    @BindView(R.id.ll_down)
    LinearLayout llDown;
    @BindView(R.id.ll_delete)
    LinearLayout llDelete;
    @BindView(R.id.iv_up)
    ImageView ivUp;
    @BindView(R.id.iv_down)
    ImageView ivDown;

    public GoodsDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
