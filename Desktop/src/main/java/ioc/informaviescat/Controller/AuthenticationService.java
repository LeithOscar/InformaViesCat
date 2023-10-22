
package ioc.informaviescat.Controller;

import com.google.gson.Gson;
import ioc.informaviescat.Entities.User;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import static javax.swing.JOptionPane.showMessageDialog;

/**
 *
 * @author Pau Cors Bardolet
 */
public class AuthenticationService {
    private static final String BASE_URL = "http://10.2.77.127:8080/api/users/";


    /**
     * Envia la petició de login al servidor i confirma la resposta retornant un objecte User
     *
     * @param username username del usuari
     * @param password Clau de pass.
     * @return Retorna una entitat user amb el seu estat
     */
    public static User login(String username, String password) {
        try {
            URL url = new URL(BASE_URL + "login/" + username + "/" + password);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Deserialitza la resposta JSON en un objecte User
                Gson gson = new Gson();
                User user = gson.fromJson(response.toString(), User.class);

                System.out.println("Login amb èxit");
                return user;
            } else {
                System.out.println("Login fracassat. Resposta HTTP: " + responseCode);
                showMessageDialog(null, "Problema al intentar entrar. Resposta HTTP del servidor: "+responseCode);
            }
        } catch (IOException e) {
            showMessageDialog(null, "Problema amb la connexió");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Envia petició de Logout al servidor i confirma la resposta
     *
     * @param username username del usuari
     * @param password Clau de pass.
     */
    public static void logout(String username, String password) {
        try {
            URL url = new URL(BASE_URL + "logout/" + username + "/" + password);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Logout amb èxit");
            } else {
                System.out.println("Logout fracassat. Resposta HTTP: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
