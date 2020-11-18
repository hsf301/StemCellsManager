package com.huashengfu.StemCellsManager.fragment.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.bumptech.glide.Glide;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.activity.ModifyActivityBannerActivity;
import com.huashengfu.StemCellsManager.activity.activity.ModifyActivityInformactionActivity;
import com.huashengfu.StemCellsManager.activity.activity.PreviewActivityActivity;
import com.huashengfu.StemCellsManager.activity.activity.PublishActivityDetailActivity;
import com.huashengfu.StemCellsManager.activity.interaction.InteractionActivity;
import com.huashengfu.StemCellsManager.adapter.interaction.activity.ActivityViewHolder;
import com.huashengfu.StemCellsManager.entity.activity.Activity;
import com.huashengfu.StemCellsManager.fragment.BaseFragment;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.utils.GlideRoundTransformation;
import com.huashengfu.StemCellsManager.utils.StringUtils;
import com.huashengfu.StemCellsManager.utils.ViewUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityFragment extends BaseFragment {

    private ActivityPopupwindow activityPopupwindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        activityPopupwindow = new ActivityPopupwindow();
        activityPopupwindow.init();
    }

    public void showActivity(Activity activity){
        activityPopupwindow.show(activity);
    }

    public void previewActivity(Activity activity){
        Intent intent = new Intent(getContext(), PreviewActivityActivity.class);
        intent.putExtra(Constants.Tag.data, activity);
        startActivity(intent);
    }

    public void updateBanner(RecyclerView recyclerView, int position, String banner){
        int first = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int last = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        if(position >= first && position <= last){
            Log.i(Constants.Log.Log, " " + recyclerView.getAdapter().toString() + " --> " + recyclerView.getAdapter().hashCode());

            View view = recyclerView.getChildAt(position - first);
            if(recyclerView.getChildViewHolder(view) instanceof ActivityViewHolder){
                ActivityViewHolder holder = (ActivityViewHolder) recyclerView.getChildViewHolder(view);

                Glide.with(this)
                        .load(banner)
                        .placeholder(R.drawable.image_loading_pic)
                        .transform(new GlideRoundTransformation(getContext(), 5))
                        .into(holder.ivImage);
            }
        }
    }

    public void update(RecyclerView recyclerView, int position, Activity activity){
        int first = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int last = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        if(position >= first && position <= last){
            View view = recyclerView.getChildAt(position - first);
            if(recyclerView.getChildViewHolder(view) instanceof ActivityViewHolder){
                Log.i(Constants.Log.Log, " " + recyclerView.getAdapter().toString() + " --> " + recyclerView.getAdapter().hashCode());

                ActivityViewHolder holder = (ActivityViewHolder) recyclerView.getChildViewHolder(view);

                holder.tvTitle.setText(activity.getTitle());
                holder.tvContent.setText(activity.getSubTitle());
                holder.tvAddress.setText(activity.getAddr());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                holder.tvDate.setText(sdf.format(new Date(activity.getStartDate())) + " " + StringUtils.getWeek(activity.getStartDate()));
            }
        }
    }

    public void remove(RecyclerView recyclerView, int position){
        int first = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        int last = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        if(position >= first && position <= last){
            View view = recyclerView.getChildAt(position - first);
            recyclerView.removeView(view);
        }
    }

    /**
     * Called when the fragment is notStarted longer in use.  This is called
     * after {@link #onStop()} and before {@link #onDetach()}.
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);
    }

    private OnActivityPopupwindowListener onActivityPopupwindowListener;

    public OnActivityPopupwindowListener getOnActivityPopupwindowListener() {
        return onActivityPopupwindowListener;
    }

    public void setOnActivityPopupwindowListener(OnActivityPopupwindowListener onActivityPopupwindowListener) {
        this.onActivityPopupwindowListener = onActivityPopupwindowListener;
    }

    public interface OnActivityPopupwindowListener {
        public void onDelete(Activity activity);
    }

    public void showDialog(Activity activity){
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setMessage(activity.getActivityStatus() == Constants.Status.Activity.notStarted ?
                        R.string.dialog_message_start_activity : R.string.dialog_message_stop_activity)
                .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        int status = 0;
                        if(activity.getActivityStatus() == Constants.Status.Activity.notStarted)
                            status = Constants.Status.Activity.progress;
                        else if(activity.getActivityStatus() == Constants.Status.Activity.progress)
                            status = Constants.Status.Activity.finished;

                        OkGo.<JSONObject>put(HttpHelper.Url.Activity.modifyStatus + activity.getId() +
                                    "?" + HttpHelper.Params.status + "=" + status)
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
                                                if(onActivityPopupwindowListener != null){
                                                    onActivityPopupwindowListener.onDelete(activity);
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

    public class ActivityPopupwindow implements View.OnClickListener {
        private PopupWindow popupWindow;
        private Activity activity;

        private TextView tvTitle;
        private ImageView ivImage;
        private TextView tvView;
        private Button btnOffline;

        @BindView(R.id.ll_banner)
        LinearLayout llBanner;
        @BindView(R.id.ll_activity)
        LinearLayout llActivity;
        @BindView(R.id.ll_detail)
        LinearLayout llDetail;
        @BindView(R.id.ll_enrollment)
        LinearLayout llEnrollment;

        private void init() {
            View view = getLayoutInflater().inflate(R.layout.popupwindow_activity_menu, null);
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

            tvTitle = view.findViewById(R.id.tv_title);
            ivImage = view.findViewById(R.id.iv_image);
            tvView = view.findViewById(R.id.tv_view);
            btnOffline = view.findViewById(R.id.btn_offline);

            ButterKnife.bind(this, view);

            view.findViewById(R.id.iv_close).setOnClickListener(this);
            view.findViewById(R.id.btn_offline).setOnClickListener(this);
            view.findViewById(R.id.ll_banner).setOnClickListener(this);
            view.findViewById(R.id.ll_activity).setOnClickListener(this);
            view.findViewById(R.id.ll_detail).setOnClickListener(this);
            view.findViewById(R.id.ll_enrollment).setOnClickListener(this);
        }

        public boolean isShowing(){
            return popupWindow.isShowing();
        }

        public void dismiss(){
            popupWindow.dismiss();
        }

        public void show(Activity activity){
            this.activity = activity;

            tvTitle.setText(activity.getTitle());
            tvView.setText(String.valueOf(activity.getViews()));

            if(activity.getActivityStatus() == Constants.Status.Activity.finished){
                btnOffline.setVisibility(View.GONE);
            }else{
                btnOffline.setVisibility(View.VISIBLE);

                if(activity.getActivityStatus() == Constants.Status.Activity.notStarted)
                    btnOffline.setText(R.string.btn_activity_starting);
                else if(activity.getActivityStatus() == Constants.Status.Activity.progress)
                    btnOffline.setText(R.string.btn_activity_stop);
            }

            if(activity.getActivityStatus() == Constants.Status.Activity.progress){
                llActivity.setVisibility(View.GONE);
                llBanner.setVisibility(View.GONE);
                llDetail.setVisibility(View.GONE);
                llEnrollment.setVisibility(View.GONE);
            }else{
                llActivity.setVisibility(View.VISIBLE);
                llBanner.setVisibility(View.VISIBLE);
                llDetail.setVisibility(View.VISIBLE);
                llEnrollment.setVisibility(View.VISIBLE);
            }

            Glide.with(getActivity())
                    .load(activity.getBanner())
                    .placeholder(R.drawable.image_loading_pic)
                    .transform(new GlideRoundTransformation(getActivity(), 5))
                    .into(ivImage);

            ViewUtils.background(getActivity(), 0.8f);
            popupWindow.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.BOTTOM, 0, 0);
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.ll_enrollment){
                Intent intent = new Intent(getActivity(), InteractionActivity.class);
                intent.putExtra(Constants.Tag.data, Constants.Type.Interaction.hdbm);
                startActivity(intent);
            }else{
                Intent intent = null;
                switch (view.getId()){
                    case R.id.iv_close:{
                        dismiss();
                        break;
                    }
                    case R.id.btn_offline:{
                        showDialog(activity);
                        dismiss();
                        break;
                    }
                    case R.id.ll_banner:{
                        intent = new Intent(getActivity(), ModifyActivityBannerActivity.class);
                        break;
                    }
                    case R.id.ll_activity:{
                        intent = new Intent(getActivity(), ModifyActivityInformactionActivity.class);
                        break;
                    }
                    case R.id.ll_detail:{
                        intent = new Intent(getActivity(), PublishActivityDetailActivity.class);
                        intent.putExtra(Constants.Tag.modify, true);
                        break;
                    }
                }

                if(intent != null) {
                    intent.putExtra(Constants.Tag.data, activity.getId());
                    startActivity(intent);

                }
            }

            dismiss();
        }
    }
}
