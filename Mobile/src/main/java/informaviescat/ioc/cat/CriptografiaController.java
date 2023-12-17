package informaviescat.ioc.cat;

import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;

public class CriptografiaController {

    private static final String secretKey_ = "abcdefghijklmnop"; // Clave de 16 bytes
    private static final String vector = "1234567890123456"; // Vector de inicialización de 16 bytes
    private static final String secretKey = "abcdefghijklmnop"; // Clave de 16 bytes

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


    //////////////////////////////////////

    public static String mydecryptToJSONObject(String encryptedData) {
        try {
            // Decode the Base64-encoded string
            byte[] decodedData = Base64.getDecoder().decode(encryptedData);

            // Extract IV from the decoded data
            byte[] iv = new byte[16];
            System.arraycopy(decodedData, 0, iv, 0, iv.length);

            // Extract encrypted data from the decoded data
            byte[] encryptedDataBytes = new byte[decodedData.length - iv.length];
            System.arraycopy(decodedData, iv.length, encryptedDataBytes, 0, encryptedDataBytes.length);

            // Initialize cipher for decryption
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

            // Perform decryption
            byte[] decryptedData = cipher.doFinal(encryptedDataBytes);

            // Create a String from the decrypted bytes
            return new String(decryptedData, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }



}
