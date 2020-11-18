package com.huashengfu.StemCellsManager.adapter.goods;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Detail;
import com.huashengfu.StemCellsManager.entity.service.Introduction;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;

import java.util.ArrayList;
import java.util.List;

public class GoodsDetailAdapter extends BaseAdapter<GoodsDetailViewHolder> {

    private ArrayList<Detail> details = new ArrayList<>();
    private boolean modify = false;

    public void add(Detail detail){
        details.add(detail);
    }

    public void clear(){
        details.clear();
    }

    public void addAll(List<Detail> collection){
        details.addAll(collection);
    }

    public List<Detail> getDetail() {
        return details;
    }

    public boolean isModify() {
        return modify;
    }

    public void setModify(boolean modify) {
        this.modify = modify;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_detail, parent, false);
        GoodsDetailViewHolder viewHolder = new GoodsDetailViewHolder(view);
        return viewHolder;
    }

    private void move(int from ,int to){
        details.add(to, details.remove(from));
        notifyItemMoved(from, to);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GoodsDetailViewHolder viewHolder = (GoodsDetailViewHolder) holder;

        Detail detail = details.get(position);

        if(detail.isPhoto()){
            viewHolder.ivImage.setVisibility(View.VISIBLE);
            viewHolder.etDesc.setVisibility(View.GONE);

            Glide.with(holder.itemView.getContext())
                    .load(detail.getContent())
                    .transform(new GlideRoundTransformation(holder.itemView.getContext(), 10))
                    .into(viewHolder.ivImage);
        }else if(detail.isText()){
            viewHolder.ivImage.setVisibility(View.GONE);
            viewHolder.etDesc.setVisibility(View.VISIBLE);
            viewHolder.etDesc.setText(detail.getContent());
        }

        if(modify){
            viewHolder.llMenu.setVisibility(View.VISIBLE);
            viewHolder.etDesc.setEnabled(false);
        }else{
            viewHolder.etDesc.setEnabled(true);
            viewHolder.llMenu.setVisibility(View.GONE);
        }

        if(position == 0){
            viewHolder.ivUp.setImageResource(R.mipmap.icon_move_up_non);
        }else{
            viewHolder.ivUp.setImageResource(R.mipmap.icon_move_up);
        }

        if(position == getItemCount() -1){
            viewHolder.ivDown.setImageResource(R.mipmap.icon_move_down_non);
        }else{
            viewHolder.ivDown.setImageResource(R.mipmap.icon_move_down);
        }

        viewHolder.llDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == getItemCount() - 1)
                    return;

                move(position, position + 1);
            }
        });

        viewHolder.llUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position == 0)
                    return;

                move(position, position - 1);
            }
        });

        viewHolder.llDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(holder.itemView.getContext())
                        .setMessage(R.string.dialog_message_delete_goods)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                details.remove(detail);
                                notifyItemChanged(position);
                                notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                dialog.show();
            }
        });

        viewHolder.setIsRecyclable(false);

        viewHolder.etDesc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                detail.setContent(editable.toString());
            }
        });

        if(position == getItemCount() - 1){
            viewHolder.etDesc.requestFocus();
        }else{
            viewHolder.etDesc.clearFocus();
        }
    }

    @Override
    public int getItemCount() {
        return details.size();
    }
}
