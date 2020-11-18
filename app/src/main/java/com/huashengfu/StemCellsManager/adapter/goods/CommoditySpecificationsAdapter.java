package com.huashengfu.StemCellsManager.adapter.goods;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.CommodityConsultationAdapter;
import com.huashengfu.StemCellsManager.db.DbHandler;
import com.huashengfu.StemCellsManager.entity.goods.Specifications;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CommoditySpecificationsAdapter extends BaseAdapter<CommoditySpecificationsViewHolder> {

    private List<Specifications> specifications = new ArrayList<>();

    public void addAll(List<Specifications> collection) {
        this.specifications.addAll(collection);
    }
    public void add(Specifications specifications){
        this.specifications.add(specifications);
    }

    public List<Specifications> getSpecifications() {
        ArrayList<Specifications> list = new ArrayList<>();
        for(Specifications tmp : specifications){
            if(tmp.isNewItem())
                continue;

            list.add(tmp);
        }
        return list;
    }

    public void clear(){
        specifications.clear();
    }

    public void update(int position, String fileName){
        Specifications tmp = specifications.get(position);
        tmp.setImage(fileName);
    }

    public boolean hasEmpty(){
        for(Specifications tmp : specifications){
            if(tmp.isNewItem()){
                return true;
            }
        }

        return false;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_goods_commodity_specifications, parent, false);
        CommoditySpecificationsViewHolder viewHolder = new CommoditySpecificationsViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CommoditySpecificationsViewHolder viewHolder = (CommoditySpecificationsViewHolder) holder;

        // 屏蔽循环使用
        viewHolder.setIsRecyclable(false);

        Specifications specifications = this.specifications.get(position);

        viewHolder.etSpecifications.setText(specifications.getSpecifications());
        viewHolder.etNumber.setText(specifications.getNumber() > 0 ? String.valueOf(specifications.getNumber()) : "");
        viewHolder.etPrice.setText(specifications.getPrice() > 0.0d ? String.valueOf(specifications.getPrice()) : "");

        Glide.with(viewHolder.itemView.getContext())
                .load(specifications.getImage())
                .transform(new GlideRoundTransformation(viewHolder.itemView.getContext(), 10))
                .error(R.mipmap.icon_add_photo)
                .into(viewHolder.ivImage);

        viewHolder.etSpecifications.setEnabled(specifications.isNewItem());
        viewHolder.etNumber.setEnabled(specifications.isNewItem());
        viewHolder.etPrice.setEnabled(specifications.isNewItem());

        viewHolder.ivDelete.setVisibility(specifications.isNewItem() && !StringUtils.isNullOrBlank(specifications.getImage()) ?
                View.VISIBLE : View.GONE);
//        viewHolder.btnDelete.setEnabled(!specifications.isNewItem());
        viewHolder.btnSave.setEnabled(specifications.isNewItem());
        viewHolder.ivImage.setEnabled(specifications.isNewItem());

        viewHolder.btnDelete.setTextAppearance(viewHolder.itemView.getContext(),
                specifications.isNewItem() ? R.style.TextGray12Sp : R.style.TextRed12Sp);

        viewHolder.btnSave.setText(
                specifications.isNewItem() ? R.string.btn_save : R.string.btn_saved);

        viewHolder.btnSave.setBackgroundResource(
                specifications.isNewItem() ? R.drawable.btn_blue_full : R.drawable.btn_gray_full);

        viewHolder.etSpecifications.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                specifications.setSpecifications(editable.toString());
            }
        });

        viewHolder.etNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                specifications.setNumber(editable.length() == 0 ? 0 : Integer.parseInt(editable.toString()));
            }
        });

        viewHolder.etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                specifications.setPrice(editable.length() == 0 ? 0 : Double.parseDouble(editable.toString()));
            }
        });

        viewHolder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setMessage(specifications.isNewItem() ?
                                R.string.dialog_message_nosave_commodity_specifications : R.string.dialog_message_delete_commodity_specifications)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                CommoditySpecificationsAdapter.this.specifications.remove(position);
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

        viewHolder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!specifications.isNewItem())
                    return;

                if(viewHolder.etSpecifications.length() == 0){
                    Toast.makeText(viewHolder.itemView.getContext(), R.string.hint_goods_specifications, Toast.LENGTH_LONG).show();
                    return;
                }

                if(viewHolder.etNumber.length() == 0){
                    Toast.makeText(viewHolder.itemView.getContext(), R.string.hint_goods_stock, Toast.LENGTH_LONG).show();
                    return;
                }

                if(Integer.parseInt(viewHolder.etNumber.getText().toString()) <= 0){
                    Toast.makeText(viewHolder.itemView.getContext(), R.string.hint_goods_stock, Toast.LENGTH_LONG).show();
                    return;
                }

                if(viewHolder.etPrice.length() == 0){
                    Toast.makeText(viewHolder.itemView.getContext(), R.string.hint_goods_publis_price, Toast.LENGTH_LONG).show();
                    return;
                }

                if(Double.parseDouble(String.valueOf(viewHolder.etPrice.getText().toString())) <= 0.0d){
                    Toast.makeText(viewHolder.itemView.getContext(), R.string.hint_goods_publis_price, Toast.LENGTH_LONG).show();
                    return;
                }

                if(StringUtils.isNullOrBlank(specifications.getImage())){
                    Toast.makeText(viewHolder.itemView.getContext(), R.string.error_photo_empty_commodity_specifications, Toast.LENGTH_LONG).show();
                    return;
                }

                specifications.setNewItem(false);

                Specifications tmp = new Specifications();
                tmp.setNewItem(true);
                add(tmp);

                notifyDataSetChanged();
            }
        });

        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                specifications.setImage("");
                notifyDataSetChanged();
            }
        });

        viewHolder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getOnItemClickListener() != null)
                    getOnItemClickListener().onItemClick(viewHolder.ivImage, position);
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getOnItemClickListener() != null)
                    getOnItemClickListener().onItemClick(viewHolder.itemView, specifications);
            }
        });
    }

    @Override
    public int getItemCount() {
        return specifications.size();
    }
}
