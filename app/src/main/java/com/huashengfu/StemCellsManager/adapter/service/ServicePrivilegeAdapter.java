package com.huashengfu.StemCellsManager.adapter.service;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.sms.Outline;
import com.huashengfu.StemCellsManager.entity.response.sms.OutlineResult;
import com.huashengfu.StemCellsManager.entity.service.Privilege;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class ServicePrivilegeAdapter extends BaseAdapter<ServicePrivilegeViewHolder> {

    private List<Outline> privileges = new ArrayList<>();
    private List<Outline> select = new ArrayList<>();

    public void addAll(List<Outline> collection, List<OutlineResult> results) {
        this.privileges.addAll(collection);

        for(OutlineResult result : results){
            for(Outline outline: privileges){
                if(result.getOid() == outline.getId()){
                    outline.setDesc(result.getDetails());
                    select.add(outline);
                }
            }
        }
    }

    public List<Outline> getSelect() {
        return select;
    }

    public void clear(){
        privileges.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_service_privilege, parent, false);
        ServicePrivilegeViewHolder viewHolder = new ServicePrivilegeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ServicePrivilegeViewHolder viewHolder = (ServicePrivilegeViewHolder) holder;

        Outline outline = privileges.get(position);

        Glide.with(holder.itemView.getContext())
                .load(outline.getIcon())
                .into(viewHolder.ivIcon);

        holder.setIsRecyclable(false);

        viewHolder.etDesc.setText(outline.getDesc());
        viewHolder.tvTitle.setText(outline.getName());
        viewHolder.tvContent.setText(outline.getContent());

        if(select.contains(outline)){
            viewHolder.ivSelect.setImageResource(R.mipmap.icon_check_select);
            viewHolder.etDesc.setVisibility(View.VISIBLE);
        }else{
            viewHolder.ivSelect.setImageResource(R.mipmap.icon_check_normal);
            viewHolder.etDesc.setVisibility(View.GONE);
        }

        viewHolder.etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                outline.setDesc(editable.toString());
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(select.contains(outline)){
                    select.remove(outline);
                }else{
                    select.add(outline);
                }

                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return privileges.size();
    }
}
