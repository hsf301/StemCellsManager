package com.huashengfu.StemCellsManager.adapter.goods.address;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProvinceViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_name)
    TextView tvName;
//    @BindView(R.id.rv_list)
//    RecyclerView rvList;
//
//    public CityAdapter cityAdapter;

    public ProvinceViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

//        cityAdapter = new CityAdapter(listener);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
//        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        rvList.setLayoutManager(layoutManager);
//        rvList.setAdapter(cityAdapter);
    }
}
