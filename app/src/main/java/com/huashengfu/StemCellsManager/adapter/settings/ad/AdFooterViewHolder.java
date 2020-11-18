package com.huashengfu.StemCellsManager.adapter.settings.ad;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdFooterViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rl_add)
    RelativeLayout rlAdd;

    public AdFooterViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
