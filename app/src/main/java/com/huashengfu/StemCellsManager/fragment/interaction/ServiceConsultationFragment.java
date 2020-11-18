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
import android.util.Log;
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
import com.huashengfu.StemCellsManager.adapter.interaction.ServiceConsultationAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.consultation.ConsultationUserAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.ConsultationUser;
import com.huashengfu.StemCellsManager.entity.interaction.ServiceConsultation;
import com.huashengfu.StemCellsManager.entity.interaction.ServiceQuestion;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.fragment.BaseFragment;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.loadmore.EndlessRecyclerOnScrollListener;
import com.huashengfu.StemCellsManager.loadmore.LoadMoreWrapper;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//服务咨询
public class ServiceConsultationFragment extends BaseFragment {

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

    private ServiceConsultationAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;

    private ConsultationUserPopupwindow consultationUserPopupwindow;
    private RxPermissions rxPermissions;

    private String status = Constants.Status.Service.yes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interaction_service_consultation, null);

        unbinder = ButterKnife.bind(this, view);

        rxPermissions = new RxPermissions(getActivity());

        consultationUserPopupwindow = new ConsultationUserPopupwindow();
        consultationUserPopupwindow.init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adapter = new ServiceConsultationAdapter();
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<ServiceConsultation>() {
            @Override
            public void onItemClick(View view, ServiceConsultation serviceConsultation) {
                consultationUserPopupwindow.show(serviceConsultation);
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
                if(status.equals(Constants.Status.Service.yes)) {
                    status = Constants.Status.Service.no;
                    tvSwitch.setText(R.string.btn_service_showing);
                }else {
                    status = Constants.Status.Service.yes;
                    tvSwitch.setText(R.string.btn_service_closed);
                }

                adapter.clear();
                adapter.notifyDataSetChanged();
                mySwipeRefreshLayout.post(()->{
                    mySwipeRefreshLayout.setRefreshing(true);
                    pageNum =1;
                    doQuery();
                });
            }
        }
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Service.Comment.list)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.pageSize, pageSize)
                .params(HttpHelper.Params.pageNum, pageNum)
                .params(HttpHelper.Params.status, status)
                .params(HttpHelper.Params.category, Constants.Type.Comment.ServiceRegistration)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                JSONObject data = ResponseUtils.getData(response.body());
                                if(data.has(Constants.Tag.consult)){
                                    JSONObject consult = data.getJSONObject(Constants.Tag.consult);
                                    PageResponse<ServiceConsultation> pageResponse = new Gson().fromJson(
                                            consult.toString(),
                                            new TypeToken<PageResponse<ServiceConsultation>>(){}.getType());

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
                            showEmpty(emptyPage, R.string.text_empty_service_consultation, true);
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

    private class ConsultationUserPopupwindow{
        private PopupWindow popupWindow;
        private RecyclerView recyclerView;
        private ConsultationUserAdapter adapter;
        private SwipeRefreshLayout swipeRefreshLayout;
        private View emptyView;

        private int pageNum = 1;
        private int pageSize = 10;
        private int pageCount = 1;

        private LoadMoreWrapper loadMoreWrapper;

        private ImageView ivImage;
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvCount;

        private ServiceConsultation serviceConsultation;

        private void init() {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_interaction_service_consultation, null);
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
            tvTitle = view.findViewById(R.id.tv_title);
            tvContent = view.findViewById(R.id.tv_content);
            tvCount = view.findViewById(R.id.tv_count);
            tvCount.setText(String.format(getResources().getString(R.string.str_user_count), 0));

            recyclerView = view.findViewById(R.id.rv_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new ConsultationUserAdapter();
            adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<ConsultationUser>() {
                @Override
                public void onItemClick(View view, ConsultationUser user) {
                    if(StringUtils.isNullOrBlank(user.getPhone())){
                        showMessage(R.string.error_consultation_user_phone_empty);
                        return;
                    }

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

            emptyView = view.findViewById(R.id.empty);

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
            OkGo.<JSONObject>get(HttpHelper.Url.Service.Consult.listBySid)
                    .tag(this)
                    .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                    .params(HttpHelper.Params.pageNum, pageNum)
                    .params(HttpHelper.Params.pageSize, pageSize)
                    .params(HttpHelper.Params.sId, serviceConsultation.getSid())
                    .execute(new JsonCallback<JSONObject>() {
                        @Override
                        public void onSuccess(Response<JSONObject> response) {
                            super.onSuccess(response);
                            try {
                                if(ResponseUtils.ok(response.body())){
                                    PageResponse<ConsultationUser> pageResponse = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                            new TypeToken<PageResponse<ConsultationUser>>(){}.getType());

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
                                showEmpty(emptyView, R.string.text_empty_service_consultation_user, true);
                            else
                                showEmpty(emptyView, false);
                        }
                    });

        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public void show(ServiceConsultation serviceConsultation){
            this.serviceConsultation = serviceConsultation;

            Glide.with(getContext())
                    .load(serviceConsultation.getScover())
                    .placeholder(R.drawable.image_loading_pic_small)
                    .transform(new GlideRoundTransformation(getContext(), 2))
                    .into(ivImage);

            tvTitle.setText(serviceConsultation.getSname());
            tvContent.setText(serviceConsultation.getScontent());

            tvCount.setText(String.format(getResources().getString(R.string.str_user_count), serviceConsultation.getScount()));

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
