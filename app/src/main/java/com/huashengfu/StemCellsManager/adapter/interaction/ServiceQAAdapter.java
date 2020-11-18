package com.huashengfu.StemCellsManager.adapter.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.ServiceQA;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class ServiceQAAdapter extends BaseAdapter<ServiceQAViewHolder> {

    private List<ServiceQA> serviceQAS = new ArrayList<>();

    public void addAll(List<ServiceQA> collection) {
        serviceQAS.addAll(collection);
    }

    public void clear(){
        serviceQAS.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_interaction_service_qa, parent, false);
        ServiceQAViewHolder viewHolder = new ServiceQAViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceQAViewHolder viewHolder = (ServiceQAViewHolder) holder;

        ServiceQA serviceQA = serviceQAS.get(position);

        viewHolder.tvReview.setText(String.format(holder.itemView.getResources().getString(R.string.str_interaction_qa), serviceQA.getTopicSum()));
        viewHolder.tvTitle.setText(serviceQA.getName());
        viewHolder.tvContent.setText(serviceQA.getContent());

        if(serviceQA.getStatus().equals(Constants.Status.Comment.yes)){
            viewHolder.dot.setVisibility(View.GONE);
        }else{
            viewHolder.dot.setVisibility(View.VISIBLE);
        }

        Glide.with(holder.itemView.getContext())
                .load(serviceQA.getCover())
                .placeholder(R.drawable.image_loading_pic_small)
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 2))
                .into(viewHolder.ivImage);

        if(getOnItemClickListener() != null){
            viewHolder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnView, serviceQA);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return serviceQAS.size();
    }
}
