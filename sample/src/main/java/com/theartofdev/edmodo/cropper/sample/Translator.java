package com.theartofdev.edmodo.cropper.sample;

import android.graphics.Bitmap;
import android.util.Log;

import com.googlecode.tesseract.android.TessBaseAPI;

/**
 * Created by 207434 on 7/12/16.
 */
// TODO make this class more efficient by not training every time something needs to be translated
public class Translator extends TessBaseAPI {

    // path where training data lives
    private static final String path = "/mnt/sdcard/tesseract";
    private static final String LANGUAGE = "eng";

    // returns translated text
    public static String translate(Bitmap bitmap) {
        Translator translator = new Translator();
        translator.init(path, LANGUAGE);
        translator.setImage(bitmap);
        String text = translator.getUTF8Text();
        translator.end(); // free resources
        return text;
    }
}
