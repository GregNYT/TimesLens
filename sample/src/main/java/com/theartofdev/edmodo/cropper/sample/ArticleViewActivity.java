package com.theartofdev.edmodo.cropper.sample;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.croppersample.R;

/**
 * Created by 207434 on 7/13/16.
 */
public class ArticleViewActivity extends Activity {

    private static final String TAG = "ArticleViewActivity";
    private WebView webView;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_view);
        url = getIntent().getStringExtra("url");
        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    protected void onResume() {
        super.onResume();
        webView.loadUrl(url);
    }
}
