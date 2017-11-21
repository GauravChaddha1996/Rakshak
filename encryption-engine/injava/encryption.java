import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

public class encryption {

    public static void main(String[] args) throws Exception {
        String user=args[0];
        String body=args[1];

        
        Map<String, Object> keys = getRSAKeys();
        PrivateKey privateKey = (PrivateKey) keys.get("private");
        PublicKey publicKey = (PublicKey) keys.get("public");
        System.out.println(privateKey);
        System.out.println(publicKey);
        
        String secretAESKeyString = getSecretAESKeyAsString();

        
        String encryptedText = encryptTextUsingAES(body, secretAESKeyString);

        
        String encryptedAESKeyString = encryptAESKey(secretAESKeyString, publicKey);

  
        
        String finalmsg=encryptedAESKeyString + "!" + encryptedText;
        System.out.println("Body:" + finalmsg);
        
        String mail=finalmsg;
        String data[]=mail.split("!");
        
        System.out.println("Private key=>");
        System.out.println(privateKey);
        String decryptedAESKeyString = decryptAESKey(encryptedAESKeyString,privateKey);

       
        String decryptedText = decryptTextUsingAES(data[1], decryptedAESKeyString);
        System.out.println("decrypted:" + decryptedText);

    }

   
    public static String getSecretAESKeyAsString() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        String encodedKey = Base64.getEncoder().encodeToString(secKey.getEncoded());
        return encodedKey;
    }

    
    public static String encryptTextUsingAES(String plainText, String aesKeyString) throws Exception {
        byte[] decodedKey = Base64.getDecoder().decode(aesKeyString);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, originalKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(byteCipherText);
    }

    
    
    private static Map<String, Object> getRSAKeys() throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        Map<String, Object> keys = new HashMap<String, Object>();
        keys.put("private", privateKey);
        keys.put("public", publicKey);
        return keys;
    }

   
    
    private static String encryptAESKey(String decryptedAESKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(decryptedAESKey)));
    }
    private static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }







    public static String decryptTextUsingAES(String encryptedText, String aesKeyString) throws Exception {

        byte[] decodedKey = Base64.getDecoder().decode(aesKeyString);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");

        // AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, originalKey);
        byte[] bytePlainText = aesCipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(bytePlainText);
    }

    
    private static String decryptAESKey(String plainAESKey, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainAESKey.getBytes()));
    }
    
    
    public static byte[] hexToBytes(String s) {
    byte[] b = new byte[s.length() / 2];
    for (int i = 0; i < b.length; i++) {
      int index = i * 2;
      int v = Integer.parseInt(s.substring(index, index + 2), 16);
      b[i] = (byte) v;
    }
    return b;
  }


}