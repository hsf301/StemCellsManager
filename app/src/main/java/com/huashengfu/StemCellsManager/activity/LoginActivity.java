package com.huashengfu.StemCellsManager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.huashengfu.StemCellsManager.BuildConfig;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.settings.user.FindPasswordActivity;
import com.huashengfu.StemCellsManager.activity.settings.user.RegisterActivity;
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

public class LoginActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_tips)
    TextView tvTips;
    @BindView(R.id.iv_eye)
    ImageView ivEye;
    @BindView(R.id.btn_login)
    Button btnLogin;

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
            if(etName.getText().length() > 0 && etPwd.getText().length() > 0)
                btnLogin.setEnabled(true);
            else
                btnLogin.setEnabled(false);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        unbinder = ButterKnife.bind(this);

        etPwd.addTextChangedListener(textWatcher);
        etName.addTextChangedListener(textWatcher);

        User user = Constants.getLastLoginUser(this);
        if(user != null){
            Log.i(Constants.Log.Log, "user is " + user.getUsername());
            etName.setText(user.getUsername());
            etPwd.setText(user.getPassword());
            btnLogin.setEnabled(true);

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }else{
            Log.i(Constants.Log.Log, "user is null");
            if(BuildConfig.test){
                etName.setText(BuildConfig.phone);
                etPwd.setText(BuildConfig.password);
            }
        }

    }

    private boolean hidden = true;
    @OnClick({R.id.btn_login, R.id.iv_eye, R.id.btn_findpwd, R.id.btn_register})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.iv_eye:{
                if(hidden){
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivEye.setImageResource(R.mipmap.icon_eye);
                }else{
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivEye.setImageResource(R.mipmap.icon_eye_close);
                }

                hidden = !hidden;
                break;
            }
            case R.id.btn_findpwd:{
                if(etName.getText().length() <= 0){
                    showMessage(etName.getHint());
                    return;
                }

                Intent intent = new Intent(this, FindPasswordActivity.class);
                intent.putExtra(Constants.Tag.data, etName.getText().toString());
                startActivity(intent);
                break;
            }
            case R.id.btn_register:{
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.btn_login:{
                tvTips.setVisibility(View.GONE);
                btnLogin.setClickable(false);

                JSONObject obj = new JSONObject();
                try {
                    obj.put(HttpHelper.Params.username, etName.getText().toString());
                    obj.put(HttpHelper.Params.password, etPwd.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                OkGo.<JSONObject>post(HttpHelper.Url.Admin.login)
                        .tag(this)
                        .upJson(obj)
                        .execute(new DialogCallback<JSONObject>(this, false) {
                            @Override
                            public void onFinish() {
                                super.onFinish();
                                btnLogin.setClickable(true);
                            }

                            @Override
                            public void onSuccess(Response<JSONObject> response) {
                                super.onSuccess(response);
                                try {
                                    if(ResponseUtils.ok(response.body())){
                                        String token = ResponseUtils.getData(response.body()).getString(Constants.Tag.token);
                                        String tokenHead = ResponseUtils.getData(response.body()).getString(HttpHelper.Params.tokenHead);

                                        User user = new User();
                                        user.setToken(tokenHead + token);
                                        user.setPassword(etPwd.getText().toString());
                                        user.setUsername(etName.getText().toString());

                                        DbHandler dbHandler = DbHandler.getInstance(getApplicationContext());
                                        if(dbHandler.hasValue(User.Table, User.Username + "=?", new String[]{user.getUsername()})){
                                            dbHandler.update(user);
                                        }else {
                                            dbHandler.save(User.Table, user);
                                        }

                                        SPUtils.getInstance().put(Constants.Tag.token, user.getToken());
                                        SPUtils.getInstance().put(Constants.Tag.lastLoginUser, user.getUsername());

                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }else{
                                        String msg = ResponseUtils.getMsg(response.body());
                                        tvTips.setText(msg);
                                        tvTips.setVisibility(View.VISIBLE);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });



//                OkGo.<JSONObject>post(HttpHelper.Url.Admin.login)
//                        .tag(this)
//                        .upJson(obj)
//                        .converter(new JsonConvert())
//                        .adapt(new ObservableResponse<JSONObject>())
//                        .subscribeOn(Schedulers.io())
//                        .flatMap(new Function<Response<JSONObject>, ObservableSource<Response<JSONObject>>>() {
//
//                            @Override
//                            public ObservableSource<Response<JSONObject>> apply(Response<JSONObject> jsonObjectResponse) throws Exception {
//
//                                if(ResponseUtils.ok(jsonObjectResponse.body())){
//                                    String token = ResponseUtils.getData(jsonObjectResponse.body()).getString(Constants.Tag.token);
//                                    String tokenHead = ResponseUtils.getData(jsonObjectResponse.body()).getString(HttpHelper.Params.tokenHead);
//
//                                    User user = new User();
//                                    user.setToken(tokenHead + token);
//                                    user.setPassword(etPwd.getText().toString());
//                                    user.setUsername(etName.getText().toString());
//
//                                    DbHandler dbHandler = DbHandler.getInstance(getApplicationContext());
//                                    if(dbHandler.hasValue(User.Table, User.Username + "=?", new String[]{user.getUsername()})){
//                                        dbHandler.updateQuestionStatus(user);
//                                    }else {
//                                        dbHandler.save(User.Table, user);
//                                    }
//
//                                    SPUtils.getInstance().put(Constants.Tag.token, user.getToken());
//                                    SPUtils.getInstance().put(Constants.Tag.lastLoginUser, user.getUsername());
//
////                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
////                                        startActivity(intent);
////                                        finish();
//                                }else{
//                                    throw new Exception(ResponseUtils.getMsg(jsonObjectResponse.body()));
//                                }
//
//                                // 获取用户信息
//                                JSONObject obj = new JSONObject();
//                                obj.put(HttpHelper.Params.token, SPUtils.getInstance().getString(Constants.Tag.token));
//                                Observable<Response<JSONObject>> observable = OkGo.<JSONObject>get(HttpHelper.Url.Admin.info)
//                                        .tag(this)
//                                        .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
//                                        .converter(new JsonConvert())
//                                        .adapt(new ObservableResponse<JSONObject>());
//
//                                Log.i(Constants.Log.Log, SPUtils.getInstance().getString(Constants.Tag.token));
//                                return observable;
//                            }
//                        }).subscribe(new Observer<Response<JSONObject>>() {
//                            @Override
//                            public void onSubscribe(Disposable d) {
//
//                            }
//
//                            @Override
//                            public void onNext(Response<JSONObject> jsonObjectResponse) {
//                                try {
//                                    if(ResponseUtils.ok(jsonObjectResponse.body())){
//
//                                    }else{
////                                        showMessage(ResponseUtils.getMsg(jsonObjectResponse.body()));
//
//                                        String msg = ResponseUtils.getMsg(jsonObjectResponse.body());
//                                        tvTips.setText(msg);
//                                        tvTips.setVisibility(View.VISIBLE);
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//
//                            @Override
//                            public void onError(Throwable e) {
//                                e.printStackTrace();
//
//                                tvTips.setText(e.getMessage());
//                                tvTips.setVisibility(View.VISIBLE);
//
//                                dismissDialog();
//                            }
//
//                            @Override
//                            public void onComplete() {
//                                dismissDialog();
//                            }
//                        });
            }
        }
    }
}
