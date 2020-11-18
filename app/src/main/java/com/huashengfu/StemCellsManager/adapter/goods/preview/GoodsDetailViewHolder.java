package com.huashengfu.StemCellsManager.adapter.goods.preview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsDetailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_desc)
    TextView tvDesc;

    public GoodsDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
