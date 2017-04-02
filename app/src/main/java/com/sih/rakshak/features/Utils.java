package com.sih.rakshak.features;

import android.content.Context;
import android.util.Base64;

import com.sih.rakshak.features.sendmail.SendMailActivity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.Properties;

import javax.crypto.NoSuchPaddingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import devliving.online.securedpreferencestore.SecuredPreferenceStore;

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
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", CONSTANTS.getSmtpHost(context));
        props.put("mail.smtp.port", CONSTANTS.getSmtpPort(context));
        props.put("mail.smtp.auth", "true");
        // props.put("mail.smtp.ssl.enable","true");
        props.put("mail.debug", "false");
        //props.put("mail.smtp.EnableSSL.enable", "true");
        //props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        //props.put("mail.smtp.ssl.trust", "*");
        return props;
    }

    public static byte[] toByteArray(Object obj) throws IOException {
        byte[] bytes = null;
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;
        try {
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
        } finally {
            if (oos != null) {
                oos.close();
            }
            if (bos != null) {
                bos.close();
            }
        }
        return bytes;
    }

    public static Object toObject(byte[] bytes) throws IOException, ClassNotFoundException {
        Object obj = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            bis = new ByteArrayInputStream(bytes);
            ois = new ObjectInputStream(bis);
            obj = ois.readObject();
        } finally {
            if (bis != null) {
                bis.close();
            }
            if (ois != null) {
                ois.close();
            }
        }
        return obj;
    }

    public static String toString(byte[] bytes) {
        return new String(bytes);
    }

    public static PrivateKey getPrivateKey(Context context) {
        try {
            SecuredPreferenceStore prefStore = SecuredPreferenceStore.getSharedInstance(context);
            String s = prefStore.getString("privateKey", null);
            return (PrivateKey) Utils.toObject(Base64.decode(s.getBytes(), Base64.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static PublicKey getPublicKey(Context context) {
        try {
            SecuredPreferenceStore prefStore = SecuredPreferenceStore.getSharedInstance(context);
            String s = prefStore.getString("publicKey", null);
            return (PublicKey) Utils.toObject(Base64.decode(s.getBytes(), Base64.DEFAULT));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnrecoverableEntryException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
