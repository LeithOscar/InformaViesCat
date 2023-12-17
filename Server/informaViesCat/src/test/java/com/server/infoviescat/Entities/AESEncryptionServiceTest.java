package com.server.infoviescat.Entities;

import com.server.informaViesCat.Entities.AESEncryptionService;
import com.server.informaViesCat.Entities.Incident.Incident;
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

        //jsonObject.put("user",  new User(45, 1, " nou usuario", "1234", false, "usernit", "nou lastname", "testnit@gmail.com", 0).convertObjectToJson());
        //jsonObject.put("incident",   new Incident(32,26,1,3,"valencia","122","Coordenadas geográficas modificades","forat","2023-11-10","2023-11-10",true).convertObjectToJson());    
        jsonObject.put("userid", 43);
        //jsonObject.put("incidentid", 32);
        jsonObject.put("sessionid", "d4ebc623-eafa-418e-b775-eec2426148a4");

        //jsonObject.put("userId", 26);
        String textEncrypted = AESEncryptionService.encryptFromJSONObject(jsonObject);
        System.out.println(jsonObject);
        System.out.println(textEncrypted);

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
