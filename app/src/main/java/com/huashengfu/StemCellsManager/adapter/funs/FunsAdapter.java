package com.huashengfu.StemCellsManager.adapter.funs;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.funs.Funs;

import java.util.ArrayList;
import java.util.List;

public class FunsAdapter extends BaseAdapter<FunsViewHolder> {

    private List<Funs> funs = new ArrayList<>();

    public void addAll(List<Funs> flags) {
        if(flags == null)
            return;
        this.funs.addAll(flags);
    }

    public void clear(){
        funs.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_flag, parent, false);
        FunsViewHolder viewHolder = new FunsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FunsViewHolder viewHolder = (FunsViewHolder) holder;

        Funs fun = funs.get(position);

//        viewHolder.tvName.setText(funs.get(position));
        if(getOnItemLongClickListener() != null){
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return getOnItemLongClickListener().onItemLongClick(view, funs.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return funs.size();
    }
}
