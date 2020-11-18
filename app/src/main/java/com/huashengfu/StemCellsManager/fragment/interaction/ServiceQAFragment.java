package com.huashengfu.StemCellsManager.fragment.interaction;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.ServiceQAAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.activity.ActivityViewHolder;
import com.huashengfu.StemCellsManager.adapter.interaction.qa.ServiceQuestionAdapter;
import com.huashengfu.StemCellsManager.adapter.interaction.qa.ServiceQuestionViewHolder;
import com.huashengfu.StemCellsManager.entity.interaction.ServiceQA;
import com.huashengfu.StemCellsManager.entity.interaction.ServiceQuestion;
import com.huashengfu.StemCellsManager.entity.response.PageResponse;
import com.huashengfu.StemCellsManager.fragment.BaseFragment;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.loadmore.EndlessRecyclerOnScrollListener;
import com.huashengfu.StemCellsManager.loadmore.LoadMoreWrapper;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//服务问答
public class ServiceQAFragment extends BaseFragment {

    private Unbinder unbinder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mySwipeRefreshLayout;
    @BindView(R.id.empty)
    View emptyPage;

    private int pageNum = 1;
    private int pageSize = 10;
    private int pageCount = 1;

    private ServiceQAAdapter adapter;
    private LoadMoreWrapper loadMoreWrapper;
    private ServiceQuestionPopupwindow serviceQuestionPopupwindow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interaction_service_qa, null);

        unbinder = ButterKnife.bind(this, view);

        serviceQuestionPopupwindow = new ServiceQuestionPopupwindow();
        serviceQuestionPopupwindow.init();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvList.setLayoutManager(layoutManager);

        adapter = new ServiceQAAdapter();
        adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<ServiceQA>() {
            @Override
            public void onItemClick(View view, ServiceQA serviceQA) {
                serviceQuestionPopupwindow.show(serviceQA);
            }
        });

        loadMoreWrapper = new LoadMoreWrapper(adapter);
        rvList.setAdapter(loadMoreWrapper);
        rvList.setLayoutManager(layoutManager);

        rvList.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore() {
                if(pageNum <= pageCount){
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
                    mySwipeRefreshLayout.postDelayed(()->{
                        doQuery();
                    }, 1000);
                }else{
                    loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
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

        return view;
    }

    private void doQuery(){
        OkGo.<JSONObject>get(HttpHelper.Url.Service.Comment.list)
                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                .params(HttpHelper.Params.pageSize, pageSize)
                .params(HttpHelper.Params.pageNum, pageNum)
                .params(HttpHelper.Params.category, Constants.Type.Comment.ServiceQA)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(Response<JSONObject> response) {
                        super.onSuccess(response);
                        try {
                            if(ResponseUtils.ok(response.body())){
                                JSONObject data = ResponseUtils.getData(response.body());
                                if(data.has(Constants.Tag.servicesQA)){
                                    JSONObject consult = data.getJSONObject(Constants.Tag.servicesQA);
                                    PageResponse<ServiceQA> pageResponse = new Gson().fromJson(
                                            consult.toString(),
                                            new TypeToken<PageResponse<ServiceQA>>(){}.getType());

                                    if(pageNum == 1)
                                        adapter.clear();

                                    adapter.addAll(pageResponse.getList());
                                    adapter.notifyDataSetChanged();

                                    pageCount = pageResponse.getTotalPage();
                                    pageNum++;
                                }
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
                            showEmpty(emptyPage, R.string.text_empty_service_qa, true);
                        }else{
                            showEmpty(emptyPage, false);
                        }
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private class ServiceQuestionPopupwindow{
        private PopupWindow popupWindow;
        private RecyclerView recyclerView;
        private ServiceQuestionAdapter adapter;
        private SwipeRefreshLayout swipeRefreshLayout;
        private View emptyView;

        private int pageNum = 1;
        private int pageSize = 10;
        private int pageCount = 1;

        private LoadMoreWrapper loadMoreWrapper;

        private ImageView ivImage;
        private TextView tvTitle;
        private TextView tvContent;
        private TextView tvCount;
        private TextView tvTxtCount;

        private LinearLayout llSend;
        private Button btnSend;
        private EditText etContent;

        private ServiceQA serviceQA;

        private void init() {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_interaction_service_qa, null);

            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT){
                @Override
                public void dismiss() {
                    if(!canBack())
                        return;
                    super.dismiss();
                }
            };
            popupWindow.setBackgroundDrawable(new BitmapDrawable());
            popupWindow.getBackground().setAlpha(170);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setFocusable(true);
            popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                @Override
                public void onDismiss() {
                    ViewUtils.background(getActivity(), 1f);
                }
            });

            view.findViewById(R.id.rl_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            ivImage = view.findViewById(R.id.iv_image);
            tvTitle = view.findViewById(R.id.tv_title);
            tvContent = view.findViewById(R.id.tv_content);
            tvCount = view.findViewById(R.id.tv_count);
            tvTxtCount = view.findViewById(R.id.tv_txt_count);
            llSend = view.findViewById(R.id.ll_send);
            btnSend = view.findViewById(R.id.btn_send);
            etContent = view.findViewById(R.id.et_content);
            emptyView = view.findViewById(R.id.empty);

            tvCount.setText(String.format(getResources().getString(R.string.str_qa_count), 0));
            etContent.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if(editable.length() > Constants.replyMaxLength){
                        etContent.setText(editable.toString().substring(0, Constants.replyMaxLength));
                        etContent.setSelection(etContent.length());
                    }

                    tvTxtCount.setText(etContent.length() + "/" + Constants.replyMaxLength);
                }
            });

            tvTxtCount.setText(etContent.length() + "/" + Constants.replyMaxLength);
            etContent.setHint(String.format(getResources().getString(R.string.hint_reply_content), ""));

            llSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    llSend.setVisibility(View.GONE);
                    etContent.setText("");
                    ViewUtils.hideSoftInput(getActivity(), etContent);
                }
            });

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(etContent.getText().length() == 0){
                        showMessage(R.string.error_service_qa_comment);
                        return;
                    }

                    btnSend.setEnabled(false);
                    ViewUtils.hideSoftInput(getActivity(), etContent);

                    try {
                        ServiceQuestion serviceQuestion = (ServiceQuestion) etContent.getTag();
                        JSONObject obj = new JSONObject();
                        obj.put(HttpHelper.Params.content, etContent.getText().toString());
                        obj.put(HttpHelper.Params.uid, serviceQuestion.getUid());
                        obj.put(HttpHelper.Params.tid, serviceQuestion.getId());

                        OkGo.<JSONObject>post(HttpHelper.Url.Service.Comment.add)
                                .tag(this)
                                .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                .upJson(obj)
                                .execute(new JsonCallback<JSONObject>() {
                                    @Override
                                    public void onSuccess(Response<JSONObject> response) {
                                        super.onSuccess(response);
                                        try {
                                            if(ResponseUtils.ok(response.body())){
                                                etContent.setText("");
                                                llSend.setVisibility(View.GONE);
                                                ViewUtils.hideSoftInput(getActivity(), etContent);

                                                serviceQuestion.setStatus(Constants.Status.Topic.yes);
                                                int position = adapter.updateQuestionStatus(serviceQuestion);
                                                adapter.notifyDataSetChanged();



                                                int first = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                                                int last = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                                                if(position >= first && position <= last){
                                                    Log.i(Constants.Log.Log, " " + recyclerView.getAdapter().toString() + " --> " + recyclerView.getAdapter().hashCode());

                                                    View view = recyclerView.getChildAt(position - first);
                                                    if(recyclerView.getChildViewHolder(view) instanceof ServiceQuestionViewHolder){
                                                        ServiceQuestionViewHolder holder = (ServiceQuestionViewHolder) recyclerView.getChildViewHolder(view);

                                                        holder.btnAnswer.setEnabled(false);
                                                        holder.btnAnswer.setText(R.string.str_answer);
                                                        holder.btnAnswer.setBackgroundResource(R.drawable.btn_gray_full);
                                                    }
                                                }
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
                                        btnSend.setEnabled(true);
                                    }
                                });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            recyclerView = view.findViewById(R.id.rv_list);
            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new ServiceQuestionAdapter();
            adapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<ServiceQuestion>() {
                @Override
                public void onItemClick(View view, ServiceQuestion user) {
                    etContent.setTag(user);
                    etContent.setHint(String.format(getResources().getString(R.string.hint_reply_content), user.getUname()));
                    llSend.setVisibility(View.VISIBLE);
                }
            });

            loadMoreWrapper = new LoadMoreWrapper(adapter);
            recyclerView.setAdapter(loadMoreWrapper);
            recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
                @Override
                public void onLoadMore() {
                    if(pageNum <= pageCount){
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING);
                        swipeRefreshLayout.postDelayed(()->{
                            doQuery();
                        }, 1000);
                    }else{
                        loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_END);
                    }
                }
            });

            swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
            swipeRefreshLayout.setColorSchemeColors(
                    getResources().getColor(R.color.colorSwipeRefreshLayout1),
                    getResources().getColor(R.color.colorSwipeRefreshLayout2),
                    getResources().getColor(R.color.colorSwipeRefreshLayout3)
            );
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pageNum=1;
                    doQuery();
                }
            });

            view.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

        }

        private void hideSendLayout(){
            llSend.setVisibility(View.GONE);
            etContent.setText("");
        }

        private boolean canBack(){
            if(llSend.getVisibility() == View.VISIBLE){
                hideSendLayout();
                return false;
            }else{
                return true;
            }
        }

        private void doQuery(){
            OkGo.<JSONObject>get(HttpHelper.Url.Service.Topic.list)
                    .tag(this)
                    .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                    .params(HttpHelper.Params.pageNum, pageNum)
                    .params(HttpHelper.Params.pageSize, pageSize)
                    .params(HttpHelper.Params.sId, serviceQA.getId())
                    .execute(new JsonCallback<JSONObject>() {
                        @Override
                        public void onSuccess(Response<JSONObject> response) {
                            super.onSuccess(response);
                            try {
                                if(ResponseUtils.ok(response.body())){
                                    PageResponse<ServiceQuestion> pageResponse = new Gson().fromJson(ResponseUtils.getData(response.body()).toString(),
                                            new TypeToken<PageResponse<ServiceQuestion>>(){}.getType());

                                    if(pageNum == 1)
                                        adapter.clear();

                                    adapter.addAll(pageResponse.getList());
                                    adapter.notifyDataSetChanged();

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
                            swipeRefreshLayout.setRefreshing(false);
                            loadMoreWrapper.setLoadState(loadMoreWrapper.LOADING_COMPLETE);

                            if(adapter.getItemCount() == 0)
                                showEmpty(emptyView, R.string.text_empty_service_qa_list, true);
                            else
                                showEmpty(emptyView, false);
                        }
                    });

        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public void show(ServiceQA serviceQA){
            this.serviceQA = serviceQA;
            etContent.setTag(null);

            Glide.with(getContext())
                    .load(serviceQA.getCover())
                    .placeholder(R.drawable.image_loading_pic_small)
                    .transform(new GlideRoundTransformation(getContext(), 2))
                    .into(ivImage);

            tvTitle.setText(serviceQA.getName());
            tvContent.setText(serviceQA.getContent());
            tvCount.setText(String.format(getResources().getString(R.string.str_qa_count), serviceQA.getTopicSum()));

            pageNum = 1;
            adapter.clear();
            adapter.notifyDataSetChanged();

            swipeRefreshLayout.post(()->{
                swipeRefreshLayout.setRefreshing(true);
                doQuery();
            });

            ViewUtils.hideSoftInput(getContext(), rvList);
            ViewUtils.background(getActivity(), 0.8f);
            popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }
    }
}
