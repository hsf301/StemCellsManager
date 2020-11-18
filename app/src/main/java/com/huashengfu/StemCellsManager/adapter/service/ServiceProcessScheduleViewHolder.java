package com.huashengfu.StemCellsManager.adapter.service;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceProcessScheduleViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.et_name)
    TextView etName;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.ll_schedule)
    LinearLayout llSchedule;

    public ServiceProcessScheduleViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
