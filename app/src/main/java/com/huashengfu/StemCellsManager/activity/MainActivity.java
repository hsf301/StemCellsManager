package com.huashengfu.StemCellsManager.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.BuildConfig;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.activity.ActivityManagementActivity;
import com.huashengfu.StemCellsManager.activity.activity.PublishActivityBannerActivity;
import com.huashengfu.StemCellsManager.activity.dynamic.DynamicManagementActivity;
import com.huashengfu.StemCellsManager.activity.dynamic.PublishDynamicActivity;
import com.huashengfu.StemCellsManager.activity.funs.FunsActivity;
import com.huashengfu.StemCellsManager.activity.goods.GoodsManagementActivity;
import com.huashengfu.StemCellsManager.activity.goods.OrdersManagerActivity;
import com.huashengfu.StemCellsManager.activity.goods.PublishGoodsBannerActivity;
import com.huashengfu.StemCellsManager.activity.interaction.InteractionActivity;
import com.huashengfu.StemCellsManager.activity.service.PublishServiceActivity;
import com.huashengfu.StemCellsManager.activity.service.ServiceManagementActivity;
import com.huashengfu.StemCellsManager.activity.settings.CustomerServiceActivity;
import com.huashengfu.StemCellsManager.activity.settings.SetupActivity;
import com.huashengfu.StemCellsManager.activity.settings.ad.AdvertisingSettingsActivity;
import com.huashengfu.StemCellsManager.activity.settings.branch.BranchActivity;
import com.huashengfu.StemCellsManager.activity.settings.user.ImproveInformationActivity;
import com.huashengfu.StemCellsManager.activity.settings.user.ModifyPasswordActivity;
import com.huashengfu.StemCellsManager.db.DbHandler;
import com.huashengfu.StemCellsManager.entity.User;
import com.huashengfu.StemCellsManager.entity.response.admin.Info;
import com.huashengfu.StemCellsManager.entity.response.admin.ShowNum;
import com.huashengfu.StemCellsManager.event.RefreshInfoEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.GlideCircleTransformation;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_zrsj)
    TextView tvZrsj;
    @BindView(R.id.tv_wdfw)
    TextView tvWdfw;
    @BindView(R.id.tv_wdsp)
    TextView tvWdsp;
    @BindView(R.id.tv_wddt)
    TextView tvWddt;
    @BindView(R.id.tv_wdhd)
    TextView tvWdhd;
    @BindView(R.id.tv_zszfw)
    TextView tvZszfw;
    @BindView(R.id.tv_dclzx)
    TextView tvDclzx;
    @BindView(R.id.tv_spgl)
    TextView tvSpgl;

    @BindView(R.id.tv_fss)
    TextView tvFss;
    @BindView(R.id.tv_fws)
    TextView tvFws;
    @BindView(R.id.tv_dts)
    TextView tvDts;
    @BindView(R.id.tv_sps)
    TextView tvSps;
    @BindView(R.id.tv_hds)
    TextView tvHds;

    @BindView(R.id.tv_dtzxl)
    TextView tvDtzxl;
    @BindView(R.id.tv_dtpll)
    TextView tvDtpll;
    @BindView(R.id.tv_dtdzl)
    TextView tvDtdzl;
    @BindView(R.id.tv_fwzxl)
    TextView tvFwzxl;
    @BindView(R.id.tv_fwbml)
    TextView tvFwbml;
    @BindView(R.id.tv_fwzxsl)
    TextView tvFwzxsl;
    @BindView(R.id.tv_spzxl)
    TextView tvSpzxl;
    @BindView(R.id.tv_spscl)
    TextView tvSpscl;
    @BindView(R.id.tv_spddl)
    TextView tvSpddl;

    @BindView(R.id.tv_zxzs)
    TextView tvZxzs;
    @BindView(R.id.tv_ddzs)
    TextView tvDdzs;
    @BindView(R.id.tv_bmzs)
    TextView tvBmzs;
    @BindView(R.id.tv_dtplzs)
    TextView tvDtplzs;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.scrollview)
    NestedScrollView scrollView;

    private Unbinder unbinder;

    private Info info;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unbinder = ButterKnife.bind(this);

        tvZrsj.setText(String.format(getResources().getString(R.string.str_home_tips_1), "12:00"));
        tvWdfw.setText(String.format(getResources().getString(R.string.str_home_tips_2), "0", "0"));
        tvWdsp.setText(String.format(getResources().getString(R.string.str_home_tips_3), "0", "0"));
        tvWddt.setText(String.format(getResources().getString(R.string.str_home_tips_4), "0"));
        tvWdhd.setText(String.format(getResources().getString(R.string.str_home_tips_5), "0", "0"));

        tvZszfw.setText(String.format(getResources().getString(R.string.str_home_item_2_2), ""));
        tvSpgl.setText(String.format(getResources().getString(R.string.str_home_item_3_2), ""));

        EventBus.getDefault().register(this);

        mySwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorSwipeRefreshLayout1),
                getResources().getColor(R.color.colorSwipeRefreshLayout2),
                getResources().getColor(R.color.colorSwipeRefreshLayout3)
        );
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doQuery();
            }
        });

        mySwipeRefreshLayout.post(()->{
            mySwipeRefreshLayout.setRefreshing(true);
            doQuery();
        });
    }

    @OnClick({R.id.iv_icon, R.id.ll_name, R.id.iv_home})
    public void onUserClick(View view){
        switch (view.getId()){
            case R.id.iv_icon:
            case R.id.ll_name:{
                Intent intent = new Intent(getApplicationContext(), ImproveInformationActivity.class);
                if(info != null && info.getStoreInfo() != null)
                    intent.putExtra(Constants.Tag.data, info.getStoreInfo());
                startActivity(intent);
                break;
            }
            case R.id.iv_home:{
                Intent intent = new Intent(this, SetupActivity.class);
                startActivity(intent);
                break;
            }
        }
    }

    @OnClick({R.id.ll_fbfw, R.id.ll_zszfw, R.id.ll_dclzx,
            R.id.ll_fbxsp, R.id.ll_spgl, R.id.ll_dcldd,
            R.id.ll_fbdt, R.id.ll_dtgl, R.id.ll_dtpl,
            R.id.ll_cjhd, R.id.ll_hdgl, R.id.ll_hdbm,
            R.id.ll_wdhd, R.id.ll_fzjg, R.id.ll_ggsz, R.id.ll_kfsz, R.id.ll_xgmm,
            R.id.ll_fss, R.id.ll_fws, R.id.ll_dts, R.id.ll_sps, R.id.ll_hds,
            R.id.iv_icon, R.id.ll_name, R.id.iv_home
    })
    public void onClick(View view){
        if(info == null){
            showMessage(R.string.error_user_info_empty);
            return;
        }

        if(!BuildConfig.test){
            if(info.getStoreInfo().getStatus() == Constants.Status.User.uncheck){
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage(R.string.dialog_message_improve_information)
                        .setPositiveButton(R.string.btn_immediate, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getApplicationContext(), ImproveInformationActivity.class);
                                if(info != null && info.getStoreInfo() != null)
                                    intent.putExtra(Constants.Tag.data, info.getStoreInfo());
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.btn_not_yet, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                return;
            }else if(info.getStoreInfo().getStatus() == Constants.Status.User.checking){
                showMessage(R.string.tips_user_info_checking);
                return;
            }else if(info.getStoreInfo().getStatus() == Constants.Status.User.reject){
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage(R.string.dialog_message_improve_information_reject)
                        .setPositiveButton(R.string.btn_immediate, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                Intent intent = new Intent(getApplicationContext(), ImproveInformationActivity.class);
                                if(info != null && info.getStoreInfo() != null)
                                    intent.putExtra(Constants.Tag.data, info.getStoreInfo());
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(R.string.btn_not_yet, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .create();
                dialog.show();
                return;
            }
        }

        Intent intent = null;
        switch (view.getId()){
            case R.id.ll_fss:{
                // 粉丝
                intent = new Intent(this, FunsActivity.class);
                break;
            }
            case R.id.ll_fws:{
                intent = new Intent(this, ServiceManagementActivity.class);
                break;
            }
            case R.id.ll_dts:{
                intent = new Intent(this, DynamicManagementActivity.class);
                break;
            }
            case R.id.ll_sps:{
                intent = new Intent(this, GoodsManagementActivity.class);
                break;
            }
            case R.id.ll_hds:{
                intent = new Intent(this, ActivityManagementActivity.class);
                break;
            }
            case R.id.ll_fbfw:{
                // 发布新服务
                intent = new Intent(this, PublishServiceActivity.class);
                break;
            }
            case R.id.ll_zszfw:{
                // 展示中服务
                intent = new Intent(this, ServiceManagementActivity.class);
                break;
            }
            case R.id.ll_dclzx:{
                // 待处理咨询 - 我的互动 - 服务咨询
                intent = new Intent(this, InteractionActivity.class);
                intent.putExtra(Constants.Tag.data, Constants.Type.Interaction.fwzx);
                break;
            }
            case R.id.ll_fbxsp:{
                // 发布新商品
                intent = new Intent(this, PublishGoodsBannerActivity.class);
                break;
            }
            case R.id.ll_spgl:{
                // 商品管理
                intent = new Intent(this, GoodsManagementActivity.class);
                break;
            }
            case R.id.ll_dcldd:{
                // 待处理订单
                intent = new Intent(this, OrdersManagerActivity.class);
                break;
            }
            case R.id.ll_fbdt:{
                // 发布动态
                intent = new Intent(this, PublishDynamicActivity.class);
                break;
            }
            case R.id.ll_dtgl:{
                // 动态管理
                intent = new Intent(this, DynamicManagementActivity.class);
                break;
            }
            case R.id.ll_dtpl:{
                // 动态评论
                intent = new Intent(this, InteractionActivity.class);
                intent.putExtra(Constants.Tag.data, Constants.Type.Interaction.dtpl);
                break;
            }
            case R.id.ll_cjhd:{
                // 创建活动
                intent = new Intent(this, PublishActivityBannerActivity.class);
                break;
            }
            case R.id.ll_hdgl:{
                // 活动管理
                intent = new Intent(this, ActivityManagementActivity.class);
                break;
            }
            case R.id.ll_hdbm:{
                // 活动报名
                intent = new Intent(this, InteractionActivity.class);
                intent.putExtra(Constants.Tag.data, Constants.Type.Interaction.hdbm);
                break;
            }
            case R.id.ll_wdhd:{
                // 我的互动
                intent = new Intent(this, InteractionActivity.class);
                break;
            }
            case R.id.ll_fzjg:{
                // 分支机构
                intent = new Intent(this, BranchActivity.class);
                break;
            }
            case R.id.ll_ggsz:{
                // 广告设置
                intent = new Intent(this, AdvertisingSettingsActivity.class);
                break;
            }
            case R.id.ll_kfsz:{
                // 客服设置
                intent = new Intent(this, CustomerServiceActivity.class);
                break;
            }
            case R.id.ll_xgmm:{
                // 修改密码
                intent = new Intent(this, ModifyPasswordActivity.class);
                break;
            }
        }

        if(intent != null)
            startActivity(intent);
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Admin.info)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(HttpHelper.Params.token))
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                String json = response.body().toString();
                                StringUtils.print(json);

                                info = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<Info>(){}.getType());

                                User user = Constants.getLastLoginUser(getApplicationContext());
                                DbHandler dbHandler = DbHandler.getInstance(getApplicationContext());
                                user.setStorename(info.getStoreInfo().getName());
                                dbHandler.update(user);

                                tvName.setText(info.getStoreInfo().getName());
                                Glide.with(getApplicationContext())
                                        .load(info.getStoreInfo().getIcon())
                                        .transform(new GlideCircleTransformation(getApplicationContext(), 100))
                                        .placeholder(R.mipmap.icon_header_default)
                                        .into(ivIcon);

                                if(info.getStoreInfo() != null){
                                    tvFss.setText(String.valueOf(info.getStoreInfo().getFansSum()));
                                    tvFws.setText(String.valueOf(info.getStoreInfo().getServiceSum()));
                                    tvDts.setText(String.valueOf(info.getStoreInfo().getDynamicSum()));
                                    tvSps.setText(String.valueOf(info.getStoreInfo().getGoodsSum()));
                                    tvHds.setText(String.valueOf(info.getStoreInfo().getActivitySum()));
                                }


                                if(info.getYesterdayData() != null){
                                    tvDtzxl.setText(String.valueOf(info.getYesterdayData().getDbrowseSum()));
                                    tvDtpll.setText(String.valueOf(info.getYesterdayData().getDcommentSum()));
                                    tvDtdzl.setText(String.valueOf(info.getYesterdayData().getDthumbsUpSum()));

                                    tvFwzxl.setText(String.valueOf(info.getYesterdayData().getSbrowseSum()));
                                    tvFwbml.setText(String.valueOf(info.getYesterdayData().getScollectionSum()));
                                    tvFwzxsl.setText(String.valueOf(info.getYesterdayData().getSconsultSum()));

                                    tvSpzxl.setText(String.valueOf(info.getYesterdayData().getGbrowseSum()));
                                    tvSpscl.setText(String.valueOf(info.getYesterdayData().getGcollectionSum()));
                                    tvSpddl.setText(String.valueOf(info.getYesterdayData().getGsellSum()));
                                }


                                for(ShowNum showNum : info.getShowNum()){
                                    if(showNum.getName().equals("pms")){
                                        tvWdsp.setText(String.format(getResources().getString(R.string.str_home_tips_3),
                                                String.valueOf(showNum.getShowNum()), String.valueOf(showNum.getNoShowNum())));
                                        if(showNum.getShowNum() > 0)
                                            tvSpgl.setText(String.format(getResources().getString(R.string.str_home_item_3_2), String.valueOf(showNum.getShowNum())));
                                        else
                                            tvSpgl.setText(String.format(getResources().getString(R.string.str_home_item_3_2), ""));
                                    }else if(showNum.getName().equals("sms")){
                                        tvWdfw.setText(String.format(getResources().getString(R.string.str_home_tips_2),
                                                String.valueOf(showNum.getShowNum()), String.valueOf(showNum.getNoShowNum())));

                                        if(showNum.getShowNum() > 0)
                                            tvZszfw.setText(String.format(getResources().getString(R.string.str_home_item_2_2), String.valueOf(showNum.getShowNum())));
                                        else
                                            tvZszfw.setText(String.format(getResources().getString(R.string.str_home_item_2_2), ""));
                                    }else if(showNum.getName().equals("dms")){
                                        tvWddt.setText(String.format(getResources().getString(R.string.str_home_tips_4), String.valueOf(showNum.getShowNum())));
                                    }else if(showNum.getName().equals("ams")){
                                        tvWdhd.setText(String.format(getResources().getString(R.string.str_home_tips_5),
                                                String.valueOf(showNum.getShowNum()), String.valueOf(showNum.getShowNum() + showNum.getNoShowNum())));
                                    }
                                }

                                if(info.getNewNumber() != null){
                                    // 动态评论
                                    if(info.getNewNumber().getNewCommentNum() > 0){
                                        tvDtplzs.setText(String.valueOf(info.getNewNumber().getNewCommentNum()));
                                        tvDtplzs.setVisibility(View.VISIBLE);
                                    }else{
                                        tvDtplzs.setVisibility(View.GONE);
                                    }

                                    // 咨询总数
                                    if(info.getNewNumber().getNewConsultNum() > 0){
                                        tvZxzs.setText(String.valueOf(info.getNewNumber().getNewConsultNum()));
                                        tvZxzs.setVisibility(View.VISIBLE);
                                    }else{
                                        tvZxzs.setVisibility(View.GONE);
                                    }

                                    // 待处理订单
                                    if(info.getNewNumber().getNewOrderNum() > 0){
                                        tvDdzs.setText(String.valueOf(info.getNewNumber().getNewOrderNum()));
                                        tvDdzs.setVisibility(View.VISIBLE);
                                    }else{
                                        tvDdzs.setVisibility(View.GONE);
                                    }

                                    // 新的报名
                                    if(info.getNewNumber().getNewSignUpNum() > 0){
                                        tvBmzs.setText(String.valueOf(info.getNewNumber().getNewSignUpNum()));
                                        tvBmzs.setVisibility(View.VISIBLE);
                                    }else{
                                        tvBmzs.setVisibility(View.GONE);
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Subscribe (threadMode = ThreadMode.MAIN)
    public void onRefreshInfo(RefreshInfoEvent event){
        mySwipeRefreshLayout.post(()->{
           mySwipeRefreshLayout.setRefreshing(true);
           doQuery();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }
}
