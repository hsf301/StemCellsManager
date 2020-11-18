package com.huashengfu.StemCellsManager.adapter.dynamic;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DynamicDetailViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ll_list)
    LinearLayout llList;
//    @BindView(R.id.rv_list)
//    RecyclerView rvList;

    public DynamicDetailPhotoAdapter adapter;

    public DynamicDetailViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

//        adapter = new DynamicDetailPhotoAdapter();
//        GridLayoutManager layoutManager = new GridLayoutManager(itemView.getContext(), 3);
//        rvList.setLayoutManager(layoutManager);
//        rvList.setAdapter(adapter);
    }
}
