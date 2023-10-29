
package com.server.informaViesCat.Entities;

import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author leith
 * Clase que encripta i desencripta format AES 
 */
public class AESEncryptionService {
    
    private static final String secretKey = "key123"; // Clave de 16 bytes
    private static final String vector = "vector23"; // Vector de inicialización de 16 bytes

    private static final String formatUTF8 ="UTF-8";
    private static final String AES ="AES/CBC/PKCS5Padding"; //AES  Esquema d'omplitud
    
    public static String Encrypt(String mensaje) {
        try {
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes(formatUTF8));
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(formatUTF8), "AES");

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            byte[] mensajeEncriptado = cipher.doFinal(mensaje.getBytes(formatUTF8));
            return Base64.getEncoder().encodeToString(mensajeEncriptado);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String Decrypt(String mensajeEncriptado) {
        try {
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes(formatUTF8));
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(formatUTF8), "AES");

            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

            byte[] mensajeDesencriptado = cipher.doFinal(Base64.getDecoder().decode(mensajeEncriptado));
            return new String(mensajeDesencriptado, formatUTF8);
            
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
