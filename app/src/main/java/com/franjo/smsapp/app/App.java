package com.franjo.smsapp.app;

import android.app.Application;

public class App extends Application {

    private static App appContext;

    public static App getAppContext() {
        return appContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

}
