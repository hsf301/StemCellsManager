package com.huashengfu.StemCellsManager.adapter.goods.type;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectTypeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;

    public SelectTypeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
