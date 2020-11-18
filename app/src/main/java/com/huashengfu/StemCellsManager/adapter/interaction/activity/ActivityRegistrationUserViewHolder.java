package com.huashengfu.StemCellsManager.adapter.interaction.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityRegistrationUserViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.btn_call)
    Button btnCall;
    @BindView(R.id.dot)
    View dot;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_number)
    TextView tvNumber;

    public ActivityRegistrationUserViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
