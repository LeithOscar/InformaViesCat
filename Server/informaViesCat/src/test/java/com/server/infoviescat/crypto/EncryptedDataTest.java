package com.server.infoviescat.crypto;

import com.server.informaViesCat.Cryptografy.EncryptedData;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author leith
 */
public class EncryptedDataTest {

    byte[] keyBytes = "1234567890123456".getBytes(); // Debe ser de 16, 24 o 32 bytes
    SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
    String dataToEncrypt = "Hola mon";

    /**
     * Test of Encrypt method
     */
    @Test
    public void testEncrypt() {
        
        String expResult = "Hola mon";
        EncryptedData instance;
        try {
            instance = new EncryptedData(dataToEncrypt, secretKey);
            assertNotEquals(expResult, instance.getEncryptedData());

        } catch (Exception ex) {
            Logger.getLogger(EncryptedDataTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Test of decrypt method
     */
    @Test
    public void testDecrypt() {

        EncryptedData instance;
        try {
            instance = new EncryptedData(dataToEncrypt, secretKey);

            String encryptedData = instance.getEncryptedData();
            String expResult = EncryptedData.Decrypt(encryptedData, instance.getIv(), secretKey);
            assertEquals(dataToEncrypt, expResult);
        } catch (Exception ex) {
            Logger.getLogger(EncryptedDataTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
