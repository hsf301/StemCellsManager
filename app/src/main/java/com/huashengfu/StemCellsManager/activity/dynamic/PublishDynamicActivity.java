package com.huashengfu.StemCellsManager.activity.dynamic;

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
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
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
import com.huashengfu.StemCellsManager.activity.video.EsayVideoEditActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.flag.FlagAdapter;
import com.huashengfu.StemCellsManager.entity.response.dynamic.Upload;
import com.huashengfu.StemCellsManager.entity.response.dynamic.Video;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.convert.JsonConvert;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.BitmapUtils;
import com.huashengfu.StemCellsManager.utils.FileUtil;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.ImageUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.huashengfu.StemCellsManager.view.FlowLayoutManager;
import com.huashengfu.StemCellsManager.view.video.UIUtil;
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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// 发布动态
public class PublishDynamicActivity extends BaseActivity {

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.et_flag)
    EditText etFlag;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    @BindView(R.id.ll_photo)
    LinearLayout llPhoto;
    @BindView(R.id.ll_photo_default)
    LinearLayout llPhotoDefault;

    @BindView(R.id.ll_menu_photo)
    LinearLayout llMenuPhoto;
    @BindView(R.id.ll_menu_video)
    LinearLayout llMenuVideo;

    @BindView(R.id.ll_video)
    LinearLayout llVideo;

    @BindView(R.id.iv_video_image)
    ImageView ivVideoImage;
    @BindView(R.id.rl_add_video)
    RelativeLayout rlAddVideo;

    private Unbinder unbinder;
    private FlagAdapter flagAdapter;

    private int photoLayoutWidth;
    private List<String> photos = new ArrayList<>();
    private final int PhotoMax = 9;
    private String videoFileName;

    private List<RelativeLayout> photoRelativeLayout = new ArrayList<>();

    private CameraPopupwindow cameraPopupwindow;

    private AppApplication app;
    private RxPermissions rxPermissions;

    private Map<Integer, View> menus = new ConcurrentHashMap<>();
    private Map<Integer, Pair<Integer, Integer>> menuResIds = new ConcurrentHashMap<>();

    private int currentType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_publish_dynamic);

        unbinder = ButterKnife.bind(this);

        currentType = llMenuPhoto.getId();

        menus.put(R.id.ll_menu_photo, llMenuPhoto);
        menus.put(R.id.ll_menu_video, llMenuVideo);

        menuResIds.put(R.id.ll_menu_photo, new Pair<>(R.mipmap.icon_camera_blue, R.mipmap.icon_camera_gray));
        menuResIds.put(R.id.ll_menu_video, new Pair<>(R.mipmap.icon_video_blue, R.mipmap.icon_video_gray));

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

        flagAdapter = new FlagAdapter();
        flagAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener<String>() {
            @Override
            public boolean onItemLongClick(View view, String flag) {
                AlertDialog dialog = new AlertDialog.Builder(PublishDynamicActivity.this)
                        .setMessage(R.string.dialog_message_delete_service_flag)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int position = flagAdapter.remove(flag);
                                flagAdapter.notifyItemRemoved(position);
                                flagAdapter.notifyDataSetChanged();
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
    }

    private void resetMenu(int id){
        currentType = id;
        for(int key : menus.keySet()){
            View view = menus.get(key);
            ImageView ivIcon = view.findViewById(R.id.iv_icon);
            TextView tvName = view.findViewById(R.id.tv_name);

            if(key == id){
                ivIcon.setImageResource(menuResIds.get(key).first);
                tvName.setTextAppearance(this, R.style.TextBlue12Sp);
            }else{
                ivIcon.setImageResource(menuResIds.get(key).second);
                tvName.setTextAppearance(this, R.style.TextGray12Sp);
            }
        }
    }

    @OnLongClick(R.id.iv_video_image)
    public boolean onLongClick(View view){
        switch (view.getId()){
            case R.id.iv_video_image:{
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage(R.string.dialog_message_delete_video)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                File file = new File(videoFileName);
                                if(file.exists()){
                                    file.delete();
                                }
                                videoFileName = null;
                                ivVideoImage.setVisibility(View.GONE);
                                rlAddVideo.setVisibility(View.VISIBLE);

                                llMenuVideo.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton(R.string.btn_cancle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create();
                dialog.show();
                return true;
            }
        }
        return false;
    }

    @OnClick({R.id.iv_back, R.id.ll_add_flag, R.id.btn_commit, R.id.ll_menu_video, R.id.ll_menu_photo, R.id.rl_add_video})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.rl_add_video:{
                rxPermissions.requestEachCombined(new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA})
                        .subscribe(permission -> {
                            if(permission.granted){
                                Intent intent = new Intent();
                                intent.setType("video/mp4");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                startActivityForResult(intent, Constants.Request.SelectVideo);
                            }else if(permission.shouldShowRequestPermissionRationale){
                                showMessage(R.string.rationale_ask_again);
                            }else{
                                showMessage(R.string.rationale_cancle);
                            }});

                break;
            }
            case R.id.ll_menu_photo:{
                resetMenu(view.getId());
                llPhoto.setVisibility(View.VISIBLE);
                llVideo.setVisibility(View.GONE);
                break;
            }
            case R.id.ll_menu_video:{
                resetMenu(view.getId());
                llPhoto.setVisibility(View.GONE);
                llVideo.setVisibility(View.VISIBLE);
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
                break;
            }
            case R.id.btn_commit:{
                // 分步处理，此处调用接口，提交本页面编辑的数据
                if(currentType == R.id.ll_menu_photo){
                    //文本+图片
                    List<File> files = new ArrayList<>();
                    for(String str : photos){
                        File file = new File(str);
                        files.add(file);
                    }

                    if(etContent.length() == 0 && files.isEmpty()){
                        showMessage(R.string.error_dynamic_content);
                        return;
                    }

                    doPost(files, null);
                }else if(currentType == R.id.ll_menu_video){
                    //文本+视频
                    if(etContent.length() == 0 && StringUtils.isNullOrBlank(videoFileName)){
                        showMessage(R.string.error_dynamic_content);
                        return;
                    }

                    doPost(null, videoFileName);
                }
                break;
            }
        }
    }

    private void doPost(List<File> files, String videoFileName){
        showDialog(false);

        if(files != null && files.size() > 0){
            OkGo.<JSONObject>post(HttpHelper.Url.Dynamic.upload)
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
                                throw new Exception("上传动态图片失败！");
                            }

                            List<Upload> uploads = new Gson().fromJson(ResponseUtils.getList(jsonObjectResponse.body()).toString(),
                                    new TypeToken<List<Upload>>(){}.getType());

                            JSONObject obj = new JSONObject();
                            obj.put(HttpHelper.Params.contentType, Constants.Type.Detail.pic);
                            obj.put(HttpHelper.Params.subTitle, etContent.getText().toString());

                            JSONArray list = new JSONArray();
                            for(Upload upload : uploads){
                                JSONObject tmp = new JSONObject();
                                tmp.put(HttpHelper.Params.assetsWidth, upload.getWidth());
                                tmp.put(HttpHelper.Params.assetsHeight, upload.getHeight());
                                tmp.put(HttpHelper.Params.thumbnail, upload.getUrl());
                                tmp.put(HttpHelper.Params.type, Constants.Type.Detail.pic);

                                list.put(tmp);
                            }
                            obj.put(HttpHelper.Params.detailsList, list);


                            if(!flagAdapter.getFlags().isEmpty()){
                                String[] flags = new String[flagAdapter.getFlags().size()];
                                flagAdapter.getFlags().toArray(flags);
                                obj.put(HttpHelper.Params.titles, new JSONArray(flags));
                            }

                            String tmp = obj.toString().replaceAll("\\\\", "");
                            StringUtils.print(tmp);


                            Observable<Response<JSONObject>> observable = OkGo.<JSONObject>post(HttpHelper.Url.Dynamic.add)
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
                                    showMessage(R.string.success_dynamic_add);
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
        }else if(!StringUtils.isNullOrBlank(videoFileName)){
            List<File> videos = new ArrayList<>();
            File video = new File(videoFileName);
            videos.add(video);

            OkGo.<JSONObject>post(HttpHelper.Url.Dynamic.video)
                    .tag(this)
                    .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                    .isMultipart(true)
                    .addFileParams(HttpHelper.Params.video, videos)
                    .converter(new JsonConvert())
                    .adapt(new ObservableResponse<JSONObject>())
                    .subscribeOn(Schedulers.io())
                    .flatMap(new Function<Response<JSONObject>, ObservableSource<Response<JSONObject>>>() {

                        @Override
                        public ObservableSource<Response<JSONObject>> apply(Response<JSONObject> jsonObjectResponse) throws Exception {
                            if(!ResponseUtils.ok(jsonObjectResponse.body())){
                                throw new Exception("上传商品规格图片失败！");
                            }

                            Video video = new Gson().fromJson(ResponseUtils.getData(jsonObjectResponse.body()).toString(),
                                    new TypeToken<Video>(){}.getType());

                            JSONObject obj = new JSONObject();
                            obj.put(HttpHelper.Params.contentType, Constants.Type.Detail.video);
                            obj.put(HttpHelper.Params.subTitle, etContent.getText().toString());

                            JSONArray list = new JSONArray();
                            JSONObject tmp = new JSONObject();
                            tmp.put(HttpHelper.Params.assetsWidth, video.getVideoWidth());
                            tmp.put(HttpHelper.Params.assetsHeight, video.getVideoHeight());
                            tmp.put(HttpHelper.Params.thumbnail, video.getUrl());
                            tmp.put(HttpHelper.Params.type, Constants.Type.Detail.video);

                            list.put(tmp);
                            obj.put(HttpHelper.Params.detailsList, list);


                            if(!flagAdapter.getFlags().isEmpty()){
                                String[] flags = new String[flagAdapter.getFlags().size()];
                                flagAdapter.getFlags().toArray(flags);
                                obj.put(HttpHelper.Params.titles, new JSONArray(flags));
                            }

                            String temp = obj.toString().replaceAll("\\\\", "");
                            StringUtils.print(temp);


                            Observable<Response<JSONObject>> observable = OkGo.<JSONObject>post(HttpHelper.Url.Dynamic.add)
                                    .tag(this)
                                    .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                    .upJson(temp)
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
            try{
                JSONObject obj = new JSONObject();
                obj.put(HttpHelper.Params.contentType, Constants.Type.Detail.pic);
                obj.put(HttpHelper.Params.subTitle, etContent.getText().toString());

                if(!flagAdapter.getFlags().isEmpty()){
                    String[] flags = new String[flagAdapter.getFlags().size()];
                    flagAdapter.getFlags().toArray(flags);
                    obj.put(HttpHelper.Params.titles, new JSONArray(flags));
                }

                OkGo.<JSONObject>post(HttpHelper.Url.Dynamic.add)
                        .tag(this)
                        .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                        .upJson(obj)
                        .execute(new JsonCallback<JSONObject>() {
                            @Override
                            public void onSuccess(Response<JSONObject> response) {
                                super.onSuccess(response);
                                try {
                                    if(ResponseUtils.ok(response.body())){
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
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
                    AlertDialog dialog = new AlertDialog.Builder(PublishDynamicActivity.this)
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

        if(photos.isEmpty()){
            llMenuVideo.setVisibility(View.VISIBLE);
        }else{
            llMenuVideo.setVisibility(View.GONE);
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

                photos.add(app.getFileName());
                resetPhoto();
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

                        photos.add(app.getFileName());
                        resetPhoto();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }else if(requestCode == Constants.Request.SelectVideo && resultCode == RESULT_OK){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
                String videoPath = null;

                Uri uri=data.getData();
                if(DocumentsContract.isDocumentUri(this,uri)){
                    String docId=DocumentsContract.getDocumentId(uri);
                    if("com.android.providers.media.documents".equals(uri.getAuthority())){
                        String id=docId.split(":")[1];
                        String selection = MediaStore.Video.Media._ID+"="+id;
                        videoPath = FileUtil.getVideoPath(this, MediaStore.Video.Media.EXTERNAL_CONTENT_URI,selection);
                    }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                        Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                        videoPath = FileUtil.getImagePath(this, contentUri,null);
                    }
                }else if("content".equalsIgnoreCase(uri.getScheme())){
                    videoPath = FileUtil.getVideoPath(this, uri,null);
                }else if("file".equalsIgnoreCase(uri.getScheme())){
                    videoPath = uri.getPath();
                }

                if(!StringUtils.isNullOrBlank(videoPath)){
                    if(Build.VERSION.SDK_INT >= 24){
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());
                        builder.detectFileUriExposure();
                    }

                    long time = UIUtil.getVideoDuration(videoPath);
                    if(time > 60 * 5 * 1000){
                        showMessage(R.string.error_video_time);
                        return;
                    }

                    // 对视频做裁剪处理
                    Intent intent = new Intent(this, EsayVideoEditActivity.class);
                    intent.putExtra(Constants.Tag.data, videoPath);
                    startActivityForResult(intent, Constants.Request.CutVideo);
                }
            }
        }else if(requestCode == Constants.Request.CutVideo && resultCode == RESULT_OK){
            videoFileName = data.getStringExtra(Constants.Tag.data);
            File file = new File(videoFileName);
            if(file.exists()){
                Glide.with(this)
                        .load(file)
                        .transform(new GlideRoundTransformation(this, 5))
                        .into(ivVideoImage);

                rlAddVideo.setVisibility(View.GONE);
                ivVideoImage.setVisibility(View.VISIBLE);

                llMenuPhoto.setVisibility(View.GONE);
            }else {
                videoFileName = null;
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
                    ViewUtils.background(PublishDynamicActivity.this, 1f);
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
                            ViewUtils.background(PublishDynamicActivity.this, 0.8f);
                            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        }else if(permission.shouldShowRequestPermissionRationale){
                            showMessage(R.string.rationale_ask_again);
                        }else{
                            showMessage(R.string.rationale_cancle);
                        }});

        }

    }
}
