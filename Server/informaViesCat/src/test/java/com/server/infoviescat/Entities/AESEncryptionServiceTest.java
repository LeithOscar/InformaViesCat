package com.server.infoviescat.Entities;

import com.server.informaViesCat.Entities.AESEncryptionService;
import com.server.informaViesCat.Entities.User.User;
import com.server.informaViesCat.Entities.User.UserResponse;
import javax.crypto.Cipher;
import org.assertj.core.api.Assertions;
import org.json.JSONObject;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 * @author leith Aquesta clase pot encriptar i desencriptar un text.
 */
public class AESEncryptionServiceTest {

    public void testEncriptarYDesencriptar() {
        String Original = "text secret";
        String txtEncrypted = "MensajeEncriptado"; // Simulem el resultat de la encriptació
        String txtDencrypted = "MensajeDesencriptado"; // Simulem el resultat de la desencriptació


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

        JSONObject jsonObject = new JSONObject();


        jsonObject.put("sessionid", "d487762c-96f8-4555-81d9-6c4a4cfeacdc");
        jsonObject.put("userId", 26);

        String textEncrypted = AESEncryptionService.encryptFromJSONObject(jsonObject);

        assertNotNull(textEncrypted);

    }

    @Test
    public void testConvertObjectToJSONObject() {
        
        User user = new User(1, 1, "", "", true, "", "", "", 1);
        UserResponse userResponse = new UserResponse(user, "sessionId456");

        JSONObject jsonObject = userResponse.convertObjectToJson();

        Assertions.assertThat(jsonObject).isNotNull();
        Assertions.assertThat(jsonObject.getString("sessionId")).isEqualTo("sessionId456");

    }

}
