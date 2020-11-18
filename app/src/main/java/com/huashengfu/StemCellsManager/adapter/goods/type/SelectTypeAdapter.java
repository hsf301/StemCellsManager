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

public class SelectTypeAdapter extends BaseAdapter<SelectTypeViewHolder> {

    private List<Type> types = new ArrayList<>();

    public void clear(){
        types.clear();
    }

    public void add(Type type){
        types.add(type);
    }

    public void addAll(List<Type> collection){
        types.addAll(collection);
    }

    public Type remove(){
        if(types.size() == 2){
            clear();
            return null;
        }else{
            types.remove(types.size() -1);
            types.remove(types.size() -1);

            int index = types.size() - 1;
            if(index < 0)
                return null;
            else
                return types.get(index);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_type_select, parent, false);
        SelectTypeViewHolder viewHolder = new SelectTypeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SelectTypeViewHolder viewHolder = (SelectTypeViewHolder) holder;

        viewHolder.tvName.setText(types.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return types.size();
    }
}
