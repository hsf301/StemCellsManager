package com.huashengfu.StemCellsManager.adapter.goods;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.view.FlowLayoutManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GoodsViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.btn_offline)
    Button btnOffline;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    public GoodsFlagAdapter adapter = new GoodsFlagAdapter();


    public GoodsViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        FlowLayoutManager flowLayoutManager = new FlowLayoutManager(itemView.getContext(), false);
        rvList.setLayoutManager(flowLayoutManager);
        rvList.setAdapter(adapter);
    }
}
