package com.server.informaViesCat.Cryptografy;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.apache.tomcat.util.codec.binary.Base64;
/**
 *
 * @author leith
 * 
 */

public class EncryptedData {
    private String encryptedData;
    private String iv; // Initialization Vector (IV)

    public EncryptedData(String text, SecretKey secretKey) throws Exception {
       
        // Cifra JSON
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(text.getBytes());

        // Genera un IV aleatorio
        byte[] ivBytes = cipher.getIV();
        iv = Base64.encodeBase64String(ivBytes);

        // Converteix les dades cifrades a base64
        encryptedData = Base64.encodeBase64String(encryptedBytes);
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public static String Decrypt(String encryptedData, String iv, SecretKey secretKey) throws Exception {
        // Descifra los datos utilizando el IV y la llave
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(Base64.decodeBase64(iv)));
        byte[] decryptedBytes = cipher.doFinal(Base64.decodeBase64(encryptedData));

        // Convierte los datos descifrados a una cadena
        return new String(decryptedBytes);
    }
}
