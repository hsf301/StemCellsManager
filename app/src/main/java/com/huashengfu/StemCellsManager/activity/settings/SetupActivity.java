package com.huashengfu.StemCellsManager.activity.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.activity.LoginActivity;
import com.huashengfu.StemCellsManager.activity.settings.user.ModifyPasswordActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class SetupActivity extends BaseActivity {

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_setup);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.rl_chpwd, R.id.rl_logout, R.id.iv_back})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.rl_chpwd:{
                Intent intent = new Intent(this, ModifyPasswordActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.rl_logout:{
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setMessage(R.string.dialog_message_logout)
                        .setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                SPUtils.getInstance().remove(Constants.Tag.lastLoginUser);
                                SPUtils.getInstance().remove(Constants.Tag.token);

                                ActivityUtils.finishAllActivities();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                ActivityUtils.startActivity(intent);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
