package com.huashengfu.StemCellsManager.adapter.service.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceDiscountViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;

    public ServiceDiscountViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
