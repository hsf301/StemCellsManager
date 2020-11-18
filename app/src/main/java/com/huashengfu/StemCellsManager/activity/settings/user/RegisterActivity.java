package com.huashengfu.StemCellsManager.activity.settings.user;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.callback.DialogCallback;
import com.huashengfu.StemCellsManager.http.callback.JsonCallback;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

// 用户注册
public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.et_repwd)
    EditText etRepwd;
    @BindView(R.id.et_shop)
    EditText etShop;
    @BindView(R.id.iv_eye)
    ImageView ivEye;
    @BindView(R.id.tv_sendcode)
    TextView tvSendcode;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.cb_protocol)
    CheckBox cbProtocol;

    private final int MaxTime = 10;
    private int time = 0;

    private Runnable timeDown = new Runnable() {
        @Override
        public void run() {
            if(time < MaxTime){
                tvSendcode.setText(String.format(getResources().getString(R.string.str_sned_time), (MaxTime - time)));
                tvSendcode.setTextAppearance(getApplicationContext(), R.style.TextGray12Sp);
                time++;
                tvSendcode.postDelayed(this, 1000);
            }else{
                tvSendcode.setText(R.string.btn_send_code);
                tvSendcode.setTextAppearance(getApplicationContext(), R.style.TextBlue12Sp);
                tvSendcode.setEnabled(true);
            }
        }
    };

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
            if(etPhone.getText().length() > 0
                    &&
                    etCode.getText().length() > 0
                    &&
                    etShop.getText().length() > 0
                    &&
                    etPwd.getText().length() > 0
                    &&
                    etRepwd.getText().length() > 0)
                btnRegister.setEnabled(true);
            else
                btnRegister.setEnabled(false);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        unbinder = ButterKnife.bind(this);

        etPhone.addTextChangedListener(textWatcher);
        etCode.addTextChangedListener(textWatcher);
        etPwd.addTextChangedListener(textWatcher);
        etRepwd.addTextChangedListener(textWatcher);
        etShop.addTextChangedListener(textWatcher);
    }

    private boolean hidden = true;

    @OnClick({R.id.btn_register, R.id.tv_sendcode, R.id.iv_eye, R.id.btn_login, R.id.tv_protocol, R.id.tv_private})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_protocol:{
                Intent intent = new Intent(this, ProtocolActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.tv_private:{
                Intent intent = new Intent(this, PrivacyActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_register:{
                if(!etPwd.getText().toString().equals(etRepwd.getText().toString())){
                    tvTips.setText(R.string.error_register_pwd_eq_repwd);
                    tvTips.setVisibility(View.VISIBLE);
                    return;
                }

                if(!cbProtocol.isChecked()){
                    showMessage(R.string.error_protocol);
                    return;
                }

                tvTips.setVisibility(View.GONE);

                try {
                    JSONObject obj = new JSONObject();
                    obj.put(HttpHelper.Params.phone, etPhone.getText().toString());
                    obj.put(HttpHelper.Params.password, etPwd.getText().toString());
                    obj.put(HttpHelper.Params.storeName, etShop.getText().toString());
                    obj.put(HttpHelper.Params.verification, etCode.getText().toString());

                    OkGo.<JSONObject>post(HttpHelper.Url.Admin.register)
                            .tag(this)
                            .upJson(obj)
                            .execute(new DialogCallback<JSONObject>(this, false) {
                                @Override
                                public void onSuccess(Response<JSONObject> response) {
                                    super.onSuccess(response);
                                    try {
                                        if(ResponseUtils.ok(response.body())){
                                            showMessage(R.string.success_register);
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
                            });
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            }
            case R.id.iv_eye:{
                if(hidden){
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    etRepwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivEye.setImageResource(R.mipmap.icon_eye);
                }else{
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    etRepwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivEye.setImageResource(R.mipmap.icon_eye_close);
                }

                hidden = !hidden;
                break;
            }
            case R.id.tv_sendcode:{
                if(etPhone.getText().length() <= 0){
                    showMessage(etPhone.getHint());
                    return;
                }

                tvSendcode.setEnabled(false);
                time=0;
                tvSendcode.post(timeDown);

                OkGo.<JSONObject>get(HttpHelper.Url.Admin.verification)
                        .tag(this)
                        .params(HttpHelper.Params.phone, etPhone.getText().toString())
                        .execute(new JsonCallback<JSONObject>() {
                            @Override
                            public void onSuccess(Response<JSONObject> response) {
                                super.onSuccess(response);
                                try {
                                    if(!ResponseUtils.ok(response.body())){
                                        showMessage(ResponseUtils.getMsg(response.body()));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(Response<JSONObject> response) {
                                super.onError(response);
                            }
                        });
                break;
            }
            case R.id.btn_login:{
                finish();
                break;
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tvSendcode.removeCallbacks(timeDown);
        unbinder.unbind();
    }
}
