package com.huashengfu.StemCellsManager.http.callback;

import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.Utils;
import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.LoginActivity;
import com.huashengfu.StemCellsManager.db.DbHandler;
import com.huashengfu.StemCellsManager.entity.User;
import com.huashengfu.StemCellsManager.http.HttpHelper;
import com.huashengfu.StemCellsManager.http.utils.ResponseUtils;
import com.huashengfu.StemCellsManager.view.video.UIUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.adapter.CacheCall;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.request.base.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Random;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    private boolean closeAll = true;

    public JsonCallback() {
    }

    public JsonCallback(boolean closeAll){
        this.closeAll = closeAll;
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public void onStart(Request<T, ? extends Request> request) {
        super.onStart(request);
//        String uuid = SPUtils.getInstance().getString(Constant.Key_sessionid);
//        byte[] decode = Base64.decode(uuid, Base64.DEFAULT);
//        uuid = new String(decode);
//        StringBuffer sb = new StringBuffer();
//        sb.append(getRndStr(10));
//        if(!TextUtils.isEmpty(uuid)){
//            for (int i = 0; i < uuid.length(); i++) {
//                sb.append(uuid.charAt(i)).append(getRndStr(2));
//            }
//        }
//        byte[] encode = Base64.encode(sb.toString().getBytes(), Base64.DEFAULT);
//        request.headers("session", new String(encode));
        request.headers("session", SPUtils.getInstance().getString(Constants.Tag.sessionid));
    }

    @Override
    public void onError(com.lzy.okgo.model.Response<T> response) {
        if(response.code() == 500){
            // 返回500错误时，解析返回的数据，判断是否为401，会话超时，如果是，则跳转到登录页面
            try {
                String tmp = new String(response.getRawResponse().body().bytes());
                JSONObject json = new JSONObject(tmp);
                int rejCode = json != null ? json.optInt(Constants.Tag.code) : null;

                if(rejCode == Constants.Code.TOKEN_INVALID){
                    OkGo.<JSONObject>get(HttpHelper.Url.Admin.refreshToken)
                            .headers(HttpHelper.Params.Authorization, SPUtils.getInstance().getString(Constants.Tag.token))
                            .execute(new JsonCallback<JSONObject>() {
                                @Override
                                public void onError(com.lzy.okgo.model.Response<JSONObject> res) {
                                    JsonCallback.super.onError(response);

                                    if(closeAll){
                                        SPUtils.getInstance().remove(Constants.Tag.token);
                                        SPUtils.getInstance().remove(Constants.Tag.lastLoginUser);

                                        ActivityUtils.finishAllActivities();
                                        ActivityUtils.startActivity(LoginActivity.class);
                                        LogUtils.d("======检测会话超时======");
                                    }
                                }

                                @Override
                                public void onSuccess(com.lzy.okgo.model.Response<JSONObject> res) {
                                    try {
                                        // 更新token
                                        String token = ResponseUtils.getData(res.body()).getString(Constants.Tag.token);
                                        String tokenHead = ResponseUtils.getData(res.body()).getString(HttpHelper.Params.tokenHead);

                                        User user = Constants.getLastLoginUser(Utils.getApp());
                                        user.setToken(tokenHead + token);

                                        DbHandler dbHandler = DbHandler.getInstance(Utils.getApp());
                                        if(dbHandler.hasValue(User.Table, User.Username + "=?", new String[]{user.getUsername()})){
                                            dbHandler.update(user);
                                        }else {
                                            dbHandler.save(User.Table, user);
                                        }

                                        SPUtils.getInstance().put(Constants.Tag.token, user.getToken());
                                        SPUtils.getInstance().put(Constants.Tag.lastLoginUser, user.getUsername());

                                        // 重新发起请求
                                        okhttp3.Request.Builder builder = new okhttp3.Request.Builder();

                                        builder.headers(response.getRawCall().request().headers());
                                        builder.url(response.getRawCall().request().url());
                                        builder.tag(response.getRawCall().request().tag());
                                        builder.method(response.getRawCall().request().method(), response.getRawCall().request().body());

                                        builder.removeHeader(HttpHelper.Params.Authorization);
                                        builder.addHeader(HttpHelper.Params.Authorization, tokenHead + token);

                                        okhttp3.Request request = builder.build();
                                        OkGo.getInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
                                            @Override
                                            public void onFailure(Call call, IOException e) {
                                            }

                                            @Override
                                            public void onResponse(Call call, Response res) throws IOException {
                                                try {
                                                    response.setBody((T) convertResponse(res));

                                                    new android.os.Handler(Looper.getMainLooper()).post(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            JsonCallback.this.onSuccess(response);
                                                        }
                                                    });
                                                } catch (Throwable throwable) {
                                                    throwable.printStackTrace();
                                                }
                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                }
            } catch (Exception e) {
                e.printStackTrace();
                super.onError(response);
            }
        }else{
            super.onError(response);
        }
    }

    @Override
    public void onSuccess(com.lzy.okgo.model.Response<T> response) {

//        if (Constant.isLogin) {
            try {
                LogUtils.d("======开始检测是否会话超时======");

                JSONObject json = new JSONObject(response.body().toString());
                int rejCode = json != null ? json.optInt(Constants.Tag.code) : null;

                if(rejCode == Constants.Code.TOKEN_INVALID){
                    if(closeAll){
                        SPUtils.getInstance().remove(Constants.Tag.token);
                        SPUtils.getInstance().remove(Constants.Tag.lastLoginUser);

                        ActivityUtils.finishAllActivities();
                        ActivityUtils.startActivity(LoginActivity.class);
                        LogUtils.d("======检测会话超时======");
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//        }
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     */
    @Override
    public T convertResponse(Response response) throws Throwable {
        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }

    private String getRndStr(int length){
        String CHARS = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random RANDOM = new Random();
        StringBuilder sb = new StringBuilder();
        char ch;
        for (int i = 0; i < length; i++) {
            ch = CHARS.charAt(RANDOM.nextInt(CHARS.length()));
            sb.append(ch);
        }
        return sb.toString();
    }
}
