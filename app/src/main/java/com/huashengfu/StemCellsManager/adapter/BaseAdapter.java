package com.huashengfu.StemCellsManager.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public OnItemLongClickListener getOnItemLongClickListener() {
        return onItemLongClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    public interface OnItemClickListener<T extends Object> {
        public void onItemClick(View view, T object);
    }

    public interface OnItemLongClickListener<T extends Object> {
        public boolean onItemLongClick(View view, T object);
    }

//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//
//    }

//    @Override
//    public abstract void onBindViewHolder(V holder, int position);
}
