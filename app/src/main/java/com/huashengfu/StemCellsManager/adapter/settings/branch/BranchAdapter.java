package com.huashengfu.StemCellsManager.adapter.settings.branch;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.settings.Branch;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class BranchAdapter extends BaseAdapter<BranchViewHolder> {

    private List<Branch> branches = new ArrayList<>();

    private Bundle savedInstanceState;
    public BranchAdapter(Bundle savedInstanceState){
        this.savedInstanceState = savedInstanceState;
    }

    public void add(Branch branch){
        branches.add(branches.size() - 1, branch);
    }

    public void addAll(List<Branch> collection){
        if(collection == null)
            return;
        branches.addAll(collection);
    }

    public int remove(Branch branch){
        int position = -1;
        for(int i=0;i<branches.size(); i++){
            if(branches.get(i).getId() == branch.getId()){
                position = i;
                break;
            }
        }

        branches.remove(position);
        return position;
    }

    public void clear(){
        branches.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_branch, parent, false);
        RecyclerView.ViewHolder holder = new BranchViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BranchViewHolder viewHolder = (BranchViewHolder) holder;

        Branch branch = branches.get(position);

        viewHolder.tvName.setText(branch.getName());
        viewHolder.tvAddress.setText(branch.getAddr());
        viewHolder.tvCity.setText("");
        viewHolder.tvCity.setVisibility(View.GONE);

        Glide.with(holder.itemView.getContext())
                .load(branch.getPic())
                .placeholder(R.drawable.image_loading_pic_small)
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 10))
                .into(viewHolder.ivImage);

        viewHolder.tvTime.setText(branch.getBusinessHours());
        viewHolder.tvPhone.setText(branch.getPhone());

        viewHolder.mapView.onCreate(savedInstanceState);

        AMap aMap = viewHolder.mapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setAllGesturesEnabled(false);

        aMap.clear();
        LatLng latLng = new LatLng(branch.getLatitude(), branch.getLongitude());
        aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker)));
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
        aMap.setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(getOnItemClickListener() != null){
                    getOnItemClickListener().onItemClick(viewHolder.itemView, branch);
                }
            }
        });

        viewHolder.tvdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getOnItemClickListener() != null){
                    getOnItemClickListener().onItemClick(view, branch);
                }
            }
        });
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getOnItemClickListener() != null){
                    getOnItemClickListener().onItemClick(view, branch);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }
}
