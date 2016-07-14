package com.theartofdev.edmodo.cropper.sample;

/**
 * Created by 207434 on 7/12/16.
 */
public class Article {

    final String mainHeadline;
    final String printHeadline;
    final String byLine;
    final String url;

    public Article(String mainHeadline, String printHeadline, String byLine, String url) {
        this.mainHeadline= mainHeadline;
        this.printHeadline = printHeadline;
        this.byLine = byLine;
        this.url = url;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder("Article Object: [");
        builder.append("MainHeadline: " + mainHeadline)
                .append(", ")
                .append("PrintHeadline: " + printHeadline)
                .append(", ")
                .append("byLine: " + byLine)
                .append(", ")
                .append("url: " + url)
                .append("]");
        return builder.toString();
    }
}
