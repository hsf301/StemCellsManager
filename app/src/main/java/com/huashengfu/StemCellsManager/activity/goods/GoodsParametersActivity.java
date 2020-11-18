package com.huashengfu.StemCellsManager.activity.goods;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.param.ParametersAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.ActivityConsultation;
import com.huashengfu.StemCellsManager.entity.response.goods.Parameters;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class GoodsParametersActivity extends BaseActivity {

    private Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;

    private int id;
    private ParametersAdapter adapter;
    private ArrayList<Parameters> oldParam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_parameters);
        unbinder = ButterKnife.bind(this);

        Object obj = getIntent().getSerializableExtra(Constants.Tag.list);
        if(obj != null){
            if(obj instanceof ArrayList){
                oldParam = (ArrayList<Parameters>) obj;
            }
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adapter = new ParametersAdapter();
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<ActivityConsultation>() {
            @Override
            public void onItemClick(View view, ActivityConsultation activityConsultation) {

            }
        });

        rvList.setAdapter(adapter);
        rvList.setLayoutManager(layoutManager);

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

        id = getIntent().getIntExtra(Constants.Tag.data, -1);
        if(id == -1)
            finish();
        else
            mySwipeRefreshLayout.post(()->{
                mySwipeRefreshLayout.setRefreshing(true);
                doQuery();
            });
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Goods.Parameters.initByTypeId + "?" + HttpHelper.Params.typeId + "=" + id)
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .execute(new DialogCallback<JSONObject>(this, false) {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                List<Parameters> parameters = new Gson().fromJson(ResponseUtils.getList(response.body()).toString(),
                                        new TypeToken<List<Parameters>>(){}.getType());

                                adapter.clear();
                                adapter.addAll(parameters, oldParam);
                                adapter.notifyDataSetChanged();

                                if(adapter.getItemCount() == 0)
                                    emptyPage.setVisibility(View.VISIBLE);
                                else
                                    emptyPage.setVisibility(View.GONE);
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

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @OnClick({R.id.iv_back, R.id.btn_next})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                goBack();
                break;
            }
            case R.id.btn_next:{
                Intent data = new Intent();
                ArrayList<Parameters> parameters = new ArrayList<>();
                parameters.addAll(adapter.getParameters());
                data.putExtra(Constants.Tag.data, parameters);
                setResult(RESULT_OK, data);
                finish();
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
        goBack();
    }

    private void goBack(){
        showNoSaveDialog(
                R.string.dialog_message_nosave_goods_parameters,
                R.string.btn_continue_edit,
                R.string.btn_quit,
                new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
