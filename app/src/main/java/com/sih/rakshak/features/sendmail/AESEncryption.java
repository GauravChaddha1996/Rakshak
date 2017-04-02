package com.sih.rakshak.features.sendmail;

import android.util.Base64;

import com.sih.rakshak.database.DataManager;

import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by Batdroid on 2/4/17 for Rakshak.
 */

public class AESEncryption {

    public static SecretKey getSecretEncryptionKey() {
        if (DataManager.getDataManager().getSecretKey() == null) {
            KeyGenerator generator = null;
            try {
                generator = KeyGenerator.getInstance("AES");
                generator.init(128); // The AES key size in number of bits
                SecretKey secKey = generator.generateKey();
                DataManager.getDataManager().setSecretKey(secKey);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }
        return DataManager.getDataManager().getSecretKey();
    }


    private static byte[] internalEncryptText(String plainText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        return aesCipher.doFinal(plainText.getBytes());
    }

    private static byte[] internalDecryptText(String plainText, SecretKey secKey) throws Exception {
        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        return aesCipher.doFinal(plainText.getBytes());
    }

    static String encryptText(String str, SecretKey secretKey) {
        try {
            byte[] cipherText = internalEncryptText(str, secretKey);
            return Base64.encodeToString(cipherText, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptText(String str, SecretKey secretKey) {
        try {
            byte[] cipherText = internalDecryptText(str, secretKey);
            return Base64.encodeToString(cipherText, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}