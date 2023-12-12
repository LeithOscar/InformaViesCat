
package com.server.infoviescat.Entities;
import com.server.informaViesCat.Entities.AESEncryptionService;
import com.server.informaViesCat.Entities.User.User;
import javax.crypto.Cipher;
import org.json.JSONObject;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author leith
 * Aquesta clase pot encriptar i desencriptar un text.
 */
public class AESEncryptionServiceTest {

    
    
    public void testEncriptarYDesencriptar() {
        String Original = "text secret";
        String txtEncrypted = "MensajeEncriptado"; // Simulem el resultat de la encriptació
        String txtDencrypted = "MensajeDesencriptado"; // Simulem el resultat de la desencriptació

        // Creamos un mock para la clase Cipher
        Cipher cipher = mock(Cipher.class);

        try {
            when(cipher.doFinal(any(byte[].class))).thenReturn(txtEncrypted.getBytes("UTF-8"));

            String mensajeEncriptadoActual = AESEncryptionService.EncryptFixed(Original);

            assertEquals(txtEncrypted, mensajeEncriptadoActual);

            when(cipher.doFinal(any(byte[].class))).thenReturn(txtDencrypted.getBytes("UTF-8"));

            String mensajeDesencriptadoActual = AESEncryptionService.DecryptFixed(txtEncrypted);

            assertEquals(txtDencrypted, mensajeDesencriptadoActual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
     @Test
    public void testLogin_SuccessfulLogin() {
       // Crear un JsonObject
        JSONObject jsonObject = new JSONObject();

        // Agregar propiedades al JsonObject
        jsonObject.put("sessionId", "c0019366-2101-4746-a5d6-dce4eb569925");
        jsonObject.put("userId", 26);
        
         String textEncrypted = AESEncryptionService.encryptFromJSONObject(jsonObject);
         
         assertNotNull(textEncrypted);
         

    }
}
