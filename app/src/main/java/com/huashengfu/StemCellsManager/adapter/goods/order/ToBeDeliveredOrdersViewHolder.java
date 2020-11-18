package com.huashengfu.StemCellsManager.adapter.goods.order;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.goods.order.detail.ToBeDeliveredOrdersDetailAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ToBeDeliveredOrdersViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.btn_deliver)
    Button btnDeliver;

    public ToBeDeliveredOrdersDetailAdapter adapter;

    public ToBeDeliveredOrdersViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        adapter = new ToBeDeliveredOrdersDetailAdapter();
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }
}
