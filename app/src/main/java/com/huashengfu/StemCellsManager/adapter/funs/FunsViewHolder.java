package com.huashengfu.StemCellsManager.adapter.funs;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FunsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.ll_call)
    LinearLayout llCall;

    public FunsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
