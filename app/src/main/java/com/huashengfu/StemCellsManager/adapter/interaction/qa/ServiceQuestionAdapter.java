package com.huashengfu.StemCellsManager.adapter.interaction.qa;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.ServiceQuestion;
import com.huashengfu.StemCellsManager.utils.GlideCircleTransformation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ServiceQuestionAdapter extends BaseAdapter<ServiceQuestionViewHolder> {

    private List<ServiceQuestion> serviceQuestions = new ArrayList<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public void addAll(List<ServiceQuestion> collection) {
        serviceQuestions.addAll(collection);
    }

    public int updateQuestionStatus(ServiceQuestion question){
        int position = 0;
        for(int i=0; i<serviceQuestions.size(); i++){
            ServiceQuestion tmp = serviceQuestions.get(i);
            if(tmp.getId() == question.getId()){
                tmp.setStatus(question.getStatus());
                position = i;
                break;
            }
        }
        return position;
    }

    public void clear(){
        serviceQuestions.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_interaction_service_qa_user, parent, false);
        ServiceQuestionViewHolder viewHolder = new ServiceQuestionViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServiceQuestionViewHolder viewHolder = (ServiceQuestionViewHolder) holder;

        ServiceQuestion serviceQuestion = serviceQuestions.get(position);

        if(serviceQuestion.getStatus().equals(Constants.Status.Topic.yes)){
            viewHolder.btnAnswer.setEnabled(false);
            viewHolder.btnAnswer.setText(R.string.str_answer);
            viewHolder.btnAnswer.setBackgroundResource(R.drawable.btn_gray_full);
        }else{
            viewHolder.btnAnswer.setEnabled(true);
            viewHolder.btnAnswer.setText(R.string.btn_answer);
            viewHolder.btnAnswer.setBackgroundResource(R.drawable.btn_blue_full);
        }

        viewHolder.tvDate.setText(sdf.format(new Date(serviceQuestion.getCreateTime())));
        viewHolder.tvName.setText(serviceQuestion.getUname());
        viewHolder.tvContent.setText(serviceQuestion.getTopic());

        Glide.with(holder.itemView.getContext())
                .load(serviceQuestion.getUheadPicUrl())
                .placeholder(R.mipmap.icon_header_default)
                .transform(new GlideCircleTransformation(holder.itemView.getContext(), 100))
                .into(viewHolder.ivHeader);

        if(getOnItemClickListener() != null){
            viewHolder.btnAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.btnAnswer, serviceQuestion);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return serviceQuestions.size();
    }
}
