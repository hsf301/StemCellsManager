package com.huashengfu.StemCellsManager.adapter.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.CommodityConsultation;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class CommodityConsultationAdapter extends BaseAdapter<CommodityConsultationViewHolder> {

    private List<CommodityConsultation> commodityConsultations = new ArrayList<>();

    public void addAll(List<CommodityConsultation> collection) {
        commodityConsultations.addAll(collection);
    }

    public void clear(){
        commodityConsultations.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_interaction_commodity_consultation, parent, false);
        CommodityConsultationViewHolder viewHolder = new CommodityConsultationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommodityConsultationViewHolder viewHolder = (CommodityConsultationViewHolder) holder;

        CommodityConsultation commodityConsultation = commodityConsultations.get(position);

        viewHolder.tvReview.setText(String.format(holder.itemView.getResources().getString(R.string.str_interaction_consultation), "30"));

        Glide.with(holder.itemView.getContext())
                .load(R.mipmap.image_service_content)
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 10))
                .into(viewHolder.ivImage);

        if(getOnItemClickListener() != null){
            viewHolder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnView, commodityConsultation);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return commodityConsultations.size();
    }
}
