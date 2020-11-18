package com.huashengfu.StemCellsManager.activity.goods;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
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
import com.huashengfu.StemCellsManager.adapter.ImageViewAdapter;
import com.huashengfu.StemCellsManager.adapter.ViewAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.preview.GoodsDetailAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.preview.ParametersAdapter;
import com.huashengfu.StemCellsManager.adapter.goods.preview.SkuAdapter;
import com.huashengfu.StemCellsManager.entity.goods.Goods;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.view.FlowLayoutManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 商品预览
public class PreviewGoodsActivity extends BaseActivity {

    @BindView(R.id.rv_sku)
    RecyclerView rvSku;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.scrollview)
    NestedScrollView scrollView;

    @BindView(R.id.tv_view_count)
    TextView tvViewCount;

    @BindView(R.id.vp_container)
    ViewPager vpContainer;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_original_price)
    TextView tvOriginalPrice;
    @BindView(R.id.tv_sell)
    TextView tvSell;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_free)
    TextView tvFree;
    @BindView(R.id.tv_7day)
    TextView tv7Day;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_type_count)
    TextView tvTypeCount;
    @BindView(R.id.rv_details)
    RecyclerView rvDetails;
    @BindView(R.id.rv_parameters)
    RecyclerView rvParameters;

    private Goods goods;
    private Unbinder unbinder;

    private ViewAdapter viewAdapter;
    private SuperPlayerView mSuperPlayerView;

    private SkuAdapter skuAdapter;
    private ParametersAdapter parametersAdapter;
    private GoodsDetailAdapter goodsDetailAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_preview);
        unbinder = ButterKnife.bind(this);

        goods = (Goods) getIntent().getSerializableExtra(Constants.Tag.data);

        vpContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 0){
                    if(mSuperPlayerView != null){
                        if (mSuperPlayerView.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAYING) {
                            mSuperPlayerView.onResume();
                            if (mSuperPlayerView.getPlayMode() == SuperPlayerConst.PLAYMODE_FLOAT) {
                                mSuperPlayerView.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
                            }
                        }
                    }
                }else{
                    if(mSuperPlayerView != null){
                        if(mSuperPlayerView.getPlayState() != SuperPlayerConst.PLAYMODE_FLOAT){
                            mSuperPlayerView.onPause();
                        }
                    }
                }

                if(vpContainer.getAdapter() != null){
                    tvViewCount.setText((position+1) + "/" + vpContainer.getAdapter().getCount());
                }else{
                    tvViewCount.setText("0/0");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        initSku();

        initParameters();

        initDetail();

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

        mySwipeRefreshLayout.post(()->{
            mySwipeRefreshLayout.setRefreshing(true);
            doQuery();
        });
    }

    private void initSku(){
        FlowLayoutManager layoutManager = new FlowLayoutManager(this, false);
        skuAdapter = new SkuAdapter();
        rvSku.setLayoutManager(layoutManager);
        rvSku.setNestedScrollingEnabled(false);
        rvSku.setAdapter(skuAdapter);
    }

    private void initParameters(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvParameters.setLayoutManager(layoutManager);
        rvParameters.setNestedScrollingEnabled(false);
        parametersAdapter = new ParametersAdapter();
        rvParameters.setAdapter(parametersAdapter);
    }

    private void initDetail(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvDetails.setLayoutManager(layoutManager);
        rvDetails.setNestedScrollingEnabled(false);
        goodsDetailAdapter = new GoodsDetailAdapter();
        rvDetails.setAdapter(goodsDetailAdapter);
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Goods.info + "?" + HttpHelper.Params.goodsId + "=" + goods.getGoodsId())
                .tag(this)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                Goods goods = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<Goods>(){}.getType());

                                List<View> views = new ArrayList<>();
                                if(!StringUtils.isNullOrBlank(goods.getVideoUrl())){
                                    View videoView = getLayoutInflater().inflate(R.layout.adapter_goods_preview_video, null);
                                    mSuperPlayerView = videoView.findViewById(R.id.superVodPlayerView);

                                    // 隐藏播放器控件的返回和全屏按钮
                                    mSuperPlayerView.findViewById(R.id.superplayer_iv_back).setVisibility(View.GONE);
                                    mSuperPlayerView.findViewById(R.id.superplayer_iv_fullscreen).setVisibility(View.INVISIBLE);

                                    SuperPlayerModel model = new SuperPlayerModel();
                                    model.url = goods.getVideoUrl();
                                    mSuperPlayerView.playWithModel(model);

                                    mSuperPlayerView.setHideVideoProgress(true);

                                    views.add(videoView);
                                }

                                if(goods.getBanners() != null){
                                    for(String str : goods.getBanners()){
                                        View imageView = getLayoutInflater().inflate(R.layout.adapter_goods_preview_image, null);
                                        ImageView ivImage = imageView.findViewById(R.id.iv_image);

                                        Glide.with(getApplicationContext())
                                                .load(str)
                                                .placeholder(R.drawable.image_loading_pic)
                                                .into(ivImage);

                                        views.add(imageView);
                                    }
                                }

                                viewAdapter = new ViewAdapter(views);
                                vpContainer.setAdapter(viewAdapter);

                                if(viewAdapter.getCount()> 0){
                                    tvViewCount.setText("1/" + viewAdapter.getCount());
                                }else{
                                    tvViewCount.setText("0/0");
                                }

                                tvPrice.setText(String.valueOf(goods.getMinPrice()));
                                tvOriginalPrice.setText("￥" + String.valueOf(goods.getOriginalPrice()));
                                tvSell.setText(String.valueOf(goods.getSellSum()));
                                tvTitle.setText(goods.getName());
                                tvContent.setText(goods.getContent());

                                if(goods.getLogistics() == 0.0d)
                                    tvFree.setText("免运费");
                                else
                                    tvFree.setText("￥" + goods.getLogistics());

                                tv7Day.setText(goods.getServiceRemarks());

                                tvAddress.setText(goods.getDeliverAddress());

                                skuAdapter.clear();
                                if(goods.getSkus() != null){
                                    tvTypeCount.setText(String.format(getResources().getString(R.string.str_goods_type_count), goods.getSkus().size()));
                                    skuAdapter.addAll(goods.getSkus());
                                }else{
                                    tvTypeCount.setText(String.format(getResources().getString(R.string.str_goods_type_count), 0));
                                }
                                skuAdapter.notifyDataSetChanged();

                                goodsDetailAdapter.clear();
                                if(goods.getDetails() != null)
                                    goodsDetailAdapter.addAll(goods.getDetails());
                                goodsDetailAdapter.notifyDataSetChanged();

                                parametersAdapter.clear();
                                if(goods.getParams() != null)
                                    parametersAdapter.addAll(goods.getParams());
                                parametersAdapter.notifyDataSetChanged();

                                scrollView.setVisibility(View.VISIBLE);
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
                        mySwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @OnClick(R.id.iv_back)
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 重新开始播放
        if (mSuperPlayerView != null && mSuperPlayerView.getPlayState() == SuperPlayerConst.PLAYSTATE_PLAYING) {
            mSuperPlayerView.onResume();
            if (mSuperPlayerView.getPlayMode() == SuperPlayerConst.PLAYMODE_FLOAT) {
                mSuperPlayerView.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 停止播放
        if (mSuperPlayerView != null && mSuperPlayerView.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
            mSuperPlayerView.onPause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // 释放
        if(mSuperPlayerView != null){
            mSuperPlayerView.release();
            if (mSuperPlayerView.getPlayMode() != SuperPlayerConst.PLAYMODE_FLOAT) {
                mSuperPlayerView.resetPlayer();
            }
        }
        unbinder.unbind();
    }
}
