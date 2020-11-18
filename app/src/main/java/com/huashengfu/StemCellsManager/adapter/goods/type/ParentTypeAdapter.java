package com.huashengfu.StemCellsManager.adapter.goods.type;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.goods.Type;

import java.util.ArrayList;
import java.util.List;

public class ParentTypeAdapter extends BaseAdapter<ParentTypeViewHolder> {

    private List<Type> types = new ArrayList<>();
    private int select = -1;

    public void clear(){
        select = -1;
        types.clear();
    }

    public void addAll(List<Type> collection){
        types.addAll(collection);
    }

    public void add(Type type){
        types.add(type);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_type_parent, parent, false);
        ParentTypeViewHolder viewHolder = new ParentTypeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ParentTypeViewHolder viewHolder = (ParentTypeViewHolder) holder;

        Type type = types.get(position);
        viewHolder.tvName.setText(types.get(position).getName());

        if(select == position){
            viewHolder.rvList.setVisibility(View.VISIBLE);
            viewHolder.ivIcon.setImageResource(R.mipmap.icon_arrow_up_gray);
        }else{
            viewHolder.rvList.setVisibility(View.GONE);
            viewHolder.ivIcon.setImageResource(R.mipmap.icon_arrow_down_gray);
        }

        viewHolder.rlType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(select == position)
                    select = -1;
                else
                    select = position;
                notifyDataSetChanged();
            }
        });

        viewHolder.childrenTypeAdapter.setTypes(type.getChildren());
        viewHolder.childrenTypeAdapter.setOnItemClickListener(getOnItemClickListener());
        viewHolder.childrenTypeAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
}
