package com.huashengfu.StemCellsManager.adapter.interaction.qa;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceQuestionViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.btn_answer)
    public Button btnAnswer;
    @BindView(R.id.tv_content)
    TextView tvContent;

    public ServiceQuestionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
