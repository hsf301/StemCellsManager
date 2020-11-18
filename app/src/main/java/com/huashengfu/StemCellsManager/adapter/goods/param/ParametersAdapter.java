package com.huashengfu.StemCellsManager.adapter.goods.param;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
        addAll(collection, null);
    }

    public void addAll(List<Parameters> collection, ArrayList<Parameters> old){
        parameters.addAll(collection);

        if(old != null){
            for(Parameters tmp : parameters){
                for(Parameters param : old){
                    if(tmp.getId() == param.getId()){
                        tmp.setDetails(param.getDetails());
                    }else{
                        if(tmp.getName().equals(param.getName())){
                            tmp.setDetails(param.getDetails());
                        }
                    }
                }
            }
        }
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_parameters, parent, false);
        ParametersViewHolder viewHolder = new ParametersViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ParametersViewHolder viewHolder = (ParametersViewHolder) holder;

        Parameters param = parameters.get(position);

        viewHolder.setIsRecyclable(false);
        viewHolder.tvName.setText(param.getName());
        viewHolder.etValue.setText(param.getDetails());
        viewHolder.etValue.setHint("请输入" + param.getName());
        viewHolder.etValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                param.setDetails(editable.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return parameters.size();
    }
}
