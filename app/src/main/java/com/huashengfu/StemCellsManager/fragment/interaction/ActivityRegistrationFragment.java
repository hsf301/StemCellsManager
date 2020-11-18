package com.huashengfu.StemCellsManager.fragment.interaction;

import android.Manifest;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.ActivityRegistrationAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.activity.ActivityRegistrationUserAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.ActivityRegistration;
import com.huashengfu.StemCellsManager.entity.interaction.ActivityRegistrationUser;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.fragment.BaseFragment;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.loadmore.EndlessRecyclerOnScrollListener;
import com.huashengfu.StemCellsManager.loadmore.LoadMoreWrapper;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//活动报名
public class ActivityRegistrationFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;
    @BindView(R.id.tv_switch)
    TextView tvSwitch;

    private int pageNum = 1;
    private int pageSize = 10;
    private int pageCount = 1;

    private String status = Constants.Status.Activity.yes;

    private ActivityRegistrationAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;

    private ActivityRegistrationPopupwindow activityRegistrationPopupwindow;
    private RxPermissions rxPermissions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interaction_activity_registration, null);

        unbinder = ButterKnife.bind(this, view);

        rxPermissions = new RxPermissions(getActivity());

        activityRegistrationPopupwindow = new ActivityRegistrationPopupwindow();
        activityRegistrationPopupwindow.init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adapter = new ActivityRegistrationAdapter();
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<ActivityRegistration>() {
            @Override
            public void onItemClick(View view, ActivityRegistration activityRegistration) {
                activityRegistrationPopupwindow.show(activityRegistration);
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

    @OnClick(R.id.tv_switch)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_switch:{
                if(status.equals(Constants.Status.Activity.yes)) {
                    status = Constants.Status.Activity.no;
                    tvSwitch.setText(R.string.btn_activity_Ongoing);
                } else {
                    status = Constants.Status.Activity.yes;
                    tvSwitch.setText(R.string.btn_activity_closed);
                }

                adapter.clear();
                adapter.notifyDataSetChanged();
                mySwipeRefreshLayout.post(()->{
                    pageNum = 1;
                    mySwipeRefreshLayout.setRefreshing(true);
                    doQuery();
                });
                break;
            }
        }
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Service.Comment.list)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.pageSize, pageSize)
                .params(HttpHelper.Params.pageNum, pageNum)
                .params(HttpHelper.Params.status, status)
                .params(HttpHelper.Params.category, Constants.Type.Comment.ActivityRegistration)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                JSONObject data = ResponseUtils.getData(response.body());
                                if(data.has(Constants.Tag.signup)){
                                    JSONObject consult = data.getJSONObject(Constants.Tag.signup);
                                    PageResponse<ActivityRegistration> pageResponse = new Gson().fromJson(
                                            consult.toString(),
                                            new TypeToken<PageResponse<ActivityRegistration>>(){}.getType());

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
                            showEmpty(emptyPage, R.string.text_empty_activity_registration, true);
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

    private class ActivityRegistrationPopupwindow{
        private PopupWindow popupWindow;
        private RecyclerView recyclerView;
        private ActivityRegistrationUserAdapter adapter;

        private ImageView ivImage;
        private TextView tvDate;
        private TextView tvContent;
        private View emptyPage;

        private SwipeRefreshLayout swipeRefreshLayout;
        private View emptyView;

        private int pageNum = 1;
        private int pageSize = 10;
        private int pageCount = 1;

        private LoadMoreWrapper loadMoreWrapper;

        private ActivityRegistration activityRegistration;

        private void init() {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_interaction_activity_registration, null);

            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.getBackground().setAlpha(170);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ViewUtils.background(getActivity(), 1f);
                }
            });

            view.findViewById(R.id.rl_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            ivImage = view.findViewById(R.id.iv_image);
            tvDate = view.findViewById(R.id.tv_date);
            tvContent = view.findViewById(R.id.tv_content);
            emptyView = view.findViewById(R.id.empty);

            recyclerView = view.findViewById(R.id.rv_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new ActivityRegistrationUserAdapter();
            adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<ActivityRegistrationUser>() {
                @Override
                public void onItemClick(View view, ActivityRegistrationUser user) {
                    rxPermissions.requestEachCombined(new String[]{
                            Manifest.permission.CALL_PHONE})
                            .subscribe(permission -> {
                                if(permission.granted){
                                    Intent intent = new Intent(Intent.ACTION_DIAL);
                                    Uri data = Uri.parse("tel:" + user.getPhone());
                                    intent.setData(data);
                                    startActivity(intent);
                                    dismiss();
                                }else if(permission.shouldShowRequestPermissionRationale){
                                    showMessage(R.string.rationale_ask_again);
                                }else{
                                    showMessage(R.string.rationale_cancle);
                                }});
                }
            });

            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });


            loadMoreWrapper = new LoadMoreWrapper(adapter);
            recyclerView.setAdapter(loadMoreWrapper);
            recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
                @Override
                public void onLoadMore() {
                    if(pageNum <= pageCount){
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
                        swipeRefreshLayout.postDelayed(()->{
                            doQuery();
                        }, 1000);
                    }else{
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });

            swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
            swipeRefreshLayout.setColorSchemeColors(
                    getResources().getColor(R.color.colorSwipeRefreshLayout1),
                    getResources().getColor(R.color.colorSwipeRefreshLayout2),
                    getResources().getColor(R.color.colorSwipeRefreshLayout3)
            );
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pageNum=1;
                    doQuery();
                }
            });

            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }

        private void doQuery(){
            OkGo.<JSONObject>get(HttpHelper.Url.Activity.Signup.listByAid)
                    .tag(this)
                    .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                    .params(HttpHelper.Params.pageNum, pageNum)
                    .params(HttpHelper.Params.pageSize, pageSize)
                    .params(HttpHelper.Params.aId, activityRegistration.getAid())
                    .execute(new JsonCallback<JSONObject>() {
                        @Override
                        public void onSuccess(Response<JSONObject> response) {
                            super.onSuccess(response);
                            try {
                                if(ResponseUtils.ok(response.body())){
                                    PageResponse<ActivityRegistrationUser> pageResponse = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                            new TypeToken<PageResponse<ActivityRegistrationUser>>(){}.getType());

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
                            swipeRefreshLayout.setRefreshing(false);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);

                            if(adapter.getItemCount() == 0)
                                showEmpty(emptyView, R.string.text_empty_activity_registration, true);
                            else
                                showEmpty(emptyView, false);
                        }
                    });

        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public void show(ActivityRegistration activityRegistration){
            this.activityRegistration = activityRegistration;

            Glide.with(getContext())
                    .load(activityRegistration.getAcover())
                    .placeholder(R.drawable.image_loading_pic_small)
                    .transform(new GlideRoundTransformation(getContext(), 2))
                    .into(ivImage);

            tvContent.setText(activityRegistration.getAsubtitle());
            tvDate.setText(new SimpleDateFormat("MM-dd HH:mm").format(new Date(activityRegistration.getAendDate())));

            pageNum = 1;
            adapter.clear();
            adapter.notifyDataSetChanged();

            swipeRefreshLayout.post(()->{
                swipeRefreshLayout.setRefreshing(true);
                doQuery();
            });

            ViewUtils.hideSoftInput(getContext(), rvList);
            ViewUtils.background(getActivity(), 0.8f);
            popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
    }
}
