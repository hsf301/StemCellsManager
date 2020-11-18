package com.huashengfu.StemCellsManager.activity.interaction;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.adapter.interaction.dynamic.CommentAdapter;
import com.huashengfu.StemCellsManager.entity.interaction.Comment;
import com.huashengfu.StemCellsManager.entity.interaction.DynamicComment;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.loadmore.EndlessRecyclerOnScrollListener;
import com.huashengfu.StemCellsManager.loadmore.LoadMoreWrapper;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 动态评论
public class DynamicCommentActivity extends BaseActivity {

    private DynamicComment dynamicComment;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_image)
    ImageView ivImage;
    @BindView(R.id.tv_manager)
    TextView tvManger;
    @BindView(R.id.rl_button)
    RelativeLayout rlButton;
    @BindView(R.id.iv_check)
    ImageView ivCheck;
    @BindView(R.id.tv_all)
    TextView tvAll;

    private Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;

    private boolean manager = false;
    private boolean all = false;


    private int pageNum = 1;
    private int pageSize = 10;
    private int pageCount = 1;

    private CommentAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;

    @SuppressLint("StringFormatMatches")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_comment);

        unbinder = ButterKnife.bind(this);

        dynamicComment = (DynamicComment) getIntent().getSerializableExtra(Constants.Tag.data);

        tvCount.setText(String.format(getResources().getString(R.string.str_dynamic_comment_count), 0));
        tvContent.setText(dynamicComment.getSubTitle());
        Glide.with(this)
                .load(dynamicComment.getPicUrl())
                .placeholder(R.drawable.image_loading_pic_small)
                .transform(new GlideRoundTransformation(this, 2))
                .into(ivImage);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adapter = new CommentAdapter();
        adapter.setOnCommentAdapter(new CommentAdapter.OnCommentAdapter() {
            @Override
            public void notifyDataSetChanged() {
                loadMoreWrapper.notifyDataSetChanged();
            }
        });

        loadMoreWrapper = new LoadMoreWrapper(adapter);
        rvList.setAdapter(loadMoreWrapper);
        rvList.setLayoutManager(layoutManager);

        rvList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if(manager){
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                }else{
                    if(pageNum <= pageCount){
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
                        mySwipeRefreshLayout.postDelayed(()->{
                            doQuery();
                        }, 1000);
                    }else{
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }

            }
        });

        mySwipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(R.color.colorSwipeRefreshLayout1),
                getResources().getColor(R.color.colorSwipeRefreshLayout2),
                getResources().getColor(R.color.colorSwipeRefreshLayout3)
        );
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                doQuery();
            }
        });

        mySwipeRefreshLayout.post(()->{
            mySwipeRefreshLayout.setRefreshing(true);
            doQuery();
        });
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Dynamic.Comment.commentListByDynamicId)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.pageSize, pageSize)
                .params(HttpHelper.Params.pageNum, pageNum)
                .params(HttpHelper.Params.dynamicId, dynamicComment.getId())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                PageResponse<Comment> pageResponse = new Gson().fromJson(
                                        ResponseUtils.getData(response.body()).toString(),
                                        new TypeToken<PageResponse<Comment>>(){}.getType());

                                if(pageNum == 1)
                                    adapter.clear();

                                adapter.addAll(pageResponse.getList());
                                adapter.notifyDataSetChanged();

                                tvCount.setText(String.format(getResources().getString(R.string.str_dynamic_comment_count), pageResponse.getTotal()));

                                pageCount = pageResponse.getTotalPage();
                                pageNum++;
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
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);

                        if(adapter.getItemCount() == 0){
                            showEmpty(emptyPage, R.string.text_empty_dynamic_comment, true);
                        }else{
                            showEmpty(emptyPage, false);
                        }
                    }
                });
    }

    @SuppressLint("StringFormatMatches")
    @OnClick({R.id.iv_back, R.id.btn_delete, R.id.ll_all, R.id.tv_manager})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.ll_all:{
                all = !all;
                if(all)
                    adapter.setAll();
                else
                    adapter.cancleAll();

                tvAll.setText(all ? R.string.btn_cancle_all : R.string.btn_all);
                loadMoreWrapper.notifyDataSetChanged();
                break;
            }
            case R.id.tv_manager:{
                resetManager();
                break;
            }
            case R.id.btn_delete:{
                if(adapter.getSelect().isEmpty()){
                    showMessage(R.string.error_delete_comment);
                    return;
                }

                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage(String.format(getResources().getString(R.string.dialog_message_delete_comment), adapter.getSelect().size()))
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int witch) {
                                dialogInterface.dismiss();

                                try{
                                    int[] ids = new int[adapter.getSelect().size()];
                                    for(int i=0; i<adapter.getSelect().size(); i++){
                                        Comment comment = adapter.getSelect().get(i);
                                        ids[i] = comment.getId();
                                    }

                                    OkGo.<JSONObject>post(HttpHelper.Url.Dynamic.Comment.deleteComment)
                                            .tag(this)
                                            .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                            .upJson(new JSONArray(ids))
                                            .execute(new DialogCallback<JSONObject>(DynamicCommentActivity.this, false) {
                                                @Override
                                                public void onFinish() {
                                                    super.onFinish();
                                                }

                                                @Override
                                                public void onSuccess(Response<JSONObject> response) {
                                                    super.onSuccess(response);
                                                    try {
                                                        if(ResponseUtils.ok(response.body())){
                                                            for(Comment comment : adapter.getSelect()){
                                                                int position = adapter.getItemPosition(comment);
                                                                loadMoreWrapper.notifyItemRemoved(position);
                                                            }

                                                            adapter.removeAll(adapter.getSelect());
                                                            adapter.cancleAll();

                                                            resetManager();
                                                        }else{
                                                            showMessage(ResponseUtils.getMsg(response.body()));
                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                }catch (Exception e){
                                    e.printStackTrace();
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
                break;
            }
        }
    }

    private void resetManager(){
        manager = !manager;
        tvManger.setText(manager ? R.string.btn_cancle : R.string.btn_manager);
        rlButton.setVisibility(manager ? View.VISIBLE : View.GONE);
        adapter.setManager(manager);
        loadMoreWrapper.notifyDataSetChanged();

        all = false;
        tvAll.setText(R.string.btn_all);
    }

    /**
     * Take care of popping the fragment back stack or finishing the activity
     * as appropriate.
     */
    @Override
    public void onBackPressed() {
        if(rlButton.getVisibility() == View.VISIBLE){
            resetManager();
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
