package com.huashengfu.StemCellsManager.adapter.map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.service.ServiceTypeGroup;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends BaseAdapter<AddressHolder> {

    private List<PoiItem> addresses = new ArrayList<>();
    private int select = 0;

    public void addAll(List<PoiItem> collection) {
        this.addresses.addAll(collection);
    }

    public PoiItem getSelect(){
        if(addresses.isEmpty())
            return null;
        else
            return addresses.get(select);
    }

    public void clear(){
        addresses.clear();
        select = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_address, parent, false);
        AddressHolder viewHolder = new AddressHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AddressHolder viewHolder = (AddressHolder) holder;

        PoiItem address = addresses.get(position);

        viewHolder.tvName.setText(address.getTitle());
        viewHolder.tvAddress.setText(
                address.getProvinceName() + address.getCityName() + address.getAdName()+
                address.getSnippet());

        if(select == position){
            viewHolder.ivIcon.setImageResource(R.mipmap.icon_location);
        }else {
            viewHolder.ivIcon.setImageResource(R.mipmap.icon_circle);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return addresses.size();
    }
}
