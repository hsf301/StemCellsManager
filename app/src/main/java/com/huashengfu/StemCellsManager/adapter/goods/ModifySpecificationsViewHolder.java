package com.huashengfu.StemCellsManager.adapter.goods;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.huashengfu.StemCellsManager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModifySpecificationsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.et_specifications)
    EditText etSpecifications;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_price)
    EditText etPrice;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.btn_delete)
    Button btnDelete;
    @BindView(R.id.btn_save)
    Button btnSave;

    public ModifySpecificationsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
