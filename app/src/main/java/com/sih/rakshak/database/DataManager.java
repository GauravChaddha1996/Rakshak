package com.sih.rakshak.database;

import android.util.Base64;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class DataManager {
    private static DataManager dataManager;
    String AndroidKeyStore = "AndroidKeyStore";
    String AES_MODE = "AES";
    private String KEY_ALIAS = "rakshak_up";
    private String FIXED_IV = "rakshak";
    private Key key = null;
    SecretKey secretKey =null;
    private DataManager() {

    }

    public static DataManager getDataManager() {
        if (dataManager == null) {
            dataManager = new DataManager();
        }
        return dataManager;
    }

    public Key getAESKey() {
        if (key == null) {
            try {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "BC");
                keyGenerator.init(256);
                key = keyGenerator.generateKey();

            } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
                e.printStackTrace();
            }
        }
        return key;
    }

    public Key getAESKeyStore() {
        try {
            KeyStore store = KeyStore.getInstance(AndroidKeyStore);
            store.load(null);
            Key k = store.getKey("key1", null);
            if (k == null) {
                KeyGenerator keyGenerator = KeyGenerator.getInstance("AES", "BC");
                keyGenerator.init(256);
                k = keyGenerator.generateKey();
                k = store.getKey("key1", null);
            }
            return k;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String encrypt(String input) {
        Cipher c = null;
        try {
            c = Cipher.getInstance(AES_MODE);
            c.init(Cipher.ENCRYPT_MODE, getAESKeyStore());
            byte[] encodedBytes = c.doFinal(input.getBytes());
            String encryptedBase64Encoded = Base64.encodeToString(encodedBytes, Base64.DEFAULT);
            return encryptedBase64Encoded;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }

        return null;
    }

    public byte[] decrypt(String input) {
        Cipher c = null;
        try {
            c = Cipher.getInstance(AES_MODE);
            c.init(Cipher.DECRYPT_MODE, getAESKeyStore());
            byte[] decodedBytes = c.doFinal(Base64.decode(input, Base64.DEFAULT));
            return decodedBytes;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }
}