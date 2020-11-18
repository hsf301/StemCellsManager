package com.huashengfu.StemCellsManager.adapter.service.detail;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceIntroductionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_content)
    TextView tvContent;

    public ServiceIntroductionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
