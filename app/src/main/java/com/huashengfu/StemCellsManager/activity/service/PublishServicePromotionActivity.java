package com.huashengfu.StemCellsManager.activity.service;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.service.ServicePromotionFlagAdapter;
import com.huashengfu.StemCellsManager.entity.response.sms.Init;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.view.FlowLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 发布新服务-服务促销信息
public class PublishServicePromotionActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.et_content)
    EditText etContent;
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
    private ServicePromotionFlagAdapter servicePromotionFlagAdapter;

    private Init init;
    private int id;
    private boolean modify, gonext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_publish_promotion);

        unbinder = ButterKnife.bind(this);

        etContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length() > Constants.promotionContentMaxLength){
                    etContent.setText(editable.toString().substring(0, Constants.promotionContentMaxLength));
                    etContent.setSelection(etContent.length());
                }

                tvContentCount.setText(etContent.length() + "/" + Constants.promotionContentMaxLength);
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

        tvContentCount.setText(etContent.length() + "/" + Constants.promotionContentMaxLength);

        servicePromotionFlagAdapter = new ServicePromotionFlagAdapter();
        servicePromotionFlagAdapter.setOnItemLongClickListener(new BaseAdapter.OnItemLongClickListener<String>() {
            @Override
            public boolean onItemLongClick(View view, String flag) {
                AlertDialog dialog = new AlertDialog.Builder(PublishServicePromotionActivity.this)
                        .setMessage(R.string.dialog_message_delete_service_flag)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                int position = servicePromotionFlagAdapter.remove(flag);
                                servicePromotionFlagAdapter.notifyItemRemoved(position);
                                servicePromotionFlagAdapter.notifyDataSetChanged();
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
        FlowLayoutManager layoutManager = new FlowLayoutManager(this, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(servicePromotionFlagAdapter);
        rvList.setNestedScrollingEnabled(false);

        id = getIntent().getIntExtra(Constants.Tag.data, -1);
        modify = getIntent().getBooleanExtra(Constants.Tag.modify, false);
        gonext = getIntent().getBooleanExtra(Constants.Tag.gonext, true);
        if(id == -1)
            finish();
        else
            doInit(id, 3, modify);
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

                        if(init.getCharacteristicInfo() != null){
                            etContent.setText(StringUtils.isNullOrBlank(init.getCharacteristicInfo().getDetails()) ? "" : init.getCharacteristicInfo().getDetails());
                            servicePromotionFlagAdapter.add(init.getCharacteristicInfo().getTitleOne());
                            servicePromotionFlagAdapter.add(init.getCharacteristicInfo().getTitleTwo());
                            servicePromotionFlagAdapter.add(init.getCharacteristicInfo().getTitleThree());
                            servicePromotionFlagAdapter.add(init.getCharacteristicInfo().getTitleFour());
                            servicePromotionFlagAdapter.notifyDataSetChanged();
                        }

                        if(servicePromotionFlagAdapter.getItemCount() >= Constants.flagMax){
                            llAddFlag.setEnabled(false);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.ll_add_flag, R.id.btn_commit})
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

                if(servicePromotionFlagAdapter.getItemCount() >= Constants.flagMax){
                    showMessage(R.string.error_service_flag);
                    break;
                }

                servicePromotionFlagAdapter.add(etFlag.getText().toString());
                servicePromotionFlagAdapter.notifyDataSetChanged();
                etFlag.setText("");

                if(servicePromotionFlagAdapter.getItemCount() >= Constants.flagMax){
                    llAddFlag.setEnabled(false);
                }

                break;
            }
            case R.id.btn_commit:{
                // 分步处理，此处调用接口，提交本页面编辑的数据
                if(etContent.getText().length() <= 0){
                    showMessage(etContent.getHint());
                    return;
                }

                try {
                    JSONObject obj = new JSONObject();
                    obj.put(HttpHelper.Params.details, etContent.getText().toString());
                    obj.put(HttpHelper.Params.sid, id);

                    if(modify && init != null
                        && init.getCharacteristicInfo() != null && init.getCharacteristicInfo().getId() > 0)
                        obj.put(HttpHelper.Params.id, init.getCharacteristicInfo().getId());

                    String[] keys = new String[]{
                            HttpHelper.Params.titleOne,
                            HttpHelper.Params.titleTwo,
                            HttpHelper.Params.titleThree,
                            HttpHelper.Params.titleFour
                    };

                    for(int i=0; i<servicePromotionFlagAdapter.getItemCount(); i++){
                        obj.put(keys[i], servicePromotionFlagAdapter.getFlags().get(i));
                    }

                    if(modify){
                        OkGo.<JSONObject>put(HttpHelper.Url.Service.characteristicModify)
                                .tag(this)
                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                .upJson(obj)
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
                    }else{
                        OkGo.<JSONObject>post(HttpHelper.Url.Service.characteristicAdd)
                                .tag(this)
                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                .upJson(obj)
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    private void goNext(){
        if(modify)
            showMessage(R.string.success_service_promotion_modify);
        else
            showMessage(R.string.success_service_promotion_add);

        if(gonext){
            Intent intent = new Intent(getApplicationContext(), PublishServicePrivilegeActivity.class);
            intent.putExtra(Constants.Tag.data, id);
            intent.putExtra(Constants.Tag.modify, false);
            startActivity(intent);
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
