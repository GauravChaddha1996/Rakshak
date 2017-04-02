package com.sih.rakshak.features;


import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AES {
 
    private static SecretKeySpec secretKey;
    private static byte[] key;
 
    public static void setKey(String myKey) 
    {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } 
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    public static String encrypt(String strToEncrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")),Base64.DEFAULT);
        } 
        catch (Exception e) 
        {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
 
    public static String decrypt(String strToDecrypt, String secret) 
    {
        try
        {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.decode(strToDecrypt,Base64.DEFAULT)));
        } 
        catch (Exception e) 
        {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    public static void main(String[] args) throws NoSuchAlgorithmException,UnsupportedEncodingException
    {
        //final String secretKey = "ssshhhhhhhhhhh!!!!";
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // for example
        SecretKey secretKey = keyGen.generateKey();
        String aeskey=bytesToHex(secretKey.getEncoded()).toString();

        String originalString = "MailBody";
        String encryptedString = AES.encrypt(originalString, aeskey) ;

        int N = Integer.parseInt(args[0]);
        RSA key = new RSA(N);
        byte[] bytes = aeskey.getBytes();
        BigInteger mess = new BigInteger(bytes);
        BigInteger encryptedkey = key.encrypt(mess);

        //System.out.println(encryptedkey.toString());
        String body=encryptedkey.toString()+"!"+encryptedString;

        System.out.println(body);
            

        //decryption
        String data[]=body.split("!");

        BigInteger e=new BigInteger(data[0]);
        BigInteger decrypt = key.decrypt(e);
        
        
        byte temp[] = decrypt.toByteArray();
        String fin = new String(temp, "UTF-8");
                
        
        

        String decryptedString = AES.decrypt(encryptedString, fin) ;
        
        System.out.println(originalString);
        System.out.println(encryptedString);
        System.out.println(decryptedString);
    }
    private static String  bytesToHex(byte[] hash) {
        //return DatatypeConverter.printHexBinary(hash);
        return "ajaj";
    }
}