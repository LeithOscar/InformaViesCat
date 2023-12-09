package com.server.informaViesCat.Entities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author leith Clase que encripta i desencripta format AES
 */
public class AESEncryptionService {

    private static final String secretKey = "abcdefghijklmnop"; // Clave de 16 bytes
    private static final String vector = "1234567890123456"; // Vector de inicialización de 16 bytes

    public static String encryptObject(Object object) {
        try {
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
                objectOutputStream.writeObject(object);
            }

            byte[] encryptedData = cipher.doFinal(outputStream.toByteArray());
            return Base64.getEncoder().encodeToString(encryptedData);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object decryptObject(String encryptedData, Class<?> objectClass) {
        try {
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));

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

    public static String Encrypt(String mensaje) {
        try {
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, iv);

            byte[] mensajeEncriptado = cipher.doFinal(mensaje.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(mensajeEncriptado);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String Decrypt(String mensajeEncriptado) {
        try {
            IvParameterSpec iv = new IvParameterSpec(vector.getBytes("UTF-8"));
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, iv);

            byte[] mensajeDesencriptado = cipher.doFinal(Base64.getDecoder().decode(mensajeEncriptado));
            return new String(mensajeDesencriptado, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
