package com.huashengfu.StemCellsManager.adapter.goods;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.view.FlowLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExpressViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;

    public ExpressViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
