package com.theartofdev.edmodo.cropper.sample;

import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by 207434 on 7/12/16.
 */
public class Translator extends TessBaseAPI {

    // path where training data lives
    private static final String path = "/mnt/sdcard/tesseract";
    private static final String LANGUAGE = "eng";

    private static Translator translator;

    // only want to call init once -- requires large file ops to train instance
    public static Translator getInstance() {
        if (translator == null) {
            translator = new Translator();
            translator.init(path, LANGUAGE);
        }
        return translator;
    }

    // returns translated text
    public String translateToString(Bitmap bitmap) {
        if (translator == null) return "";
        setImage(bitmap);
        return getUTF8Text();
    }

    // frees memory associated with instance
    public void destroy() {
        if (translator == null) return;
        translator.end();
    }
}
