package com.huashengfu.StemCellsManager.activity.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.service.ServicePrivilegeAdapter;
import com.huashengfu.StemCellsManager.entity.response.sms.Init;
import com.huashengfu.StemCellsManager.entity.response.sms.Outline;
import com.huashengfu.StemCellsManager.entity.service.Privilege;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.GetRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 发布新服务-服务特权
public class PublishServicePrivilegeActivity extends BaseActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;

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
    private ServicePrivilegeAdapter servicePrivilegeAdapter;

    private Init init;
    private int id;
    private boolean modify, gonext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_publish_privilege);

        unbinder = ButterKnife.bind(this);

        servicePrivilegeAdapter = new ServicePrivilegeAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(servicePrivilegeAdapter);
        rvList.setNestedScrollingEnabled(false);

        id = getIntent().getIntExtra(Constants.Tag.data, -1);
        modify = getIntent().getBooleanExtra(Constants.Tag.modify, false);
        gonext = getIntent().getBooleanExtra(Constants.Tag.gonext, true);
        if(id == -1)
            finish();
        else
            doInit(id, 4, modify);
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

                        if(init.getOutlineList() != null){
                            servicePrivilegeAdapter.clear();
                            servicePrivilegeAdapter.addAll(init.getOutlineList(), init.getResultList());
                            servicePrivilegeAdapter.notifyDataSetChanged();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick({R.id.iv_back, R.id.btn_commit})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.btn_commit:{
                // 分步处理，此处调用接口，提交本页面编辑的数据
                if(servicePrivilegeAdapter.getSelect().isEmpty()){
                    showMessage(R.string.error_service_privilege_empty);
                    return;
                }

                // 校验
                for(Outline outline : servicePrivilegeAdapter.getSelect()){
                    if(StringUtils.isNullOrBlank(outline.getDesc())){
                        showMessage("请填写" + outline.getTitle() + "服务特权内容");
                        return;
                    }
                }

                try {
                    JSONArray list = new JSONArray();
                    for(Outline outline : servicePrivilegeAdapter.getSelect()){
                        JSONObject obj = new JSONObject();
                        obj.put(HttpHelper.Params.sid, id);
                        obj.put(HttpHelper.Params.oid, outline.getId());
                        obj.put(HttpHelper.Params.details, outline.getDesc());
                        obj.put(HttpHelper.Params.title, outline.getTitle());

                        list.put(obj);
                    }

                    StringUtils.print(list.toString());

                    if(modify){
                        OkGo.<JSONObject>put(HttpHelper.Url.Service.outlineModify + id)
                                .tag(this)
                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                .upJson(list)
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
                        OkGo.<JSONObject>post(HttpHelper.Url.Service.outlineAdd)
                                .tag(this)
                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                .upJson(list)
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
            showMessage(R.string.success_service_privilege_modify);
        else
            showMessage(R.string.success_service_privilege_add);

        if(gonext){
            Intent intent = new Intent(getApplicationContext(), PublishServiceProcessActivity.class);
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
