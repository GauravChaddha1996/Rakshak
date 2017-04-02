package com.sih.rakshak.features;

import android.content.Context;

import com.sih.rakshak.features.sendmail.SendMailActivity;

import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Properties;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;

/**
 * Created by Batdroid on 1/4/17 for Rakshak.
 */

public class Utils {
    public static KeyPair keyPair = null;
    private static Properties smtpProps;

    public static Properties getProps(Context context) {
        Properties props = new Properties();
        props.put("mail.imap.auth", "true");
        props.put("mail.imap.ssl.enable", "true");
        props.put("mail.imap.host", CONSTANTS.getImapHost(context));
        props.put("mail.imap.port", CONSTANTS.getImapPort(context));
        return props;
    }

    public static Session getSession(Context context) {
        return Session.getInstance(getProps(context),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(CONSTANTS.getUsername(context), CONSTANTS.getPassword(context));
                    }
                });
    }

    public static PublicKey getRSAPublicKey() {
        return getKeyPair().getPublic();
    }

    public static KeyPair getKeyPair() {
        if (keyPair == null) {
            keyPair = generateKeys();
        }
        return keyPair;
    }

    private static KeyPair generateKeys() {
        try {
            // get instance of rsa cipher
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);            // initialize key generator
            keyPair = keyGen.generateKeyPair(); // generate pair of keys
        } catch (GeneralSecurityException e) {
            System.out.println(e);
        }
        return keyPair;
    }


    public static Session getSmtpSession(SendMailActivity sendMailActivity) {
        return Session.getInstance(getSmtpProps(sendMailActivity),
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(CONSTANTS.getUsername(sendMailActivity), CONSTANTS.getPassword(sendMailActivity));
                    }
                });
    }

    public static Properties getSmtpProps(Context context) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", CONSTANTS.getSmtpHost(context));
        props.put("mail.smtp.port", CONSTANTS.getSmtpPort(context));
        return props;
    }
}
