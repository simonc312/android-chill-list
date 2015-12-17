package com.example.simon.chillist;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by Simon on 12/16/2015.
 */
public class ChillistApp extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        FlowManager.destroy();
    }
}
