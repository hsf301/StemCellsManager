package com.huashengfu.StemCellsManager.http.callback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;

import com.blankj.utilcode.util.SPUtils;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.lzy.okgo.request.base.Request;

public abstract class DialogCallback<T> extends JsonCallback<T> {

    private ProgressDialog dialog;

    private void initDialog(Activity activity, boolean cancelable) {
        dialog = new ProgressDialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(cancelable);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(activity.getResources().getString(R.string.dialog_message_wait));
    }

    public DialogCallback(Activity activity, boolean cancelable) {
        super();
        initDialog(activity, cancelable);
    }

    public DialogCallback(Activity activity, boolean cancelable, boolean closeAll) {
        super(closeAll);
        initDialog(activity, cancelable);
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        request.headers("session", SPUtils.getInstance().getString(Constants.Tag.sessionid));
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onFinish() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
