package com.huashengfu.StemCellsManager.activity.goods;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.goods.order.RefundOrdersDetailAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Orders;
import com.huashengfu.StemCellsManager.event.goods.UpdateOrderEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//退款详情
public class RefundInformactionActivity extends BaseActivity {

    @BindView(R.id.tv_desc)
    TextView tvDesc;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;

    private Unbinder unbinder;

    private Orders orders;
    private RefundOrdersDetailAdapter refundOrdersDetailAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_refund_informaction);

        unbinder = ButterKnife.bind(this);

        orders = (Orders) getIntent().getSerializableExtra(Constants.Tag.data);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setNestedScrollingEnabled(false);

        refundOrdersDetailAdapter = new RefundOrdersDetailAdapter();
        refundOrdersDetailAdapter.add(orders);
        rvList.setAdapter(refundOrdersDetailAdapter);

        tvOrderNum.setText(orders.getOrderId());

        if(orders.getReturnOrder() != null)
            tvDesc.setText(orders.getReturnOrder().getRemarks());
        else
            tvDesc.setText("");

        if(orders.getStatus() == Constants.Status.Order.returnGoods)
            btnCommit.setText(R.string.btn_agree_to_return);
        else if(orders.getStatus() == Constants.Status.Order.refund)
            btnCommit.setText(R.string.btn_immediate_refund);
        else
            btnCommit.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back, R.id.btn_commit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.btn_commit:{
                String message = "";
                String btnOk = "";

                if(orders.getStatus() == Constants.Status.Order.returnGoods) {
                    btnOk = "同意退货";
                    message = String.format(getResources().getString(R.string.dialog_message_refund), String.valueOf(orders.getTotalAmount()), "同意退货");
                } else if(orders.getStatus() == Constants.Status.Order.refund){
                    btnOk = "立即退款";
                    message = String.format(getResources().getString(R.string.dialog_message_refund), String.valueOf(orders.getTotalAmount()), "立即退款");
                }

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage(message)
                        .setPositiveButton(btnOk, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                //1120102815483249426

                                OkGo.<JSONObject>put(HttpHelper.Url.Order.agreeRefund + "?" + HttpHelper.Params.orderId + "=" + orders.getOrderId())
                                        .tag(this)
                                        .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                        .execute(new DialogCallback<JSONObject>(RefundInformactionActivity.this, false) {
                                            @Override
                                            public void onFinish() {
                                                super.onFinish();
                                            }

                                            @Override
                                            public void onSuccess(Response<JSONObject> response) {
                                                super.onSuccess(response);
                                                try {
                                                    if(ResponseUtils.ok(response.body())){
                                                        if(orders.getStatus() == Constants.Status.Order.returnGoods)
                                                            orders.setStatus(8);
                                                        else if(orders.getStatus() == Constants.Status.Order.refund)
                                                            orders.setStatus(5);

                                                        EventBus.getDefault().post(new UpdateOrderEvent().setOrders(orders));
                                                        finish();
                                                    }else{
                                                        showMessage(ResponseUtils.getMsg(response.body()));
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
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
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
