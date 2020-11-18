package com.huashengfu.StemCellsManager.adapter.map;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_address)
    TextView tvAddress;

    public AddressHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
