package com.huashengfu.StemCellsManager.adapter.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.ServiceConsultation;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class ServiceConsultationAdapter extends BaseAdapter<ServiceConsultationViewHolder> {

    private List<ServiceConsultation> serviceConsultations = new ArrayList<>();

    public void addAll(List<ServiceConsultation> collection) {
        serviceConsultations.addAll(collection);
    }

    public void clear(){
        serviceConsultations.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_interaction_service_consultation, parent, false);
        ServiceConsultationViewHolder viewHolder = new ServiceConsultationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceConsultationViewHolder viewHolder = (ServiceConsultationViewHolder) holder;

        ServiceConsultation serviceConsultation = serviceConsultations.get(position);

        viewHolder.tvReview.setText(String.format(holder.itemView.getResources().getString(R.string.str_interaction_consultation), serviceConsultation.getScount()));

        viewHolder.tvTitle.setText(serviceConsultation.getSname());
        viewHolder.tvContent.setText(serviceConsultation.getScontent());

        if(serviceConsultation.hasUnRead())
            viewHolder.dot.setVisibility(View.VISIBLE);
        else
            viewHolder.dot.setVisibility(View.GONE);

        Glide.with(holder.itemView.getContext())
                .load(serviceConsultation.getScover())
                .placeholder(R.drawable.image_loading_pic_small)
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 2))
                .into(viewHolder.ivImage);

        if(getOnItemClickListener() != null){
            viewHolder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnView, serviceConsultation);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return serviceConsultations.size();
    }
}
