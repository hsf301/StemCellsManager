package com.huashengfu.StemCellsManager.adapter.goods.preview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.response.goods.Parameters;

import java.util.ArrayList;
import java.util.List;

public class ParametersAdapter extends BaseAdapter<ParametersViewHolder> {

    private List<Parameters> parameters = new ArrayList<>();

    public void addAll(List<Parameters> collection){
        parameters.addAll(collection);
    }

    public void clear(){
        parameters.clear();
    }

    public List<Parameters> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameters> parameters) {
        this.parameters = parameters;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_preview_parameters, parent, false);
        ParametersViewHolder viewHolder = new ParametersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ParametersViewHolder viewHolder = (ParametersViewHolder) holder;

        Parameters param = parameters.get(position);

        viewHolder.setIsRecyclable(false);
        viewHolder.tvName.setText(param.getName());
        viewHolder.tvValue.setText(param.getDetails());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getOnItemClickListener() != null)
                    getOnItemClickListener().onItemClick(view, param);
            }
        });
    }

    @Override
    public int getItemCount() {
        return parameters.size();
    }
}
