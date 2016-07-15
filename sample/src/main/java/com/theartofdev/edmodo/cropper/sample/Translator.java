package com.theartofdev.edmodo.cropper.sample;

import android.graphics.Bitmap;
import android.os.Environment;
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
    private static final String TAG = "TRANSLATOR";

    // returns translated text
    public static String translate(Bitmap bitmap) {
        Translator translator = new Translator();
        Log.d(TAG, isExternalStorageWritable()); // Log external storage write access
        Log.d(TAG,isExternalStorageReadable()); // Log external storage read access
        translator.init(path, LANGUAGE);
        translator.setImage(bitmap);
        String text = translator.getUTF8Text();
        translator.end(); // free resources
        return text;
    }

    /* Checks if external storage is available to at least write to and returns the path name */
    private static String isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        String retval = "External storage is not writable";
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            retval = Environment.getExternalStorageDirectory().toString();
            return retval;
        }
        return retval;
    }

    /* Checks if external storage is available to at least read from and returns the path name */
    private static String isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        String retval = "External storage is not readable";
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            retval = Environment.getExternalStorageDirectory().toString();
            return retval;
        }
        return retval;
    }

}
