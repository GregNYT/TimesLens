// "Therefore those skilled at the unorthodox
// are infinite as heaven and earth,
// inexhaustible as the great rivers.
// When they come to an end,
// they begin again,
// like the days and months;
// they die and are reborn,
// like the four seasons."
//
// - Sun Tsu,
// "The Art of War"

package com.theartofdev.edmodo.cropper.sample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.croppersample.R;
import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.File;
import java.io.FileInputStream;
import java.net.URI;
import java.util.List;

public final class CropResultActivity extends Activity {

    /**
     * The image to show in the activity.
     */
    static Bitmap mImage;

    private ImageView imageView;
    private ProgressDialog progressDialog;
    private ArticleFetcher articleFetcher = new ArticleFetcher();

    private static final String TAG = "CropResultActivity";
    public static final int ARTICLE_FOUND = 1;
    public static final int NO_ARTICLE_FOUND = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crop_result);

        imageView = ((ImageView) findViewById(R.id.resultImageView));
        imageView.setBackgroundResource(R.drawable.backdrop);

        if (mImage != null) {
            imageView.setImageBitmap(mImage);

            displayArticleView();

            double ratio = ((int) (10 * mImage.getWidth() / (double) mImage.getHeight())) / 10d;
            int byteCount = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
                byteCount = mImage.getByteCount() / 1024;
            }
            String desc = "(" + mImage.getWidth() + ", " + mImage.getHeight() + "), Ratio: " + ratio + ", Bytes: " + byteCount + "K";
            ((TextView) findViewById(R.id.resultImageText)).setText(desc);
        } else {
            Intent intent = getIntent();
            Uri imageUri = intent.getParcelableExtra("URI");
            if (imageUri != null) {
                imageView.setImageURI(imageUri);
                displayArticleView();
            } else {
                Toast.makeText(this, "No image is set to show", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void displayArticleView() {
        if (!isConnected()) {
            Toast.makeText(getApplicationContext(), "Finding article requires Internet connection.", Toast.LENGTH_LONG).show();
            return;
        }

        // handles transition to a webView with given URL on main thread
        final Handler urlHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                progressDialog.dismiss();
                Bundle data = msg.getData();
                if (msg.what == ARTICLE_FOUND) {
                    String url = data.getString("url");
                    Intent intent = new Intent(getApplicationContext(), ArticleViewActivity.class);
                    intent.putExtra("url", url);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), data.getString("error") + " Try again.", Toast.LENGTH_LONG).show();
                }
            }
        };

//                ProgressDialog.show(this, "Article Finder",
//                "Searching for matching articles...", true);

        // handle translation/fetching articles off UI thread
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                final String[] searchWords = Translator.translate(mImage).replaceAll("[^a-zA-z ]", "").split("\\s+");
                articleFetcher.fetchArticles(urlHandler, searchWords);
            }
        });
        thread.start();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Searching for matching articles...");
        progressDialog.show();
    }

    // determine if device has internet access
    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        releaseBitmap();
        super.onBackPressed();
    }

    public void onImageViewClicked(View view) {
        releaseBitmap();
        finish();
    }

    private void releaseBitmap() {
        if (mImage != null) {
            mImage.recycle();
            mImage = null;
        }
    }
}
