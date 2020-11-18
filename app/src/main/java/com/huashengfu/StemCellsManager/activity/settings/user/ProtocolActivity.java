package com.huashengfu.StemCellsManager.activity.settings.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.huashengfu.StemCellsManager.Constants;
import com.huashengfu.StemCellsManager.R;
import com.huashengfu.StemCellsManager.activity.BaseActivity;
import com.huashengfu.StemCellsManager.http.HttpHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ProtocolActivity extends BaseActivity {

    @BindView(R.id.webview)
    WebView webView;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);

        unbinder = ButterKnife.bind(this);

        progressBar.setMax(100);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);

                if(newProgress == 100)
                    progressBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(HttpHelper.Url.protocol);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
