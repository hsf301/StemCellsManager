package com.huashengfu.StemCellsManager.activity.service;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.service.ServiceAdapter;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.entity.service.Service;
import com.huashengfu.StemCellsManager.event.service.UpdateServiceCoverEvent;
import com.huashengfu.StemCellsManager.event.service.UpdateServiceEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.loadmore.EndlessRecyclerOnScrollListener;
import com.huashengfu.StemCellsManager.loadmore.LoadMoreWrapper;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
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

// 展示中服务 - 服务管理
public class ServiceManagementActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;
    @BindView(R.id.tv_offline)
    TextView tvOffline;

    private ServiceAdapter serviceAdapter;
    private LoadMoreWrapper loadMoreWrapper;

    private int pageNum = 1;
    private int pageSize = 10;
    private int pageCount = 1;

    private boolean online = true;

    private MenuPopupwindow menuPopupwindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_management);

        unbinder = ButterKnife.bind(this);

        menuPopupwindow = new MenuPopupwindow();
        menuPopupwindow.init();

        EventBus.getDefault().register(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        serviceAdapter = new ServiceAdapter();
        serviceAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Service>() {
            @Override
            public void onItemClick(View view, Service service) {
                switch (view.getId()){
                    case R.id.btn_offline:{
                        showDialog(service);
                        break;
                    }
                    case R.id.iv_menu:{
                        menuPopupwindow.show(service);
                        break;
                    }
                    default:{
                        Intent intent = new Intent(getApplicationContext(), PreviewServiceActivity.class);
                        intent.putExtra(Constants.Tag.data, service);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        loadMoreWrapper = new LoadMoreWrapper(serviceAdapter);

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

        mySwipeRefreshLayout.postDelayed(()->{
            mySwipeRefreshLayout.setRefreshing(true);
            doQuery();
        }, 200);
    }

    private void showDialog(Service service){
        AlertDialog dialog = new AlertDialog.Builder(ServiceManagementActivity.this)
                .setMessage(service.getStatus().equals(Constants.Status.Service.yes) ? R.string.dialog_message_offline_service : R.string.dialog_message_online_service)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        OkGo.<JSONObject>put(HttpHelper.Url.Service.upDownStatus + service.getId() + "?" +
                                        HttpHelper.Params.status + "=" + (service.getStatus().equals(Constants.Status.Service.yes) ?
                                                Constants.Status.Service.no : Constants.Status.Service.yes))
                                .tag(this)
                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                .execute(new DialogCallback<JSONObject>(ServiceManagementActivity.this, false) {
                                    @Override
                                    public void onSuccess(Response<JSONObject> response) {
                                        super.onSuccess(response);
                                        try {
                                            if(ResponseUtils.ok(response.body())){
                                                int position = serviceAdapter.remove(service);
                                                loadMoreWrapper.notifyItemRemoved(position);
                                                loadMoreWrapper.notifyDataSetChanged();

                                                showMessage(service.getStatus().equals(Constants.Status.Service.yes) ? R.string.success_update_offline : R.string.success_update_online);
                                            }else{
                                                showMessage(ResponseUtils.getMsg(response.body()));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Response<JSONObject> response) {
                                        super.onError(response);
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
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Service.serviceList)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.pageNum, pageNum)
                .params(HttpHelper.Params.pageSize, pageSize)
                .params(HttpHelper.Params.status, online ? Constants.Status.Service.yes : Constants.Status.Service.no)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                PageResponse<Service> pageResponse = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<PageResponse<Service>>(){}.getType());

                                pageCount = pageResponse.getTotalPage();

                                if(pageNum == 1)
                                    serviceAdapter.clear();

                                serviceAdapter.addAll(pageResponse.getList());
                                serviceAdapter.notifyDataSetChanged();

                                if(serviceAdapter.getItemCount() == 0){
                                    emptyPage.setVisibility(View.VISIBLE);
                                }else{
                                    emptyPage.setVisibility(View.GONE);
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
                    public void onError(Response<JSONObject> response) {
                        super.onError(response);
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();

                        if(serviceAdapter.getItemCount() == 0){
                            showEmpty(emptyPage, R.string.text_empty_service,true);
                        }else{
                            showEmpty(emptyPage, false);
                        }

                        mySwipeRefreshLayout.setRefreshing(false);
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);
                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.tv_offline, R.id.btn_create})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.tv_offline:{
                online = !online;
                if(!online){
                    tvOffline.setText(R.string.btn_service_online);
                }else{
                    tvOffline.setText(R.string.btn_service_offline);
                }

                mySwipeRefreshLayout.post(()->{
                    mySwipeRefreshLayout.setRefreshing(true);
                    pageNum=1;
                    doQuery();
                });

                break;
            }
            case R.id.btn_create:{
                Intent intent = new Intent(this, PublishServiceActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void updateServiceEvent(UpdateServiceEvent event){
        serviceAdapter.update(event.getService());
        loadMoreWrapper.notifyDataSetChanged();
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void updateServiceCoverEvent(UpdateServiceCoverEvent event){
        serviceAdapter.updateCover(event.getService());
        loadMoreWrapper.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();

        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    private class MenuPopupwindow implements View.OnClickListener {
        private PopupWindow popupWindow;
        private Service service;

        private TextView tvTitle;
        private ImageView ivImage;
        private TextView tvView;
        private TextView tvCount;
        private Button btnOffline;

        private void init() {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_service_menu, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.getBackground().setAlpha(170);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ViewUtils.background(ServiceManagementActivity.this, 1f);
                }
            });

            tvTitle = view.findViewById(R.id.tv_title);
            ivImage = view.findViewById(R.id.iv_image);
            tvView = view.findViewById(R.id.tv_view);
            tvCount = view.findViewById(R.id.tv_count);
            btnOffline = view.findViewById(R.id.btn_offline);

            view.findViewById(R.id.iv_close).setOnClickListener(this);
            view.findViewById(R.id.btn_offline).setOnClickListener(this);
            view.findViewById(R.id.ll_banner).setOnClickListener(this);
            view.findViewById(R.id.ll_service).setOnClickListener(this);
            view.findViewById(R.id.ll_promotion).setOnClickListener(this);
            view.findViewById(R.id.ll_privilege).setOnClickListener(this);
            view.findViewById(R.id.ll_process).setOnClickListener(this);
            view.findViewById(R.id.ll_introduction).setOnClickListener(this);
        }

        public boolean isShowing(){
            return popupWindow.isShowing();
        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public void show(Service service){
            this.service = service;

            if(service.getStatus().equals(Constants.Status.Service.yes))
                btnOffline.setText(R.string.btn_offline_service);
            else
                btnOffline.setText(R.string.btn_online_service);

            Glide.with(getApplicationContext())
                    .load(service.getCover())
                    .transform(new GlideRoundTransformation(getApplicationContext(), 5))
                    .into(ivImage);

            tvTitle.setText(service.getName());
            tvView.setText(String.valueOf(service.getBrowseSum()));
            tvCount.setText(String.valueOf(service.getCollectionSum()));

            ViewUtils.background(ServiceManagementActivity.this, 0.8f);
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }

        @Override
        public void onClick(View view) {
            Intent intent = null;
            switch (view.getId()){
                case R.id.iv_close:{
                    dismiss();
                    break;
                }
                case R.id.btn_offline:{
                    showDialog(service);
                    dismiss();
                    break;
                }
                case R.id.ll_banner:{
                    intent = new Intent(getApplicationContext(), PublishServiceBannerActivity.class);
                    break;
                }
                case R.id.ll_service:{
                    intent = new Intent(getApplicationContext(), PublishServiceActivity.class);
                    break;
                }
                case R.id.ll_promotion:{
                    intent = new Intent(getApplicationContext(), PublishServicePromotionActivity.class);
                    break;
                }
                case R.id.ll_privilege:{
                    intent = new Intent(getApplicationContext(), PublishServicePrivilegeActivity.class);
                    break;
                }
                case R.id.ll_process:{
                    intent = new Intent(getApplicationContext(), PublishServiceProcessActivity.class);
                    break;
                }
                case R.id.ll_introduction:{
                    intent = new Intent(getApplicationContext(), PublishServiceIntroductionActivity.class);
                    break;
                }
            }

            if(intent != null) {
                intent.putExtra(Constants.Tag.data, service.getId());
                intent.putExtra(Constants.Tag.modify, true);
                intent.putExtra(Constants.Tag.gonext, false);
                dismiss();
                startActivity(intent);
            }
        }
    }
}
