
package ioc.informaviescat.Controller;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/**
 *
 * @author Pau Cors Bardolet
 */
public class UsersManagement {
    private static final String BASE_URL = "http://10.2.77.127:8080/api/users/";
    
    /**
     * Envia la petició de getall de usuaris al servidor i transforma la resposta en un objecte String[][] que el programa pot fer servir
     *
     * @return retorna String[][] amb les dades dels usuaris existents
     */
    public static String[][] getUserData() throws IOException {
        
        URL url = new URL(BASE_URL + "getall");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(response.toString(), JsonArray.class);

            List<String[]> userDataList = new ArrayList<>();

            for (JsonElement element : jsonArray) {
                JsonObject userObject = element.getAsJsonObject();
                String[] userData = new String[6];
                userData[0] = userObject.get("id").getAsString();
                userData[1] = getRole(userObject.get("rolId").getAsInt());
                userData[2] = userObject.get("name").getAsString();
                userData[3] = userObject.get("lastName").getAsString();
                userData[4] = userObject.get("userName").getAsString();
                userData[5] = userObject.get("logged").getAsBoolean() ? "Logat" : "No Logat";
                userDataList.add(userData);
            }

            return userDataList.toArray(new String[0][0]);
        } else {
            throw new IOException("No s'ha pogut obtenir la informació des de l'API. Codi de resposta: " + responseCode);
        }
    }

    /**
     * Segons un int d'entrada retorna quin tipus de Usuari és en format String, per a la visualització en taula
     *
     * @param roleId Id del rol de l'usuari
     * @return retorna String amb tipus d'usuari
     */
    public static String getRole(int roleId) {
        if(roleId == 1){
            return "Administrador";
        }
        else if(roleId == 2){
            return "Tècnic";
        }
        else{
            return "Usuari comú";
        }
    }
}
