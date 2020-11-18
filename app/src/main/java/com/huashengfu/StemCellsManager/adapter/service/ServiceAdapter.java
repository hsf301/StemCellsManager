package com.huashengfu.StemCellsManager.adapter.service;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.service.Service;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends BaseAdapter<ServiceViewHolder> {

    private List<Service> services = new ArrayList<>();

    public void addAll(List<Service> collection) {
        this.services.addAll(collection);
    }

    public int remove(Service service){
        int position = services.indexOf(service);
        services.remove(service);
        return position;
    }

    public void update(Service service){
        for(Service tmp : services){
            if(service.getId() == tmp.getId()){
                tmp.setName(service.getName());
                tmp.setContent(service.getContent());
                return;
            }
        }
    }

    public void updateCover(Service service){
        for(Service tmp : services){
            if(service.getId() == tmp.getId()){
                tmp.setCover(service.getCover());
                return;
            }
        }
    }

    public void clear(){
        services.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service, parent, false);
        ServiceViewHolder viewHolder = new ServiceViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceViewHolder viewHolder = (ServiceViewHolder) holder;

        Service service = services.get(position);

        Glide.with(holder.itemView.getContext())
                .load(service.getCover())
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 5))
                .into(viewHolder.ivImage);

        viewHolder.tvTitle.setText(service.getName());
        viewHolder.tvContent.setText(service.getContent());
        viewHolder.tvView.setText(String.valueOf(service.getBrowseSum()));
        viewHolder.tvCount.setText(String.valueOf(service.getCollectionSum()));

        if(service.getStatus().equals(Constants.Status.Service.yes)) {
            viewHolder.ivMenu.setImageResource(R.mipmap.icon_menu_blue);
            viewHolder.btnOffline.setText(R.string.btn_offline_service);
        }else {
            viewHolder.ivMenu.setImageResource(R.mipmap.icon_menu_gray);
            viewHolder.btnOffline.setText(R.string.btn_online_service);
        }

        if(getOnItemClickListener() != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.itemView, service);
                }
            });

            viewHolder.btnOffline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnOffline, service);
                }
            });

            viewHolder.ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.ivMenu, service);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return services.size();
    }
}
