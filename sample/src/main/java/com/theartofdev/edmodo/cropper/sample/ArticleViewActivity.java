package com.theartofdev.edmodo.cropper.sample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.example.croppersample.R;

/**
 * Created by 207434 on 7/13/16.
 */
public class ArticleViewActivity extends Activity {

    private static final String TAG = "ArticleViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setContentView(R.layout.activity_article_view);
        String url = getIntent().getStringExtra("url");
        Log.d(TAG, url);
        ((WebView) findViewById(R.id.webView)).loadUrl(url);
        Log.d(TAG, "loaded url");
    }
}
