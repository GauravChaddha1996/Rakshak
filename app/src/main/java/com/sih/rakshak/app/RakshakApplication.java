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
        //   LeakCanary.install(this);
/*
        String s = DataManager.getDataManager().encrypt("ThisisSecure");
        System.out.println(s);
        String s1 = new String(DataManager.getDataManager().decrypt(s));
        System.out.println(s1);
*/
    }
}

