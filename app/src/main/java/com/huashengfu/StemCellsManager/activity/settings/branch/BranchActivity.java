package com.huashengfu.StemCellsManager.activity.settings.branch;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.settings.branch.BranchAdapter;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.entity.settings.Branch;
import com.huashengfu.StemCellsManager.event.RefreshBranchEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
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
import butterknife.OnClick;
import butterknife.Unbinder;

//分支机构
public class BranchActivity extends BaseActivity {

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

    private BranchAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_branch);

        unbinder = ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adapter = new BranchAdapter(savedInstanceState);
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Branch>() {
            @Override
            public void onItemClick(View view, Branch branch) {
                switch (view.getId()){
                    case R.id.tv_delete:{
                        AlertDialog dialog = new AlertDialog.Builder(BranchActivity.this)
                                .setMessage(R.string.dialog_message_delete_branch)
                                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        OkGo.<JSONObject>delete(HttpHelper.Url.Store.Branch.del + branch.getEid())
                                                .tag(this)
                                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                                .execute(new DialogCallback<JSONObject>(BranchActivity.this, false) {
                                                    @Override
                                                    public void onFinish() {
                                                        super.onFinish();
                                                    }

                                                    @Override
                                                    public void onSuccess(Response<JSONObject> response) {
                                                        super.onSuccess(response);
                                                        try {
                                                            if(ResponseUtils.ok(response.body())){
                                                                int position = adapter.remove(branch);
                                                                loadMoreWrapper.notifyItemRemoved(position);
                                                                loadMoreWrapper.notifyDataSetChanged();
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
                    default:{
                        Intent intent = new Intent(getApplicationContext(), ModifyBranchActivity.class);
                        intent.putExtra(Constants.Tag.data, branch);
                        startActivity(intent);
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
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void refreshBranch(RefreshBranchEvent event){
        mySwipeRefreshLayout.post(()->{
            mySwipeRefreshLayout.setRefreshing(true);
            pageNum = 1;
            doQuery();
        });
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Store.Branch.list)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.pageNum, pageNum)
                .params(HttpHelper.Params.pageSize, pageSize)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                PageResponse<Branch> pageResponse = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<PageResponse<Branch>>(){}.getType());

                                if(pageNum == 1)
                                    adapter.clear();

                                adapter.addAll(pageResponse.getList());
                                adapter.notifyDataSetChanged();

                                pageCount = pageResponse.getTotalPage();
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

                        if(adapter.getItemCount() == 0)
                            showEmpty(emptyPage, R.string.text_empty_branch, true);
                        else
                            showEmpty(emptyPage, false);

                        mySwipeRefreshLayout.setRefreshing(false);
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                    }
                });

    }

    @OnClick({R.id.iv_back, R.id.tv_add})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.tv_add:{
                Intent intent = new Intent(this, AddBranchActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
