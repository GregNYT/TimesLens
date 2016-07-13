package com.theartofdev.edmodo.cropper.sample;

import android.util.Log;

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
        String query = buildQuery(searchTerms);
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
}
