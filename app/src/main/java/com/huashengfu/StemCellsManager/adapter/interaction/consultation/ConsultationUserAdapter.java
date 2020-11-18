package com.huashengfu.StemCellsManager.adapter.interaction.consultation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.ConsultationUser;
import com.huashengfu.StemCellsManager.utils.GlideCircleTransformation;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConsultationUserAdapter extends BaseAdapter<ConsultationUserViewHolder> {

    private List<ConsultationUser> consultationUsers = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void addAll(List<ConsultationUser> collection) {
        consultationUsers.addAll(collection);
    }

    public void clear(){
        consultationUsers.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_interaction_service_consultation_user, parent, false);
        ConsultationUserViewHolder viewHolder = new ConsultationUserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ConsultationUserViewHolder viewHolder = (ConsultationUserViewHolder) holder;

        ConsultationUser consultationUser = consultationUsers.get(position);

        if(consultationUser.getStatus() == Constants.Status.Comment.processed){
            viewHolder.dot.setVisibility(View.GONE);
        }else{
            viewHolder.dot.setVisibility(View.VISIBLE);
        }

        viewHolder.tvDate.setText(sdf.format(new Date(consultationUser.getCreateTime())));
        viewHolder.tvAge.setText(String.valueOf(consultationUser.getAge()));
        viewHolder.tvSex.setText(StringUtils.getSex(consultationUser.getSex()));
        viewHolder.tvName.setText(consultationUser.getApplicant());
        viewHolder.tvContent.setText(consultationUser.getMedicalHistory());

        Glide.with(holder.itemView.getContext())
                .load(consultationUser.getHeadPic())
                .placeholder(R.mipmap.icon_header_default)
                .transform(new GlideCircleTransformation(holder.itemView.getContext(), 100))
                .into(viewHolder.ivHeader);

        if(getOnItemClickListener() != null){
            viewHolder.btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnCall, consultationUser);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return consultationUsers.size();
    }
}
