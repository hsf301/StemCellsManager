package com.huashengfu.StemCellsManager.activity.settings.user;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.AppApplication;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.activity.activity.PublishActivityBannerActivity;
import com.huashengfu.StemCellsManager.activity.activity.PublishActivityInformactionActivity;
import com.huashengfu.StemCellsManager.activity.map.SelectAddressActivity;
import com.huashengfu.StemCellsManager.adapter.settings.enterprise.EnterpriseTypeAdapter;
import com.huashengfu.StemCellsManager.entity.activity.Detail;
import com.huashengfu.StemCellsManager.entity.response.DetailsUpload;
import com.huashengfu.StemCellsManager.entity.response.EnterpriseType;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.entity.response.admin.StoreInfo;
import com.huashengfu.StemCellsManager.event.RefreshInfoEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.convert.JsonConvert;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.BitmapUtils;
import com.huashengfu.StemCellsManager.utils.FileUtil;
import com.huashengfu.StemCellsManager.utils.GlideCircleTransformation;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.ImageUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// 公司信息
public class ImproveInformationActivity extends BaseActivity {

    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.et_qymc)
    EditText etQymc;
    @BindView(R.id.et_qylx)
    EditText etQylx;
    @BindView(R.id.et_dsz)
    EditText etDsz;
    @BindView(R.id.tv_clrq)
    TextView tvClrq;
    @BindView(R.id.tv_gsdz)
    TextView tvGsdz;
    @BindView(R.id.et_qybq)
    EditText etQybq;
    @BindView(R.id.et_qyjs)
    EditText etQyjs;
    @BindView(R.id.et_qygw)
    EditText etQygw;
    @BindView(R.id.et_lxdh)
    EditText etLxdh;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.et_wxh)
    EditText etWxh;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_jglx)
    TextView tvJglx;
    @BindView(R.id.tv_tips_header)
    TextView tvTipsHeader;
    @BindView(R.id.tv_tips_image)
    TextView tvTipsImage;

    private Unbinder unbinder;
    private RxPermissions rxPermissions;
    private AppApplication app;

    private CameraPopupwindow cameraPopupwindow;
    private DatePopupwindow datePopupwindow;
    private TypePopupwindow typePopupwindow;

    private PoiItem poiItem;
    private int currentId;

    private String hearderFileName, imageFileName;

    private String uploadLogo, uploadBanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_improve_information);

        unbinder = ButterKnife.bind(this);

        app = (AppApplication) getApplication();
        rxPermissions = new RxPermissions(this);

        cameraPopupwindow = new CameraPopupwindow();
        cameraPopupwindow.init();

        datePopupwindow = new DatePopupwindow();
        datePopupwindow.init();

        typePopupwindow = new TypePopupwindow();
        typePopupwindow.init();

        etQymc.addTextChangedListener(textWatcher);
        etQylx.addTextChangedListener(textWatcher);
        etDsz.addTextChangedListener(textWatcher);
        etDsz.addTextChangedListener(textWatcher);
        tvClrq.addTextChangedListener(textWatcher);
        tvGsdz.addTextChangedListener(textWatcher);
        etQybq.addTextChangedListener(textWatcher);

        etQygw.addTextChangedListener(textWatcher);
        etLxdh.addTextChangedListener(textWatcher);
        etWxh.addTextChangedListener(textWatcher);

        etQyjs.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tvCount.setText(editable.length() + "/150");
            }
        });

        init();
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            check();
        }
    };

    private void init(){
        if(getIntent() != null){
            Object obj = getIntent().getSerializableExtra(Constants.Tag.data);
            if(obj != null && obj instanceof StoreInfo){
                StoreInfo info = (StoreInfo)obj;

                Glide.with(this)
                        .load(info.getIcon())
                        .placeholder(R.mipmap.icon_header_default)
                        .transform(new GlideCircleTransformation(this, 100))
                        .into(ivHeader);

                hearderFileName = info.getIcon();

                Glide.with(this)
                        .load(info.getBanners())
                        .placeholder(R.drawable.image_loading_pic)
                        .transform(new GlideRoundTransformation(this, 10))
                        .into(ivImage);

                imageFileName = info.getBanners();

                etDsz.setText(info.getCeo());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                tvClrq.setText(sdf.format(new Date(info.getCreateDate())));
                etQyjs.setText(info.getDetails());
                etQylx.setText(info.getEnterpriseType());
                etQymc.setText(info.getEnterpriseName());
                etQygw.setText(info.getOfficialWeb());
                etLxdh.setText(info.getPhone());
                tvGsdz.setText(info.getRegisterAddress());
                etWxh.setText(info.getWeChat());
                etQybq.setText(info.getSketch());

                poiItem = new PoiItem("", new LatLonPoint(info.getLatitude(),info.getLongitude()), "", "");
                poiItem.setCityCode(String.valueOf(info.getCity()));

                typePopupwindow.doQuery(new Runnable() {
                    @Override
                    public void run() {
                        typePopupwindow.setType(info.getType());
                        if(typePopupwindow.getType() != null)
                            tvJglx.setText(typePopupwindow.getType().getName());

                        check();
                    }
                });

                check();
            }
        }
    }

    private void check(){
        boolean ok = etQymc.getText().length() > 0;
        ok &= etQylx.getText().length() > 0;
        ok &= etDsz.getText().length() > 0;
        ok &= tvClrq.getText().length() > 0;
        ok &= tvGsdz.getText().length() > 0;
        ok &= etQybq.getText().length() > 0;
        ok &= etQyjs.getText().length() > 0;
        ok &= etQygw.getText().length() > 0;
        ok &= etLxdh.getText().length() > 0;
        ok &= etWxh.getText().length() > 0;

        btnSave.setEnabled(ok);
    }

    @OnClick({R.id.iv_back, R.id.iv_header, R.id.tv_clrq, R.id.btn_save, R.id.iv_image, R.id.tv_gsdz, R.id.tv_jglx})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.iv_image:
            case R.id.iv_header:{
                currentId = view.getId();
                cameraPopupwindow.show();
                break;
            }
            case R.id.tv_gsdz:{
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
            case R.id.tv_clrq:{
                datePopupwindow.show(tvClrq);
                break;
            }
            case R.id.tv_jglx:{
                typePopupwindow.show();
                break;
            }
            case R.id.btn_save:{
                if(StringUtils.isNullOrBlank(hearderFileName)){
                    showMessage(R.string.error_header);
                    return;
                }

                if(StringUtils.isNullOrBlank(imageFileName)){
                    showMessage(R.string.error_banner);
                    return;
                }

                showDialog(false);

                List<File> logo = new ArrayList<>();
                if(!hearderFileName.startsWith("http"))
                    logo.add(new File(hearderFileName));

                List<File> banner = new ArrayList<>();
                if(!imageFileName.startsWith("http"))
                    banner.add(new File(imageFileName));

                if(!logo.isEmpty() && !banner.isEmpty()){
                    OkGo.<JSONObject>post(HttpHelper.Url.Store.uploadLogo)
                            .tag(this)
                            .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                            .isMultipart(true)
                            .addFileParams(HttpHelper.Params.files, logo)
                            .converter(new JsonConvert())
                            .adapt(new ObservableResponse<JSONObject>())
                            .subscribeOn(Schedulers.io())
                            .flatMap(new Function<Response<JSONObject>, ObservableSource<Response<JSONObject>>>() {

                                @Override
                                public ObservableSource<Response<JSONObject>> apply(Response<JSONObject> jsonObjectResponse) throws Exception {
                                    if(!ResponseUtils.ok(jsonObjectResponse.body())){
                                        throw new Exception("上传图片失败！");
                                    }

                                    List<String> pics = new Gson().fromJson(ResponseUtils.getList(jsonObjectResponse.body()).toString(),
                                            new TypeToken<List<String>>(){}.getType());

                                    uploadLogo = pics.get(0);

                                    Observable<Response<JSONObject>> observable = OkGo.<JSONObject>post(HttpHelper.Url.Store.uploadBanner)
                                            .tag(this)
                                            .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                            .isMultipart(true)
                                            .addFileParams(HttpHelper.Params.files, banner)
                                            .converter(new JsonConvert())
                                            .adapt(new ObservableResponse<JSONObject>());
                                    return observable;
                                }
                            })
                            .flatMap(new Function<Response<JSONObject>, ObservableSource<Response<JSONObject>>>() {

                                @Override
                                public ObservableSource<Response<JSONObject>> apply(Response<JSONObject> jsonObjectResponse) throws Exception {
                                    if(!ResponseUtils.ok(jsonObjectResponse.body())){
                                        throw new Exception("上传图片失败！");
                                    }

                                    List<String> pics = new Gson().fromJson(ResponseUtils.getList(jsonObjectResponse.body()).toString(),
                                            new TypeToken<List<String>>(){}.getType());

                                    uploadBanner = pics.get(0);

                                    JSONObject obj = new JSONObject();

                                    obj.put(HttpHelper.Params.banners, uploadBanner);
                                    obj.put(HttpHelper.Params.ceo, etDsz.getText().toString());
                                    obj.put(HttpHelper.Params.city, poiItem.getCityCode());
                                    obj.put(HttpHelper.Params.createDate, tvClrq.getText().toString());
                                    obj.put(HttpHelper.Params.details, etQyjs.getText().toString());
                                    obj.put(HttpHelper.Params.enterpriseType, etQylx.getText().toString());
                                    obj.put(HttpHelper.Params.enterpriseName, etQymc.getText().toString());
                                    obj.put(HttpHelper.Params.icon, uploadLogo);
                                    obj.put(HttpHelper.Params.latitude, poiItem.getLatLonPoint().getLatitude());
                                    obj.put(HttpHelper.Params.longitude, poiItem.getLatLonPoint().getLongitude());
                                    obj.put(HttpHelper.Params.officialWeb, etQygw.getText().toString());
                                    obj.put(HttpHelper.Params.phone, etLxdh.getText().toString());
                                    obj.put(HttpHelper.Params.registerAddress, tvGsdz.getText().toString());
                                    obj.put(HttpHelper.Params.weChat, etWxh.getText().toString());
                                    obj.put(HttpHelper.Params.type, typePopupwindow.getType().getId());
                                    obj.put(HttpHelper.Params.sketch, etQybq.getText().toString());

                                    String tmp = obj.toString().replaceAll("\\\\", "");
                                    StringUtils.print(tmp);

                                    Observable<Response<JSONObject>> observable = OkGo.<JSONObject>put(HttpHelper.Url.Store.modify)
                                            .tag(this)
                                            .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                            .upJson(tmp)
                                            .converter(new JsonConvert())
                                            .adapt(new ObservableResponse<JSONObject>());

                                    return observable;
                                }
                            }).subscribe(new Observer<Response<JSONObject>>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(Response<JSONObject> jsonObjectResponse) {
                                    try {
                                        if(ResponseUtils.ok(jsonObjectResponse.body())){
                                            EventBus.getDefault().post(new RefreshInfoEvent());
                                            showMessage(R.string.success_improve_informaction_modify);
                                            finish();
                                        }else{
                                            showMessage(ResponseUtils.getMsg(jsonObjectResponse.body()));
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                    e.printStackTrace();
                                    dismissDialog();
                                }

                                @Override
                                public void onComplete() {
                                    dismissDialog();
                                }
                    });
                }else{
                    if(!logo.isEmpty()){
                        //只有logo
                        OkGo.<JSONObject>post(HttpHelper.Url.Store.uploadLogo)
                                .tag(this)
                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                .isMultipart(true)
                                .addFileParams(HttpHelper.Params.files, logo)
                                .converter(new JsonConvert())
                                .adapt(new ObservableResponse<JSONObject>())
                                .subscribeOn(Schedulers.io())
                                .flatMap(new Function<Response<JSONObject>, ObservableSource<Response<JSONObject>>>() {

                                    @Override
                                    public ObservableSource<Response<JSONObject>> apply(Response<JSONObject> jsonObjectResponse) throws Exception {
                                        if(!ResponseUtils.ok(jsonObjectResponse.body())){
                                            throw new Exception("上传图片失败！");
                                        }

                                        List<String> pics = new Gson().fromJson(ResponseUtils.getList(jsonObjectResponse.body()).toString(),
                                                new TypeToken<List<String>>(){}.getType());

                                        uploadLogo = pics.get(0);

                                        JSONObject obj = new JSONObject();

                                        obj.put(HttpHelper.Params.banners, imageFileName);
                                        obj.put(HttpHelper.Params.ceo, etDsz.getText().toString());
                                        obj.put(HttpHelper.Params.city, poiItem.getCityCode());
                                        obj.put(HttpHelper.Params.createDate, tvClrq.getText().toString());
                                        obj.put(HttpHelper.Params.details, etQyjs.getText().toString());
                                        obj.put(HttpHelper.Params.enterpriseType, etQylx.getText().toString());
                                        obj.put(HttpHelper.Params.enterpriseName, etQymc.getText().toString());
                                        obj.put(HttpHelper.Params.icon, uploadLogo);
                                        obj.put(HttpHelper.Params.latitude, poiItem.getLatLonPoint().getLatitude());
                                        obj.put(HttpHelper.Params.longitude, poiItem.getLatLonPoint().getLongitude());
                                        obj.put(HttpHelper.Params.officialWeb, etQygw.getText().toString());
                                        obj.put(HttpHelper.Params.phone, etLxdh.getText().toString());
                                        obj.put(HttpHelper.Params.registerAddress, tvGsdz.getText().toString());
                                        obj.put(HttpHelper.Params.weChat, etWxh.getText().toString());
                                        obj.put(HttpHelper.Params.type, typePopupwindow.getType().getId());
                                        obj.put(HttpHelper.Params.sketch, etQybq.getText().toString());

                                        String tmp = obj.toString().replaceAll("\\\\", "");
                                        StringUtils.print(tmp);

                                        Observable<Response<JSONObject>> observable = OkGo.<JSONObject>put(HttpHelper.Url.Store.modify)
                                                .tag(this)
                                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                                .upJson(tmp)
                                                .converter(new JsonConvert())
                                                .adapt(new ObservableResponse<JSONObject>());
                                        return observable;
                                    }
                                }).subscribe(new Observer<Response<JSONObject>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Response<JSONObject> jsonObjectResponse) {
                                        try {
                                            if(ResponseUtils.ok(jsonObjectResponse.body())){
                                                EventBus.getDefault().post(new RefreshInfoEvent());
                                                showMessage(R.string.success_improve_informaction_modify);
                                                finish();
                                            }else{
                                                showMessage(ResponseUtils.getMsg(jsonObjectResponse.body()));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                        dismissDialog();
                                    }

                                    @Override
                                    public void onComplete() {
                                        dismissDialog();
                                    }
                        });
                    }else if(!banner.isEmpty()){
                        //只有封皮
                        OkGo.<JSONObject>post(HttpHelper.Url.Store.uploadBanner)
                                .tag(this)
                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                .isMultipart(true)
                                .addFileParams(HttpHelper.Params.files, banner)
                                .converter(new JsonConvert())
                                .adapt(new ObservableResponse<JSONObject>())
                                .subscribeOn(Schedulers.io())
                                .flatMap(new Function<Response<JSONObject>, ObservableSource<Response<JSONObject>>>() {

                                    @Override
                                    public ObservableSource<Response<JSONObject>> apply(Response<JSONObject> jsonObjectResponse) throws Exception {
                                        if(!ResponseUtils.ok(jsonObjectResponse.body())){
                                            throw new Exception("上传图片失败！");
                                        }

                                        List<String> pics = new Gson().fromJson(ResponseUtils.getList(jsonObjectResponse.body()).toString(),
                                                new TypeToken<List<String>>(){}.getType());

                                        uploadBanner = pics.get(0);

                                        JSONObject obj = new JSONObject();

                                        obj.put(HttpHelper.Params.banners, uploadBanner);
                                        obj.put(HttpHelper.Params.ceo, etDsz.getText().toString());
                                        obj.put(HttpHelper.Params.city, poiItem.getCityCode());
                                        obj.put(HttpHelper.Params.createDate, tvClrq.getText().toString());
                                        obj.put(HttpHelper.Params.details, etQyjs.getText().toString());
                                        obj.put(HttpHelper.Params.enterpriseType, etQylx.getText().toString());
                                        obj.put(HttpHelper.Params.enterpriseName, etQymc.getText().toString());
                                        obj.put(HttpHelper.Params.icon, hearderFileName);
                                        obj.put(HttpHelper.Params.latitude, poiItem.getLatLonPoint().getLatitude());
                                        obj.put(HttpHelper.Params.longitude, poiItem.getLatLonPoint().getLongitude());
                                        obj.put(HttpHelper.Params.officialWeb, etQygw.getText().toString());
                                        obj.put(HttpHelper.Params.phone, etLxdh.getText().toString());
                                        obj.put(HttpHelper.Params.registerAddress, tvGsdz.getText().toString());
                                        obj.put(HttpHelper.Params.weChat, etWxh.getText().toString());
                                        obj.put(HttpHelper.Params.type, typePopupwindow.getType().getId());
                                        obj.put(HttpHelper.Params.sketch, etQybq.getText().toString());

                                        String tmp = obj.toString().replaceAll("\\\\", "");
                                        StringUtils.print(tmp);

                                        Observable<Response<JSONObject>> observable = OkGo.<JSONObject>put(HttpHelper.Url.Store.modify)
                                                .tag(this)
                                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                                .upJson(tmp)
                                                .converter(new JsonConvert())
                                                .adapt(new ObservableResponse<JSONObject>());

                                        return observable;
                                    }
                                }).subscribe(new Observer<Response<JSONObject>>() {
                                    @Override
                                    public void onSubscribe(Disposable d) {

                                    }

                                    @Override
                                    public void onNext(Response<JSONObject> jsonObjectResponse) {
                                        try {
                                            if(ResponseUtils.ok(jsonObjectResponse.body())){
                                                EventBus.getDefault().post(new RefreshInfoEvent());
                                                showMessage(R.string.success_improve_informaction_modify);
                                                finish();
                                            }else{
                                                showMessage(ResponseUtils.getMsg(jsonObjectResponse.body()));
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(Throwable e) {
                                        e.printStackTrace();
                                        dismissDialog();
                                    }

                                    @Override
                                    public void onComplete() {
                                        dismissDialog();
                                    }
                        });
                    }else{
                        //无文件上传，只更新
                        try{
                            JSONObject obj = new JSONObject();

                            obj.put(HttpHelper.Params.banners, imageFileName);
                            obj.put(HttpHelper.Params.ceo, etDsz.getText().toString());
                            obj.put(HttpHelper.Params.city, poiItem.getCityCode());
                            obj.put(HttpHelper.Params.createDate, tvClrq.getText().toString());
                            obj.put(HttpHelper.Params.details, etQyjs.getText().toString());
                            obj.put(HttpHelper.Params.enterpriseType, etQylx.getText().toString());
                            obj.put(HttpHelper.Params.enterpriseName, etQymc.getText().toString());
                            obj.put(HttpHelper.Params.icon, hearderFileName);
                            obj.put(HttpHelper.Params.latitude, poiItem.getLatLonPoint().getLatitude());
                            obj.put(HttpHelper.Params.longitude, poiItem.getLatLonPoint().getLongitude());
                            obj.put(HttpHelper.Params.officialWeb, etQygw.getText().toString());
                            obj.put(HttpHelper.Params.phone, etLxdh.getText().toString());
                            obj.put(HttpHelper.Params.registerAddress, tvGsdz.getText().toString());
                            obj.put(HttpHelper.Params.weChat, etWxh.getText().toString());
                            obj.put(HttpHelper.Params.type, typePopupwindow.getType().getId());
                            obj.put(HttpHelper.Params.sketch, etQybq.getText().toString());

                            String tmp = obj.toString().replaceAll("\\\\", "");
                            StringUtils.print(tmp);

                            OkGo.<JSONObject>put(HttpHelper.Url.Store.modify)
                                    .tag(this)
                                    .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                    .upJson(tmp)
                                    .execute(new JsonCallback<JSONObject>() {
                                        @Override
                                        public void onError(Response<JSONObject> response) {
                                            super.onError(response);
                                            dismissDialog();
                                        }

                                        @Override
                                        public void onSuccess(Response<JSONObject> response) {
                                            super.onSuccess(response);
                                            try {
                                                if(ResponseUtils.ok(response.body())){
                                                    EventBus.getDefault().post(new RefreshInfoEvent());
                                                    showMessage(R.string.success_improve_informaction_modify);
                                                    finish();
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
                                            dismissDialog();
                                        }
                                    });
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void addPhoto(String fileName){
        File file = new File(fileName);
        if(file.exists()){
            if(file.length() > Constants.photoMaxSize){
                showMessage(R.string.error_photo_size);
                return;
            }
        }

        if(currentId == R.id.iv_header){
            hearderFileName = fileName;
            Glide.with(this)
                    .load(file)
                    .transform(new GlideCircleTransformation(this, 100))
                    .placeholder(R.mipmap.icon_header_default)
                    .into(ivHeader);
            tvTipsHeader.setVisibility(View.INVISIBLE);
        }else if(currentId == R.id.iv_image){
            imageFileName = fileName;
            Glide.with(this)
                    .load(file)
                    .transform(new GlideRoundTransformation(this, 10))
                    .placeholder(R.drawable.image_loading_pic)
                    .into(ivImage);
            tvTipsImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.Request.CameraWithData && resultCode == RESULT_OK){
            try {
                Bitmap photo = ImageUtils.decodeStream(this, Uri.fromFile(new File(app.getFileName())));

                // 处理图片旋转问题
                int degree = BitmapUtils.readPictureDegree(app.getFileName());
                photo = BitmapUtils.rotaingImageView(degree, photo);

                // 压缩
                int width = 0;
                int height = 0;
                int desWidth = 1024;
                if(photo.getWidth() > desWidth || photo.getHeight() > desWidth){
                    if(photo.getWidth() > desWidth){
                        width = desWidth;
                        float scale = (float) photo.getWidth() / (float) photo.getHeight();
                        height = (int) (width / scale);
                    }else if(photo.getHeight() > desWidth){
                        height = desWidth;
                        float scale = (float) photo.getWidth() / (float) photo.getHeight();
                        width = (int) (height * scale);
                    }

                    photo = Bitmap.createScaledBitmap(photo, width, height, true);
                }

                FileOutputStream fos = new FileOutputStream(app.getFileName());
                photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();

                addPhoto(app.getFileName());
            }catch (Exception e) {
                e.printStackTrace();
            }
        }else if(requestCode == Constants.Request.SelectPhotoFromAlbum && resultCode == RESULT_OK){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                String imagePath = null;

                Uri uri=data.getData();
                if(DocumentsContract.isDocumentUri(this,uri)){
                    String docId=DocumentsContract.getDocumentId(uri);
                    if("com.android.providers.media.documents".equals(uri.getAuthority())){
                        String id=docId.split(":")[1];
                        String selection = MediaStore.Images.Media._ID+"="+id;
                        imagePath = FileUtil.getImagePath(this, MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
                    }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                        imagePath = FileUtil.getImagePath(this, contentUri,null);
                    }
                }else if("content".equalsIgnoreCase(uri.getScheme())){
                    imagePath = FileUtil.getImagePath(this, uri,null);
                }else if("file".equalsIgnoreCase(uri.getScheme())){
                    imagePath = uri.getPath();
                }

                if(!StringUtils.isNullOrBlank(imagePath)){
                    if(Build.VERSION.SDK_INT >= 24){
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        builder.detectFileUriExposure();
                    }

                    try {
                        Bitmap photo = ImageUtils.decodeStream(this, Uri.fromFile(new File(imagePath)));

                        // 压缩
                        int width = 0;
                        int height = 0;
                        int desWidth = 1024;
                        if(photo.getWidth() > desWidth || photo.getHeight() > desWidth){
                            if(photo.getWidth() > desWidth){
                                width = desWidth;
                                float scale = (float) photo.getWidth() / (float) photo.getHeight();
                                height = (int) (width / scale);
                            }else if(photo.getHeight() > desWidth){
                                height = desWidth;
                                float scale = (float) photo.getWidth() / (float) photo.getHeight();
                                width = (int) (height * scale);
                            }

                            photo = Bitmap.createScaledBitmap(photo, width, height, true);
                        }

                        FileOutputStream fos = new FileOutputStream(app.getFileName());
                        photo.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                        fos.close();

                        addPhoto(app.getFileName());
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if(requestCode == Constants.Request.SelectAddress && resultCode == RESULT_OK){
            Object object = data.getParcelableExtra(Constants.Tag.data);
            if(object != null && object instanceof PoiItem){
                poiItem = (PoiItem) object;
                tvGsdz.setText(poiItem.getProvinceName() + poiItem.getCityName() + poiItem.getSnippet());

                check();
            }
        }
    }

    private class CameraPopupwindow{
        private PopupWindow popupWindow;

        private void init() {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_camera, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.getBackground().setAlpha(170);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ViewUtils.background(ImproveInformationActivity.this, 1f);
                }
            });

            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            view.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            view.findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String fileName = sdf.format(new Date(System.currentTimeMillis())) + ".jpg";

                    app.setFileName(Constants.Path.imgPath + fileName);

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.Path.imgPath + fileName)));

                    if(Build.VERSION.SDK_INT >= 24){
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        builder.detectFileUriExposure();
                    }

                    startActivityForResult(intent, Constants.Request.CameraWithData);
                }
            });

            view.findViewById(R.id.btn_album).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    String fileName = sdf.format(new Date(System.currentTimeMillis())) + ".jpg";

                    app.setFileName(Constants.Path.imgPath + fileName);

                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
                    intent.setType("image/*");
                    intent.putExtra("return-data", false);
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Constants.Path.imgPath + fileName)));
                    intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                    intent.putExtra("noFaceDetection", true);

                    startActivityForResult(intent, Constants.Request.SelectPhotoFromAlbum);
                }
            });
        }

        public boolean isShowing(){
            return popupWindow.isShowing();
        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public void show(){
            rxPermissions.requestEachCombined(new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA})
                    .subscribe(permission -> {
                        if(permission.granted){
                            ViewUtils.background(ImproveInformationActivity.this, 0.8f);
                            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        }else if(permission.shouldShowRequestPermissionRationale){
                            showMessage(R.string.rationale_ask_again);
                        }else{
                            showMessage(R.string.rationale_cancle);
                        }});

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
                    ViewUtils.background(ImproveInformationActivity.this, 1f);
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

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    tvDate.setText(sdf.format(new Date(calendar.getTimeInMillis())));
                    tvDate.setTag(calendar.getTimeInMillis());

                    check();
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
            }else{
                calendarView.setDate(System.currentTimeMillis());
            }

            ViewUtils.hideSoftInput(getApplicationContext(), etDsz);
            ViewUtils.background(ImproveInformationActivity.this, 0.8f);
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
    }

    private class TypePopupwindow{
        private PopupWindow popupWindow;
        private RecyclerView rvList;
        private SwipeRefreshLayout mySwipeRefreshLayout;
        private EnterpriseTypeAdapter enterpriseTypeAdapter;

        private int pageNum = 1;
        private int pageSize = 1000;
        private int pageCount = 1;

        private void init() {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_enterprise_type, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.getBackground().setAlpha(170);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ViewUtils.background(ImproveInformationActivity.this, 1f);
                }
            });

            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();

                }
            });

            view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                    EnterpriseType type = getType();
                    tvJglx.setText(type.getName());
                    check();
                }
            });

            mySwipeRefreshLayout = view.findViewById(R.id.swiperefresh);
            rvList = view.findViewById(R.id.rv_list);

            enterpriseTypeAdapter = new EnterpriseTypeAdapter();
            LinearLayoutManager layoutManager = new LinearLayoutManager(ImproveInformationActivity.this);
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rvList.setLayoutManager(layoutManager);
            rvList.setAdapter(enterpriseTypeAdapter);

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

        private void doQuery(){
            doQuery(null);
        }

        public void doQuery(Runnable callBack){
            OkGo.<JSONObject>get(HttpHelper.Url.Store.Type.pageList)
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
                                    PageResponse<EnterpriseType> pageResponse = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                            new TypeToken<PageResponse<EnterpriseType>>(){}.getType());

                                    enterpriseTypeAdapter.clear();
                                    enterpriseTypeAdapter.addAll(pageResponse.getList());
                                    enterpriseTypeAdapter.notifyDataSetChanged();
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

                            if(callBack != null)
                                callBack.run();
                        }
                    });
        }

        public void setType(int type){
            enterpriseTypeAdapter.setType(type);
            enterpriseTypeAdapter.notifyDataSetChanged();
        }

        public EnterpriseType getType(){
            return enterpriseTypeAdapter.getSelect();
        }

        public boolean isShowing(){
            return popupWindow.isShowing();
        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public void show(){
            ViewUtils.background(ImproveInformationActivity.this, 0.8f);
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }

    }
}
