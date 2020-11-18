package com.huashengfu.StemCellsManager.activity.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.ExpressAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.order.ToBeDeliveredOrdersDetailAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Express;
import com.huashengfu.StemCellsManager.entity.goods.Orders;
import com.huashengfu.StemCellsManager.event.goods.DeliverGoodsEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//发货信息
public class DeliverGoodsInformactionActivity extends BaseActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.rv_express)
    RecyclerView rvExpress;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;

    private Unbinder unbinder;

    private Orders orders;
    private ExpressAdapter expressAdapter;

    private ToBeDeliveredOrdersDetailAdapter toBeDeliveredOrdersDetailAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_deliver_informaction);

        unbinder = ButterKnife.bind(this);

        orders = (Orders) getIntent().getSerializableExtra(Constants.Tag.data);

        expressAdapter = new ExpressAdapter();
        expressAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Express>() {
            @Override
            public void onItemClick(View view, Express express) {

            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rvExpress.setLayoutManager(gridLayoutManager);
        rvExpress.setNestedScrollingEnabled(false);
        rvExpress.setAdapter(expressAdapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setNestedScrollingEnabled(false);

        toBeDeliveredOrdersDetailAdapter = new ToBeDeliveredOrdersDetailAdapter();
        toBeDeliveredOrdersDetailAdapter.add(orders);
        rvList.setAdapter(toBeDeliveredOrdersDetailAdapter);

        tvName.setText(orders.getReceiverName() + " " + orders.getReceiverPhone());
        tvAddress.setText(orders.getReceiverRegion() + " " + orders.getReceiverDetailAddress());
        tvOrderNum.setText(orders.getOrderId());

        doQuery();
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Courier.list)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                List<Express> list = new Gson().fromJson(ResponseUtils.getList(response.body()).toString(),
                                        new TypeToken<List<Express>>(){}.getType());

                                expressAdapter.clear();
                                expressAdapter.addAll(list);
                                expressAdapter.notifyDataSetChanged();
                            }else{
                                showMessage(ResponseUtils.getMsg(response.body()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.btn_commit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.btn_commit:{
                if(etNumber.getText().length() == 0){
                    showMessage(etNumber.getHint());
                    return;
                }

                if(expressAdapter.getSelectExpress() == null){
                    showMessage(R.string.error_select_express);
                    return;
                }

                try {
                    JSONObject obj = new JSONObject();
                    obj.put(HttpHelper.Params.orderId, orders.getOrderId());
                    obj.put(HttpHelper.Params.courierName, expressAdapter.getSelectExpress().getCourierName());
                    obj.put(HttpHelper.Params.courierNo, etNumber.getText().toString());

                    JSONObject courierInfo = new JSONObject();
                    courierInfo.put(HttpHelper.Params.courierInfo, obj);

                    OkGo.<JSONObject>put(HttpHelper.Url.Order.sendOutGoods)
                            .tag(this)
                            .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                            .upJson(obj)
                            .execute(new DialogCallback<JSONObject>(this, false) {
                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                }

                                @Override
                                public void onSuccess(Response<JSONObject> response) {
                                    super.onSuccess(response);
                                    try {
                                        if(ResponseUtils.ok(response.body())){
                                            EventBus.getDefault().post(new DeliverGoodsEvent().setOrders(orders));
                                            finish();
                                        }else{
                                            showMessage(ResponseUtils.getMsg(response.body()));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
