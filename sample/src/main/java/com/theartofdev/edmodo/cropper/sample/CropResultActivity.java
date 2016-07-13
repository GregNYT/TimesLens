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
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
    private ArticleFetcher articleFetcher = new ArticleFetcher();

    private static final String TAG = "CropResultActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "activity created");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_crop_result);

        imageView = ((ImageView) findViewById(R.id.resultImageView));
        imageView.setBackgroundResource(R.drawable.backdrop);

        if (mImage != null) {
            Log.d(TAG, "image view not null");
            imageView.setImageBitmap(mImage);
            // code to translate image to article object
            String[] translation = Translator.translate(mImage).replaceAll("[^a-zA-z ]", "").split("\\s+");
            Log.d(TAG, "Search terms: ");
            for (String word : translation) {
                Log.d(TAG, word);
            }
            List<Article> articles = articleFetcher.fetchArticles(translation);
            if (articles.isEmpty()) {
                Toast.makeText(this, "No matching articles found", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Article url: " + articles.get(0).url, Toast.LENGTH_LONG).show();
            }
            // end code to get article
            double ratio = ((int) (10 * mImage.getWidth() / (double) mImage.getHeight())) / 10d;
            int byteCount = 0;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB_MR1) {
                byteCount = mImage.getByteCount() / 1024;
            }
            String desc = "(" + mImage.getWidth() + ", " + mImage.getHeight() + "), Ratio: " + ratio + ", Bytes: " + byteCount + "K";
            ((TextView) findViewById(R.id.resultImageText)).setText(desc);
        } else {
            Log.d(TAG, "image view is null");
            Intent intent = getIntent();
            Uri imageUri = intent.getParcelableExtra("URI");
            if (imageUri != null) {
                imageView.setImageURI(imageUri);
            } else {
                Toast.makeText(this, "No image is set to show", Toast.LENGTH_LONG).show();
            }
        }
    }

    // TODO: remove weird chars
    private String transform(String original) {
        return original;
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
