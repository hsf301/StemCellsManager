package com.huashengfu.StemCellsManager.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.MessageHandler;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.LoginActivity;


public class BaseFragment extends Fragment {

    public MessageHandler handler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new MessageHandler(getContext());
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

    public void showEmpty(View empty, int emptyResId, boolean show){
        empty.setVisibility(show ? View.VISIBLE : View.GONE);
        TextView tvEmpty = empty.findViewById(R.id.tv_empty);
        if(tvEmpty != null)
            tvEmpty.setText(emptyResId);
    }

    public void showEmpty(View empty, boolean show){
        showEmpty(empty, R.string.text_empty, show);
    }

    protected void doLogin(){
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
