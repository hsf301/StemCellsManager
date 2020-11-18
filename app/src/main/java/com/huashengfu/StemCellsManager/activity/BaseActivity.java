package com.huashengfu.StemCellsManager.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.MessageHandler;
import com.huashengfu.StemCellsManager.R;
import com.lzy.okgo.OkGo;

import java.io.File;

public class BaseActivity extends AppCompatActivity {

    private ProgressDialog dialog;

    public MessageHandler handler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        handler = new MessageHandler(this);

        File path = new File(Constants.Path.imgPath);
        if(!path.exists())
            path.mkdirs();
    }

    public void showDialog(boolean cancelable){
        if(dialog != null && dialog.isShowing())
            return;

        dialog = new ProgressDialog(this);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(cancelable);
        dialog.setMessage(getResources().getString(R.string.dialog_message_wait));
        dialog.show();
    }

    public void showMessage(int id){
        try{
            handler.showMessage(id);
        }catch(Exception e){}
    }

    public void showMessage(String msg){
        try{
            handler.showMessage(msg);
        }catch(Exception e){}
    }

    public void showMessage(CharSequence charSequence){
        try{
            handler.showMessage(charSequence.toString());
        }catch(Exception e){}
    }

    public void dismissDialog(){
        if(dialog != null){
            dialog.dismiss();
            dialog = null;
        }
    }

    public void showEmpty(View empty, int emptyResId, boolean show){
        empty.setVisibility(show ? View.VISIBLE : View.GONE);
        TextView tvEmpty = empty.findViewById(R.id.tv_empty);
        if(tvEmpty != null)
            tvEmpty.setText(emptyResId);
    }

    public void showEmpty(View empty, boolean show){
        showEmpty(empty, R.string.text_empty, show);
    }

    public void showNoSaveDialog(DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancle){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_message_no_save)
                .setPositiveButton(R.string.btn_save_quit, ok)
                .setNegativeButton(R.string.btn_discard_editing, cancle)
                .create();
        dialog.show();
    }

    public void showNoSaveDialog(int msgId, int okId, int cancleId,
                                 DialogInterface.OnClickListener ok, DialogInterface.OnClickListener cancle){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(msgId)
                .setPositiveButton(okId, ok)
                .setNegativeButton(cancleId, cancle)
                .create();
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    @Override
    protected void onResume() {
        Constants.isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        Constants.isForeground = false;
        super.onPause();
    }
}
