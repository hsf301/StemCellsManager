package com.huashengfu.StemCellsManager.fragment.dynamic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.photo.PreviewPhotoActivity;
import com.huashengfu.StemCellsManager.activity.video.VideoPlayActivity;
import com.huashengfu.StemCellsManager.adapter.BaseAdapter;
import com.huashengfu.StemCellsManager.adapter.dynamic.DynamicDetailAdapter;
import com.huashengfu.StemCellsManager.db.DbHandler;
import com.huashengfu.StemCellsManager.entity.User;
import com.huashengfu.StemCellsManager.entity.dynamic.Detail;
import com.huashengfu.StemCellsManager.entity.dynamic.Dynamic;
import com.huashengfu.StemCellsManager.entity.dynamic.DynamicDetail;
import com.huashengfu.StemCellsManager.event.dynamic.DeleteDynamicEvent;
import com.huashengfu.StemCellsManager.fragment.BaseFragment;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

public class DynamicFragment extends BaseFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    /**
     * Called when the fragment is not_started longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    private OnDetailPopupwindowListener onDetailPopupwindowListener;

    public OnDetailPopupwindowListener getOnDetailPopupwindowListener() {
        return onDetailPopupwindowListener;
    }

    public void setOnDetailPopupwindowListener(OnDetailPopupwindowListener onDetailPopupwindowListener) {
        this.onDetailPopupwindowListener = onDetailPopupwindowListener;
    }

    public interface OnDetailPopupwindowListener {
        public void onDelete(Dynamic dynamic);
    }

    public class DetailPopupwindow{
        private PopupWindow popupWindow;
        private Dynamic dynamic;
        private RecyclerView rvList;

        private DynamicDetailAdapter dynamicDetailAdapter;

        private ShareOnClickListener shareOnClickListener = new ShareOnClickListener();
        private boolean video = false;

        public void init() {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_dynamic_detail, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
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

            view.findViewById(R.id.btn_cancle).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });

            view.findViewById(R.id.btn_delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog dialog = new AlertDialog.Builder(getActivity())
                            .setMessage(R.string.dialog_message_delete_dynamic_detail)
                            .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    dismiss();

                                    OkGo.<JSONObject>delete(HttpHelper.Url.Dynamic.del + dynamic.getId())
                                            .tag(this)
                                            .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                                            .execute(new DialogCallback<JSONObject>(getActivity(), false) {
                                                @Override
                                                public void onFinish() {
                                                    super.onFinish();
                                                }

                                                @Override
                                                public void onSuccess(Response<JSONObject> response) {
                                                    super.onSuccess(response);
                                                    try {
                                                        if(ResponseUtils.ok(response.body())){
                                                            EventBus.getDefault().post(new DeleteDynamicEvent().setDynamic(dynamic));

                                                            if(getOnDetailPopupwindowListener() != null){
                                                                getOnDetailPopupwindowListener().onDelete(dynamic);
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
            });

            view.findViewById(R.id.iv_weixin).setOnClickListener(shareOnClickListener);
            view.findViewById(R.id.iv_pengyouquan).setOnClickListener(shareOnClickListener);
            view.findViewById(R.id.iv_qq).setOnClickListener(shareOnClickListener);
            view.findViewById(R.id.iv_weibo).setOnClickListener(shareOnClickListener);
            view.findViewById(R.id.iv_qqzone).setOnClickListener(shareOnClickListener);

            rvList = view.findViewById(R.id.rv_list);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            rvList.setLayoutManager(layoutManager);

            dynamicDetailAdapter = new DynamicDetailAdapter();
            dynamicDetailAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener<DynamicDetail>() {
                @Override
                public void onItemClick(View view, DynamicDetail dynamicDetail) {
                    switch (view.getId()){
                        case R.id.iv_play:{
                            Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                            intent.putExtra(Constants.Tag.data, dynamicDetail.getVideoUrl());
                            startActivity(intent);
                            break;
                        }
                        case R.id.iv_image:{
                            Intent intent = new Intent(getContext(), PreviewPhotoActivity.class);
                            intent.putExtra(Constants.Tag.data, dynamicDetail.getUrl());
                            startActivity(intent);
                            break;
                        }
                    }
                }
            });
            rvList.setAdapter(dynamicDetailAdapter);
        }

        public boolean isShowing(){
            return popupWindow.isShowing();
        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public void show(Dynamic dynamic, boolean video){
            this.dynamic = dynamic;
            this.video = video;

            dynamicDetailAdapter.clear();
            dynamicDetailAdapter.notifyDataSetChanged();

            ViewUtils.background(getActivity(), 0.8f);
            popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);

            rvList.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    rvList.smoothScrollToPosition(0);
                    DynamicDetail detail = new DynamicDetail();
                    detail.setContent(dynamic.getSubTitle());
                    dynamicDetailAdapter.addContent(detail);

                    if(dynamic.getDetailsList() != null && !dynamic.getDetailsList().isEmpty()){
                        if(video){
                            DynamicDetail dynamicDetail = new DynamicDetail();
                            for(Detail tmp : dynamic.getDetailsList()){
                                if(tmp.getType().equals(Constants.Type.Detail.video)){
                                    dynamicDetail.setVideoUrl(tmp.getThumbnail());
                                }else if(tmp.getType().equals(Constants.Type.Detail.pic)){
                                    dynamicDetail.setUrl(tmp.getThumbnail());
                                }
                                dynamicDetail.setUrl(tmp.getThumbnail());
                            }

                            dynamicDetail.setVideo(true);
                            dynamicDetail.setImage(false);
                            dynamicDetail.setArticle(false);
                            dynamicDetailAdapter.add(dynamicDetail);
                        }else{
                            for(Detail tmp : dynamic.getDetailsList()){
                                DynamicDetail dynamicDetail = new DynamicDetail();
                                dynamicDetail.setUrl(tmp.getThumbnail());
                                dynamicDetail.setVideo(tmp.getType().equals(Constants.Type.Detail.video));
                                dynamicDetail.setImage(tmp.getType().equals(Constants.Type.Detail.pic));
                                dynamicDetailAdapter.add(dynamicDetail);
                            }
                        }
                    }

                    dynamicDetailAdapter.notifyDataSetChanged();
                }
            }, 200);
        }

        public void show(Dynamic dynamic){
            show(dynamic, false);
        }

        private Platform.ShareParams buildShareParams(){
            Platform.ShareParams params = new Platform.ShareParams();
            if(dynamicDetailAdapter.getDynamicDetails().isEmpty())
                params.setShareType(Platform.SHARE_TEXT);
            else{
                params.setShareType(Platform.SHARE_IMAGE);

                for(DynamicDetail detail : dynamicDetailAdapter.getDynamicDetails()){
                    if(detail.isImage()){
                        params.setImageUrl(detail.getUrl());
                        break;
                    }else if(detail.isVideo()){
                        params.setImageUrl(detail.getUrl());
                        break;
                    }
                }
            }

            params.setTitle(dynamic.getSubTitle());
//            params.setText(dynamic.getSubTitle() + " " + HttpHelper.Url.shared + dynamic.getId());
//            params.setComment(dynamic.getSubTitle());
            if(StringUtils.isNullOrBlank(dynamic.getSubTitle())){
                User user = Constants.getLastLoginUser(getContext());
                if(StringUtils.isNullOrBlank(user.getStorename()))
                    params.setText(getResources().getString(R.string.app_name));
                else
                    params.setText(user.getStorename());
            } else
                params.setText(dynamic.getSubTitle());

            params.setUrl(HttpHelper.Url.shared + dynamic.getId());

            params.setShareType(Platform.SHARE_WEBPAGE);

            return params;
        }

        // 分享事件
        private class ShareOnClickListener implements View.OnClickListener {

            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.iv_weixin:{
                        Platform platform = ShareSDK.getPlatform(Wechat.NAME);
                        platform.setPlatformActionListener(new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                Log.i(Constants.Log.Log, "onComplete --> ");
                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {
                                Log.i(Constants.Log.Log, "onError --> " + throwable);
                            }

                            @Override
                            public void onCancel(Platform platform, int i) {
                                Log.i(Constants.Log.Log, "onCancel --> ");
                            }
                        });
                        platform.share(buildShareParams());
                        break;
                    }
                    case R.id.iv_pengyouquan:{
                        Platform wechatMoments = ShareSDK.getPlatform(WechatMoments.NAME);
                        wechatMoments.setPlatformActionListener(new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                Log.i(Constants.Log.Log, "onComplete");
                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {
                                Log.i(Constants.Log.Log, "onError -> " + throwable);
                            }

                            @Override
                            public void onCancel(Platform platform, int i) {
                                Log.i(Constants.Log.Log, "onCancel");
                            }
                        });
                        wechatMoments.share(buildShareParams());
                        break;
                    }
                    case R.id.iv_qq:{
                        Platform qq = ShareSDK.getPlatform(QQ.NAME);
                        qq.setPlatformActionListener(new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                Log.i(Constants.Log.Log, "onComplete");
                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {
                                Log.i(Constants.Log.Log, "onError -> " + throwable);
                            }

                            @Override
                            public void onCancel(Platform platform, int i) {
                                Log.i(Constants.Log.Log, "onCancel");
                            }
                        });

                        Platform.ShareParams sp = buildShareParams();
//                        sp.setTitleUrl(HttpHelper.Url.shared + dynamic.getId());

//                        sp.setTitleUrl("http://www.163.com");
//                        sp.setUrl("http://www.163.com");
//                        sp.setImageUrl("http://pic-bucket.ws.126.net/photo/0003/2020-11-06/FQO11OES00AJ0003NOS.jpg");


                        sp.setTitleUrl(HttpHelper.Url.shared + dynamic.getId());
                        sp.setUrl(HttpHelper.Url.shared + dynamic.getId());
//                        sp.setImageUrl("https://huashengfu.cn/upload/images/goods/pic/20200711111305.jpg");

                        qq.share(sp);
                        break;
                    }
                    case R.id.iv_weibo:{
                        Platform sinaWeibo = ShareSDK.getPlatform(SinaWeibo.NAME);
                        sinaWeibo.setPlatformActionListener(new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                Log.i(Constants.Log.Log, "onComplete");
                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {
                                Log.i(Constants.Log.Log, "onError -> " + throwable);
                            }

                            @Override
                            public void onCancel(Platform platform, int i) {
                                Log.i(Constants.Log.Log, "onCancel");
                            }
                        });
                        sinaWeibo.share(buildShareParams());
                        break;
                    }
                    case R.id.iv_qqzone:{
                        Platform qZone = ShareSDK.getPlatform(QZone.NAME);
                        qZone.setPlatformActionListener(new PlatformActionListener() {
                            @Override
                            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                                Log.i(Constants.Log.Log, "onComplete");
                            }

                            @Override
                            public void onError(Platform platform, int i, Throwable throwable) {
                                Log.i(Constants.Log.Log, "onError -> " + throwable);
                            }

                            @Override
                            public void onCancel(Platform platform, int i) {
                                Log.i(Constants.Log.Log, "onCancel");
                            }
                        });

                        Platform.ShareParams sp = buildShareParams();
                        sp.setTitleUrl(HttpHelper.Url.shared + dynamic.getId());
                        sp.setSite(getResources().getString(R.string.app_name));
                        sp.setSiteUrl(HttpHelper.Url.shared);
                        sp.setText(dynamic.getSubTitle() + " " + HttpHelper.Url.shared + dynamic.getId());
                        qZone.share(sp);
                        break;
                    }
                }
            }
        }
    }
}
