package com.huashengfu.StemCellsManager.adapter.interaction;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.ActivityRegistration;
import com.huashengfu.StemCellsManager.entity.interaction.ActivityRegistrationUser;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class ActivityRegistrationAdapter extends BaseAdapter<ActivityRegistrationViewHolder> {

    private List<ActivityRegistration> activityRegistrations = new ArrayList<>();

    public void addAll(List<ActivityRegistration> collection) {
        activityRegistrations.addAll(collection);
    }

    public void clear(){
        activityRegistrations.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_interaction_activity_registration, parent, false);
        ActivityRegistrationViewHolder viewHolder = new ActivityRegistrationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ActivityRegistrationViewHolder viewHolder = (ActivityRegistrationViewHolder) holder;

        ActivityRegistration activityRegistration = activityRegistrations.get(position);

        viewHolder.tvReview.setText(String.format(holder.itemView.getResources().getString(R.string.str_interaction_registration), activityRegistration.getAcount()));

        if(activityRegistration.hasUnRead())
            viewHolder.dot.setVisibility(View.VISIBLE);
        else
            viewHolder.dot.setVisibility(View.GONE);


        viewHolder.tvTitle.setText(activityRegistration.getAname());
        viewHolder.tvContent.setText(activityRegistration.getAsubtitle());

        Glide.with(holder.itemView.getContext())
                .load(activityRegistration.getAcover())
                .placeholder(R.drawable.image_loading_pic_small)
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 2))
                .into(viewHolder.ivImage);

        if(getOnItemClickListener() != null){
            viewHolder.btnView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnView, activityRegistration);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return activityRegistrations.size();
    }
}
