package com.huashengfu.StemCellsManager.adapter.flag;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class FlagAdapter extends BaseAdapter<FlagViewHolder> {

    private List<String> flags = new ArrayList<>();

    public void addAll(List<String> flags) {
        if(flags == null)
            return;
        this.flags.addAll(flags);
    }

    public void add(String flag){
        flags.add(flag);
    }

    public List<String> getFlags() {
        return flags;
    }

    public int remove(String flag){
        int position = flags.indexOf(flag);
        flags.remove(flag);
        return position;
    }

    public void clear(){
        flags.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_flag, parent, false);
        FlagViewHolder viewHolder = new FlagViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FlagViewHolder viewHolder = (FlagViewHolder) holder;

        viewHolder.tvName.setText(flags.get(position));
        if(getOnItemLongClickListener() != null){
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return getOnItemLongClickListener().onItemLongClick(view, flags.get(position));
                }
            });
        }

        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getOnItemLongClickListener() != null)
                    getOnItemLongClickListener().onItemLongClick(view, flags.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return flags.size();
    }
}
