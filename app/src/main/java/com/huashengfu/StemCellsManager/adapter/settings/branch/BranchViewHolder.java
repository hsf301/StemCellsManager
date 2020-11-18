package com.huashengfu.StemCellsManager.adapter.settings.branch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BranchViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_delete)
    TextView tvdelete;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_phone)
    TextView tvPhone;

    public BranchViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
