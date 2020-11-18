package com.huashengfu.StemCellsManager.adapter.goods.address;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.goods.City;

import java.util.ArrayList;
import java.util.List;

public class HotCityAdapter extends BaseAdapter<HotCityViewHolder> {

    private List<City> cities;

    public List<City> getCities() {
        return cities;
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_address_hot, parent, false);
        HotCityViewHolder viewHolder = new HotCityViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        HotCityViewHolder viewHolder = (HotCityViewHolder) holder;

        viewHolder.tvName.setText(cities.get(position).getCityName());
        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getOnItemClickListener()!= null){
                    getOnItemClickListener().onItemClick(view, cities.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }
}
