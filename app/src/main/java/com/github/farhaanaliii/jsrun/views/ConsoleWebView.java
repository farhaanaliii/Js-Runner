package com.github.farhaanaliii.jsrun.views;

import android.annotation.SuppressLint;
import android.webkit.WebView;
import android.content.Context;
import android.util.AttributeSet;

public class ConsoleWebView extends WebView {

    public ConsoleWebView(Context context) {
        super(context);
        init(context);
    }

    public ConsoleWebView(Context context, AttributeSet attr) {
        super(context, attr);
        init(context);
    }

    public ConsoleWebView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        init(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void init(Context context) {
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setAllowFileAccess(true);
        this.getSettings().setAllowFileAccessFromFileURLs(true);
        this.getSettings().setAllowUniversalAccessFromFileURLs(true);
    }
}
