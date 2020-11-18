package com.huashengfu.StemCellsManager.fragment.interaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.interaction.DynamicCommentActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.DynamicCommentAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.DynamicComment;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.fragment.BaseFragment;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.loadmore.EndlessRecyclerOnScrollListener;
import com.huashengfu.StemCellsManager.loadmore.LoadMoreWrapper;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//动态评论
public class DynamicCommentFragment extends BaseFragment {

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

    private DynamicCommentAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interaction_dynamic_comment, null);

        unbinder = ButterKnife.bind(this, view);


        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adapter = new DynamicCommentAdapter();
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<DynamicComment>() {
            @Override
            public void onItemClick(View view, DynamicComment dynamicComment) {
                Intent intent = new Intent(getContext(), DynamicCommentActivity.class);
                intent.putExtra(Constants.Tag.data, dynamicComment);
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
        OkGo.<JSONObject>get(HttpHelper.Url.Service.Comment.list)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.pageSize, pageSize)
                .params(HttpHelper.Params.pageNum, pageNum)
                .params(HttpHelper.Params.category, Constants.Type.Comment.DynamicComment)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                JSONObject data = ResponseUtils.getData(response.body());
                                if(data.has(Constants.Tag.comment)){
                                    JSONObject consult = data.getJSONObject(Constants.Tag.comment);
                                    PageResponse<DynamicComment> pageResponse = new Gson().fromJson(
                                            consult.toString(),
                                            new TypeToken<PageResponse<DynamicComment>>(){}.getType());

                                    if(pageNum == 1)
                                        adapter.clear();

                                    adapter.addAll(pageResponse.getList());
                                    adapter.notifyDataSetChanged();

                                    pageCount = pageResponse.getTotalPage();
                                    pageNum++;
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

                        if(adapter.getItemCount() == 0){
                            showEmpty(emptyPage, R.string.text_empty_dynamic_comment, true);
                        }else{
                            showEmpty(emptyPage, false);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
