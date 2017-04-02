package com.sih.rakshak.app;

import android.app.Application;

import io.realm.Realm;

public class RakshakApplication extends Application {
    public RakshakApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}

