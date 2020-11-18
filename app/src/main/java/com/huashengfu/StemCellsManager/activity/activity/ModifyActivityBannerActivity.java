package com.huashengfu.StemCellsManager.activity.activity;

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
import com.huashengfu.StemCellsManager.entity.activity.Activity;
import com.huashengfu.StemCellsManager.entity.response.activity.Init;
import com.huashengfu.StemCellsManager.event.activity.UpdateActivityBannerEvent;
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
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okrx2.adapter.ObservableResponse;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
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

// 编辑活动-Banner
public class ModifyActivityBannerActivity extends BaseActivity {

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
    private final int PhotoMax = 1;

    private List<RelativeLayout> photoRelativeLayout = new ArrayList<>();

    private CameraPopupwindow cameraPopupwindow;

    private AppApplication app;
    private RxPermissions rxPermissions;

    private int id;
    private Init init;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_modify_banner);

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
        doInit(id, 1);
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

                        if(!StringUtils.isNullOrBlank(init.getBanner())){
                            photos.add(init.getBanner());
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

    private String banner;
    @OnClick({R.id.iv_back, R.id.btn_commit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.btn_commit:{
                // 调用文件上传接口
                if(photos.isEmpty()){
                    showMessage(R.string.error_photo_empty_activity);
                    return;
                }

                if(photos.get(0).startsWith("http")){
                    finish();
                    return;
                }

                showDialog(false);

                File file =new File(photos.get(0));
                List<File> files = new ArrayList<>();
                files.add(file);

                OkGo.<JSONObject>post(HttpHelper.Url.Activity.Details.uploadBanner)
                        .tag(this)
                        .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                        .isMultipart(true)
                        .addFileParams(HttpHelper.Params.pic, files)
                        .converter(new JsonConvert())
                        .adapt(new ObservableResponse<JSONObject>())
                        .subscribeOn(Schedulers.io())
                        .flatMap(new Function<Response<JSONObject>, ObservableSource<Response<JSONObject>>>() {

                            @Override
                            public ObservableSource<Response<JSONObject>> apply(Response<JSONObject> jsonObjectResponse) throws Exception {
                                if(!ResponseUtils.ok(jsonObjectResponse.body())){
                                    throw new Exception("上传轮播图片失败！");
                                }

                                banner = jsonObjectResponse.body().getString(Constants.Tag.data);

                                Observable<Response<JSONObject>> observable = OkGo.<JSONObject>put(HttpHelper.Url.Activity.modifyBanner + id
                                        + "?" + HttpHelper.Params.banner + "=" + banner)
                                        .tag(this)
                                        .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
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
                                        showMessage(R.string.success_activity_banner_modify);

                                        Activity activity = new Activity();
                                        activity.setId(id);
                                        activity.setBanner(banner);
                                        EventBus.getDefault().post(new UpdateActivityBannerEvent().setActivity(activity));
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
                break;
            }
        }
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
                    AlertDialog dialog = new AlertDialog.Builder(ModifyActivityBannerActivity.this)
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

    private Map<ImageView, String> imageView2Photo = new ArrayMap<>();

    private void resetPhoto(){
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
            }else if(i==photos.size() && photos.isEmpty()){
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
                    ViewUtils.background(ModifyActivityBannerActivity.this, 1f);
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
                            ViewUtils.background(ModifyActivityBannerActivity.this, 0.8f);
                            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        }else if(permission.shouldShowRequestPermissionRationale){
                            showMessage(R.string.rationale_ask_again);
                        }else{
                            showMessage(R.string.rationale_cancle);
                        }});

        }

    }
}
