package com.huashengfu.StemCellsManager.activity.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.flag.FlagAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.activity.detail.ActivityDetailAdapter;
import com.huashengfu.StemCellsManager.entity.activity.Activity;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.view.FlowLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class PreviewActivityActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.rv_flag)
    RecyclerView rvFlag;
    @BindView(R.id.scrollview)
    NestedScrollView scrollView;

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.map)
    MapView mapView;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_quota)
    TextView tvQuota;
    @BindView(R.id.tv_hour)
    TextView tvHour;
    @BindView(R.id.tv_day)
    TextView tvDay;
    @BindView(R.id.ll_day)
    LinearLayout llDay;
    @BindView(R.id.ll_time_countdown)
    LinearLayout llTimeCountdown;
    @BindView(R.id.ll_quota)
    LinearLayout llQuota;

    private Unbinder unbinder;
    private AMap aMap;
    private Activity activity;

    private FlagAdapter flagAdapter;
    private ActivityDetailAdapter activityDetailAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_preview);

        unbinder = ButterKnife.bind(this);

        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        aMap.getUiSettings().setAllGesturesEnabled(false);
        aMap.getUiSettings().setZoomControlsEnabled(false);

        activity = (Activity) getIntent().getSerializableExtra(Constants.Tag.data);

        initFlag();
        initDetail();

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

    private void initFlag(){
        FlowLayoutManager layoutManager = new FlowLayoutManager(this, false);
        flagAdapter = new FlagAdapter(false);
        rvFlag.setLayoutManager(layoutManager);
        rvFlag.setAdapter(flagAdapter);
    }

    private void initDetail(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        activityDetailAdapter = new ActivityDetailAdapter();
        rvList.setAdapter(activityDetailAdapter);
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Activity.info + "?" + HttpHelper.Params.id + "=" + activity.getId())
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                Activity activity = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<Activity>(){}.getType());

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");

                                Glide.with(getApplicationContext())
                                        .load(activity.getBanner())
                                        .placeholder(R.drawable.image_loading_pic)
                                        .transform(new GlideRoundTransformation(getApplicationContext(), 10))
                                        .into(ivImage);

                                tvTitle.setText(activity.getTitle());
                                tvContent.setText(activity.getSubTitle());
                                tvAddress.setText(activity.getAddr());
                                tvDate.setText(sdf.format(new Date(activity.getStartDate())) + " " + StringUtils.getWeek(activity.getStartDate()));

                                LatLng latLng = new LatLng(activity.getLatitude(), activity.getLongitude());
                                aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker)));
                                aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                                flagAdapter.clear();
                                if(activity.getLabels() != null){
                                    flagAdapter.addAll(activity.getLabels());
                                }
                                flagAdapter.notifyDataSetChanged();

                                activityDetailAdapter.clear();
                                if(activity.getDetails() != null){
                                    activityDetailAdapter.addAll(activity.getDetails());
                                }
                                activityDetailAdapter.notifyDataSetChanged();

                                tvQuota.setText(String.valueOf(activity.getSurplusQuota()));



                                switch (activity.getActivityStatus()){
                                    case Constants.Status.Activity.notStarted:{
                                        long time = activity.getStartDate() - System.currentTimeMillis();
                                        int day = (int) (time / (24 * 60 * 60 * 1000));
                                        int hour = (int) ((time - (long) day * 24 * 60 * 60 * 1000) / (60 * 60 * 1000));

                                        if(hour < 0)
                                            hour = 0;

                                        if(day > 0){
                                            llDay.setVisibility(View.VISIBLE);
                                        }else{
                                            llDay.setVisibility(View.GONE);
                                        }

                                        tvDay.setText(String.valueOf(day));
                                        tvHour.setText(String.valueOf(hour));
                                        break;
                                    }
                                    case Constants.Status.Activity.finished:{
                                        llTimeCountdown.setVisibility(View.GONE);
                                        llQuota.setVisibility(View.GONE);
                                        break;
                                    }
                                    case Constants.Status.Activity.progress:{
                                        long time = activity.getEndDate() - activity.getStartDate();
                                        Log.i(Constants.Log.Log, "time --> " + time);
                                        int day = (int) (time / (24 * 60 * 60 * 1000));
                                        int hour = (int) ((time - (long) day * 24 * 60 * 60 * 1000) / (60 * 60 * 1000));

                                        if(hour < 0)
                                            hour = 0;

                                        if(day > 0){
                                            llDay.setVisibility(View.VISIBLE);
                                        }else{
                                            llDay.setVisibility(View.GONE);
                                        }

                                        tvDay.setText(String.valueOf(day));
                                        tvHour.setText(String.valueOf(hour));
                                        break;
                                    }
                                }



                                scrollView.setVisibility(View.VISIBLE);
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
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
