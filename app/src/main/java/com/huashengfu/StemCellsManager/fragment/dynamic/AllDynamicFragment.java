package com.huashengfu.StemCellsManager.fragment.dynamic;

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
import com.huashengfu.StemCellsManager.activity.video.VideoPlayActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.dynamic.DynamicAdapter;
import com.huashengfu.StemCellsManager.entity.dynamic.Detail;
import com.huashengfu.StemCellsManager.entity.dynamic.Dynamic;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.event.dynamic.DeleteDynamicEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.loadmore.EndlessRecyclerOnScrollListener;
import com.huashengfu.StemCellsManager.loadmore.LoadMoreWrapper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class AllDynamicFragment extends DynamicFragment {

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

    private DynamicAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;
    private DetailPopupwindow detailPopupwindow;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void deleteDynamic(DeleteDynamicEvent event){
        doDelete(event.getDynamic());
    }

    private void doDelete(Dynamic dynamic){
        int position = adapter.remove(dynamic);
        Log.i(Constants.Log.Log, getClass().getSimpleName() + " --> " + position);
        if(position >= 0){
            adapter.notifyItemRemoved(position);
            loadMoreWrapper.notifyDataSetChanged();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamic_all, null);

        unbinder = ButterKnife.bind(this, view);

        detailPopupwindow = new DynamicFragment.DetailPopupwindow();
        detailPopupwindow.init();
        setOnDetailPopupwindowListener(new OnDetailPopupwindowListener() {
            @Override
            public void onDelete(Dynamic dynamic) {
                doDelete(dynamic);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adapter = new DynamicAdapter();
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Dynamic>() {
            @Override
            public void onItemClick(View view, Dynamic dynamic) {
                switch (view.getId()){
                    case R.id.iv_play:{
                        if(dynamic.getDetailsList() != null && !dynamic.getDetailsList().isEmpty()){
                            for(Detail detail : dynamic.getDetailsList()){
                                if(detail.getType().equals(Constants.Type.Detail.video)){
                                    Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                                    intent.putExtra(Constants.Tag.data, detail.getThumbnail());
                                    startActivity(intent);
                                    return;
                                }
                            }
                        }
                        break;
                    }
                    default:{
                        boolean video = false;
                        if(dynamic.getDetailsList() != null && !dynamic.getDetailsList().isEmpty()){
                            for(Detail detail : dynamic.getDetailsList()){
                                if(detail.getType().equals(Constants.Type.Detail.video)){
                                    video = true;
                                }
                            }
                        }
                        detailPopupwindow.show(dynamic, video);
                        break;
                    }
                }
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

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Dynamic.dynamicList)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.pageSize, pageSize)
                .params(HttpHelper.Params.pageNum, pageNum)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                PageResponse<Dynamic> pageResponse = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<PageResponse<Dynamic>>(){}.getType());

                                pageCount = pageResponse.getTotalPage();

                                if(pageNum == 1)
                                    adapter.clear();

                                adapter.addAll(pageResponse.getList());
                                adapter.notifyDataSetChanged();

                                if(adapter.getItemCount() == 0){
                                    showEmpty(emptyPage, R.string.text_empty_dynamic, true);
                                }else{
                                    showEmpty(emptyPage, false);
                                }

                                pageNum++;
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
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
