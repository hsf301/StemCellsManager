package com.huashengfu.StemCellsManager.activity.settings.ad;

import android.Manifest;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.AppApplication;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.settings.ad.AdAdapter;
import com.huashengfu.StemCellsManager.entity.settings.Ad;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.convert.JsonConvert;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.BitmapUtils;
import com.huashengfu.StemCellsManager.utils.FileUtil;
import com.huashengfu.StemCellsManager.utils.ImageUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

//广告设置
public class AdvertisingSettingsActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;

    @BindView(R.id.tv_modify)
    TextView tvModify;

    private final int AdMax = 6;

    private AdAdapter adAdapter;
    private Unbinder unbinder;
    private RxPermissions rxPermissions;
    private AppApplication app;
    private CameraPopupwindow cameraPopupwindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_advertising);
        unbinder = ButterKnife.bind(this);

        rxPermissions = new RxPermissions(this);
        app = (AppApplication) getApplication();

        cameraPopupwindow = new CameraPopupwindow();
        cameraPopupwindow.init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adAdapter = new AdAdapter();
        adAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Object object) {
                if(adAdapter.getItemCount() - 1 < AdMax){
                    cameraPopupwindow.show();
                }else{
                    showMessage(R.string.error_ad_photo_count);
                }
            }
        });

        rvList.setAdapter(adAdapter);
        adAdapter.notifyDataSetChanged();

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

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Store.getAD)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                List<String> ads = new Gson().fromJson(ResponseUtils.getList(response.body()).toString(),
                                        new TypeToken<List<String>>(){}.getType());

                                adAdapter.clear();
                                if(ads != null){
                                    for(String tmp : ads){
                                        Ad ad = new Ad();
                                        ad.setPic(tmp);
                                        adAdapter.add(ad);
                                    }
                                }
                                adAdapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick({R.id.iv_back, R.id.btn_save, R.id.tv_modify})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.tv_modify:{
                if(adAdapter.isModify()){
                    adAdapter.setModify(false);
                    tvModify.setText("编辑");
                }else{
                    adAdapter.setModify(true);
                    tvModify.setText("完成");
                }

                break;
            }
            case R.id.btn_save:{
                doSave();
                break;
            }
        }
    }

    private void doSave(){
        List<File> files = new ArrayList<>();
        List<String> photos = new ArrayList<>();
        for(Ad ad : adAdapter.getAds()){
            if(StringUtils.isNullOrBlank(ad.getPic()))
                continue;

            if(ad.getPic().startsWith("http"))
                photos.add(ad.getPic());
            else {
                File file = new File(ad.getPic());
                files.add(file);
            }
        }

        showDialog(false);

        if(files.isEmpty() && photos.isEmpty()){
            showMessage(R.string.error_ad_photo_empty);
            return;
        }

        if(files.isEmpty()){
            //没有新加的图片，只更新操作
            try{
                String tmp = new JSONArray(photos).toString().replaceAll("\\\\", "");
                StringUtils.print(tmp);

                OkGo.<JSONObject>put(HttpHelper.Url.Store.modifyAD)
                        .tag(this)
                        .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                        .upJson(tmp)
                        .execute(new JsonCallback<JSONObject>() {
                            @Override
                            public void onSuccess(Response<JSONObject> response) {
                                super.onSuccess(response);
                                try {
                                    if(ResponseUtils.ok(response.body())){
                                        goFinish();
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
        }else{
            //有新加的图片
            OkGo.<JSONObject>post(HttpHelper.Url.Store.uploadBanner)
                    .tag(this)
                    .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                    .isMultipart(true)
                    .addFileParams(HttpHelper.Params.files, files)
                    .converter(new JsonConvert())
                    .adapt(new ObservableResponse<JSONObject>())
                    .subscribeOn(Schedulers.io())
                    .flatMap(new Function<Response<JSONObject>, ObservableSource<Response<JSONObject>>>() {

                        @Override
                        public ObservableSource<Response<JSONObject>> apply(Response<JSONObject> jsonObjectResponse) throws Exception {
                            if(!ResponseUtils.ok(jsonObjectResponse.body())){
                                throw new Exception("上传分支机构图片失败！");
                            }

                            List<String> pics = new Gson().fromJson(ResponseUtils.getList(jsonObjectResponse.body()).toString(),
                                    new TypeToken<List<String>>(){}.getType());

                            pics.addAll(photos);

                            String tmp = new JSONArray(pics).toString().replaceAll("\\\\", "");
                            StringUtils.print(tmp);

                            Observable<Response<JSONObject>> observable = OkGo.<JSONObject>put(HttpHelper.Url.Store.modifyAD)
                                    .tag(this)
                                    .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                    .upJson(tmp)
                                    .converter(new JsonConvert())
                                    .adapt(new ObservableResponse<JSONObject>());

                            return observable;
                        }
                    })
                    .subscribe(new Observer<Response<JSONObject>>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(Response<JSONObject> jsonObjectResponse) {
                            try {
                                if(ResponseUtils.ok(jsonObjectResponse.body())){
                                    goFinish();
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
        }
    }

    private void goFinish(){
        showMessage(R.string.success_ad_modify);
        finish();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if(adAdapter.isModify()){
            tvModify.setText("编辑");
            adAdapter.setModify(false);
            return;
        }

        if(adAdapter.getItemCount() - 1 > 0){
            showNoSaveDialog(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    doSave();
                }
            }, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                    finish();
                }
            });
            return;
        }
        super.onBackPressed();
    }

    private void addPhoto(String fileName){
        File file = new File(fileName);
        if(file.exists()){
            if(file.length() > Constants.photoMaxSize){
                showMessage(R.string.error_photo_size);
                return;
            }
        }

        Ad ad = new Ad();
        ad.setPic(fileName);
        adAdapter.add(ad);
        adAdapter.notifyDataSetChanged();
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
                    ViewUtils.background(AdvertisingSettingsActivity.this, 1f);
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
                            ViewUtils.background(AdvertisingSettingsActivity.this, 0.8f);
                            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        }else if(permission.shouldShowRequestPermissionRationale){
                            showMessage(R.string.rationale_ask_again);
                        }else{
                            showMessage(R.string.rationale_cancle);
                        }});

        }

    }
}
