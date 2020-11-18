package com.huashengfu.StemCellsManager.activity.goods;

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
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
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
import com.huashengfu.StemCellsManager.adapter.goods.GoodsDetailAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Goods;
import com.huashengfu.StemCellsManager.entity.response.goods.Detail;
import com.huashengfu.StemCellsManager.entity.response.goods.Init;
import com.huashengfu.StemCellsManager.entity.response.DetailsUpload;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.convert.JsonConvert;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.BitmapUtils;
import com.huashengfu.StemCellsManager.utils.FileUtil;
import com.huashengfu.StemCellsManager.utils.ImageUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;
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
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

// 发布新商品-商品详情
public class PublishGoodsDetailActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.scrollview)
    NestedScrollView scrollView;

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
    @BindView(R.id.ll_modify_text)
    LinearLayout llModifyText;

    private Unbinder unbinder;
    private GoodsDetailAdapter goodsDetailAdapter;

    private CameraPopupwindow cameraPopupwindow;

    private AppApplication app;
    private RxPermissions rxPermissions;

    private Goods goods;
    private Init init;
    private boolean modify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_publish_detail);

        unbinder = ButterKnife.bind(this);

        app = (AppApplication) getApplication();
        rxPermissions = new RxPermissions(this);

        cameraPopupwindow = new CameraPopupwindow();
        cameraPopupwindow.init();

        goodsDetailAdapter = new GoodsDetailAdapter();
        goodsDetailAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                if(goodsDetailAdapter.getItemCount() > 0)
                    llModifyText.setVisibility(View.VISIBLE);
                else
                    llModifyText.setVisibility(View.GONE);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(goodsDetailAdapter);
        rvList.setNestedScrollingEnabled(false);

        goods = (Goods) getIntent().getSerializableExtra(Constants.Tag.data);
        modify = getIntent().getBooleanExtra(Constants.Tag.modify, false);

        rvList.postDelayed(()->{
            doInit(goods.getGoodsId(), 3);
        }, 200);
    }

    private void doInit(int id, int dNo){
        GetRequest<JSONObject> getRequest = OkGo.<JSONObject>get(HttpHelper.Url.Goods.init)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.dNo, dNo);

        if(id > 0){
            getRequest.params(HttpHelper.Params.goodsId, id);
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

                        if(init.getDetails() != null){
                            goodsDetailAdapter.clear();
                            for(Detail details : init.getDetails()){
                                com.huashengfu.StemCellsManager.entity.goods.Detail detail = new com.huashengfu.StemCellsManager.entity.goods.Detail();
                                detail.setContent(details.getDetailsText());
                                detail.setPhoto(details.getType().equals(Constants.Type.Detail.pic));
                                detail.setText(details.getType().equals(Constants.Type.Detail.text));

                                goodsDetailAdapter.add(detail);
                            }
                            goodsDetailAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.btn_commit, R.id.ll_add_photo, R.id.ll_add_text, R.id.ll_modify_text})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.ll_add_photo:{
                if(goodsDetailAdapter.isModify())
                    return;

                cameraPopupwindow.show();
                break;
            }
            case R.id.ll_add_text:{
                if(goodsDetailAdapter.isModify())
                    return;

                com.huashengfu.StemCellsManager.entity.goods.Detail detail = new com.huashengfu.StemCellsManager.entity.goods.Detail();
                detail.setText(true);
                goodsDetailAdapter.add(detail);
                goodsDetailAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.ll_modify_text:{
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rvList.getLayoutParams();
                layoutParams.rightMargin = 0;

                goodsDetailAdapter.setModify(true);
                goodsDetailAdapter.notifyDataSetChanged();
                break;
            }
            case R.id.btn_commit:{
                // 分步处理，此处调用接口，提交本页面编辑的数据
                if(goodsDetailAdapter.getDetail().isEmpty()){
                    showMessage(R.string.error_goods_details_empty);
                    return;
                }

                List<File> files = new ArrayList<>();
                Map<Integer, Object> positionMap = new ConcurrentHashMap<>();
                for(int i=0; i<goodsDetailAdapter.getDetail().size(); i++){
                    com.huashengfu.StemCellsManager.entity.goods.Detail detail = goodsDetailAdapter.getDetail().get(i);

                    if(detail.isPhoto()){
                        File file = new File(detail.getContent());
                        if(file.exists()) {
                            positionMap.put(i, detail);
                            files.add(file);
                        }
                    }
                }

                if(files.isEmpty()){
                    //没有图片
                    try {
                        JSONArray list = new JSONArray();

                        for(com.huashengfu.StemCellsManager.entity.goods.Detail detail: goodsDetailAdapter.getDetail()){
                            JSONObject obj = new JSONObject();
                            obj.put(HttpHelper.Params.detailsText, detail.getContent());
                            obj.put(HttpHelper.Params.goodsId, goods.getGoodsId());

                            if(detail.isPhoto())
                                obj.put(HttpHelper.Params.type, Constants.Type.Detail.pic);
                            else if(detail.isText())
                                obj.put(HttpHelper.Params.type, Constants.Type.Detail.text);

                            list.put(obj);
                        }

                        String tmp = "";

                        if(modify){
                            JSONObject obj = new JSONObject();
                            obj.put(HttpHelper.Params.goodsDetailsList, list);
                            obj.put(HttpHelper.Params.goodsId, goods.getGoodsId());
                            tmp = obj.toString();
                        }else{
                            tmp = list.toString();
                        }

                        tmp = tmp.replaceAll("\\\\", "");
                        StringUtils.print(tmp);

                        if(modify){
                            OkGo.<JSONObject>put(HttpHelper.Url.Goods.Details.modify)
                                    .tag(this)
                                    .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                    .upJson(tmp)
                                    .execute(new DialogCallback<JSONObject>(this, false) {
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
                                        public void onError(Response<JSONObject> response) {
                                            super.onError(response);
                                        }
                                    });
                        }else{
                            OkGo.<JSONObject>post(HttpHelper.Url.Goods.Details.add)
                                    .tag(this)
                                    .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                    .upJson(tmp)
                                    .execute(new DialogCallback<JSONObject>(this, false) {
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
                                        public void onError(Response<JSONObject> response) {
                                            super.onError(response);
                                        }
                                    });
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else{
                    //有图片
                    showDialog(false);

                    OkGo.<JSONObject>post(HttpHelper.Url.Goods.Details.upload)
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
                                        throw new Exception("上传图片失败！");
                                    }

                                    List<DetailsUpload> detailsUploads = new Gson().fromJson(ResponseUtils.getList(jsonObjectResponse.body()).toString(),
                                            new TypeToken<List<DetailsUpload>>(){}.getType());

                                    JSONArray list = new JSONArray();

                                    int photos = 0;
                                    for(int i=0; i<goodsDetailAdapter.getDetail().size(); i++){
                                        com.huashengfu.StemCellsManager.entity.goods.Detail detail = goodsDetailAdapter.getDetail().get(i);
                                        JSONObject obj = new JSONObject();

                                        if(!modify)
                                            obj.put(HttpHelper.Params.goodsId, goods.getGoodsId());

                                        if(detail.isPhoto()) {
                                            // 找到对应位置的图片，这里有可能为非本地图片，非本地图片，不再上传
                                            if(positionMap.get(i) != null){
                                                DetailsUpload detailsUpload = detailsUploads.get(photos);
                                                obj.put(HttpHelper.Params.detailsText, detailsUpload.getUrl());
                                                obj.put(HttpHelper.Params.width, detailsUpload.getWidth());
                                                obj.put(HttpHelper.Params.height, detailsUpload.getHeight());

                                                photos++;
                                            }else{
                                                obj.put(HttpHelper.Params.detailsText, detail.getContent());
//                                                obj.put(HttpHelper.Params.width, detail.getWidth());
//                                                obj.put(HttpHelper.Params.height, detail.getHeight());
                                            }

                                            obj.put(HttpHelper.Params.type, Constants.Type.Detail.pic);
                                        } else if(detail.isText()) {
                                            obj.put(HttpHelper.Params.type, Constants.Type.Detail.text);
                                            obj.put(HttpHelper.Params.detailsText, detail.getContent());
                                        }

                                        list.put(obj);
                                    }

                                    String tmp = "";
                                    if(modify){
                                        JSONObject obj = new JSONObject();
                                        obj.put(HttpHelper.Params.goodsDetailsList, list);
                                        obj.put(HttpHelper.Params.goodsId, goods.getGoodsId());
                                        tmp = obj.toString();
                                    }else{
                                        tmp = list.toString();
                                    }

                                    tmp = tmp.replaceAll("\\\\", "");
                                    StringUtils.print(tmp);

                                    Observable<Response<JSONObject>> observable = null;
                                    if(modify){
                                        observable = OkGo.<JSONObject>put(HttpHelper.Url.Goods.Details.modify)
                                                .tag(this)
                                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                                .upJson(tmp)
                                                .converter(new JsonConvert())
                                                .adapt(new ObservableResponse<JSONObject>());
                                    }else{
                                        observable = OkGo.<JSONObject>post(HttpHelper.Url.Goods.Details.add)
                                                .tag(this)
                                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                                .upJson(tmp)
                                                .converter(new JsonConvert())
                                                .adapt(new ObservableResponse<JSONObject>());
                                    }
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
                break;
            }
        }
    }

    private void goFinish(){
        if(modify)
            showMessage(R.string.success_goods_detail_modify);
        else
            showMessage(R.string.success_goods_detail_add);

        finish();
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if(goodsDetailAdapter.isModify()){
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rvList.getLayoutParams();
            layoutParams.rightMargin = ViewUtils.dip2px(70);

            goodsDetailAdapter.setModify(false);
            goodsDetailAdapter.notifyDataSetChanged();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void addPhoto(String fileName){
        com.huashengfu.StemCellsManager.entity.goods.Detail detail = new com.huashengfu.StemCellsManager.entity.goods.Detail();
        detail.setPhoto(true);
        detail.setContent(fileName);

        goodsDetailAdapter.add(detail);
        goodsDetailAdapter.notifyDataSetChanged();
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
                    ViewUtils.background(PublishGoodsDetailActivity.this, 1f);
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
                            ViewUtils.background(PublishGoodsDetailActivity.this, 0.8f);
                            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        }else if(permission.shouldShowRequestPermissionRationale){
                            showMessage(R.string.rationale_ask_again);
                        }else{
                            showMessage(R.string.rationale_cancle);
                        }});

        }

    }
}
