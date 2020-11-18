package com.huashengfu.StemCellsManager.activity.service;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.ImageViewAdapter;
import com.huashengfu.StemCellsManager.adapter.flag.FlagAdapter;
import com.huashengfu.StemCellsManager.adapter.service.detail.ServiceIntroductionAdapter;
import com.huashengfu.StemCellsManager.adapter.service.detail.ServiceProcessStepAdapter;
import com.huashengfu.StemCellsManager.adapter.service.detail.ServiceQAAdapter;
import com.huashengfu.StemCellsManager.adapter.service.detail.ServiceDiscountAdapter;
import com.huashengfu.StemCellsManager.adapter.service.detail.ServiceSummaryPart1Adapter;
import com.huashengfu.StemCellsManager.adapter.service.detail.ServiceSummaryPart2Adapter;
import com.huashengfu.StemCellsManager.entity.response.sms.ServiceInfo;
import com.huashengfu.StemCellsManager.entity.response.sms.detail.Process;
import com.huashengfu.StemCellsManager.entity.response.sms.detail.ProcessDay;
import com.huashengfu.StemCellsManager.entity.service.Discount;
import com.huashengfu.StemCellsManager.entity.service.ProcessSchedule;
import com.huashengfu.StemCellsManager.entity.service.ProcessStep;
import com.huashengfu.StemCellsManager.entity.service.QA;
import com.huashengfu.StemCellsManager.entity.service.Service;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.view.FlowLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 服务预览
public class PreviewServiceActivity extends BaseActivity {

    @BindView(R.id.vp_image)
    ViewPager vpImage;
    @BindView(R.id.ll_dot)
    LinearLayout llDot;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.rv_flag)
    RecyclerView rvFlag;
    @BindView(R.id.rv_discount)
    RecyclerView rvDisCount;
    @BindView(R.id.tv_discount)
    TextView tvDiscount;
    @BindView(R.id.rv_qa)
    RecyclerView rvQa;
    @BindView(R.id.rv_summary_part_1)
    RecyclerView rvSummaryPart1;
    @BindView(R.id.rv_summary_part_2)
    RecyclerView rvSummaryPart2;
    @BindView(R.id.rv_process)
    RecyclerView rvProcess;
    @BindView(R.id.rv_details)
    RecyclerView rvDetails;

    @BindView(R.id.scrollview)
    NestedScrollView scrollView;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;

    private ImageViewAdapter imageViewAdapter;
    private FlagAdapter flagAdapter;
    private ServiceDiscountAdapter serviceDiscountAdapter;
    private ServiceQAAdapter serviceQAAdapter;
    private ServiceSummaryPart1Adapter serviceSummaryPart1Adapter;
    private ServiceSummaryPart2Adapter serviceSummaryPart2Adapter;
    private ServiceProcessStepAdapter serviceProcessStepAdapter;
    private ServiceIntroductionAdapter serviceIntroductionAdapter;
    private Unbinder unbinder;

    private Service service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_preview);

        unbinder = ButterKnife.bind(this);

        service = (Service) getIntent().getSerializableExtra(Constants.Tag.data);

        initFlag();
        initDiscount();
        initQa();
        initSummaryPart1();
        initSummaryPart2();
        initProcess();
        initServiceIntroduction();

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

        mySwipeRefreshLayout.postDelayed(()->{
            mySwipeRefreshLayout.setRefreshing(true);
            doQuery();
        }, 200);

    }

    private void initServiceIntroduction(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDetails.setLayoutManager(layoutManager);
        rvDetails.setNestedScrollingEnabled(false);
        serviceIntroductionAdapter = new ServiceIntroductionAdapter();
        rvDetails.setAdapter(serviceIntroductionAdapter);
    }

    private void initProcess(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvProcess.setLayoutManager(layoutManager);
        rvProcess.setNestedScrollingEnabled(false);
        serviceProcessStepAdapter = new ServiceProcessStepAdapter();
        rvProcess.setAdapter(serviceProcessStepAdapter);
    }

    private void initSummaryPart2(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvSummaryPart2.setLayoutManager(layoutManager);
        rvSummaryPart2.setNestedScrollingEnabled(false);
        serviceSummaryPart2Adapter = new ServiceSummaryPart2Adapter();
        rvSummaryPart2.setAdapter(serviceSummaryPart2Adapter);
    }

    private void initSummaryPart1(){
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        rvSummaryPart1.setLayoutManager(layoutManager);
        rvSummaryPart1.setNestedScrollingEnabled(false);
        serviceSummaryPart1Adapter = new ServiceSummaryPart1Adapter();
        rvSummaryPart1.setAdapter(serviceSummaryPart1Adapter);
    }

    private void initQa(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvQa.setLayoutManager(layoutManager);
        rvQa.setNestedScrollingEnabled(false);
        serviceQAAdapter = new ServiceQAAdapter();
        serviceQAAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<QA>() {
            @Override
            public void onItemClick(View view, QA qa) {

            }
        });
        rvQa.setAdapter(serviceQAAdapter);
    }

    private void initDiscount(){
        FlowLayoutManager layoutManager = new FlowLayoutManager(this, false);
        rvDisCount.setLayoutManager(layoutManager);
        serviceDiscountAdapter = new ServiceDiscountAdapter();
        rvDisCount.setAdapter(serviceDiscountAdapter);
        rvDisCount.setNestedScrollingEnabled(false);
    }

    private void initFlag(){
        FlowLayoutManager layoutManager = new FlowLayoutManager(this, false);
        rvFlag.setLayoutManager(layoutManager);
        flagAdapter = new FlagAdapter();
        rvFlag.setAdapter(flagAdapter);
        rvFlag.setNestedScrollingEnabled(false);
    }

    private void initImage(List<String> images){
        List<ImageView> imageViews = new ArrayList<>();
        llDot.removeAllViews();

        for(int i=0; i<images.size(); i++){
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            Glide.with(this)
                    .load(images.get(i))
                    .into(imageView);

            View view = getLayoutInflater().inflate(R.layout.adapter_dot, null);
            llDot.addView(view);

            imageViews.add(imageView);
        }

        imageViewAdapter = new ImageViewAdapter(imageViews);
        vpImage.setAdapter(imageViewAdapter);
        vpImage.setCurrentItem(0);
        vpImage.setOffscreenPageLimit(imageViews.size());
        vpImage.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetDot(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        resetDot(0);
    }

    private void resetDot(int position){
        for(int i=0; i<llDot.getChildCount(); i++){
            View view = llDot.getChildAt(i);
            View dot = view.findViewById(R.id.dot);
            if(i==position){
                dot.setBackgroundResource(R.drawable.bg_dot_blue);
            }else{
                dot.setBackgroundResource(R.drawable.bg_dot_white);
            }
        }
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Service.serversInfo + "?" + HttpHelper.Params.id + "=" + service.getId())
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                ServiceInfo serviceInfo = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<ServiceInfo>(){}.getType());

                                // 轮播图
                                initImage(serviceInfo.getBannerList());

                                // 服务信息
                                tvTitle.setText(serviceInfo.getName());
                                tvContent.setText(serviceInfo.getContent());

                                // 标签
                                List<String> flags = new ArrayList<>();
                                if(serviceInfo.getLabelList() != null){
                                    for(int i=0; i<serviceInfo.getLabelList().size(); i++){
                                        flags.add(serviceInfo.getLabelList().get(i).getName());
                                    }
                                }
                                flagAdapter.clear();
                                flagAdapter.addAll(flags);
                                flagAdapter.notifyDataSetChanged();

                                // 优惠信息
                                tvDiscount.setText(serviceInfo.getCharacteristic().getDetails());

                                List<Discount> discounts = new ArrayList<>();
                                for(String str : serviceInfo.getCharacteristic().getTitleList()){
                                    Discount discount = new Discount();
                                    discount.setName(str);
                                    discounts.add(discount);
                                }
                                serviceDiscountAdapter.clear();
                                serviceDiscountAdapter.addAll(discounts);
                                serviceDiscountAdapter.notifyDataSetChanged();

                                // 服务摘要
                                serviceSummaryPart1Adapter.clear();
                                serviceSummaryPart1Adapter.addAll(serviceInfo.getOutLineList());
                                serviceSummaryPart1Adapter.notifyDataSetChanged();

                                serviceSummaryPart2Adapter.clear();
                                serviceSummaryPart2Adapter.addAll(serviceInfo.getServicesOutLineList());
                                serviceSummaryPart2Adapter.notifyDataSetChanged();

                                // 服务流程
                                serviceProcessStepAdapter.clear();
                                for(Process process : serviceInfo.getProcessList()){
                                    ProcessStep step = new ProcessStep();
                                    step.setTitle(process.getTitle());
                                    for(ProcessDay day : process.getProcessList()){
                                        ProcessSchedule schedule = new ProcessSchedule();
                                        schedule.setName(day.getName());
                                        schedule.setContent(day.getContent());

                                        step.getProcessSchedules().add(schedule);
                                    }

                                    serviceProcessStepAdapter.add(step);
                                }
                                serviceProcessStepAdapter.notifyDataSetChanged();

                                // 服务介绍
                                serviceIntroductionAdapter.clear();
                                serviceIntroductionAdapter.addAll(serviceInfo.getDetailsList());
                                serviceIntroductionAdapter.notifyDataSetChanged();

                                scrollView.setVisibility(View.VISIBLE);
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
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @OnClick(R.id.iv_back)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
