package com.huashengfu.StemCellsManager;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

//import com.baidu.mapapi.CoordType;
//import com.baidu.mapapi.SDKInitializer;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.mob.MobSDK;
import com.tencent.rtmp.TXLiveBase;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import cn.sharesdk.framework.ShareSDK;
import okhttp3.OkHttpClient;

public class AppApplication extends MultiDexApplication {

    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);

        initOkGo();

//        ShareSDK.in

        MobSDK.init(this);
        // 设置为同意隐私政策
        MobSDK.submitPolicyGrantResult(true, null);
    }

    /**
     * 初始化网络请求
     */
    private void initOkGo() {
        long time = 60 * 1000 * 5;
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if(BuildConfig.ifShowLog){
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
            loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
            loggingInterceptor.setColorLevel(Level.INFO);
            builder.addInterceptor(loggingInterceptor);
            //builder.addInterceptor(new ChuckInterceptor(this));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.put("os", "android");
        //超时时间设置，默认60秒
        builder.readTimeout(time, TimeUnit.MILLISECONDS);
        builder.writeTimeout(time, TimeUnit.MILLISECONDS);
        builder.connectTimeout(time, TimeUnit.MILLISECONDS);
        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        OkGo.getInstance().init(this)
                .setOkHttpClient(builder.build())
                .setCacheMode(CacheMode.NO_CACHE)
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)
                .setRetryCount(0)
                .addCommonHeaders(headers);
    }
}
