package com.huashengfu.StemCellsManager.fragment.goods;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.goods.DeliverGoodsInformactionActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.order.ToBeDeliveredOrdersAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Orders;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.event.goods.DeliverGoodsEvent;
import com.huashengfu.StemCellsManager.event.goods.ShowToBeDeliveredDotEvent;
import com.huashengfu.StemCellsManager.fragment.BaseFragment;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.loadmore.EndlessRecyclerOnScrollListener;
import com.huashengfu.StemCellsManager.loadmore.LoadMoreWrapper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

// 待发货
public class ToBeDeliveredFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;

    private int pageNum = 1;
    private int pageSize = 10;
    private int pageCount = 1;

    private ToBeDeliveredOrdersAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_orders_tobe_delivered, null);

        unbinder = ButterKnife.bind(this, view);

        EventBus.getDefault().register(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adapter = new ToBeDeliveredOrdersAdapter();
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Orders>() {
            @Override
            public void onItemClick(View view, Orders orders) {
                Intent intent = new Intent(getContext(), DeliverGoodsInformactionActivity.class);
                intent.putExtra(Constants.Tag.data, orders);
                startActivity(intent);
            }
        });

        loadMoreWrapper = new LoadMoreWrapper(adapter);
        rvList.setAdapter(loadMoreWrapper);
        rvList.setLayoutManager(layoutManager);

        rvList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if(pageNum <= pageCount){
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
                    mySwipeRefreshLayout.postDelayed(()->{
                        doQuery();
                    }, 1000);
                }else{
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                }
            }

//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
//                    EventBus.getDefault().post(new ShowToBeDeliveredDotEvent().setShow(false));
//                }
//            }

        });

        mySwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorSwipeRefreshLayout1),
                getResources().getColor(R.color.colorSwipeRefreshLayout2),
                getResources().getColor(R.color.colorSwipeRefreshLayout3)
        );
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                doQuery();
            }
        });

        mySwipeRefreshLayout.post(()->{
            mySwipeRefreshLayout.setRefreshing(true);
            doQuery();
        });

        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeliverGoodsEvent(DeliverGoodsEvent event){
        int position = adapter.remove(event.getOrders());
        Log.i(Constants.Log.Log, "Pos ---> " + position);
        loadMoreWrapper.notifyItemRemoved(position);
        loadMoreWrapper.notifyDataSetChanged();
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Order.pageList)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.status, Constants.Status.Order.toBeDelivered)
                .params(HttpHelper.Params.pageSize, pageSize)
                .params(HttpHelper.Params.pageNum, pageNum)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                PageResponse<Orders> pageResponse = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<PageResponse<Orders>>(){}.getType());

                                if(pageNum == 1)
                                    adapter.clear();

                                adapter.addAll(pageResponse.getList());
                                adapter.notifyDataSetChanged();

                                pageCount = pageResponse.getTotalPage();
                                pageNum++;

                                if(pageResponse.getList().isEmpty()){
                                    EventBus.getDefault().post(new ShowToBeDeliveredDotEvent().setShow(false));
                                }else{
                                    EventBus.getDefault().post(new ShowToBeDeliveredDotEvent().setShow(true));
                                }
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
                        mySwipeRefreshLayout.setRefreshing(false);
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);

                        if(adapter.getItemCount() == 0)
                            showEmpty(emptyPage, R.string.text_empty_orders, true);
                        else
                            showEmpty(emptyPage, false);
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
