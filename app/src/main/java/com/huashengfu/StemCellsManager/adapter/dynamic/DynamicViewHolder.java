package com.huashengfu.StemCellsManager.adapter.dynamic;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DynamicViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_month)
    TextView tvMonth;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.tv_view)
    TextView tvView;
    @BindView(R.id.tv_read)
    TextView tvRead;
    @BindView(R.id.tv_comments)
    TextView tvComments;
    @BindView(R.id.rl_image)
    RelativeLayout rlImage;

    public DynamicViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
