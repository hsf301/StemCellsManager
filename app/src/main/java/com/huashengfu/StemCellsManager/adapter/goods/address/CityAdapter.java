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

public class CityAdapter extends BaseAdapter<CityViewHolder> {

    private List<City> cities = new ArrayList<>();

    public void add(City city){
        cities.add(city);
    }

    public void clear(){
        cities.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_address_city, parent, false);
        CityViewHolder viewHolder = new CityViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CityViewHolder viewHolder = (CityViewHolder) holder;

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
