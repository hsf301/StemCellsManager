package com.huashengfu.StemCellsManager.adapter.settings.enterprise;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.EnterpriseType;

import java.util.ArrayList;
import java.util.List;

public class EnterpriseTypeAdapter extends BaseAdapter<EnterpriseTypeViewHolder> {

    private List<EnterpriseType> enterpriseTypes = new ArrayList<>();

    private int select = 0;

    public void addAll(List<EnterpriseType> collection) {
        if(collection == null)
            return;
        this.enterpriseTypes.addAll(collection);
    }

    public void add(EnterpriseType enterpriseType){
        enterpriseTypes.add(enterpriseType);
    }

    public EnterpriseType getSelect(){
        if(enterpriseTypes.isEmpty())
            return null;

        return enterpriseTypes.get(select);
    }

    public void setType(int type){
        for(int i=0; i<enterpriseTypes.size(); i++){
            EnterpriseType enterpriseType = enterpriseTypes.get(i);
            if(enterpriseType.getId() == type){
                select = i;
                return;
            }
        }
    }

    public void clear(){
        enterpriseTypes.clear();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_enterprise_type, parent, false);
        EnterpriseTypeViewHolder viewHolder = new EnterpriseTypeViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        EnterpriseTypeViewHolder viewHolder = (EnterpriseTypeViewHolder) holder;

        EnterpriseType enterpriseType = enterpriseTypes.get(position);

        viewHolder.tvName.setText(enterpriseType.getName());
        viewHolder.ivSelect.setVisibility(select == position ? View.VISIBLE : View.INVISIBLE);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                select = position;
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return enterpriseTypes.size();
    }
}
