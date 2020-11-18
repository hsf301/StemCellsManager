package com.huashengfu.StemCellsManager.adapter.service.abandonment;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceTypeChildViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.iv_select)
    ImageView ivSelect;

    public ServiceTypeChildViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
