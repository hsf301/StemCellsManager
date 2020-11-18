package com.huashengfu.StemCellsManager.adapter.settings.enterprise;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EnterpriseTypeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_select)
    ImageView ivSelect;

    public EnterpriseTypeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
