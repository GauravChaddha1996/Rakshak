package com.sih.rakshak.app;

import android.app.Application;

public class RakshakApplication extends Application {
    public RakshakApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
     //   LeakCanary.install(this);
    }
}

