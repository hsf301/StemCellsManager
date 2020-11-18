package com.huashengfu.StemCellsManager.adapter.goods.address;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HotCityViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;

    public HotCityViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
