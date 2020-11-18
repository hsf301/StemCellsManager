package com.huashengfu.StemCellsManager.adapter.settings.customer.service;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TelePhoneViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.iv_modify)
    ImageView ivModify;

    public TelePhoneViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
