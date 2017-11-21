import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.bind.DatatypeConverter;


public class encrypt {
 
    
  
    public static void main(String[] args) throws Exception {
        System.out.println(args[0]);
      String user=args[0];
      String mail=args[1];
        SecretKey secKey = getSecretEncryptionKey();
        byte[] cipherText = encryptText(mail, secKey);
        String decryptedText = decryptText(cipherText, secKey);
        
      String s = new String(hexTosecKey);
      String ss = new String(cipherText);
      
      String Final=s+"G"+ss;
      System.out.println(Final);
      
        System.out.println("Decrypted Text:"+decryptedText);
        System.out.println(secKey);
        
    }
    
    
    public static SecretKey getSecretEncryptionKey() throws Exception{
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128); // The AES key size in number of bits
        SecretKey secKey = generator.generateKey();
        return secKey;
    }
    
    public static byte[] encryptText(String plainText,SecretKey secKey) throws Exception{
		// AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(plainText.getBytes());
        return byteCipherText;
    }
    
    
    public static String decryptText(byte[] byteCipherText, SecretKey secKey) throws Exception {
		// AES defaults to AES/ECB/PKCS5Padding in Java 7
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, secKey);
        byte[] bytePlainText = aesCipher.doFinal(byteCipherText);
        return new String(bytePlainText);
    }
    
    
    private static String  bytesToHex(byte[] hash) {
        return DatatypeConverter.printHexBinary(hash);
    }
}