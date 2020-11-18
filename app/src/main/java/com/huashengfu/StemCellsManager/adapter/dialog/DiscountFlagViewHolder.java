package com.huashengfu.StemCellsManager.adapter.dialog;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DiscountFlagViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.cb_select)
    CheckBox cbSelect;

    public DiscountFlagViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
