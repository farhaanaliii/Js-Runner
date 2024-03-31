package com.github.farhanaliofficial.jsrun.views;

import android.webkit.WebView;
import android.content.Context;
import android.util.AttributeSet;

public class CompilerWebView extends WebView{
    public CompilerWebView(Context context){
        super(context);
        init(context);
    }
    public CompilerWebView(Context context, AttributeSet attr){
        super(context,attr);
    }
    public void init(Context context){
        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setUseWideViewPort(true);
        this.getSettings().setLoadWithOverviewMode(true);
        this.getSettings().setDomStorageEnabled(true);
        
    }
    
}
