package com.huashengfu.StemCellsManager.activity.service;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.flag.FlagAdapter;
import com.huashengfu.StemCellsManager.adapter.service.ServiceTypeAdapter;
import com.huashengfu.StemCellsManager.adapter.service.abandonment.ServiceTypeParentAdapter;
import com.huashengfu.StemCellsManager.entity.response.sms.Init;
import com.huashengfu.StemCellsManager.entity.response.sms.Unfinished;
import com.huashengfu.StemCellsManager.entity.response.sms.init.Type;
import com.huashengfu.StemCellsManager.entity.service.Service;
import com.huashengfu.StemCellsManager.entity.service.ServiceType;
import com.huashengfu.StemCellsManager.entity.service.ServiceTypeGroup;
import com.huashengfu.StemCellsManager.event.service.UpdateServiceEvent;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.huashengfu.StemCellsManager.view.FlowLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 发布新服务-文字描述
public class PublishServiceActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_type)
    TextView tvType;

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
    private ServiceTypePopupwindow serviceTypePopupwindow;
    private FlagAdapter flagAdapter;

    private Init init;
    private int id;
    private boolean modify;
    private boolean gonext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_publish);

        unbinder = ButterKnife.bind(this);

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

        serviceTypePopupwindow = new ServiceTypePopupwindow();
        serviceTypePopupwindow.init();

        flagAdapter = new FlagAdapter();
        flagAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener<String>() {
            @Override
            public boolean onItemLongClick(View view, String flag) {
                AlertDialog dialog = new AlertDialog.Builder(PublishServiceActivity.this)
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

        id = getIntent().getIntExtra(Constants.Tag.data, -1);
        modify = getIntent().getBooleanExtra(Constants.Tag.modify, false);
        gonext = getIntent().getBooleanExtra(Constants.Tag.gonext, true);

        rvList.postDelayed(()->{
            if(id == -1)
                doQueryUnFinished();
            else
                doInit(id, 1);
        }, 200);
    }

    @OnClick({R.id.iv_back, R.id.rl_select, R.id.ll_add_flag, R.id.btn_commit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
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
            case R.id.rl_select:{
                serviceTypePopupwindow.show();
                break;
            }
            case R.id.btn_commit:{
                // 分步处理，此处调用接口，提交本页面编辑的数据
                if(etName.getText().length() <= 0){
                    showMessage(etName.getHint());
                    return;
                }

                if(etContent.getText().length() <= 0){
                    showMessage(etContent.getHint());
                    return;
                }

                if(etPhone.getText().length() <= 0){
                    showMessage(etPhone.getHint());
                    return;
                }

                if(etPhone.getText().length() != 11){
                    showMessage(R.string.error_phone_length);
                    return;
                }

                if(flagAdapter.getFlags().isEmpty()){
                    showMessage(R.string.error_service_flag_empty);
                    return;
                }

                Type type = serviceTypePopupwindow.getServiceType();
                if(type == null){
                    showMessage(R.string.error_service_type_select);
                    return;
                }

                try {
                    JSONObject obj = new JSONObject();

                    obj.put(HttpHelper.Params.name, etName.getText().toString());
                    obj.put(HttpHelper.Params.content, etContent.getText().toString());
                    obj.put(HttpHelper.Params.phone, etPhone.getText().toString());

                    obj.put(HttpHelper.Params.typeId, type.getId());

                    if(init.getBaisicInfo() != null){
                        obj.put(HttpHelper.Params.id, init.getBaisicInfo().getId());

                        String[] titles = new String[flagAdapter.getFlags().size()];
                        flagAdapter.getFlags().toArray(titles);
                        obj.put(HttpHelper.Params.titles, new JSONArray(titles));

                        doModify(obj);
                    }else{
                        JSONArray label = new JSONArray();
                        for(String str : flagAdapter.getFlags()){
                            JSONObject flag = new JSONObject();
                            flag.put(HttpHelper.Params.name, str);
                            label.put(flag);
                        }
                        obj.put(HttpHelper.Params.labelList, label);

                        doAdd(obj);
                    }
                    StringUtils.print(obj.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            }
        }
    }

    private void goNext(int id, boolean modify){
        Intent intent = new Intent(this, PublishServiceBannerActivity.class);
        intent.putExtra(Constants.Tag.data, id);
        intent.putExtra(Constants.Tag.modify, modify);
        startActivity(intent);
        finish();
    }

    private void goPage(int id, int page){
        Intent intent = null;
        switch (page){
            case 2:
                intent = new Intent(this, PublishServiceBannerActivity.class);
                break;
            case 3:
                intent = new Intent(this, PublishServicePromotionActivity.class);
                break;
            case 4:
                intent = new Intent(this, PublishServicePrivilegeActivity.class);
                break;
            case 5:
                intent = new Intent(this, PublishServiceProcessActivity.class);
                break;
            case 6:
                intent = new Intent(this, PublishServiceIntroductionActivity.class);
                break;
        }

        if(intent != null){
            intent.putExtra(Constants.Tag.data, id);
            intent.putExtra(Constants.Tag.modify, true);
            startActivity(intent);
            finish();
        }
    }


    private void doAdd(JSONObject obj){
        StringUtils.print(obj.toString());

        OkGo.<JSONObject>post(HttpHelper.Url.Service.addService)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .upJson(obj)
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
                                int id = response.body().getInt(Constants.Tag.data);
                                showMessage(R.string.success_service_add);
                                goNext(id, false);
                            }else{
                                showMessage(ResponseUtils.getMsg(response.body()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void doModify(JSONObject obj){
        StringUtils.print(obj.toString());

        OkGo.<JSONObject>put(HttpHelper.Url.Service.modifyBasicInfo)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .upJson(obj)
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
                                showMessage(R.string.success_service_modify);
                                if(gonext)
                                    goNext(init.getBaisicInfo().getId(), false);
                                else{
                                    Service service = new Service();
                                    service.setId(id);
                                    service.setName(etName.getText().toString());
                                    service.setContent(etContent.getText().toString());

                                    EventBus.getDefault().post(new UpdateServiceEvent().setService(service));
                                    finish();
                                }
                            }else{
                                showMessage(ResponseUtils.getMsg(response.body()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private void doInit(int id, int dNo){
        GetRequest<JSONObject> getRequest = OkGo.<JSONObject>get(HttpHelper.Url.Service.init)
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

                        flagAdapter.clear();

                        llAddFlag.setEnabled(true);

                        if(init.getBaisicInfo() != null){
                            flagAdapter.addAll(init.getBaisicInfo().getTitles());
                            etName.setText(init.getBaisicInfo().getName());
                            etContent.setText(init.getBaisicInfo().getContent());
                            etPhone.setText(init.getBaisicInfo().getPhone());

                            serviceTypePopupwindow.initData(init.getBaisicInfo().getTypeId());
                        }else{
                            serviceTypePopupwindow.initData();
                        }

                        serviceTypePopupwindow.resetType(false);
                        flagAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void doDelete(int id){
        OkGo.<JSONObject>delete(HttpHelper.Url.Service.delUnfinished + id)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                doInit(-1, 1);
                            }else{
                                finish();
                                showMessage(ResponseUtils.getMsg(response.body()));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    private void doQueryUnFinished(){
        OkGo.<JSONObject>get(HttpHelper.Url.Service.unfinished)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .execute(new DialogCallback<JSONObject>(this, false) {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                Unfinished unfinished = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<Unfinished>(){}.getType());

                                if(unfinished != null && unfinished.getId() > 0 && unfinished.getIsFinish() > 0){
                                    AlertDialog dialog = new AlertDialog.Builder(PublishServiceActivity.this)
                                            .setCancelable(false)
                                            .setMessage(R.string.dialog_message_publish_service_unfinished)
                                            .setPositiveButton(R.string.btn_continue_editing, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    if(unfinished.getIsFinish() == 1)
                                                        doInit(unfinished.getId(), unfinished.getIsFinish());
                                                    else if(unfinished.getIsFinish() == 2)
                                                        goNext(unfinished.getId(), true);
                                                    else
                                                        goPage(unfinished.getId(), unfinished.getIsFinish());
                                                }
                                            })
                                            .setNegativeButton(R.string.btn_discard_editing, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                    doDelete(unfinished.getId());
                                                }
                                            })
                                            .create();
                                    dialog.show();
                                }else{
                                    doInit(-1, 1);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private class ServiceTypePopupwindow{
        private PopupWindow popupWindow;
        private RecyclerView rvType, rvSystem, rvName;
        private ServiceTypeAdapter typeAdapter, systemAdapter, nameAdapter;

        private void init() {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_service_type, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.getBackground().setAlpha(170);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ViewUtils.background(PublishServiceActivity.this, 1f);
                }
            });

            rvType = view.findViewById(R.id.rv_type);
            rvSystem = view.findViewById(R.id.rv_system);
            rvName = view.findViewById(R.id.rv_name);

            initType();
            initSystem();
            initName();

            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            view.findViewById(R.id.rl_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetType(true);
                }
            });
        }

        public void resetType(boolean dismiss){
            int count = 0;
            StringBuffer sb = new StringBuffer();
            if(typeAdapter.getSelect() != null) {
                sb.append(typeAdapter.getSelect().getName()).append(" - ");
                count++;
            }

            if(systemAdapter.getSelect() != null) {
                sb.append(systemAdapter.getSelect().getName()).append(" - ");
                count++;
            }

            if(nameAdapter.getSelect() != null) {
                sb.append(nameAdapter.getSelect().getName()).append(" - ");
                count++;
            }

            if(count != 3){
                showMessage(R.string.error_service_type_select);
            }else{
//                if(sb.length() > 3)
//                    tvType.setText(sb.substring(0, sb.length() - 3));

                tvType.setText(nameAdapter.getSelect().getName());

                if(dismiss)
                    dismiss();
            }
        }

        private void initType(){
            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            rvType.setLayoutManager(layoutManager);
            rvType.setNestedScrollingEnabled(false);
            rvType.setHasFixedSize(true);

            typeAdapter = new ServiceTypeAdapter();
            typeAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Type>() {
                @Override
                public void onItemClick(View view, Type type) {
                    systemAdapter.clear();
                    systemAdapter.addAll(type.getChildren());
                    systemAdapter.notifyDataSetChanged();

                    nameAdapter.clear();
                    if(type.getChildren().size() > 0)
                        nameAdapter.addAll(type.getChildren().get(0).getChildren());
                    nameAdapter.notifyDataSetChanged();
                }
            });
            rvType.setAdapter(typeAdapter);
        }

        private void initSystem(){
            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            rvSystem.setLayoutManager(layoutManager);
            rvSystem.setNestedScrollingEnabled(false);
            rvSystem.setHasFixedSize(true);

            systemAdapter = new ServiceTypeAdapter();
            systemAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Type>() {
                @Override
                public void onItemClick(View view, Type type) {
                    nameAdapter.clear();
                    nameAdapter.addAll(type.getChildren());
                    nameAdapter.notifyDataSetChanged();
                }
            });
            rvSystem.setAdapter(systemAdapter);
        }

        private void initName(){
            GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 3);
            rvName.setLayoutManager(layoutManager);
            rvName.setNestedScrollingEnabled(false);
            rvName.setHasFixedSize(true);

            nameAdapter = new ServiceTypeAdapter();
            nameAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<Type>() {
                @Override
                public void onItemClick(View view, Type type) {

                }
            });
            rvName.setAdapter(nameAdapter);
        }

        public void initData(int id){
            typeAdapter.clear();
            systemAdapter.clear();
            nameAdapter.clear();

            if(id < 0){
                typeAdapter.addAll(init.getTypeList());

                if(init.getTypeList().size() > 0){
                    systemAdapter.addAll(init.getTypeList().get(0).getChildren());

                    if(init.getTypeList().get(0).getChildren().get(0).getChildren().size() > 0){
                        nameAdapter.addAll(init.getTypeList().get(0).getChildren().get(0).getChildren());
                    }
                }
            }else{
                typeAdapter.addAll(init.getTypeList(), id);

                if(typeAdapter.getSelect() != null)
                    systemAdapter.addAll(typeAdapter.getSelect().getChildren(), id);

                if(systemAdapter.getSelect() != null)
                    nameAdapter.addAll(systemAdapter.getSelect().getChildren(), id);
            }

            typeAdapter.notifyDataSetChanged();
            systemAdapter.notifyDataSetChanged();
            nameAdapter.notifyDataSetChanged();
        }

        public void initData(){
            initData(-1);
        }

        public boolean isShowing(){
            return popupWindow.isShowing();
        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public Type getServiceType(){
            return nameAdapter.getSelect();
        }

        public void show(){
            ViewUtils.hideSoftInput(getApplicationContext(), etName);
            ViewUtils.background(PublishServiceActivity.this, 0.8f);
            popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
    }
}
