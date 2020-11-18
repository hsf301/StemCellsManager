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

public class
ChildrenTypeAdapter extends BaseAdapter<ChildrenTypeViewHolder> {

    private List<Type> types;

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_type_children, parent, false);
        ChildrenTypeViewHolder viewHolder = new ChildrenTypeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ChildrenTypeViewHolder viewHolder = (ChildrenTypeViewHolder) holder;

        viewHolder.tvName.setText(types.get(position).getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getOnItemClickListener() != null){
                    getOnItemClickListener().onItemClick(view, types.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
}
