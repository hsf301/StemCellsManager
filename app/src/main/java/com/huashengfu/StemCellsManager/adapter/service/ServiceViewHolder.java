package com.huashengfu.StemCellsManager.adapter.service;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.btn_offline)
    Button btnOffline;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_view)
    TextView tvView;
    @BindView(R.id.tv_count)
    TextView tvCount;

    public ServiceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
