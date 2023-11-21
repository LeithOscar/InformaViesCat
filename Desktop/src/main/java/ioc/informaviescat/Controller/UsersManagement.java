
package ioc.informaviescat.Controller;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import ioc.informaviescat.Entities.Response;
import ioc.informaviescat.Entities.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static javax.swing.JOptionPane.showMessageDialog;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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
     * @throws java.io.IOException
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
     * Envia la petició de getall de usuaris al servidor i transforma la resposta en un objecte List de objectes User que el programa pot fer servir
     *
     * @return retorna List de objectes User amb les dades dels usuaris existents
     * @throws java.io.IOException
     */
    public static List<User> getUserList() throws IOException{
        
        List<User> userList;
                
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
            
            String resposta = response.toString();
            
             Gson gson = new Gson();
            TypeToken<List<User>> listType = new TypeToken<List<User>>() {};
            userList = gson.fromJson(resposta, listType.getType());
        } else {
            throw new IOException("No s'ha pogut obtenir la llista d'usuaris. Codi de resposta: " + responseCode);
        }
        return userList;
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
    
    /**
     * Funció que envia la petició al servidor per crear un nou usuari amb un objecte User i analitza la resposta
     *
     * @param nouUsuari és l'usuari nou que es vol crear
     */
    public static void createUser(User nouUsuari){
         
        String url = BASE_URL + "create";

        String json = "{\"rolId\":\""+nouUsuari.getRolId()+"\",\"name\":\""+nouUsuari.getName()+"\","
                + "\"userName\":\""+nouUsuari.getUserName()+"\",\"lastName\":\""+nouUsuari.getLastName()+"\","
                + "\"email\":\""+nouUsuari.GetEmail()+"\",\"password\":\""+nouUsuari.getPassword()+"\",\"islogged\":false}";

        System.out.println(url);
        System.out.println(json);
        try {
            
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(url);

            // Afegir el JSON com a cos de la sol·licitud
            StringEntity entity = new StringEntity(json);
            httpPut.setEntity(entity);
            httpPut.setHeader("Content-Type", "application/json");

            // Enviar la sol·licitud
            HttpResponse response = httpClient.execute(httpPut);

            // Processar la resposta
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);

            // Mostrar la resposta del servidor
            if(responseBody.equals("Usuari creat.")){
                showMessageDialog(null, "Resposta del servidor: "+responseBody);
            }
            else{
                showMessageDialog(null, "Hi ha hagut algun error creant l'usuari. Resposta del servidor: "+responseBody);
            }
            System.out.println("Resposta del servidor: "+responseBody);

            // Tancar el client HTTP
            httpClient.close();
        } catch (ConnectException e){
            showMessageDialog(null, "Problema amb la connexió: Timeout");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Funció que envia la petició al servidor per modificar un usuari ja existent amb un objecte User i analitza la resposta
     *
     * @param modifyedUser és l'usuari que es vol modificar, ja amb les dades canviades.
     */
    public static void modifyUser(User modifyedUser){
        
        String url = BASE_URL + "modify";
        
        String json = "{\"id\":" + modifyedUser.getId() + "," +
                        "\"name\":\"" + modifyedUser.getName() + "\"," +
                        "\"userName\":\"" + modifyedUser.getUserName() + "\"," +
                        "\"lastName\":\"" + modifyedUser.getLastName() + "\"," +
                        "\"email\":\"" + modifyedUser.GetEmail() + "\"," +
                        "\"password\":\"" + modifyedUser.getPassword() + "\"," +
                        "\"logged\":" + modifyedUser.isLogged() + "," +
                        "\"rolId\":" + modifyedUser.getRolId() + "}";
        
        System.out.println(url);
        System.out.println(json);
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(url);

            // Afegir el JSON com a cos de la sol·licitud
            StringEntity entity = new StringEntity(json);
            httpPut.setEntity(entity);
            httpPut.setHeader("Content-Type", "application/json");

            // Enviar la sol·licitud
            HttpResponse response = httpClient.execute(httpPut);

            // Processar la resposta
            HttpEntity responseEntity = response.getEntity();
            String responseBody = EntityUtils.toString(responseEntity);

            // Mostrar la resposta del servidor
            if(responseBody.equals("Usuari modificat.")){
                showMessageDialog(null, "Resposta del servidor: "+responseBody);
            }
            else{
                showMessageDialog(null, "Hi ha hagut algun error modificant l'usuari. Resposta del servidor: "+responseBody);
            }
            System.out.println("Resposta del servidor: "+responseBody);

            // Tancar el client HTTP
            httpClient.close();
        } catch (ConnectException e){
            showMessageDialog(null, "Problema amb la connexió: Timeout");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Funció que envia la petició al servidor per eliminar un usuari ja existent amb un objecte User i analitza la resposta
     *
     * @param userToDelete és l'usuari que es vol eliminar.
     */
    public static void deleteUser(User userToDelete){
        
        int idToDelete = userToDelete.getId();
        String url = BASE_URL + "delete/"+idToDelete;
        
        System.out.println(url);
        
        try{
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("DELETE");
            int responseCode = con.getResponseCode();
            System.out.println("Código de respuesta: " + responseCode);
            
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Imprimir la respuesta del servidor
                showMessageDialog(null, "Resposta del servidor: "+response.toString());
            } else {
                showMessageDialog(null, "Error en la petició. Resposta del servidor: "+responseCode);
            }
        } catch (ConnectException e){
            showMessageDialog(null, "Problema amb la connexió: Timeout");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
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
                Response resposta = gson.fromJson(response.toString(), Response.class);
                User user = resposta.getUser();
                user.setSessionID(resposta.getSessionId());
                System.out.println("Resposta: "+response);

                System.out.println("Login amb èxit");
                System.out.println("Session ID: "+user.getSessionId());
                System.out.println("Nom: "+user.getName());
                System.out.println("Username: "+user.getUserName());
                return user;
            } else {
                System.out.println("Login fracassat. Resposta HTTP: " + responseCode);
                showMessageDialog(null, "Problema al intentar entrar. Resposta HTTP del servidor: "+responseCode);
            }
        } catch (ConnectException e){
            showMessageDialog(null, "Problema amb la connexió: Timeout");
            e.printStackTrace();
        } catch (IOException e) {
            showMessageDialog(null, "Problema amb la connexió");
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Envia petició de Logout al servidor i confirma la resposta
     *
     * @param sessionId id de sessió per al logout
     * @param userId id de usuari per al logout
     */
    public static void logout(String sessionId, int userId) {
        try {
            URL url = new URL(BASE_URL + "logout/" + sessionId +"/"+ userId);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("Logout amb èxit");
            } else {
                System.out.println("Logout fracassat. Resposta HTTP: " + responseCode);
            }
        } catch (ConnectException e){
            showMessageDialog(null, "Problema amb la connexió: Timeout");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
