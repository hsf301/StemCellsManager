package com.huashengfu.StemCellsManager.activity.goods;

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
import com.huashengfu.StemCellsManager.adapter.goods.GoodsAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Goods;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.event.goods.UpdateGoodsInfoEvent;
import com.huashengfu.StemCellsManager.event.goods.UpdateGoodsPicEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.loadmore.EndlessRecyclerOnScrollListener;
import com.huashengfu.StemCellsManager.loadmore.LoadMoreWrapper;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.liteav.demo.play.SuperPlayerModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//商品管理
public class GoodsManagementActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;
    @BindView(R.id.tv_offline)
    TextView tvOffline;

    private GoodsAdapter goodsAdapter;
    private LoadMoreWrapper loadMoreWrapper;

    private int pageNum = 1;
    private int pageSize = 10;
    private int pageCount = 1;

    private boolean online = true;

    private MenuPopupwindow menuPopupwindow;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_management);

        unbinder = ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        menuPopupwindow = new MenuPopupwindow();
        menuPopupwindow.init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        goodsAdapter = new GoodsAdapter();
        goodsAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Goods>() {
            @Override
            public void onItemClick(View view, Goods goods) {
                switch (view.getId()){
                    case R.id.btn_offline:{
                        showDialog(goods);
                        break;
                    }
                    case R.id.iv_menu:{
                        menuPopupwindow.show(goods);
                        break;
                    }
                    default:{
                        Intent intent = new Intent(getApplicationContext(), PreviewGoodsActivity.class);
                        intent.putExtra(Constants.Tag.data, goods);
                        startActivity(intent);
                        break;
                    }
                }
            }
        });
        loadMoreWrapper = new LoadMoreWrapper(goodsAdapter);

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

    private void showDialog(Goods goods){
        AlertDialog dialog = new AlertDialog.Builder(GoodsManagementActivity.this)
                .setMessage(goods.getIsUpperShelf().equals(Constants.Status.Goods.yes) ? R.string.dialog_message_offline_goods : R.string.dialog_message_online_goods)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        OkGo.<JSONObject>put(HttpHelper.Url.Goods.updatePublishStatus + goods.getGoodsId() + "?" +
                                    HttpHelper.Params.publishStatus + "=" + (goods.getIsUpperShelf().equals(Constants.Status.Service.yes) ?
                                    Constants.Status.Service.no : Constants.Status.Service.yes))
                                .tag(this)
                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                .execute(new DialogCallback<JSONObject>(GoodsManagementActivity.this, false) {
                                    @Override
                                    public void onFinish() {
                                        super.onFinish();
                                    }

                                    @Override
                                    public void onSuccess(Response<JSONObject> response) {
                                        super.onSuccess(response);
                                        try {
                                            if(ResponseUtils.ok(response.body())){
                                                int position = goodsAdapter.remove(goods);
                                                loadMoreWrapper.notifyItemRemoved(position);
                                                loadMoreWrapper.notifyDataSetChanged();

                                                showMessage(goods.getIsUpperShelf().equals(Constants.Status.Goods.yes) ? R.string.success_update_offline : R.string.success_update_online);
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
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Goods.list)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.pageNum, pageNum)
                .params(HttpHelper.Params.pageSize, pageSize)
                .params(HttpHelper.Params.publishStatus, online ? Constants.Status.Goods.yes : Constants.Status.Goods.no)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                PageResponse<Goods> pageResponse = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<PageResponse<Goods>>(){}.getType());

                                pageCount = pageResponse.getTotalPage();

                                if(pageNum == 1)
                                    goodsAdapter.clear();

                                goodsAdapter.addAll(pageResponse.getList());
                                goodsAdapter.notifyDataSetChanged();

                                if(goodsAdapter.getItemCount() == 0){
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

                        if(goodsAdapter.getItemCount() == 0){
                            showEmpty(emptyPage, R.string.text_empty_goods,true);
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
                    tvOffline.setText(R.string.btn_goods_online);
                }else{
                    tvOffline.setText(R.string.btn_goods_offline);
                }

                mySwipeRefreshLayout.post(()->{
                    pageNum=1;
                    mySwipeRefreshLayout.setRefreshing(true);
                    doQuery();
                });
                break;
            }
            case R.id.btn_create:{
                Intent intent = new Intent(this, PublishGoodsBannerActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateGoodsPic(UpdateGoodsPicEvent event){
        goodsAdapter.updatePic(event.getGoods());
        loadMoreWrapper.notifyDataSetChanged();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateGoodsInfo(UpdateGoodsInfoEvent event){
        goodsAdapter.updateInfo(event.getGoods());
        loadMoreWrapper.notifyDataSetChanged();
    }

    private class MenuPopupwindow implements View.OnClickListener {
        private PopupWindow popupWindow;
        private Goods goods;

        private TextView tvContent;
        private ImageView ivImage;
        private TextView tvCount;
        private Button btnOffline;

        private void init() {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_goods_menu, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.getBackground().setAlpha(170);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ViewUtils.background(GoodsManagementActivity.this, 1f);
                }
            });

            tvContent = view.findViewById(R.id.tv_content);
            ivImage = view.findViewById(R.id.iv_image);
            tvCount = view.findViewById(R.id.tv_count);
            btnOffline = view.findViewById(R.id.btn_offline);



            view.findViewById(R.id.iv_close).setOnClickListener(this);
            view.findViewById(R.id.btn_offline).setOnClickListener(this);
            view.findViewById(R.id.ll_banner).setOnClickListener(this);
            view.findViewById(R.id.ll_informaction).setOnClickListener(this);
            view.findViewById(R.id.ll_detail).setOnClickListener(this);
            view.findViewById(R.id.ll_inventory).setOnClickListener(this);
            view.findViewById(R.id.ll_parameters).setOnClickListener(this);
        }

        public boolean isShowing(){
            return popupWindow.isShowing();
        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public void show(Goods goods){
            this.goods = goods;

            if(goods.getIsUpperShelf().equals(Constants.Status.Goods.yes))
                btnOffline.setText(R.string.btn_offline_goods);
            else
                btnOffline.setText(R.string.btn_online_goods);

            tvContent.setText(goods.getName());
            tvCount.setText(String.format(getResources().getString(R.string.str_goods_count), goods.getSellSum()));

            Glide.with(getApplicationContext())
                    .load(goods.getFirstPicUrl())
                    .transform(new GlideRoundTransformation(getApplicationContext(), 5))
                    .into(ivImage);

            ViewUtils.background(GoodsManagementActivity.this, 0.8f);
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
                    showDialog(goods);
                    dismiss();
                    break;
                }
                case R.id.ll_banner:{
                    intent = new Intent(getApplicationContext(), ModifyGoodsBannerActivity.class);
                    break;
                }
                case R.id.ll_informaction:{
                    intent = new Intent(getApplicationContext(), ModifyGoodsInformactionActivity.class);
                    break;
                }
                case R.id.ll_detail:{
                    intent = new Intent(getApplicationContext(), PublishGoodsDetailActivity.class);
                    intent.putExtra(Constants.Tag.modify, true);
                    break;
                }
                case R.id.ll_inventory:{
                    intent = new Intent(getApplicationContext(), ModifySpecificationsActivity.class);
                    break;
                }
                case R.id.ll_parameters:{
                    intent = new Intent(getApplicationContext(), ModifyGoodsParametersActivity.class);
                    break;
                }
            }

            if(intent != null) {
                intent.putExtra(Constants.Tag.data, goods);
                startActivity(intent);
                dismiss();
            }
        }
    }
}
