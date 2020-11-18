package com.huashengfu.StemCellsManager.adapter.dialog;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.service.ServiceTypeGroup;

import java.util.ArrayList;
import java.util.List;

public class DiscountFlagAdapter extends BaseAdapter<DiscountFlagViewHolder> {

    private List<String> flags = new ArrayList<>();
    private List<String> select = new ArrayList<>();

    public void addAll(List<String> flags) {
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

    public List<String> getSelect() {
        return select;
    }

    public void clear(){
        flags.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_discount_flags, parent, false);
        DiscountFlagViewHolder viewHolder = new DiscountFlagViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DiscountFlagViewHolder viewHolder = (DiscountFlagViewHolder) holder;

        viewHolder.tvName.setText(flags.get(position));
        if(select.contains(flags.get(position))){
            viewHolder.cbSelect.setChecked(true);
        }else{
            viewHolder.cbSelect.setChecked(false);
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(select.contains(flags.get(position)))
                    select.remove(flags.get(position));
                else
                    select.add(flags.get(position));

                notifyDataSetChanged();
            }
        });

        viewHolder.cbSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(select.contains(flags.get(position)))
                    select.remove(flags.get(position));
                else
                    select.add(flags.get(position));

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return flags.size();
    }
}
