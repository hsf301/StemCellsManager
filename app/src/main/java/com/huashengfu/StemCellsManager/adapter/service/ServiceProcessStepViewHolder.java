package com.huashengfu.StemCellsManager.adapter.service;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceProcessStepViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.et_title)
    TextView etTitle;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.ll_step)
    LinearLayout llStep;

    public ServiceProcessScheduleAdapter adapter = new ServiceProcessScheduleAdapter();

    public ServiceProcessStepViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setNestedScrollingEnabled(false);
        rvList.setAdapter(adapter);
    }

}
