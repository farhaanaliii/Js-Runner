package com.github.farhaanaliii.jsrun.views;

import android.app.Application;
import com.github.farhaanaliii.jsrun.Handler.CrashHandler;

public class App extends Application {

    private static App sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
        CrashHandler.init(this);
    }
    public static App getApp() {
        return sApp;
    }

}
