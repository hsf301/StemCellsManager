package com.huashengfu.StemCellsManager.adapter.goods.type;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ParentTypeViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.rl_type)
    RelativeLayout rlType;

    public ChildrenTypeAdapter childrenTypeAdapter = new ChildrenTypeAdapter();

    public ParentTypeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(childrenTypeAdapter);
    }
}
