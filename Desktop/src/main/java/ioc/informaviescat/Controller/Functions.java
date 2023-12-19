
package ioc.informaviescat.Controller;


import ioc.informaviescat.Entities.Incident;
import ioc.informaviescat.Entities.Response;
import ioc.informaviescat.Entities.User;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import static javax.swing.JOptionPane.showMessageDialog;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpResponse;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Pau Cors Bardolet
 */
public class Functions {
    
    private static final String BASE_URL_USERS = "http://10.2.77.128:8080/api/users/";
    private static final String BASE_URL_INCIDENTS = "http://10.2.77.128:8080/api/incidents/";
    private static final String XMLFILE = "data.xml"; 
    
    
    /**
     * Retorna el Response que conté la informació del login i fa login al server
     *
     * @param username nom d'usuari per entrar
     * @param password contrasenya de l'usuari per a entrar
     * @return response amb el resultat del login, user i sessionId
     */
    public static Response login(String username, String password) {
        
        String url = BASE_URL_USERS + "login";
        
        Response resposta = new Response();
        User user = new User();
        
        // Dades de l'usuari
        JSONObject userData = new JSONObject();
        userData.put("username", username);
        userData.put("password", password);
        
        boolean reintentar = true;

        while(reintentar){
            try {
                // Encripta les dades de l'usuari
                String encryptedData = AESEncryptionService.encryptFromJSONObject(userData);

                // Crea la sol·licitud HTTP POST
                HttpPost httpPost = new HttpPost(url);
                StringEntity entity = new StringEntity(encryptedData);
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-Type", "application/json");

                // Crea l'objecte HttpClient i executa la sol·licitud
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse response = httpClient.execute(httpPost);

                    // Obtenir el cos de la resposta
                    HttpEntity responseEntity = response.getEntity();
                    String responseBody = EntityUtils.toString(responseEntity);

                    System.out.println("Resposta: "+responseBody);
                    JSONObject jsonResponse = AESEncryptionService.decryptToJSONObject(responseBody);
                    System.out.println("Resposta desencriptada: "+jsonResponse.toString());
                    

                    if (jsonResponse.has("user")) {
                        // L'usuari existeix
                        JSONObject usuari = jsonResponse.getJSONObject("user");
                        String sessionId = jsonResponse.getString("sessionId");
                        resposta.setSessionId(sessionId);
                        user.setEmail(usuari.getString("email"));
                        user.setId(usuari.getInt("id"));
                        user.setLastName(usuari.getString("lastname"));
                        user.setName(usuari.getString("name"));
                        user.setPassword(usuari.getString("password"));
                        user.setRolId(usuari.getInt("rolid"));
                        user.setSessionId(sessionId);
                        user.setUserName(usuari.getString("username"));
                        user.setLogged(true);
                        
                        resposta.setUser(user);
                        
                    } else {
                        // L'usuari no existeix, obtenim un error
                        showMessageDialog(null, "Error: Usuari no trobat");
                        System.out.println("Error: Usuari no trobat");
                    }
                }
                reintentar = false;
            } catch (ConnectException e){
                System.out.println("#################################");
                System.out.println("Problema amb la connexio: TIMEOUT");
                System.out.println("#################################");
            } catch (Exception e) {
                showMessageDialog(null, "Problema amb la connexió");
                System.out.println("Problema amb la connexio");
                e.printStackTrace();
                reintentar = false;
            }
        }
        
        return resposta;
    }
    
    /**
     * Retorna un string que indica si el login s'ha fet correcta o incorrectament
     *
     * @param userId id del usuari que fa el logout
     * @param sessionId sessionId del usuari que fa el logout
     * @return string amb el resultat
     */
    public static String logout(int userId, String sessionId){
        
        String url = BASE_URL_USERS + "logout";
        String resposta = null;
        
        JSONObject userData = new JSONObject();
        userData.put("userid", userId);
        userData.put("sessionid", sessionId);
        
        boolean reintentar = true;

        while(reintentar){
            try {
                // Encripta les dades de l'usuari
                String encryptedData = AESEncryptionService.encryptFromJSONObject(userData);

                // Crea la sol·licitud HTTP POST
                HttpPost httpPost = new HttpPost(url);
                StringEntity entity = new StringEntity(encryptedData);
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-Type", "application/json");

                // Crea l'objecte HttpClient i executa la sol·licitud
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse response = httpClient.execute(httpPost);

                    // Obtenir el cos de la resposta
                    HttpEntity responseEntity = response.getEntity();
                    String responseBody = EntityUtils.toString(responseEntity);
                    System.out.println("Resposta: "+responseBody);
                    
                }
                reintentar = false;
            } catch (ConnectException e){
                System.out.println("#################################");
                System.out.println("Problema amb la connexio: TIMEOUT");
                System.out.println("#################################");
            } catch (Exception e) {
                showMessageDialog(null, "Problema amb la connexió");
                System.out.println("Problema amb la connexio");
                e.printStackTrace();
                reintentar = false;
            }
        }
        return resposta;
    }
    
    /**
     * Envia petició de creació d'usuari al server i mostra si hi ha hagut èxit amb una finestra
     *
     * @param nouUsuari objecte User amb la info de l'usuari que es vol crear
     * @param sessionId sessionId de l'usuari que fa l'acció de crear
     */
    public static void createUser(User nouUsuari, String sessionId){
        
        String url = BASE_URL_USERS + "create";
        
        JSONObject petition = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("name", nouUsuari.getName());
        user.put("username", nouUsuari.getUserName());
        user.put("lastname", nouUsuari.getLastName());
        user.put("email", nouUsuari.GetEmail());
        user.put("password", nouUsuari.getPassword());
        user.put("rolid", nouUsuari.getRolId());
        
        petition.put("user", user);
        petition.put("sessionid", sessionId);
        
        
        boolean reintentar = true;

        while(reintentar){
            try {
                
                System.out.println("Abans d'encriptar: "+petition);
                // Encripta les dades de l'usuari
                String encryptedData = AESEncryptionService.encryptFromJSONObject(petition);
                System.out.println("Encriptat: "+encryptedData);

                // Crea la sol·licitud HTTP PUT
                HttpPut httpPut = new HttpPut(url);
                StringEntity entity = new StringEntity(encryptedData);
                httpPut.setEntity(entity);
                httpPut.setHeader("Content-Type", "application/json");

                // Crea l'objecte HttpClient i executa la sol·licitud
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse response = httpClient.execute(httpPut);

                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    
                    System.out.println("Resposta: "+statusCode);
                    if(statusCode == 200){
                        showMessageDialog(null, "Usuari creat correctament");
                        System.out.println("Usuari creat correctament");
                    }
                    else if(statusCode == 409){
                        showMessageDialog(null, "Usuari ja existent");
                        System.out.println("Usuari ja existent");
                    }
                    else{
                        showMessageDialog(null, "Error al clrear l'usuari");
                        System.out.println("Error al crera l'usuari");
                    }
                }
                reintentar = false;
            } catch (ConnectException e){
                System.out.println("#################################");
                System.out.println("Problema amb la connexio: TIMEOUT");
                System.out.println("#################################");
            } catch (Exception e) {
                showMessageDialog(null, "Problema amb la connexió");
                System.out.println("Problema amb la connexio");
                e.printStackTrace();
                reintentar = false;
            }
        }
    }
    
    /**
     * Envia petició de modificació d'usuari al server i mostra si hi ha hagut èxit amb una finestra
     *
     * @param userToModify objecte User amb la info de l'usuari que es vol modificar
     * @param sessionId sessionId de l'usuari que fa l'acció de modificar
     */
    public static void modifyUser(User userToModify, String sessionId){
        String url = BASE_URL_USERS + "modify";
        
        JSONObject petition = new JSONObject();
        JSONObject user = new JSONObject();
        user.put("id", userToModify.getId());
        user.put("name", userToModify.getName());
        user.put("username", userToModify.getUserName());
        user.put("lastname", userToModify.getLastName());
        user.put("email", userToModify.GetEmail());
        user.put("password", userToModify.getPassword());
        user.put("rolid", userToModify.getRolId());
    
        petition.put("user", user);
        petition.put("sessionid", sessionId);
        
        
        boolean reintentar = true;

        while(reintentar){
            try {
                
                System.out.println("Abans d'encriptar: "+petition);
                // Encripta les dades de l'usuari
                String encryptedData = AESEncryptionService.encryptFromJSONObject(petition);
                System.out.println("Encriptat: "+encryptedData);

                // Crea la sol·licitud HTTP PUT
                HttpPut httpPut = new HttpPut(url);
                StringEntity entity = new StringEntity(encryptedData);
                httpPut.setEntity(entity);
                httpPut.setHeader("Content-Type", "application/json");

                // Crea l'objecte HttpClient i executa la sol·licitud
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse response = httpClient.execute(httpPut);

                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    
                    System.out.println("Resposta: "+statusCode);
                    if(statusCode == 200){
                        showMessageDialog(null, "Usuari modificat correctament");
                        System.out.println("Usuari modificat correctament");
                    }
                    else if(statusCode == 304){
                        showMessageDialog(null, "Usuari no modificat");
                        System.out.println("Usuari no modificat");
                    }
                    else{
                        showMessageDialog(null, "Error al modificar l'usuari");
                        System.out.println("Error al modificar l'usuari");
                    }
                }
                reintentar = false;
            } catch (ConnectException e){
                System.out.println("#################################");
                System.out.println("Problema amb la connexio: TIMEOUT");
                System.out.println("#################################");
            } catch (Exception e) {
                showMessageDialog(null, "Problema amb la connexió");
                System.out.println("Problema amb la connexio");
                e.printStackTrace();
                reintentar = false;
            }
        }
    }
    
    /**
     * Envia petició d'eliminació d'usuari al server i mostra si hi ha hagut èxit amb una finestra
     *
     * @param userToDelete Objecte User de l'usuari que es vol eliminar
     * @param user Objecte user de l'usuari que fa l'acció d'eliminar
     */
    public static void deleteUser(User userToDelete, User user){
        String url = BASE_URL_USERS + "delete";
        
        JSONObject userData = new JSONObject();
        userData.put("userid", userToDelete.getId());
        userData.put("sessionid", user.getSessionId());
        
        boolean reintentar = true;

        while(reintentar){
            try {
                String encryptedData = AESEncryptionService.encryptFromJSONObject(userData);

                HttpPost httpPost = new HttpPost(url);
                StringEntity entity = new StringEntity(encryptedData);
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-Type", "application/json");

                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse response = httpClient.execute(httpPost);

                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    
                    System.out.println("Resposta: "+statusCode);
                    if(statusCode == 200){
                        System.out.println("Usuari eliminat");
                    }
                    else{
                        showMessageDialog(null, "Error al eliminar l'usuari");
                        System.out.println("Error al eliminar l'usuari");
                    }
                }
                reintentar = false;
            } catch (ConnectException e){
                System.out.println("#################################");
                System.out.println("Problema amb la connexio: TIMEOUT");
                System.out.println("#################################");
            } catch (Exception e) {
                showMessageDialog(null, "Problema amb la connexió");
                System.out.println("Problema amb la connexio");
                e.printStackTrace();
                reintentar = false;
            }
        }
    
        
    }
    
    /**
     * Envia petició al servidor per a obtenir una llista de tots els usuaris
     *
     * @param sessionId sessionId de l'usuari que fa l'acció de obtenir tots els usuaris
     * @return resposta Objecte List de Users amb tota la informació dels usuaris
     */
    public static List<User> getAllUsers(String sessionId){
        
        String url = BASE_URL_USERS + "getall";
        
        JSONObject userData = new JSONObject();
        userData.put("sessionid", sessionId);
        
        boolean reintentar = true;
        
        List<User> resposta = new ArrayList<>();

        while(reintentar){
            try {
                // Encripta les dades
                String encryptedData = AESEncryptionService.encryptFromJSONObject(userData);

                // Crea la sol·licitud HTTP POST
                HttpPost httpPost = new HttpPost(url);
                StringEntity entity = new StringEntity(encryptedData);
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-Type", "application/json");

                // Crea l'objecte HttpClient i executa la sol·licitud
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse response = httpClient.execute(httpPost);

                    // Obtenir el cos de la resposta
                    HttpEntity responseEntity = response.getEntity();
                    String responseBody = EntityUtils.toString(responseEntity);

                    System.out.println("Resposta: "+responseBody);
                    JSONObject jsonResponse = AESEncryptionService.decryptToJSONObject(responseBody);
                    System.out.println("Resposta desencriptada: "+jsonResponse.toString());
                    
                    JSONArray userArray = jsonResponse.getJSONArray("user");
                    List<User> userList = new ArrayList<>();
                    
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject userObject = userArray.getJSONObject(i);

                        User user = new User();
                        user.setRolId(userObject.getInt("rolid"));
                        user.setPassword(userObject.getString("password"));
                        user.setLogged(userObject.getBoolean("islogged"));
                        user.setName(userObject.getString("name"));
                        user.setId(userObject.getInt("id"));
                        user.setEmail(userObject.getString("email"));
                        user.setUserName(userObject.getString("username"));
                        user.setLastName(userObject.getString("lastname"));

                        userList.add(user);
                    }
                    resposta = userList;
                }
                reintentar = false;
            } catch (ConnectException e){
                System.out.println("#################################");
                System.out.println("Problema amb la connexio: TIMEOUT");
                System.out.println("#################################");
            } catch (Exception e) {
                showMessageDialog(null, "Problema amb la connexió");
                System.out.println("Problema amb la connexio");
                e.printStackTrace();
                reintentar = false;
            }
        }
        
        return resposta;
    }
    
    /**
     * Envia petició al servidor per a obtenir una llista de tots els usuaris
     *
     * @param user User de l'usuari que fa l'acció de obtenir tots els usuaris
     * @return resposta Objecte Array String amb tota la informació dels usuaris per a mostrar a la finestra principal
     */
    public static String [][] getUserData(User user){
        List<User> llista = getAllUsers(user.getSessionId());
        String[][] arrayRetorn = new String[llista.size()][6];
        
        for(int i = 0; i < llista.size(); i ++){
            arrayRetorn[i][0]=String.valueOf(llista.get(i).getId());
            arrayRetorn[i][1]=getRole(llista.get(i).getRolId());
            arrayRetorn[i][2]=llista.get(i).getName();
            arrayRetorn[i][3]=llista.get(i).getLastName();
            arrayRetorn[i][4]=llista.get(i).getUserName();
            arrayRetorn[i][5]=getLogat(llista.get(i).logged);
        }
        
        return arrayRetorn;
    }
    
    /**
     * Retorna el string que indica si un usuari està logat o no
     *
     * @param logged booleà que ens indica si està logat o no
     * @return string amb el resultat, logat o no logat
     */
    public static String getLogat(Boolean logged){
        if(logged){
            return "Logat";
        }
        else{
            return "no Logat";
        }
    }
    
    /**
     * Envia petició al servidor per a obtenir una llista de tots els incidents
     *
     * @param user User de l'usuari que fa l'acció de obtenir tots els usuaris
     * @return resposta Objecte List de Incident amb tota la informació dels incidents
     */
    public static List<Incident> getAllIncidents(User user){
        String url = BASE_URL_INCIDENTS + "getall";
        
        JSONObject userData = new JSONObject();
        userData.put("userid", user.getId());
        userData.put("rolid", user.getRolId());
        userData.put("sessionid", user.getSessionId());
        
        boolean reintentar = true;
        
        List<Incident> resposta = new ArrayList<>();
        
        while(reintentar){
            try {
                // Encripta les dades
                String encryptedData = AESEncryptionService.encryptFromJSONObject(userData);

                // Crea la sol·licitud HTTP POST
                HttpPost httpPost = new HttpPost(url);
                StringEntity entity = new StringEntity(encryptedData);
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-Type", "application/json");

                // Crea l'objecte HttpClient i executa la sol·licitud
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse response = httpClient.execute(httpPost);

                    // Obtenir el cos de la resposta
                    HttpEntity responseEntity = response.getEntity();
                    String responseBody = EntityUtils.toString(responseEntity);

                    System.out.println("Resposta: "+responseBody);
                    JSONObject jsonResponse = AESEncryptionService.decryptToJSONObject(responseBody);
                    System.out.println("Resposta desencriptada: "+jsonResponse.toString());
                    
                    JSONArray userArray = jsonResponse.getJSONArray("incidents");
                    List<Incident> incidentList = new ArrayList<>();
                    
                    for (int i = 0; i < userArray.length(); i++) {
                        JSONObject incidentObject = userArray.getJSONObject(i);

                        Incident incident = new Incident();
                        incident.setDescription(incidentObject.getString("description"));
                        incident.setEndDate(incidentObject.getString("enddate"));
                        incident.setGeo(incidentObject.getString("geo"));
                        incident.setId(incidentObject.getInt("id"));
                        incident.setIncidentTypeId(incidentObject.getInt("incidenttypeid"));
                        incident.setKM(incidentObject.getString("km"));
                        incident.setRoadName(incidentObject.getString("raodname"));
                        incident.setStartDate(incidentObject.getString("startdate"));
                        incident.setTecnicId(incidentObject.getInt("tecnicid"));
                        incident.setUrgent(incidentObject.getBoolean("urgent"));
                        incident.setUserId(incidentObject.getInt("userid"));

                        incidentList.add(incident);
                    }
                    resposta = incidentList;
                }
                reintentar = false;
            } catch (ConnectException e){
                System.out.println("#################################");
                System.out.println("Problema amb la connexio: TIMEOUT");
                System.out.println("#################################");
            } catch (Exception e) {
                showMessageDialog(null, "Problema amb la connexió");
                System.out.println("Problema amb la connexio");
                e.printStackTrace();
                reintentar = false;
            }
        }
        
        return resposta;
    }
    
    /**
     * Envia petició de creació d'incident al server i mostra si hi ha hagut èxit amb una finestra
     *
     * @param incident objecte Incident amb la info de l'usuari que es vol crear
     * @param user User de l'usuari que fa l'acció de crear
     */
    public static void createIncident(User user, Incident incident){
        String url = BASE_URL_INCIDENTS + "create";
        
        JSONObject petition = new JSONObject();
        JSONObject newIncident = new JSONObject();
        newIncident.put("userid", user.getId());
        newIncident.put("tecnicid", incident.getTecnicId());
        newIncident.put("incidenttypeid", incident.getIncidentTypeId());
        newIncident.put("raodname", incident.getRoadName());
        newIncident.put("km", incident.getKM());
        newIncident.put("geo", incident.getGeo());
        newIncident.put("description", incident.getDescription());
        newIncident.put("startdate", incident.getStartDate());
        newIncident.put("enddate", incident.getEndDate());
        newIncident.put("urgent", incident.Urgent);
        
        petition.put("incident", newIncident);
        petition.put("sessionid", user.getSessionId());
        
        
        boolean reintentar = true;

        while(reintentar){
            try {
                
                System.out.println("Abans d'encriptar: "+petition);
                // Encripta les dades de l'usuari
                String encryptedData = AESEncryptionService.encryptFromJSONObject(petition);
                System.out.println("Encriptat: "+encryptedData);

                // Crea la sol·licitud HTTP PUT
                HttpPut httpPut = new HttpPut(url);
                StringEntity entity = new StringEntity(encryptedData);
                httpPut.setEntity(entity);
                httpPut.setHeader("Content-Type", "application/json");

                // Crea l'objecte HttpClient i executa la sol·licitud
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse response = httpClient.execute(httpPut);

                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    
                    System.out.println("Resposta: "+statusCode);
                    if(statusCode == 200){
                        showMessageDialog(null, "Incidència creada");
                        System.out.println("Incidència creada");
                    }
                    else{
                        showMessageDialog(null, "Error al crear la incidència");
                        System.out.println("Error al crera la incidència");
                    }
                }
                reintentar = false;
            } catch (ConnectException e){
                System.out.println("#################################");
                System.out.println("Problema amb la connexio: TIMEOUT");
                System.out.println("#################################");
            } catch (Exception e) {
                showMessageDialog(null, "Problema amb la connexió");
                System.out.println("Problema amb la connexio");
                e.printStackTrace();
                reintentar = false;
            }
        }
    }
    
    /**
     * Envia petició de modificació d'incident al server i mostra si hi ha hagut èxit amb una finestra
     *
     * @param incident objecte Incident amb la info de l'incident que es vol modificar
     * @param user User de l'usuari que fa l'acció de modificar
     */
    public static void modifyIncident(User user, Incident incident){
        String url = BASE_URL_INCIDENTS + "modify";
        
        JSONObject petition = new JSONObject();
        JSONObject newIncident = new JSONObject();
        newIncident.put("id", incident.getId());
        newIncident.put("userid", user.getId());
        newIncident.put("tecnicid", incident.getTecnicId());
        newIncident.put("incidenttypeid", incident.getIncidentTypeId());
        newIncident.put("raodname", incident.getRoadName());
        newIncident.put("km", incident.getKM());
        newIncident.put("geo", incident.getGeo());
        newIncident.put("description", incident.getDescription());
        newIncident.put("startdate", incident.getStartDate());
        newIncident.put("enddate", incident.getEndDate());
        newIncident.put("urgent", incident.Urgent);
        
        petition.put("incident", newIncident);
        petition.put("sessionid", user.getSessionId());
        
        
        boolean reintentar = true;

        while(reintentar){
            try {
                
                System.out.println("Abans d'encriptar: "+petition);
                // Encripta les dades de l'usuari
                String encryptedData = AESEncryptionService.encryptFromJSONObject(petition);
                System.out.println("Encriptat: "+encryptedData);

                // Crea la sol·licitud HTTP PUT
                HttpPut httpPut = new HttpPut(url);
                StringEntity entity = new StringEntity(encryptedData);
                httpPut.setEntity(entity);
                httpPut.setHeader("Content-Type", "application/json");

                // Crea l'objecte HttpClient i executa la sol·licitud
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse response = httpClient.execute(httpPut);

                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    
                    System.out.println("Resposta: "+statusCode);
                    if(statusCode == 200){
                        showMessageDialog(null, "Incidència modificada");
                        System.out.println("Incidència modificada");
                    }
                    else{
                        showMessageDialog(null, "Error al modificar la incidència");
                        System.out.println("Error al modificar la incidència");
                    }
                }
                reintentar = false;
            } catch (ConnectException e){
                System.out.println("#################################");
                System.out.println("Problema amb la connexio: TIMEOUT");
                System.out.println("#################################");
            } catch (Exception e) {
                showMessageDialog(null, "Problema amb la connexió");
                System.out.println("Problema amb la connexio");
                e.printStackTrace();
                reintentar = false;
            }
        }
    }
    
    /**
     * Envia petició d'eliminació d'incident al server i mostra si hi ha hagut èxit amb una finestra
     *
     * @param incident Objecte Incident de l'incident que es vol eliminar
     * @param user Objecte user de l'usuari que fa l'acció d'eliminar
     */
    public static void deleteIncident(User user, Incident incident){
        String url = BASE_URL_INCIDENTS + "delete";
        
        JSONObject userData = new JSONObject();
        userData.put("incidentid", incident.getId());
        userData.put("sessionid", user.getSessionId());
        
        boolean reintentar = true;

        while(reintentar){
            try {
                String encryptedData = AESEncryptionService.encryptFromJSONObject(userData);

                HttpPost httpPost = new HttpPost(url);
                StringEntity entity = new StringEntity(encryptedData);
                httpPost.setEntity(entity);
                httpPost.setHeader("Content-Type", "application/json");

                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    HttpResponse response = httpClient.execute(httpPost);

                    StatusLine statusLine = response.getStatusLine();
                    int statusCode = statusLine.getStatusCode();
                    
                    System.out.println("Resposta: "+statusCode);
                    if(statusCode == 200){
                        showMessageDialog(null, "Incidència eliminada");
                        System.out.println("Incidència eliminada");
                    }
                    else{
                        showMessageDialog(null, "Error al eliminar la incidència");
                        System.out.println("Error al eliminar la incidència");
                    }
                }
                reintentar = false;
            } catch (ConnectException e){
                System.out.println("#################################");
                System.out.println("Problema amb la connexio: TIMEOUT");
                System.out.println("#################################");
            } catch (Exception e) {
                showMessageDialog(null, "Problema amb la connexió");
                System.out.println("Problema amb la connexio");
                e.printStackTrace();
                reintentar = false;
            }
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
    
    /**
     * Retorna l'estat de la incidència segons els seu id d'estat
     *
     * @param incident l'incident a analitzar
     * @return result Retorna una String amb el resultat d'estat
     */
    public static String getIncidentType(Incident incident){
        String result = null;
        
        switch (incident.getIncidentTypeId()) {
            case 1:
                result = "En validació";
                break;
            case 2:
                result = "Validada";
                break;
            case 3:
                result = "Assignada";
                break;
            case 4:
                result = "Reparada";
                break;
            case 5:
                result = "Tancada";
                break;
            default:
                break;
        }
        
        return result;
    }
    
    /**
     * Retorna la prioritat de la incidència segons el seu isUrgent
     *
     * @param incident l'incident a analitzar
     * @return result Retorna una String amb el resultat de prioritat
     */
    public static String getUrgency (Incident incident){
        String resultat = null;
        
        if(incident.isUrgent()){
            resultat = "Urgent";
        } else{
            resultat = "No Urgent";
        }
        return resultat;
    }
    
    /**
     * Guarda les dades d'entrada de l'usuari encriptades
     *
     * @param username username del usuari
     * @param password Clau de pass.
     */
    public static void saveCredentials(String username, String password) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc;

            File file = new File(XMLFILE);
            if (file.exists()) {
                doc = dBuilder.parse(file);
            } else {
                doc = dBuilder.newDocument();
                doc.appendChild(doc.createElement("data"));
            }

            Element dataElement = doc.getDocumentElement();

            NodeList usernameNodes = dataElement.getElementsByTagName("username");
            NodeList passwordNodes = dataElement.getElementsByTagName("password");
            if (usernameNodes.getLength() > 0) {
                dataElement.removeChild(usernameNodes.item(0));
            }
            if (passwordNodes.getLength() > 0) {
                dataElement.removeChild(passwordNodes.item(0));
            }

            Element usernameElement = doc.createElement("username");
            usernameElement.appendChild(doc.createTextNode(AESEncryptionService.EncryptFixed(username)));
            Element passwordElement = doc.createElement("password");
            passwordElement.appendChild(doc.createTextNode(AESEncryptionService.EncryptFixed(password)));

            dataElement.appendChild(usernameElement);
            dataElement.appendChild(passwordElement);

            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory.newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(doc);
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(new File(XMLFILE));
            transformer.transform(source, result);
        } catch (ParserConfigurationException | org.xml.sax.SAXException | IOException | javax.xml.transform.TransformerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recupera les dades d'entrada de l'usuari desencriptades
     *
     * @return dades d'entrada per a carregar
     */
    public static String[] restoreCredentials() {
        try {
            File file = new File(XMLFILE);
            if (!file.exists()) {
                return new String[]{"", ""};
            }

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);

            Element dataElement = doc.getDocumentElement();
            NodeList usernameNodes = dataElement.getElementsByTagName("username");
            NodeList passwordNodes = dataElement.getElementsByTagName("password");

            String username = (usernameNodes.getLength() > 0) ? usernameNodes.item(0).getTextContent() : "";
            String password = (passwordNodes.getLength() > 0) ? passwordNodes.item(0).getTextContent() : "";

            return new String[]{AESEncryptionService.DecryptFixed(username), AESEncryptionService.DecryptFixed(password)};
        } catch (ParserConfigurationException | org.xml.sax.SAXException | IOException e) {
            e.printStackTrace();
        }

        return new String[]{"", ""};
    }
    
    /**
     * Crea un arxiu excel a la ruta solicitada amb les dades de les incidències
     *
     * @param incidents informació dels incidents per a guardar a l'informe
     * @param path direció del directori on es guardarà el report.xsls
     */
    public static void reportGenerator(List<User> users, List<Incident> incidents, String path){
         // Crea un nou workbook (fitxer Excel)
        Workbook workbook = new XSSFWorkbook();

        // Crea una nova fulla d'Excel
        Sheet sheet = workbook.createSheet("report");

        int contador = 1;
        Row filaTitols = sheet.createRow(0);
        Cell cellTitol0 = filaTitols.createCell(0);
        cellTitol0.setCellValue("ID INCIDENCIA");
        
        Cell cellTitol1 = filaTitols.createCell(1);
        cellTitol1.setCellValue("NOM CARRETERA");
        
        Cell cellTitol2 = filaTitols.createCell(2);
        cellTitol2.setCellValue("DESCRIPCIO");
        
        Cell cellTitol3 = filaTitols.createCell(3);
        cellTitol3.setCellValue("DATA INICI");
        
        Cell cellTitol4 = filaTitols.createCell(4);
        cellTitol4.setCellValue("DATA FINAL");
        
        Cell cellTitol5 = filaTitols.createCell(5);
        cellTitol5.setCellValue("ESTAT INCIDENT");
        
        Cell cellTitol6 = filaTitols.createCell(6);
        cellTitol6.setCellValue("PUNT KM");
        
        Cell cellTitol7 = filaTitols.createCell(7);
        cellTitol7.setCellValue("GEO");
        
        Cell cellTitol8 = filaTitols.createCell(8);
        cellTitol8.setCellValue("ID TECNIC");
        
        Cell cellTitol9 = filaTitols.createCell(9);
        cellTitol9.setCellValue("ID USUARI");
        
        Cell cellTitol10 = filaTitols.createCell(10);
        cellTitol10.setCellValue("URGENCIA");
        
        for(Incident incident : incidents){
            Row row = sheet.createRow(contador);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(incident.getId());
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(incident.getRoadName());
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(incident.getDescription());
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(incident.getStartDate());
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(incident.getEndDate());
            
            Cell cell5 = row.createCell(5);
            cell5.setCellValue(incident.getIncidentTypeId());
            
            Cell cell6 = row.createCell(6);
            cell6.setCellValue(incident.getKM());
            
            Cell cell7 = row.createCell(7);
            cell7.setCellValue(incident.getGeo());
            
            Cell cell8 = row.createCell(8);
            cell8.setCellValue(incident.getTecnicId());
            
            Cell cell9 = row.createCell(9);
            cell9.setCellValue(incident.getUserId());
            
            Cell cell10 = row.createCell(10);
            cell10.setCellValue(getUrgency(incident));
            
            contador ++;
            
        }

        try {
            // Guarda el fitxer Excel
            FileOutputStream fileOut = new FileOutputStream(path+"/report.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();
            System.out.println("Fitxer Excel creat i dades afegides amb èxit!");
            showMessageDialog(null, "Report creat a: "+path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Ordena una llista de incidències segons la seva id
     *
     * @param llista llista desordenada d'entrada en format array
     * @return  llista ordenada en format array
     */
    public static String[][] sortByID(String[][] llista){
        
        Arrays.sort(llista, new Comparator<String[]>() {
            @Override
            public int compare(String[] fila1, String[] fila2) {
                // Converteix les ID a enters i compara
                int id1 = Integer.parseInt(fila1[0]);
                int id2 = Integer.parseInt(fila2[0]);
                return Integer.compare(id1, id2);
            }
        });
        
        for (String[] fila : llista) {
            System.out.println(Arrays.toString(fila));
        }
        
        return llista;
    }
    
    /**
     * Ordena una llista de incidències segons la seva carretera
     *
     * @param llista llista desordenada d'entrada en format array
     * @return  llista ordenada en format array
     */
    public static String[][] sortByRoad(String[][] llista){
        
        Arrays.sort(llista, new Comparator<String[]>() {
            @Override
            public int compare(String[] fila1, String[] fila2) {
                // Compara les cadenes de la segona columna
                return fila1[1].compareTo(fila2[1]);
            }
        });

        // Imprimeix l'array ordenat
        System.out.println("Array ordenat per la segona columna:");
        for (String[] fila : llista) {
            System.out.println(Arrays.toString(fila));
        }
        
        return llista;
    }
    
    /**
     * Ordena una llista de incidències segons la seva prioritat
     *
     * @param llista llista desordenada d'entrada en format array
     * @return  llista ordenada en format array
     */
    public static String[][] sortByPriority(String[][] llista){
        
        Arrays.sort(llista, new Comparator<String[]>() {
            @Override
            public int compare(String[] fila1, String[] fila2) {
                // Compara les cadenes de la segona columna
                return fila1[3].compareTo(fila2[3]);
            }
        });

        // Imprimeix l'array ordenat
        System.out.println("Array ordenat per la segona columna:");
        for (String[] fila : llista) {
            System.out.println(Arrays.toString(fila));
        }
        
        return llista;
    }
}