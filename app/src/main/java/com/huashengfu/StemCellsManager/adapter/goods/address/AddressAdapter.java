package com.huashengfu.StemCellsManager.adapter.goods.address;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.goods.City;
import com.huashengfu.StemCellsManager.entity.response.goods.Province;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends BaseAdapter<AddressViewHolder> {

    private List<Province> provinces = new ArrayList<>();
    private List<City> cities = new ArrayList<>();

    public AddressAdapter(List<Province> provinces, List<City> cities){
        this.provinces.addAll(provinces);
        this.cities.addAll(cities);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_address, parent, false);
        AddressViewHolder viewHolder = new AddressViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AddressViewHolder viewHolder = (AddressViewHolder) holder;

        viewHolder.hotCityAdapter.setCities(cities);
        viewHolder.hotCityAdapter.notifyDataSetChanged();

        viewHolder.provinceAdapter.setProvinces(provinces);
        viewHolder.provinceAdapter.notifyDataSetChanged();

        viewHolder.hotCityAdapter.setOnItemClickListener(getOnItemClickListener());
        viewHolder.provinceAdapter.setOnItemClickListener(getOnItemClickListener());
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
