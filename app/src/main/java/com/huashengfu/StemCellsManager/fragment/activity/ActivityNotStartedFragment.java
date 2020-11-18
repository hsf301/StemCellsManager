package com.huashengfu.StemCellsManager.fragment.activity;

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
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.activity.ActivityAdapter;
import com.huashengfu.StemCellsManager.entity.activity.Activity;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.event.activity.UpdateActivityBannerEvent;
import com.huashengfu.StemCellsManager.event.activity.UpdateActivityEvent;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ActivityNotStartedFragment extends ActivityFragment{

    private Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;

    private ActivityAdapter activityAdapter;
    private LoadMoreWrapper loadMoreWrapper;

    private int pageNum = 1;
    private int pageSize = 10;
    private int pageCount = 1;

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateActivityBanner(UpdateActivityBannerEvent event){
        int position = activityAdapter.updateBanner(event.getActivity());
        updateBanner(rvList, position, event.getActivity().getBanner());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateActivity(UpdateActivityEvent event){
        int position = activityAdapter.update(event.getActivity());
        update(rvList, position, event.getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity_not_started, null);
        unbinder = ButterKnife.bind(this, view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        setOnActivityPopupwindowListener(new OnActivityPopupwindowListener() {
            @Override
            public void onDelete(Activity activity) {
                int position = activityAdapter.remove(activity);
                remove(rvList, position);
            }
        });

        activityAdapter = new ActivityAdapter();
        activityAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Activity>() {
            @Override
            public void onItemClick(View view, Activity activity) {
                switch (view.getId()){
                    case R.id.btn_offline:{
                        showDialog(activity);
                        break;
                    }
                    case R.id.iv_menu:{
                        showActivity(activity);
                        break;
                    }
                    default:{
                        previewActivity(activity);
                        break;
                    }
                }
            }
        });
        loadMoreWrapper = new LoadMoreWrapper(activityAdapter);

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
        OkGo.<JSONObject>get(HttpHelper.Url.Activity.pageList)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.pageNum, pageNum)
                .params(HttpHelper.Params.pageSize, pageSize)
                .params(HttpHelper.Params.activityStatus, Constants.Status.Activity.notStarted)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);

                        try {
                            if(ResponseUtils.ok(response.body())){

                                PageResponse<Activity> pageResponse = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<PageResponse<Activity>>(){}.getType());

                                if(pageNum == 1)
                                    activityAdapter.clear();

                                activityAdapter.addAll(pageResponse.getList());
                                activityAdapter.notifyDataSetChanged();

                                pageCount = pageResponse.getTotalPage();
                                pageNum++;

                                if(activityAdapter.getItemCount() == 0){
                                    showEmpty(emptyPage, R.string.text_empty_activity, true);
                                }else{
                                    showEmpty(emptyPage, false);
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
                    }
                });
    }
}
