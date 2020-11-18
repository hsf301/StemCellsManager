package com.huashengfu.StemCellsManager.activity.settings.branch;

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
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.services.core.PoiItem;
import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.AppApplication;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.activity.map.SelectAddressActivity;
import com.huashengfu.StemCellsManager.http.HttpHelper;
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
import com.lzy.okrx2.adapter.ObservableResponse;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
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

public class AddBranchActivity extends BaseActivity {

    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.ll_add)
    LinearLayout llAdd;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.et_address)
    EditText etAddress;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private Unbinder unbinder;
    private AppApplication app;
    private RxPermissions rxPermissions;
    private CameraPopupwindow cameraPopupwindow;
    private Calendar calBegin, calEnd;
    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

    private List<String> photos = new ArrayList<>();

    private TimePopupwindow timePopupwindow;

    private PoiItem poiItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_branch_add);
        unbinder = ButterKnife.bind(this);

        app = (AppApplication) getApplication();
        rxPermissions = new RxPermissions(this);

        cameraPopupwindow = new CameraPopupwindow();
        cameraPopupwindow.init();

        timePopupwindow = new TimePopupwindow();
        timePopupwindow.init();

        calBegin = Calendar.getInstance();
        calBegin.setTimeInMillis(System.currentTimeMillis());
        calBegin.set(Calendar.HOUR_OF_DAY, 8);
        calBegin.set(Calendar.MINUTE, 30);
        calBegin.set(Calendar.SECOND, 0);

        calEnd = Calendar.getInstance();
        calEnd.setTimeInMillis(System.currentTimeMillis());
        calEnd.set(Calendar.HOUR_OF_DAY, 17);
        calEnd.set(Calendar.MINUTE, 30);
        calEnd.set(Calendar.SECOND, 0);

        resetTime();

        ivImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(AddBranchActivity.this)
                        .setMessage(R.string.dialog_message_delete_photo)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                photos.clear();
                                resetPhoto();
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

    private void resetTime(){
        tvTime.setText(sdf.format(new Date(calBegin.getTimeInMillis())) + " - " + sdf.format(new Date(calEnd.getTimeInMillis())));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.iv_back, R.id.ll_add, R.id.ll_address, R.id.btn_commit, R.id.tv_time})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                showTips();
                break;
            }
            case R.id.ll_add:{
                cameraPopupwindow.show();
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
            case R.id.btn_commit:{
                if(photos.isEmpty()){
                    showMessage(R.string.error_branch_photo);
                    return;
                }

                if(etName.getText().length() == 0){
                    showMessage(etName.getHint());
                    return;
                }

                if(tvCity.getText().length() == 0){
                    showMessage(tvCity.getHint());
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

                List<File> files = new ArrayList<>();
                for(String str : photos){
                    File file = new File(str);
                    files.add(file);
                }

                showDialog(false);

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

                                JSONObject obj = new JSONObject();
                                obj.put(HttpHelper.Params.addr, etAddress.getText().toString());
                                obj.put(HttpHelper.Params.businessHours, tvTime.getText().toString());
                                obj.put(HttpHelper.Params.latitude, poiItem.getLatLonPoint().getLatitude());
                                obj.put(HttpHelper.Params.longitude, poiItem.getLatLonPoint().getLongitude());
                                obj.put(HttpHelper.Params.businessHours, tvTime.getText().toString());
                                obj.put(HttpHelper.Params.name, etName.getText().toString());
                                obj.put(HttpHelper.Params.phone, etPhone.getText().toString());
                                obj.put(HttpHelper.Params.pic, pics.get(0));

                                String tmp = obj.toString().replaceAll("\\\\", "");
                                StringUtils.print(tmp);

                                Observable<Response<JSONObject>> observable = OkGo.<JSONObject>post(HttpHelper.Url.Store.Branch.add)
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
                                        showMessage(R.string.success_branch_add);
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
            case R.id.tv_time:{
                timePopupwindow.show();
                break;
            }
        }
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        showTips();
    }

    private void showTips(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_message_nosave_branch)
                .setPositiveButton(R.string.btn_quit, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
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

    private void resetPhoto(){
        if(photos.isEmpty()){
            llAdd.setVisibility(View.VISIBLE);

            ivImage.setImageResource(android.R.color.transparent);
        }else{
            llAdd.setVisibility(View.GONE);

            Glide.with(this)
                    .load(photos.get(0))
                    .transform(new GlideRoundTransformation(this, 10))
                    .into(ivImage);
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
                tvCity.setText(poiItem.getProvinceName() + poiItem.getCityName());
                etAddress.setText(poiItem.getProvinceName() + poiItem.getCityName() + poiItem.getSnippet());
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
                    ViewUtils.background(AddBranchActivity.this, 1f);
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
                            ViewUtils.background(AddBranchActivity.this, 0.8f);
                            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
                        }else if(permission.shouldShowRequestPermissionRationale){
                            showMessage(R.string.rationale_ask_again);
                        }else{
                            showMessage(R.string.rationale_cancle);
                        }});

        }

    }

    private class TimePopupwindow {

        private PopupWindow timePopupWindow;
        private TimePicker timePickerBegin, timePickerEnd;

        private void init(){
            View view = getLayoutInflater().inflate(R.layout.popupwindow_time_branch, null);
            timePopupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            timePopupWindow.setBackgroundDrawable(new BitmapDrawable());
            timePopupWindow.getBackground().setAlpha(170);
            timePopupWindow.setOutsideTouchable(true);
            timePopupWindow.setFocusable(true);
            timePopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            timePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ViewUtils.background(AddBranchActivity.this, 1f);
                }
            });

            timePickerBegin = view.findViewById(R.id.tp_begin);
            timePickerBegin.setIs24HourView(true);

            timePickerEnd = view.findViewById(R.id.tp_end);
            timePickerEnd.setIs24HourView(true);

            view.findViewById(R.id.cancle).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    timePopupWindow.dismiss();
                }
            });

            view.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(timePickerBegin.getHour() > timePickerEnd.getHour()){
                        showMessage(R.string.error_branch_time);
                        return;
                    }else if(timePickerBegin.getHour() == timePickerEnd.getHour()){
                        if(timePickerBegin.getMinute() > timePickerEnd.getMinute()){
                            showMessage(R.string.error_branch_time);
                            return;
                        }
                    }

                    calBegin.set(Calendar.HOUR_OF_DAY, timePickerBegin.getHour());
                    calBegin.set(Calendar.MINUTE, timePickerBegin.getMinute());

                    calEnd.set(Calendar.HOUR_OF_DAY, timePickerEnd.getHour());
                    calEnd.set(Calendar.MINUTE, timePickerEnd.getMinute());

                    resetTime();
                    timePopupWindow.dismiss();
                }
            });
        }

        public boolean isShowing(){
            return timePopupWindow.isShowing();
        }

        public void dismiss(){
            timePopupWindow.dismiss();
        }

        public void show(){
            timePickerBegin.setHour(calBegin.get(Calendar.HOUR_OF_DAY));
            timePickerBegin.setMinute(calBegin.get(Calendar.MINUTE));

            timePickerEnd.setHour(calEnd.get(Calendar.HOUR_OF_DAY));
            timePickerEnd.setMinute(calEnd.get(Calendar.MINUTE));

            ViewUtils.hideSoftInput(getApplicationContext(), etName);
            ViewUtils.background(AddBranchActivity.this, 0.8f);
            timePopupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
    }
}
