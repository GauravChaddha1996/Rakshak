package com.sih.rakshak.features;

import android.content.Context;

import devliving.online.securedpreferencestore.SecuredPreferenceStore;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public class CONSTANTS {
    public static String host = "imap.gmail.com";
    public static String port = "993";
    public static String smtpHost = "smtp.gmail.com";
    public static String smtpPort = "587";

    public static String getPassword(Context context) {
        SecuredPreferenceStore prefStore = null;
        try {
            prefStore = SecuredPreferenceStore.getSharedInstance(context);
            return prefStore.getString("password", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getUsername(Context context) {
        SecuredPreferenceStore prefStore = null;
        try {
            prefStore = SecuredPreferenceStore.getSharedInstance(context);
            return prefStore.getString("username", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
