package com.sih.rakshak.features;

import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public class Utils {
    public static Properties getProps() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", CONSTANTS.host);
        props.put("mail.smtp.port", "587");
        return props;
    }

    public static Session getSession() {
        return Session.getInstance(getProps(),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(CONSTANTS.username, CONSTANTS.password);
                    }
                });
    }

}
