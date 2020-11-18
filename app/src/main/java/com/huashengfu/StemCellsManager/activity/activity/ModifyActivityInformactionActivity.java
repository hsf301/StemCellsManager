package com.huashengfu.StemCellsManager.activity.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.activity.map.SelectAddressActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.flag.FlagAdapter;
import com.huashengfu.StemCellsManager.entity.activity.Activity;
import com.huashengfu.StemCellsManager.entity.response.activity.Init;
import com.huashengfu.StemCellsManager.entity.response.activity.ParamInfo;
import com.huashengfu.StemCellsManager.event.activity.UpdateActivityEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.huashengfu.StemCellsManager.view.FlowLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 编辑活动信息
public class ModifyActivityInformactionActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_content)
    EditText etContent;

    @BindView(R.id.tv_begin)
    TextView tvBegin;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.map)
    MapView mapView;

    @BindView(R.id.et_person)
    EditText etPerson;
    @BindView(R.id.et_phone)
    EditText etPhone;

    @BindView(R.id.tv_name_count)
    TextView tvNameCount;
    @BindView(R.id.tv_content_count)
    TextView tvContentCount;
    @BindView(R.id.et_flag)
    EditText etFlag;

    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.iv_pic)
    ImageView ivPic;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content1)
    TextView tvContent1;
    @BindView(R.id.tv_content2)
    TextView tvContent2;
    @BindView(R.id.tv_content3)
    TextView tvContent3;

    @BindView(R.id.ll_add_flag)
    LinearLayout llAddFlag;

    private Unbinder unbinder;
    private FlagAdapter flagAdapter;
    private DatePopupwindow datePopupwindow;
    private RxPermissions rxPermissions;
    private AMap aMap;
    private Marker marker;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

    private int id;
    private Init init;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_modify_informaction);

        unbinder = ButterKnife.bind(this);

        datePopupwindow = new DatePopupwindow();
        datePopupwindow.init();

        rxPermissions = new RxPermissions(this);

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > Constants.titleMaxLength){
                    etName.setText(editable.toString().substring(0, Constants.titleMaxLength));
                    etName.setSelection(etName.length());
                }

                tvNameCount.setText(etName.length() + "/" + Constants.titleMaxLength);
            }
        });

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > Constants.contentMaxLength){
                    etContent.setText(editable.toString().substring(0, Constants.contentMaxLength));
                    etContent.setSelection(etContent.length());
                }

                tvContentCount.setText(etContent.length() + "/" + Constants.contentMaxLength);
            }
        });

        etFlag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > Constants.flagMaxLength){
                    etFlag.setText(editable.toString().substring(0, Constants.flagMaxLength));
                    etFlag.setSelection(etFlag.length());
                }
            }
        });

        tvNameCount.setText(etName.length() + "/" + Constants.titleMaxLength);
        tvContentCount.setText(etContent.length() + "/" + Constants.contentMaxLength);

        etPerson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                if(str.contains("-")){
                    str = str.replaceAll("-", "");
                    etPerson.setText(str);
                }
            }
        });

        flagAdapter = new FlagAdapter();
        flagAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener<String>() {
            @Override
            public boolean onItemLongClick(View view, String flag) {
                AlertDialog dialog = new AlertDialog.Builder(ModifyActivityInformactionActivity.this)
                        .setMessage(R.string.dialog_message_delete_service_flag)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int position = flagAdapter.remove(flag);
                                flagAdapter.notifyItemRemoved(position);
                                flagAdapter.notifyDataSetChanged();

                                llAddFlag.setEnabled(true);
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
                return false;
            }
        });
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager(this, false);
        rvList.setLayoutManager(flowLayoutManager);
        rvList.setAdapter(flagAdapter);
        rvList.setNestedScrollingEnabled(false);

        mapView.onCreate(savedInstanceState);
        initMap();

        id = getIntent().getIntExtra(Constants.Tag.data, -1);
        doInit(id, 2);
    }

    private void initMap(){
        aMap = mapView.getMap();
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setAllGesturesEnabled(false);

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                aMap.clear();
                addMarker(new LatLng(location.getLatitude(), location.getLongitude()));
            }
        });
    }

    private void addMarker(LatLng latLng){
        if(marker != null)
            marker.remove();

        marker = aMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_marker)));
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
    }

    private void doInit(int id, int dNo){
        GetRequest<JSONObject> getRequest = OkGo.<JSONObject>get(HttpHelper.Url.Activity.init)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.dNo, dNo);

        if(id > 0){
            getRequest.params(HttpHelper.Params.id, id);
        }

        getRequest.execute(new DialogCallback<JSONObject>(this, false) {
            @Override
            public void onFinish() {
                super.onFinish();
            }

            @Override
            public void onSuccess(Response<JSONObject> response) {
                super.onSuccess(response);
                try {
                    if(ResponseUtils.ok(response.body())){
                        StringUtils.print(response.body().toString());
                        init = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                new TypeToken<Init>(){}.getType());

                        Glide.with(getApplicationContext())
                                .load(init.getDescription().getIcon())
                                .into(ivIcon);

                        Glide.with(getApplicationContext())
                                .load(init.getDescription().getPic())
                                .into(ivPic);

                        tvTitle.setText(init.getDescription().getTitle());
                        tvContent1.setText(init.getDescription().getContent1());
                        tvContent2.setText(init.getDescription().getContent2());
                        tvContent3.setText(init.getDescription().getContent3());

                        if(init.getParamInfo() != null){
                            ParamInfo info = init.getParamInfo();

                            etName.setText(info.getTitle());
                            etContent.setText(info.getSubTitle());
                            tvBegin.setTag(info.getStartDate());
                            tvEnd.setTag(info.getEndDate());
                            etPerson.setText(String.valueOf(info.getQuota()));
                            etAddress.setText(info.getAddr());
                            etPhone.setText(info.getPhone());

                            tvBegin.setText(sdf.format(new Date(info.getStartDate())));
                            tvEnd.setText(sdf.format(new Date(info.getEndDate())));

                            addMarker(new LatLng(info.getLatitude(), info.getLongitude()));

                            flagAdapter.clear();
                            flagAdapter.addAll(info.getLabels());
                            flagAdapter.notifyDataSetChanged();

                            if(flagAdapter.getItemCount() >= Constants.flagMax){
                                llAddFlag.setEnabled(false);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.ll_add_flag, R.id.btn_commit, R.id.tv_begin, R.id.tv_end, R.id.ll_address})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.tv_begin:{
                datePopupwindow.show(tvBegin);
                break;
            }
            case R.id.tv_end:{
                datePopupwindow.show(tvEnd);
                break;
            }
            case R.id.ll_address:{
                rxPermissions.requestEachCombined(new String[]{
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE})
                        .subscribe(permission -> {
                            if(permission.granted){
                                Intent intent = new Intent(this, SelectAddressActivity.class);
                                startActivityForResult(intent, Constants.Request.SelectAddress);
                            }else if(permission.shouldShowRequestPermissionRationale){
                                showMessage(R.string.rationale_ask_again);
                            }else{
                                showMessage(R.string.rationale_cancle);
                            }});

                break;
            }
            case R.id.ll_add_flag:{
                if(etFlag.length() == 0 || etFlag.length() > Constants.flagMaxLength){
                    showMessage(R.string.error_service_flag);
                    break;
                }

                if(flagAdapter.getItemCount() >= Constants.flagMax){
                    showMessage(R.string.error_service_flag);
                    break;
                }

                flagAdapter.add(etFlag.getText().toString());
                flagAdapter.notifyDataSetChanged();
                etFlag.setText("");

                if(flagAdapter.getItemCount() >= Constants.flagMax){
                    llAddFlag.setEnabled(false);
                }

                break;
            }
            case R.id.btn_commit:{
                // 分步处理，此处调用接口，提交本页面编辑的数据
                if(etName.getText().length() == 0){
                    showMessage(etName.getHint());
                    return;
                }

                if(etContent.getText().length() == 0){
                    showMessage(etContent.getHint());
                    return;
                }

                if(tvBegin.getText().length() == 0){
                    showMessage(R.string.error_activity_begin_date);
                    return;
                }

                if(tvEnd.getText().length() == 0){
                    showMessage(R.string.error_activity_end_date);
                    return;
                }

                long begin = (Long) tvBegin.getTag();
                long end = (Long) tvEnd.getTag();

                if(begin > end){
                    showMessage(R.string.error_activity_date);
                    return;
                }

                if(etPerson.getText().length() == 0){
                    showMessage(etPerson.getHint());
                    return;
                }

                if(Integer.parseInt(etPerson.getText().toString()) == 0){
                    showMessage(etPerson.getHint());
                    return;
                }

                if(etAddress.getText().length() == 0){
                    showMessage(etAddress.getHint());
                    return;
                }

                if(etPhone.getText().length() == 0){
                    showMessage(etPhone.getHint());
                    return;
                }

                if(etPhone.getText().length() != 11){
                    showMessage(R.string.error_phone_length);
                    return;
                }


                try {
                    JSONObject obj = new JSONObject();
                    obj.put(HttpHelper.Params.addr, etAddress.getText().toString());
                    obj.put(HttpHelper.Params.startDate, (Long)tvBegin.getTag());
                    obj.put(HttpHelper.Params.endDate, (Long)tvEnd.getTag());

                    obj.put(HttpHelper.Params.latitude, marker.getPosition().latitude);
                    obj.put(HttpHelper.Params.longitude, marker.getPosition().longitude);
                    obj.put(HttpHelper.Params.phone, etPhone.getText().toString());
                    obj.put(HttpHelper.Params.quota, etPerson.getText().toString());
                    obj.put(HttpHelper.Params.title, etName.getText().toString());
                    obj.put(HttpHelper.Params.subTitle, etContent.getText().toString());

                    if(flagAdapter.getItemCount() > 0){
                        String[] labels = new String[flagAdapter.getItemCount()];
                        flagAdapter.getFlags().toArray(labels);
                        obj.put(HttpHelper.Params.labels, new JSONArray(labels));
                    }

                    String tmp = obj.toString().replaceAll("\\\\", "");
                    StringUtils.print(tmp);

                    OkGo.<JSONObject>put(HttpHelper.Url.Activity.modifyInfo + id)
                            .tag(this)
                            .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                            .upJson(tmp)
                            .execute(new DialogCallback<JSONObject>(this, false) {
                                @Override
                                public void onFinish() {
                                    super.onFinish();
                                }

                                @Override
                                public void onSuccess(Response<JSONObject> response) {
                                    super.onSuccess(response);
                                    try {
                                        if(ResponseUtils.ok(response.body())){
                                            Activity activity = new Activity();
                                            activity.setId(id);
                                            activity.setTitle(etName.getText().toString());
                                            activity.setSubTitle(etContent.getText().toString());
                                            activity.setStartDate((Long)tvBegin.getTag());
                                            activity.setEndDate((Long) tvEnd.getTag());
                                            activity.setAddr(etAddress.getText().toString());
                                            EventBus.getDefault().post(new UpdateActivityEvent().setActivity(activity));

                                            showMessage(R.string.success_activity_informaction_modify);
                                            finish();
                                        }else{
                                            showMessage(ResponseUtils.getMsg(response.body()));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.Request.SelectAddress){
                Object object = data.getParcelableExtra(Constants.Tag.data);
                if(object != null && object instanceof PoiItem){
                    PoiItem poiItem = (PoiItem) object;
                    addMarker(new LatLng(poiItem.getLatLonPoint().getLatitude(), poiItem.getLatLonPoint().getLongitude()));
                    etAddress.setText(poiItem.getProvinceName() + poiItem.getCityName() + poiItem.getSnippet());
                }
            }
        }
    }

    private class DatePopupwindow {

        /**
         * 窗口
         */
        private PopupWindow popupWindow;

        private CalendarView calendarView;
        private TextView tvDate;

        private Calendar calendar;
        private SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

        private PopupWindow timePopupWindow;
        private TimePicker timePicker;

        private void init(){
            View view = getLayoutInflater().inflate(R.layout.popupwindow_date, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.getBackground().setAlpha(170);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ViewUtils.background(ModifyActivityInformactionActivity.this, 1f);
                }
            });

            calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());

            calendarView = view.findViewById(R.id.calendarView);
            calendarView.setDate(System.currentTimeMillis());
            calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                @Override
                public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                    calendar.set(Calendar.YEAR, i);
                    calendar.set(Calendar.MONTH, i1);
                    calendar.set(Calendar.DAY_OF_MONTH, i2);
                }
            });

            view.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

            view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();

                    ViewUtils.hideSoftInput(getApplicationContext(), etName);
                    ViewUtils.background(ModifyActivityInformactionActivity.this, 0.8f);
                    timePopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                }
            });

            initTime();
        }

        private void initTime(){
            View view = getLayoutInflater().inflate(R.layout.popupwindow_time, null);
            timePopupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            timePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            timePopupWindow.getBackground().setAlpha(170);
            timePopupWindow.setOutsideTouchable(true);
            timePopupWindow.setFocusable(true);
            timePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            timePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ViewUtils.background(ModifyActivityInformactionActivity.this, 1f);
                }
            });

            timePicker = view.findViewById(R.id.tp_time);
            timePicker.setIs24HourView(true);

            view.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    timePopupWindow.dismiss();
                }
            });

            view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                    calendar.set(Calendar.MINUTE, timePicker.getMinute());

                    tvDate.setText(sdf.format(new Date(calendar.getTimeInMillis())));
                    tvDate.setTag(calendar.getTimeInMillis());
                    timePopupWindow.dismiss();
                }
            });
        }

        public String getDate(){
            return sdf.format(calendarView.getDate());
        }

        public boolean isShowing(){
            return popupWindow.isShowing();
        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public void show(TextView tvDate){
            this.tvDate = tvDate;
            Object obj = tvDate.getTag();
            if(obj != null){
                long time = (long) tvDate.getTag();
                calendarView.setDate(time);
                calendar.setTimeInMillis(time);

                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(time);
                timePicker.setHour(calendar.get(Calendar.HOUR_OF_DAY));
                timePicker.setMinute(calendar.get(Calendar.MINUTE));
            }else{
                calendarView.setDate(System.currentTimeMillis());
            }

            ViewUtils.hideSoftInput(getApplicationContext(), etName);
            ViewUtils.background(ModifyActivityInformactionActivity.this, 0.8f);
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
    }
}
