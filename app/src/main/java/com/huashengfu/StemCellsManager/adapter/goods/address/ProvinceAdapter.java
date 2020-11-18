package com.huashengfu.StemCellsManager.adapter.goods.address;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.goods.City;
import com.huashengfu.StemCellsManager.entity.response.goods.Province;

import java.util.ArrayList;
import java.util.List;

public class ProvinceAdapter extends BaseAdapter<ProvinceViewHolder> {

    private List<Province> provinces;// = new ArrayList<>();

    private final int TYPE_PROVINCE = 1;
    private final int TYPE_CITY = 2;

    private int selectPosition = -1;

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    public Pair<Province, City> getAddress(){
        int start = 0;
        Province province = null;
        for(int i=0;i<provinces.size(); i++){
            if(selectPosition > start && selectPosition <= start + provinces.get(i).getCityList().size() + 1){
                province = provinces.get(i);
                break;
            }

            start += provinces.get(i).getCityList().size() + 1;
        }

        if(province != null){
            City city = province.getCityList().get(selectPosition - start - 1);
            return new Pair<>(province, city);
        }

        return null;
    }

    private boolean isProvince(int position){
        int start = 0;
        for(int i=0;i<provinces.size(); i++){
            if(position == start)
                return true;

            start += provinces.get(i).getCityList().size() + 1;
        }
        return false;
    }

    private Province getProvince(int position){
        int start = 0;
        for(int i=0;i<provinces.size(); i++){
            if(position == start)
                return provinces.get(i);

            start += provinces.get(i).getCityList().size() + 1;
        }
        return null;
    }

    private City getCity(int position){
        int start = 0;
        Province province = null;
        for(int i=0;i<provinces.size(); i++){
            if(position > start && position <= start + provinces.get(i).getCityList().size() + 1){
                province = provinces.get(i);
                break;
            }

            start += provinces.get(i).getCityList().size() + 1;
        }

        if(province != null){
            return province.getCityList().get(position - start - 1);
        }

        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if(isProvince(position))
            return TYPE_PROVINCE;

        return TYPE_CITY;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;

        if(viewType == TYPE_CITY){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_address_city, parent, false);
            holder = new CityViewHolder(view);
        }else if(viewType == TYPE_PROVINCE){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_address_province, parent, false);
            holder = new ProvinceViewHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_PROVINCE){
            Province province = getProvince(position);
            ProvinceViewHolder viewHolder = (ProvinceViewHolder) holder;
            viewHolder.tvName.setText(province.getProvinceName());
        }else{
            City city = getCity(position);
            CityViewHolder viewHolder = (CityViewHolder) holder;
            viewHolder.tvName.setText(city.getCityName());

//            if(selectPosition == position){
//                viewHolder.ivSelect.setVisibility(View.VISIBLE);
//            }else{
//                viewHolder.ivSelect.setVisibility(View.INVISIBLE);
//            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectPosition = position;

                    if(getOnItemClickListener() != null){
                        getOnItemClickListener().onItemClick(view, city);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        for(Province province : provinces)
            count += province.getCityList().size();

        return provinces.size() + count;
    }

}
