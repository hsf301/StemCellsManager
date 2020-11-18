package com.huashengfu.StemCellsManager.adapter.interaction.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.activity.Activity;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ActivityAdapter extends BaseAdapter<ActivityViewHolder> {

    private List<Activity> activities = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");

    public void addAll(List<Activity> collection) {
        this.activities.addAll(collection);
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public int updateBanner(Activity activity){
        int position = -1;
        for(int i=0; i<activities.size(); i++){
            Activity tmp = activities.get(i);
            if(tmp.getId() == activity.getId()){
                tmp.setBanner(activity.getBanner());
                position = i;
                break;
            }
        }
        return position;
    }

    public int update(Activity activity){
        int position = -1;
        for(int i=0; i<activities.size(); i++){
            Activity tmp = activities.get(i);
            if(tmp.getId() == activity.getId()){
                tmp.setTitle(activity.getTitle());
                tmp.setSubTitle(activity.getSubTitle());
                tmp.setAddr(activity.getAddr());
                tmp.setStartDate(activity.getStartDate());
                tmp.setEndDate(activity.getEndDate());

                position = i;
                break;
            }
        }
        return position;
    }

    public int remove(Activity activity){
        int position = activities.indexOf(activity);
        activities.remove(activity);
        return position;
    }

    public void clear(){
        activities.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_activity, parent, false);
        ActivityViewHolder viewHolder = new ActivityViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ActivityViewHolder viewHolder = (ActivityViewHolder) holder;

        holder.setIsRecyclable(false);

        Activity activity = activities.get(position);

        Glide.with(holder.itemView.getContext())
                .load(activity.getBanner())
                .placeholder(R.drawable.image_loading_pic)
                .transform(new GlideRoundTransformation(holder.itemView.getContext(), 10))
                .into(viewHolder.ivImage);

        viewHolder.tvTitle.setText(activity.getTitle());
        viewHolder.tvContent.setText(activity.getSubTitle());
        viewHolder.tvAddress.setText(activity.getAddr());
        viewHolder.tvDate.setText(sdf.format(new Date(activity.getStartDate())) + " " + StringUtils.getWeek(activity.getStartDate()));
        viewHolder.tvEnrollment.setText(String.format(holder.itemView.getResources().getString(R.string.str_activity_enrollment),
                activity.getQuota() - activity.getSurplusQuota()));

        if(activity.getActivityStatus() == Constants.Status.Activity.notStarted)
            viewHolder.tvEnrollment.setVisibility(View.GONE);
        else
            viewHolder.tvEnrollment.setVisibility(View.VISIBLE);

        if(activity.getActivityStatus() == Constants.Status.Activity.finished){
            viewHolder.btnOffline.setVisibility(View.GONE);
            viewHolder.ivMenu.setImageResource(R.mipmap.icon_menu_gray);
        }else{
            viewHolder.btnOffline.setVisibility(View.VISIBLE);

            if(activity.getActivityStatus() == Constants.Status.Activity.progress) {
                viewHolder.ivMenu.setImageResource(R.mipmap.icon_menu_blue);
                viewHolder.btnOffline.setText(R.string.btn_activity_stop);
            }else if(activity.getActivityStatus() == Constants.Status.Activity.notStarted) {
                viewHolder.ivMenu.setImageResource(R.mipmap.icon_menu_blue);
                viewHolder.btnOffline.setText(R.string.btn_activity_starting);
            }
        }

        if(getOnItemClickListener() != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.itemView, activity);
                }
            });

            viewHolder.btnOffline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnOffline, activity);
                }
            });

            viewHolder.ivMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.ivMenu, activity);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }
}
