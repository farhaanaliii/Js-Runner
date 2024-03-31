package com.github.farhanaliofficial.jsrun.views;

import android.app.Application;
import com.github.farhanaliofficial.jsrun.Handler.CrashHandler;

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
