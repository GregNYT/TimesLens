package com.theartofdev.edmodo.cropper.sample;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 207434 on 7/12/16.
 */
public class ArticleFetcher {

    private static final String BASE_URL =
            "http://search-add-api.prd.use1.nytimes.com/svc/add/v1/sitesearch.json";
    private static final String QUERY_SEPARATOR = "?q=";
    private static final String SPACE = "%20";
    private static final String TAG = "ArticleFetcher";

    // TODO: fetch articles asynchronously then update GUI on UI thread.
    public List<Article> fetchArticles(String... searchTerms) {
        final String url = buildQuery(searchTerms);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run(){
                //code to do the HTTP request
                JSONArray results = sendRequest(url);
                Log.d(TAG, "Results: " + results);
            }
        });
        thread.start();

        return new ArrayList<Article>();
    }

    // encodes query terms in BASE_URL using OR to connect searchTerms
    private String buildQuery(String... searchTerms) {
        String result = BASE_URL + QUERY_SEPARATOR;
        for (int i = 0; i < searchTerms.length; ++i) {
            String term = searchTerms[i];
            String toAppend = (i == searchTerms.length - 1) ? term : term + SPACE + "OR" + SPACE;
            result += toAppend;
        }
        return result;
    }

    // sends request and returns array of docs (articles) matching search
    private JSONArray sendRequest(String urlString) {
        HttpURLConnection connection = null;
        JSONObject object = null;
        try {
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String contents = readStream(in);
            Log.d(TAG, contents);
            object = new JSONObject(contents);
            Log.d(TAG, "Object: " + object.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null)
                connection.disconnect();
        }

        // try returning array of docs matching query
        return (object == null) ? null : object.optJSONArray("docs");
    }

    // returns String version of contents inside the InputStream
    private String readStream(InputStream in) {
        byte[] buffer = new byte[1024];
        int bytesRead;
        String contents = "";
        try {
            while((bytesRead = in.read(buffer)) != -1) {
                contents += new String(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents;
    }
}
