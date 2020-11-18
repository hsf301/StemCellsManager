package com.huashengfu.StemCellsManager.adapter.goods;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Express;

import java.util.ArrayList;
import java.util.List;

public class ExpressAdapter extends BaseAdapter<ExpressViewHolder> {

    private List<Express> expresses = new ArrayList<>();
    private int select = 0;

    public void addAll(List<Express> collection) {
        this.expresses.addAll(collection);
    }

    public Express getSelectExpress(){
        if(expresses.isEmpty())
            return null;

        return expresses.get(select);
    }

    public void clear(){
        expresses.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_express, parent, false);
        ExpressViewHolder viewHolder = new ExpressViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ExpressViewHolder viewHolder = (ExpressViewHolder) holder;

        Express express = expresses.get(position);

        viewHolder.tvName.setText(express.getCourierName());

        if(position == select){
            viewHolder.tvName.setBackgroundResource(R.drawable.bg_express);
            viewHolder.tvName.setTextAppearance(holder.itemView.getContext(), R.style.TextWhite12Sp);
        }else{
            viewHolder.tvName.setBackgroundResource(R.drawable.bg_express_normal);
            viewHolder.tvName.setTextAppearance(holder.itemView.getContext(), R.style.TextBlue12Sp);
        }

        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                notifyDataSetChanged();

                if(getOnItemClickListener() != null)
                    getOnItemClickListener().onItemClick(viewHolder.tvName, express);
            }
        });
    }

    @Override
    public int getItemCount() {
        return expresses.size();
    }
}
