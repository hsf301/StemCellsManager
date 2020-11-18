package com.huashengfu.StemCellsManager.adapter.flag;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FlagViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;

    public FlagViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
