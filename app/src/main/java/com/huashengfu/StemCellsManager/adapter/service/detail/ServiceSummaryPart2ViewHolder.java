package com.huashengfu.StemCellsManager.adapter.service.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceSummaryPart2ViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    public ServiceSummaryPart2ViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
