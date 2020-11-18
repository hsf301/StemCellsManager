package com.huashengfu.StemCellsManager.adapter.interaction.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.ActivityRegistrationUser;
import com.huashengfu.StemCellsManager.utils.GlideCircleTransformation;
import com.huashengfu.StemCellsManager.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityRegistrationUserAdapter extends BaseAdapter<ActivityRegistrationUserViewHolder> {

    private List<ActivityRegistrationUser> activityRegistrationUsers = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void addAll(List<ActivityRegistrationUser> collection) {
        if(collection == null)
            return;
        activityRegistrationUsers.addAll(collection);
    }

    public void clear(){
        activityRegistrationUsers.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_interaction_activity_registration_user, parent, false);
        ActivityRegistrationUserViewHolder viewHolder = new ActivityRegistrationUserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ActivityRegistrationUserViewHolder viewHolder = (ActivityRegistrationUserViewHolder) holder;

        ActivityRegistrationUser activityRegistrationUser = activityRegistrationUsers.get(position);

        if(activityRegistrationUser.getStatus().equals(Constants.Status.Activity.yes)){
            viewHolder.dot.setVisibility(View.GONE);
        }else{
            viewHolder.dot.setVisibility(View.VISIBLE);
        }

        viewHolder.tvDate.setText(sdf.format(new Date(activityRegistrationUser.getCreateTime())));
        viewHolder.tvNumber.setText("共" + activityRegistrationUser.getApplySum() + "人");
        viewHolder.tvName.setText(activityRegistrationUser.getName());
        viewHolder.tvSex.setText(StringUtils.getSex(activityRegistrationUser.getSex()));

        Glide.with(holder.itemView.getContext())
                .load(R.mipmap.image_header)
                .transform(new GlideCircleTransformation(holder.itemView.getContext(), 100))
                .into(viewHolder.ivHeader);

        if(getOnItemClickListener() != null){
            viewHolder.btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnCall, activityRegistrationUser);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return activityRegistrationUsers.size();
    }
}
