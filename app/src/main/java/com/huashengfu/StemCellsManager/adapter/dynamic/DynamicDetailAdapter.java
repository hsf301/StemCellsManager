package com.huashengfu.StemCellsManager.adapter.dynamic;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.dynamic.DynamicDetail;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DynamicDetailAdapter extends BaseAdapter<DynamicDetailViewHolder> {

    private List<DynamicDetail> dynamicDetails = new ArrayList<>();
    private List<DynamicDetail> dynamic = new ArrayList<>();

    public void addAll(List<DynamicDetail> collection) {
        this.dynamicDetails.addAll(collection);
    }

    public void addContent(DynamicDetail dynamicDetail){
        dynamic.add(dynamicDetail);
    }

    public void add(DynamicDetail dynamicDetail){
        dynamicDetails.add(dynamicDetail);
    }

    public void clear(){
        dynamic.clear();
        dynamicDetails.clear();
    }

    public List<DynamicDetail> getDynamicDetails() {
        return dynamicDetails;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_dynamic_detail, parent, false);
        DynamicDetailViewHolder holder = new DynamicDetailViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DynamicDetailViewHolder viewHolder = (DynamicDetailViewHolder)holder;

        DynamicDetail dynamicDetail = dynamic.get(position);

        viewHolder.tvContent.setText(dynamicDetail.getContent());
        if(StringUtils.isNullOrBlank(dynamicDetail.getContent()))
            viewHolder.tvContent.setVisibility(View.GONE);
        else
            viewHolder.tvContent.setVisibility(View.VISIBLE);

        viewHolder.llList.removeAllViews();
        int col = 3;
        int row = dynamicDetails.size() % col == 0 ? dynamicDetails.size() / col : dynamicDetails.size() / col + 1;
        for(int i=0; i<row; i++){
            LinearLayout ll = new LinearLayout(holder.itemView.getContext());
            ll.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(lp);

            for(int j=0; j<col; j++){
                View view = LayoutInflater.from(holder.itemView.getContext()).inflate(R.layout.adapter_dynamic_detail_photo, null);
                if(i*col + j < dynamicDetails.size()){
                    DynamicDetail dynamic = dynamicDetails.get(i*col + j);

                    ImageView ivImage = view.findViewById(R.id.iv_image);
                    ImageView ivPlay = view.findViewById(R.id.iv_play);

                    Glide.with(viewHolder.itemView.getContext())
                            .load(dynamic.getUrl())
                            .placeholder(R.drawable.image_loading_pic)
                            .transform(new GlideRoundTransformation(holder.itemView.getContext(), 5))
                            .into(ivImage);

                    if(dynamic.isVideo()){
                        ivPlay.setVisibility(View.VISIBLE);
                    }else{
                        ivPlay.setVisibility(View.GONE);
                    }

                    if(getOnItemClickListener() != null){
                        ivPlay.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getOnItemClickListener().onItemClick(ivPlay, dynamic);
                            }
                        });

                        ivImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                getOnItemClickListener().onItemClick(ivImage, dynamic);
                            }
                        });
                    }
                }else{
                    view.setVisibility(View.INVISIBLE);
                }
                LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
                lp1.weight = 1;
                ll.addView(view, lp1);
            }
            viewHolder.llList.addView(ll);
        }



//        viewHolder.adapter.setOnItemClickListener(getOnItemClickListener());
//        viewHolder.adapter.clear();
//        viewHolder.adapter.addAll(dynamicDetails);
//        viewHolder.adapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dynamic.size();
    }
}
