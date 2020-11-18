package com.huashengfu.StemCellsManager.adapter.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.InteractionType;

import java.util.ArrayList;
import java.util.List;

public class InteractionTypeAdapter extends BaseAdapter<InteractionTypeViewHolder> {

    private List<InteractionType> interactionTypes = new ArrayList<>();

    private int select = 0;

    public void addAll(List<InteractionType> collection) {
        interactionTypes.addAll(collection);
    }

    public void add(InteractionType interactionType){
        interactionTypes.add(interactionType);
    }

    public int getSelect() {
        return select;
    }

    public void setSelect(int select) {
        this.select = select;
    }

    public int getItemPosition(InteractionType type){
        return interactionTypes.indexOf(type);
    }

    public void clear(){
        interactionTypes.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_interaction_type, parent, false);
        InteractionTypeViewHolder viewHolder = new InteractionTypeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        InteractionTypeViewHolder viewHolder = (InteractionTypeViewHolder) holder;

        InteractionType type = interactionTypes.get(position);

        viewHolder.tvName.setText(type.getName());

        if(select == position)
            Glide.with(holder.itemView.getContext())
                .load(type.getResSelectId())
                .into(viewHolder.ivIcon);
        else
            Glide.with(holder.itemView.getContext())
                    .load(type.getResId())
                    .into(viewHolder.ivIcon);

        if(getOnItemClickListener() != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select = position;
                    getOnItemClickListener().onItemClick(viewHolder.itemView, type);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return interactionTypes.size();
    }
}
