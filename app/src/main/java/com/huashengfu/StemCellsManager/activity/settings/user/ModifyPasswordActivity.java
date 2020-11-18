package com.huashengfu.StemCellsManager.activity.settings.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.db.DbHandler;
import com.huashengfu.StemCellsManager.entity.User;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

//修改密码
public class ModifyPasswordActivity extends BaseActivity {

    @BindView(R.id.et_oldpwd)
    EditText etOldpwd;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_repwd)
    EditText etRepwd;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    @BindView(R.id.tv_tips)
    TextView tvTips;

    private Unbinder unbinder;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(etOldpwd.getText().length() > 0 && etPwd.getText().length() > 0
                    && etRepwd.getText().length() > 0)
                btnConfirm.setEnabled(true);
            else
                btnConfirm.setEnabled(false);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifypassword);

        unbinder = ButterKnife.bind(this);

        etOldpwd.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);
        etRepwd.addTextChangedListener(textWatcher);
    }

    @OnClick({R.id.iv_back, R.id.btn_confirm})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_back:{
                finish();
                break;
            }
            case R.id.btn_confirm:{
                if(!etPwd.getText().toString().equals(etRepwd.getText().toString())){
                    tvTips.setText(R.string.error_register_pwd_eq_repwd);
                    tvTips.setVisibility(View.VISIBLE);
                    return;
                }

                tvTips.setVisibility(View.GONE);
                btnConfirm.setClickable(false);

                try {
                    JSONObject obj = new JSONObject();
                    obj.put(HttpHelper.Params.oldPassword, etOldpwd.getText().toString());
                    obj.put(HttpHelper.Params.newPassword, etPwd.getText().toString());

                    User user = Constants.getLastLoginUser(this);
                    obj.put(HttpHelper.Params.username, user.getUsername());

                    OkGo.<JSONObject>put(HttpHelper.Url.Admin.updatePassword)
                            .tag(this)
                            .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                            .upJson(obj)
                            .execute(new DialogCallback<JSONObject>(this, false) {
                                @Override
                                public void onSuccess(Response<JSONObject> response) {
                                    super.onSuccess(response);
                                    try {
                                        if(ResponseUtils.ok(response.body())){
                                            User user = Constants.getLastLoginUser(getApplicationContext());
                                            user.setPassword(etPwd.getText().toString());

                                            DbHandler dbHandler = DbHandler.getInstance(getApplicationContext());
                                            dbHandler.update(user);

                                            showMessage(R.string.success_modify_password);
                                            finish();
                                        }else{
                                            tvTips.setText(ResponseUtils.getMsg(response.body()));
                                            tvTips.setVisibility(View.VISIBLE);
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
                                    btnConfirm.setClickable(true);
                                }
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
