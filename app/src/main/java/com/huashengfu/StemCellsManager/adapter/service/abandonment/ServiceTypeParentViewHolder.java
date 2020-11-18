package com.huashengfu.StemCellsManager.adapter.service.abandonment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ServiceTypeParentViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    public ServiceTypeChildAdapter adapter;

    public ServiceTypeParentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        adapter = new ServiceTypeChildAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(itemView.getContext(), 3);
        rvList.setLayoutManager(gridLayoutManager);
        rvList.setAdapter(adapter);
    }
}
