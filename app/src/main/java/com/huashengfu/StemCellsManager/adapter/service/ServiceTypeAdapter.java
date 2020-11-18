package com.huashengfu.StemCellsManager.adapter.service;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.sms.init.Type;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceTypeAdapter extends BaseAdapter<ServiceTypeViewHolder> {

    private List<Type> types = new ArrayList<>();
    private int select = 0;

    public void addAll(List<Type> collection){
        types.addAll(collection);
    }

    public void addAll(List<Type> collection, int childrenId){
        types.addAll(collection);

        findTree(collection, childrenId);
        for(int i=0; i<types.size(); i++){
            Type type = types.get(i);
            for(Type tmp : tree){
                if(type.getId() == tmp.getId()){
                    select = i;
                    return;
                }
            }
        }
    }

    private List<Type> tree = new ArrayList<>();
    // 找出目标节点及所有父节点
    private void findTree(List<Type> list, int id){
        for(Type type : list){
            if(type.getId() == id) {
                // 保存到tree里
                tree.add(0, type);

                // 不是跟节点，继续查找上一级
                if(!type.isRoot())
                    findTree(types, type.getPid());
            }

            findTree(type.getChildren(), id);
        }
    }

    public Type getSelect(){
        if(select < 0 || types.isEmpty())
            return null;
        else
            return types.get(select);
    }

    public void clear(){
        types.clear();
        select = 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_type, parent, false);
        ServiceTypeViewHolder viewHolder = new ServiceTypeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceTypeViewHolder viewHolder = (ServiceTypeViewHolder) holder;

        Type type = types.get(position);
        viewHolder.tvName.setText(type.getName());
        Glide.with(holder.itemView.getContext())
                .load(type.getIcon())
                .into(viewHolder.ivIcon);

        if(select == position){
            viewHolder.ivSelect.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ivSelect.setVisibility(View.GONE);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                notifyDataSetChanged();

                if(getOnItemClickListener() != null)
                    getOnItemClickListener().onItemClick(viewHolder.itemView, type);
            }
        });
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
}
