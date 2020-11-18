package com.huashengfu.StemCellsManager.adapter.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommodityConsultationViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_review)
    TextView tvReview;
    @BindView(R.id.btn_view)
    Button btnView;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public CommodityConsultationViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
