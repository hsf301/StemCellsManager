package com.huashengfu.StemCellsManager.activity.goods;

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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.AppApplication;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.CommoditySpecificationsAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.ModifySpecificationsAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Goods;
import com.huashengfu.StemCellsManager.entity.goods.Specifications;
import com.huashengfu.StemCellsManager.entity.response.goods.Sku;
import com.huashengfu.StemCellsManager.entity.response.goods.Upload;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
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

// 编辑商品规格
public class ModifySpecificationsActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;

    private ModifySpecificationsAdapter adapter;
    private AppApplication app;
    private RxPermissions rxPermissions;
    private CameraPopupwindow cameraPopupwindow;

    private int currentPosition;

    private Goods goods;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_modify_specifications);

        unbinder = ButterKnife.bind(this);

        app = (AppApplication) getApplication();
        rxPermissions = new RxPermissions(this);

        cameraPopupwindow = new CameraPopupwindow();
        cameraPopupwindow.init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adapter = new ModifySpecificationsAdapter();
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Object>() {
            @Override
            public void onItemClick(View view, Object object) {
                switch (view.getId()){
                    case R.id.iv_image:{
                        currentPosition = (int) object;
                        cameraPopupwindow.show();
                        break;
                    }
                    default:{
                        Specifications specifications = (Specifications)object;
                    }
                }

            }
        });

        rvList.setAdapter(adapter);
        rvList.setNestedScrollingEnabled(false);

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

        goods = (Goods) getIntent().getSerializableExtra(Constants.Tag.data);

        mySwipeRefreshLayout.post(()->{
            mySwipeRefreshLayout.setRefreshing(true);
            doQuery();
        });
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if(adapter.hasEmpty()){
            showDialog();
            return;
        }
        super.onBackPressed();
    }

    private void showDialog(){
        showDialog(null);
    }

    private void showDialog(Runnable callBack){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_message_nosave_commodity_specifications)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int witch) {
                        dialogInterface.dismiss();

                        if(callBack != null)
                            callBack.run();
                        else
                            finish();
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
    }

    @OnClick({R.id.iv_back, R.id.btn_create, R.id.btn_next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                if(adapter.hasEmpty()){
                    showDialog();
                }else{
                    finish();
                }
                break;
            }
            case R.id.btn_create:{
                if(adapter.hasEmpty()){
                    showMessage(R.string.error_nosave_commodity_specifications);
                    return;
                }else{
                    Specifications tmp = new Specifications();
                    tmp.setNewItem(true);
                    adapter.add(tmp);
                    adapter.notifyDataSetChanged();
                }
                break;
            }
            case R.id.btn_next:{
                if(adapter.hasEmpty()){
                    showDialog(new Runnable() {
                        @Override
                        public void run() {
                            doSave();
                        }
                    });
                }else{
                    doSave();
                }
                break;
            }
        }
    }

    private void doSave(){
        List<File> files = new ArrayList<>();
        List<String> oldPhotos = new ArrayList<>();
        Map<Integer, Object> photoMap = new ConcurrentHashMap<>();
        for(int i=0; i<adapter.getSpecifications().size(); i++){
            Specifications tmp = adapter.getSpecifications().get(i);
            if(tmp.getImage().startsWith("http")){
                oldPhotos.add(tmp.getImage());
            }else{
                File file = new File((tmp.getImage()));
                files.add(file);
                photoMap.put(i, tmp);
            }
        }

        showDialog(false);
        if(files.isEmpty()){
            //没有新图片，直接更新
            try{
                JSONArray list = new JSONArray();
                for(Specifications tmp : adapter.getSpecifications()){
                    JSONObject obj = new JSONObject();

                    obj.put(HttpHelper.Params.skuName, tmp.getSpecifications());
                    obj.put(HttpHelper.Params.skuPic, tmp.getImage());
                    obj.put(HttpHelper.Params.skuSum, tmp.getNumber());
                    obj.put(HttpHelper.Params.skuPrice, tmp.getPrice());

                    list.put(obj);
                }

                String tmp = list.toString().replaceAll("\\\\", "");
                StringUtils.print(tmp);

                OkGo.<JSONObject>put(HttpHelper.Url.Goods.Stock.modify + goods.getGoodsId())
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
            OkGo.<JSONObject>post(HttpHelper.Url.Goods.upload)
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
                                throw new Exception("上传商品规格图片失败！");
                            }

                            Upload uploadSpecificationsPic = new Gson().fromJson(jsonObjectResponse.body().toString(),
                                    new TypeToken<Upload>(){}.getType());

                            //商品规格
                            JSONArray skus = new JSONArray();
                            int photos = 0;
                            for(int i=0; i<adapter.getSpecifications().size(); i++){
                                Specifications tmp = adapter.getSpecifications().get(i);

                                JSONObject sku = new JSONObject();
                                sku.put(HttpHelper.Params.skuName, tmp.getSpecifications());

                                if(tmp.getImage().startsWith("http"))
                                    sku.put(HttpHelper.Params.skuPic, tmp.getImage());
                                else{
                                    if(photoMap.get(i) != null) {
                                        sku.put(HttpHelper.Params.skuPic, uploadSpecificationsPic.getData().get(photos));
                                        photos++;
                                    }
                                }
                                sku.put(HttpHelper.Params.skuPrice, tmp.getPrice());
                                sku.put(HttpHelper.Params.skuSum, tmp.getNumber());

                                skus.put(sku);
                            }

                            String tmp = skus.toString().replaceAll("\\\\", "");
                            StringUtils.print(tmp);

                            Observable<Response<JSONObject>> observable = OkGo.<JSONObject>put(HttpHelper.Url.Goods.Stock.modify + goods.getGoodsId())
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
        showMessage(R.string.success_goods_specifications_modify);
        finish();
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Goods.Stock.list + "?" + HttpHelper.Params.goodsId + "=" + goods.getGoodsId())
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .execute(new DialogCallback<JSONObject>(this, false) {
                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                List<Sku> skus = new Gson().fromJson(ResponseUtils.getList(response.body()).toString(),
                                        new TypeToken<List<Sku>>(){}.getType());

                                adapter.clear();
                                for(Sku sku : skus){
                                    Specifications tmp = new Specifications();
                                    tmp.setImage(sku.getSkuPic());
                                    tmp.setPrice(sku.getSkuPrice());
                                    tmp.setNumber(sku.getSkuSurplusSum());
                                    tmp.setSpecifications(sku.getSkuName());
                                    adapter.add(tmp);
                                }
                                adapter.notifyDataSetChanged();
                            }else{
                                showMessage(ResponseUtils.getMsg(response.body()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void  resetPhoto(String fileName){
        File file = new File(fileName);
        if(file.exists()){
            if(file.length() > Constants.photoMaxSize){
                showMessage(R.string.error_photo_size);
                return;
            }
        }

        adapter.update(currentPosition, fileName);
        adapter.notifyDataSetChanged();
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

                resetPhoto(app.getFileName());
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

                        resetPhoto(app.getFileName());
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
                    ViewUtils.background(ModifySpecificationsActivity.this, 1f);
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
                            ViewUtils.background(ModifySpecificationsActivity.this, 0.8f);
                            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        }else if(permission.shouldShowRequestPermissionRationale){
                            showMessage(R.string.rationale_ask_again);
                        }else{
                            showMessage(R.string.rationale_cancle);
                        }});

        }

    }
}
