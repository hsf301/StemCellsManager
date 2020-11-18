package com.huashengfu.StemCellsManager.activity.service;

import android.Manifest;
import android.app.AlertDialog;
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
import android.util.ArrayMap;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.AppApplication;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.entity.response.sms.Init;
import com.huashengfu.StemCellsManager.entity.response.sms.UploadCover;
import com.huashengfu.StemCellsManager.entity.response.sms.UploadBanner;
import com.huashengfu.StemCellsManager.entity.service.Service;
import com.huashengfu.StemCellsManager.event.service.UpdateServiceCoverEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.convert.JsonConvert;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.BitmapUtils;
import com.huashengfu.StemCellsManager.utils.FileUtil;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.ImageUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

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

// 发布新服务-Banner
public class PublishServiceBannerActivity extends BaseActivity {

    @BindView(R.id.ll_photo)
    LinearLayout llPhoto;
    @BindView(R.id.ll_photo_default)
    LinearLayout llPhotoDefault;

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

    private Unbinder unbinder;
    private int photoLayoutWidth;
    private List<String> photos = new ArrayList<>();
    private final int PhotoMax = 9;

    private List<RelativeLayout> photoRelativeLayout = new ArrayList<>();

    private CameraPopupwindow cameraPopupwindow;

    private AppApplication app;
    private RxPermissions rxPermissions;

    private Init init;
    private boolean modify, gonext;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_publish_banner);

        unbinder = ButterKnife.bind(this);

        app = (AppApplication) getApplication();
        rxPermissions = new RxPermissions(this);

        cameraPopupwindow = new CameraPopupwindow();
        cameraPopupwindow.init();

        llPhoto.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                photoLayoutWidth = llPhoto.getWidth();

                initDefaultPhoto();
            }
        });

        id = getIntent().getIntExtra(Constants.Tag.data, -1);
        modify = getIntent().getBooleanExtra(Constants.Tag.modify, false);
        gonext = getIntent().getBooleanExtra(Constants.Tag.gonext, true);
        if(id == -1){
            finish();
        }else{
            doInit(id, 2, modify);
        }
    }

    @OnClick({R.id.iv_back, R.id.btn_commit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.btn_commit:{
                // 调用文件上传接口
                List<File> files = new ArrayList<>();
                List<String> oldPhotos = new ArrayList<>();
                for(String str : photos){
                    File file = new File(str);
                    if(file.isFile()) {
                        files.add(file);
                    }else if(str.startsWith("http")){
                        oldPhotos.add(str);
                    }
                }

                if(files.isEmpty() && oldPhotos.isEmpty()){
                    showMessage(R.string.error_service_photo_empty);
                    return;
                }

                if(modify && files.isEmpty()){
                    try {
                        JSONObject obj = new JSONObject();

                        String[] banners = banners = new String[oldPhotos.size()];
                        oldPhotos.toArray(banners);
                        obj.put(HttpHelper.Params.banners, new JSONArray(banners));
                        obj.put(HttpHelper.Params.sid, id);

                        String tmp = obj.toString().replaceAll("\\\\", "");
                        StringUtils.print(tmp);

                        OkGo.<JSONObject>put(HttpHelper.Url.Service.modifyBanner)
                                .tag(this)
                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                .upJson(tmp)
                                .execute(new DialogCallback<JSONObject>(this, false) {
                                    @Override
                                    public void onSuccess(Response<JSONObject> response) {
                                        super.onSuccess(response);
                                        try {
                                            if(ResponseUtils.ok(response.body())){
                                                goNext();
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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    showDialog(false);

                    OkGo.<JSONObject>post(HttpHelper.Url.Service.uploadBanner)
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
                                        throw new Exception("上传轮播图片失败！");
                                    }

                                    UploadBanner uploadBanner = new Gson().fromJson(jsonObjectResponse.body().toString(),
                                            new TypeToken<UploadBanner>(){}.getType());

                                    for(String str : uploadBanner.getData()){
                                        Log.i(Constants.Log.Log, "url --> " + str);
                                    }

                                    JSONObject obj = new JSONObject();

                                    String[] banners = null;
                                    if(modify){
                                        // 保留服务端的图片数据，删除了，就不管了，如果没有了，那么就以本地新上传的图片为准
                                        List<String> serverFiles = new ArrayList<>();
                                        for(String str : photos){
                                            if(str.startsWith("http"))
                                                serverFiles.add(str);

                                        }
                                        banners = new String[serverFiles.size() + uploadBanner.getData().size()];
                                        serverFiles.addAll(uploadBanner.getData());
                                        serverFiles.toArray(banners);
                                    }else{
                                        banners = new String[uploadBanner.getData().size()];
                                        uploadBanner.getData().toArray(banners);
                                    }
                                    obj.put(HttpHelper.Params.banners, new JSONArray(banners));

                                    obj.put(HttpHelper.Params.sid, id);

                                    String tmp = obj.toString().replaceAll("\\\\", "");
                                    StringUtils.print(tmp);

                                    Observable<Response<JSONObject>> observable = null;
                                    if(modify)
                                        observable = OkGo.<JSONObject>put(HttpHelper.Url.Service.modifyBanner)
                                                .tag(this)
                                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                                .upJson(tmp)
                                                .converter(new JsonConvert())
                                                .adapt(new ObservableResponse<JSONObject>());
                                    else
                                        observable = OkGo.<JSONObject>post(HttpHelper.Url.Service.addServiceBanner)
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
                                            goNext();
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
                break;
            }
        }
    }

    private void goNext(){
        if(modify){
            showMessage(R.string.success_service_banner_modify);
        }else{
            showMessage(R.string.success_service_banner_add);
        }
        if(gonext){
            Intent intent = new Intent(getApplicationContext(), PublishServicePromotionActivity.class);
            intent.putExtra(Constants.Tag.data, id);
            intent.putExtra(Constants.Tag.modify, false);
            startActivity(intent);
        }else{
            Service service = new Service();
            service.setId(id);

            List<File> files = new ArrayList<>();
            List<String> oldPhotos = new ArrayList<>();
            for(String str : photos){
                File file = new File(str);
                if(file.isFile()) {
                    files.add(file);
                }else if(str.startsWith("http")){
                    oldPhotos.add(str);
                }
            }

            if(!files.isEmpty()){
                if(oldPhotos.isEmpty()){
                    service.setCover(files.get(0).getAbsolutePath());
                    EventBus.getDefault().post(new UpdateServiceCoverEvent().setService(service));
                }else{
                    service.setCover(oldPhotos.get(0));
                    EventBus.getDefault().post(new UpdateServiceCoverEvent().setService(service));
                }

            }else if(!oldPhotos.isEmpty()){
                service.setCover(oldPhotos.get(0));
                EventBus.getDefault().post(new UpdateServiceCoverEvent().setService(service));
            }
        }

        finish();
    }

    private void initDefaultPhoto(){
        int width = photoLayoutWidth - ViewUtils.dip2px(10) * 4;
        int height = width / llPhotoDefault.getChildCount();
        Log.i(Constants.Log.Log, "llPhoto.getWidth() -> " +llPhoto.getWidth() + " | " + height);

        for(int i=0; i<llPhotoDefault.getChildCount(); i++){
            View view = llPhotoDefault.getChildAt(i);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
            layoutParams.height = height;
        }

        llPhotoDefault.findViewById(R.id.rl_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraPopupwindow.show();
            }
        });
    }

    private void initPhoto(){
        llPhoto.removeAllViews();
        View v = getLayoutInflater().inflate(R.layout.adapter_banner_nine_add, llPhoto, false);
        llPhoto.addView(v);

        photoRelativeLayout.clear();
        photoRelativeLayout.add(v.findViewById(R.id.rl_photo_1));
        photoRelativeLayout.add(v.findViewById(R.id.rl_photo_2));
        photoRelativeLayout.add(v.findViewById(R.id.rl_photo_3));
        photoRelativeLayout.add(v.findViewById(R.id.rl_photo_4));
        photoRelativeLayout.add(v.findViewById(R.id.rl_photo_5));
        photoRelativeLayout.add(v.findViewById(R.id.rl_photo_6));
        photoRelativeLayout.add(v.findViewById(R.id.rl_photo_7));
        photoRelativeLayout.add(v.findViewById(R.id.rl_photo_8));
        photoRelativeLayout.add(v.findViewById(R.id.rl_photo_9));

        // 设置第一个图片的宽度和高度
        int width = photoLayoutWidth - ViewUtils.dip2px(10) * 4;
        int height = (int)((float)width * 2 / 3) + ViewUtils.dip2px(10);

        Log.i(Constants.Log.Log, width + " || -> " + height);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) photoRelativeLayout.get(0).getLayoutParams();
        layoutParams.width = height;
        layoutParams.height = height;

        LinearLayout linearLayout = v.findViewById(R.id.ll_photo_2_3);
        LinearLayout.LayoutParams linearLayoutLayoutParams = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
        linearLayoutLayoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;

        // 设置第4-9个图片的宽度和高度
        width = photoLayoutWidth - ViewUtils.dip2px(10) * 4;
        height = width / 3;
        for(int i=3; i<photoRelativeLayout.size(); i++){
            LinearLayout.LayoutParams tmp = (LinearLayout.LayoutParams) photoRelativeLayout.get(i).getLayoutParams();
            tmp.width = height;
            tmp.height = height;
        }

        for(int i=0; i<photoRelativeLayout.size(); i++) {
            RelativeLayout relativeLayout = photoRelativeLayout.get(i);
            RelativeLayout rlAdd = relativeLayout.findViewById(R.id.rl_add);
            ImageView imageView = relativeLayout.findViewById(R.id.iv_image);

            rlAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cameraPopupwindow.show();
                }
            });

            imageView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    AlertDialog dialog = new AlertDialog.Builder(PublishServiceBannerActivity.this)
                            .setMessage(R.string.dialog_message_delete_photo)
                            .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    String photo = imageView2Photo.get(imageView);
                                    if(photo != null){
                                        photos.remove(photo);
                                        resetPhoto();
                                    }
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
                    return true;
                }
            });
        }
    }

    private Map<ImageView, String> imageView2Photo = new ArrayMap<>();

    private void resetPhoto(){
        Log.i(Constants.Log.Log, " photos.size() --> " + photos.size());
        if(photos.isEmpty()){
            llPhoto.removeAllViews();
            llPhoto.addView(llPhotoDefault);

            initDefaultPhoto();
        }

        if(photos.size() == 1)
            initPhoto();

        imageView2Photo.clear();
        for(int i=0; i<photoRelativeLayout.size(); i++){
            RelativeLayout view = photoRelativeLayout.get(i);
            ImageView imageView = view.findViewById(R.id.iv_image);
            RelativeLayout rlAdd = view.findViewById(R.id.rl_add);

            if(i < photos.size()){
                imageView.setVisibility(View.VISIBLE);
                imageView2Photo.put(imageView, photos.get(i));
                rlAdd.setVisibility(View.GONE);

                Glide.with(this)
                        .load(photos.get(i))
                        .transform(new GlideRoundTransformation(this, 5))
                        .into(imageView);
            }else if(i==photos.size()){
                imageView.setVisibility(View.GONE);
                rlAdd.setVisibility(View.VISIBLE);
            }else{
                imageView.setVisibility(View.GONE);
                rlAdd.setVisibility(View.GONE);
            }
        }

        if(photos.size() >= 3){
            RelativeLayout view = photoRelativeLayout.get(3);
            ((LinearLayout)view.getParent()).setVisibility(View.VISIBLE);
        }else{
            RelativeLayout view = photoRelativeLayout.get(3);
            ((LinearLayout)view.getParent()).setVisibility(View.GONE);
        }

        if(photos.size() >= 6){
            RelativeLayout view = photoRelativeLayout.get(6);
            ((LinearLayout)view.getParent()).setVisibility(View.VISIBLE);
        }else{
            RelativeLayout view = photoRelativeLayout.get(6);
            ((LinearLayout)view.getParent()).setVisibility(View.GONE);
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

        photos.add(app.getFileName());
        resetPhoto();
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
                    if(android.os.Build.VERSION.SDK_INT >= 24){
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

    private void doInit(int id, int dNo, boolean modify){
        GetRequest<JSONObject> getRequest = OkGo.<JSONObject>get(HttpHelper.Url.Service.init)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.dNo, dNo);

        if(id > 0 && modify){
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

                        if(init.getBanners() != null){
                            photos.addAll(init.getBanners());
                            initPhoto();
                            resetPhoto();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
                    ViewUtils.background(PublishServiceBannerActivity.this, 1f);
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

                    if(android.os.Build.VERSION.SDK_INT >= 24){
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
                            ViewUtils.background(PublishServiceBannerActivity.this, 0.8f);
                            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        }else if(permission.shouldShowRequestPermissionRationale){
                            showMessage(R.string.rationale_ask_again);
                        }else{
                            showMessage(R.string.rationale_cancle);
                        }});

        }

    }
}
