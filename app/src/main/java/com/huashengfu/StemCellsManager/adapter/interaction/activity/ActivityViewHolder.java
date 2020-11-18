package com.huashengfu.StemCellsManager.adapter.interaction.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.tv_content)
    public TextView tvContent;
    @BindView(R.id.iv_image)
    public ImageView ivImage;
    @BindView(R.id.btn_offline)
    Button btnOffline;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_date)
    public TextView tvDate;
    @BindView(R.id.tv_address)
    public TextView tvAddress;
    @BindView(R.id.tv_enrollment)
    TextView tvEnrollment;

    public ActivityViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
