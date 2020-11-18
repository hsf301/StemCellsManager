package com.huashengfu.StemCellsManager.adapter.goods;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.service.ServicePromotionFlagViewHolder;
import com.huashengfu.StemCellsManager.entity.service.ServiceTypeGroup;

import java.util.ArrayList;
import java.util.List;

public class GoodsFlagAdapter extends BaseAdapter<GoodsFlagViewHolder> {

    private List<String> flags = new ArrayList<>();
    private ServiceTypeGroup serviceTypeGroup;
    private int select = -1;

    public void addAll(List<String> flags) {
        if(flags == null)
            return;
        this.flags.addAll(flags);
    }

    public void add(String flag){
        flags.add(flag);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_flag, parent, false);
        GoodsFlagViewHolder viewHolder = new GoodsFlagViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsFlagViewHolder viewHolder = (GoodsFlagViewHolder) holder;

        viewHolder.tvName.setText(flags.get(position));
        if(getOnItemLongClickListener() != null){
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return getOnItemLongClickListener().onItemLongClick(view, flags.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return flags.size();
    }
}
