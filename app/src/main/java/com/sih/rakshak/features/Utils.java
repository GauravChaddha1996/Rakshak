package com.sih.rakshak.features;

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


}
