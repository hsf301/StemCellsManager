package com.huashengfu.StemCellsManager.adapter.goods.order.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToBeDeliveredOrdersDetailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_image)
    ImageView ivImage;

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_skuname)
    TextView tvSkuName;
    @BindView(R.id.tv_count)
    TextView tvCount;

    public ToBeDeliveredOrdersDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
