package com.huashengfu.StemCellsManager.adapter.goods.address;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddressViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rv_hot)
    RecyclerView rvHot;
    @BindView(R.id.rv_province)
    RecyclerView rvProvince;

    public HotCityAdapter hotCityAdapter = new HotCityAdapter();
    public ProvinceAdapter provinceAdapter = new ProvinceAdapter();

    public AddressViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(itemView.getContext(), 4);
        rvHot.setLayoutManager(gridLayoutManager);
        rvHot.setAdapter(hotCityAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(itemView.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvProvince.setLayoutManager(linearLayoutManager);
        rvProvince.setAdapter(provinceAdapter);
    }
}
