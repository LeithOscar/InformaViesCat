package com.server.informaViesCat.Entities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;

/**
 *
 * @author leith Clase que encripta i desencripta format AES
 */
public class AESEncryptionService {

    private static final String secretKey_ = "abcdefghijklmnop"; // Clave de 16 bytes
    private static final String vector = "1234567890123456"; // Vector de inicialización de 16 bytes

    private static final String secretKey = "abcdefghijklmnop"; // Clave de 16 bytes

    // vector aleatori
    public static String encryptObject(Object object) {
        try {
            // Genera un IV aleatorio
            byte[] salt = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(salt);

            // Genera un IV aleatorio
            byte[] iv = new byte[16];
            random.nextBytes(iv);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
                objectOutputStream.writeObject(object);
            }

            byte[] encryptedData = cipher.doFinal(outputStream.toByteArray());

            // Concatena salt e IV al inicio del texto cifrado
            byte[] result = new byte[salt.length + iv.length + encryptedData.length];
            System.arraycopy(salt, 0, result, 0, salt.length);
            System.arraycopy(iv, 0, result, salt.length, iv.length);
            System.arraycopy(encryptedData, 0, result, salt.length + iv.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(result);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    // vector aleatori
    public static Object decryptObject(String encryptedData, Class<?> objectClass) {
        try {
            // Decodifica el texto cifrado
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);

            // Extrae el salt, IV y resto del texto cifrado
            byte[] salt = new byte[16];
            byte[] iv = new byte[16];
            byte[] encryptedBytes = new byte[decodedData.length - salt.length - iv.length];

            System.arraycopy(decodedData, 0, salt, 0, salt.length);
            System.arraycopy(decodedData, salt.length, iv, 0, iv.length);
            System.arraycopy(decodedData, salt.length + iv.length, encryptedBytes, 0, encryptedBytes.length);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

            byte[] decryptedData = cipher.doFinal(encryptedBytes);

            ByteArrayInputStream inputStream = new ByteArrayInputStream(decryptedData);
            try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                Object decryptedObject = objectInputStream.readObject();
                if (decryptedObject != null && objectClass.isInstance(decryptedObject)) {
                    return decryptedObject;
                } else {
                    throw new IllegalArgumentException("El objeto descifrado no es una instancia de " + objectClass.getName());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // vector Fixa, per BBDD
    public static String EncryptFixed(String mensaje) {
        try {
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(secretKey_.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            byte[] mensajeEncriptado = cipher.doFinal(mensaje.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(mensajeEncriptado);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // vector Fixa, per BBDD
    public static String DecryptFixed(String mensajeEncriptado) {
        try {
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(secretKey_.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

            byte[] mensajeDesencriptado = cipher.doFinal(Base64.getDecoder().decode(mensajeEncriptado));
            return new String(mensajeDesencriptado, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /* for app mobile*/
    public static String encryptFromJSONObject(JSONObject jsonObject) {
        try {
            // Genera un IV aleatorio
            byte[] iv = new byte[16];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

            byte[] jsonData = jsonObject.toString().getBytes("UTF-8");
            byte[] encryptedData = cipher.doFinal(jsonData);

            // Concatena IV al inicio del texto cifrado
            byte[] result = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encryptedData, 0, result, iv.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(result);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JSONObject decryptToJSONObject(String encryptedData) {
        try {
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);

            // Extrae el IV y resto del texto cifrado
            byte[] iv = new byte[16];
            byte[] encryptedBytes = new byte[decodedData.length - iv.length];

            System.arraycopy(decodedData, 0, iv, 0, iv.length);
            System.arraycopy(decodedData, iv.length, encryptedBytes, 0, encryptedBytes.length);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

            byte[] decryptedData = cipher.doFinal(encryptedBytes);
            String jsonString = new String(decryptedData, "UTF-8");

            return new JSONObject(jsonString);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
