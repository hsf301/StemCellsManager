package com.huashengfu.StemCellsManager.adapter.dynamic;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.dynamic.Detail;
import com.huashengfu.StemCellsManager.entity.dynamic.Dynamic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DynamicAdapter extends BaseAdapter<DynamicViewHolder> {

    private List<Dynamic> dynamics = new ArrayList<>();

    private SimpleDateFormat sdfMonth = new SimpleDateFormat("MM月dd日");
    private SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy年");

    public void addAll(List<Dynamic> collection) {
        this.dynamics.addAll(collection);
    }

    public void add(Dynamic dynamic){
        dynamics.add(dynamic);
    }

    public int remove(Dynamic dynamic){
        int position = -1;

        for(int i=0; i<dynamics.size(); i++){
            Dynamic tmp = dynamics.get(i);
            if(tmp.getId() == dynamic.getId()){
                position = i;
                break;
            }
        }

        if(position >= 0)
            dynamics.remove(dynamic);

        return position;
    }

    public void clear(){
        dynamics.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dynamic, parent, false);
        DynamicViewHolder viewHolder = new DynamicViewHolder(view);
        return viewHolder;
    }

    @SuppressLint("StringFormatMatches")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DynamicViewHolder viewHolder = (DynamicViewHolder) holder;

        Dynamic dynamic = dynamics.get(position);

        viewHolder.tvMonth.setText(sdfMonth.format(new Date(dynamic.getCreateTime())));
        viewHolder.tvYear.setText(sdfYear.format(new Date(dynamic.getCreateTime())));
        viewHolder.tvContent.setText(dynamic.getSubTitle());

        viewHolder.tvView.setText(
                String.format(viewHolder.itemView.getResources().getString(R.string.str_dynamic_adapter_1), dynamic.getForwardSum())
        );
        viewHolder.tvRead.setText(
                String.format(viewHolder.itemView.getResources().getString(R.string.str_dynamic_adapter_2), dynamic.getBrowseSum())
        );
        viewHolder.tvComments.setText(
                String.format(viewHolder.itemView.getResources().getString(R.string.str_dynamic_adapter_3), dynamic.getCommentSum())
        );

        if(dynamic.getDetailsList() == null){
            viewHolder.rlImage.setVisibility(View.GONE);
        }else{
            if(dynamic.getDetailsList().isEmpty()){
                viewHolder.rlImage.setVisibility(View.GONE);
            }else{
                viewHolder.rlImage.setVisibility(View.VISIBLE);

                boolean video = false;
                boolean showImage = false;

                for(Detail detail : dynamic.getDetailsList()){
                    if(detail.getType().equals(Constants.Type.Detail.pic)){
                        if(showImage)
                            continue;

                        Glide.with(viewHolder.itemView.getContext())
                                .load(detail.getThumbnail())
                                .placeholder(R.drawable.image_loading_pic)
                                .error(R.drawable.image_loading_pic)
                                .into(viewHolder.ivImage);

                        showImage = true;
                    }else if(detail.getType().equals(Constants.Type.Detail.video)){
                        video = true;
                    }
                }

                if(video){
                    viewHolder.ivPlay.setVisibility(View.VISIBLE);
                }else{
                    viewHolder.ivPlay.setVisibility(View.GONE);
                }
            }
        }


        if(getOnItemClickListener() != null){
            viewHolder.ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.ivPlay, dynamic);
                }
            });

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getOnItemClickListener().onItemClick(viewHolder.itemView, dynamic);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return dynamics.size();
    }
}
