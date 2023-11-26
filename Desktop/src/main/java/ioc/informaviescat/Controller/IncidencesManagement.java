
package ioc.informaviescat.Controller;

import com.google.gson.Gson;
import ioc.informaviescat.Entities.Incident;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static javax.swing.JOptionPane.showMessageDialog;


/**
 *
 * @author Pau Cors Bardolet
 */
public class IncidencesManagement {
    
    private static final String BASE_URL = "http://10.2.77.127:8080/api/incidents/";
    
    /**
     * Funció per a obtenir totes les incidències del servidor
     *
     * @return incidents Retorna una List de objectes incidence obtingudes del servidor
     */
    public static List<Incident> getIncidenceList(){
        List<Incident> incidents = new ArrayList<>();
        String urlPeticio = BASE_URL + "getall/UserId";
        
        System.out.println("Ha arribat aquí 2.2.1");
        
        try {
            // Crea una connexió HTTP amb la URL
            URL url = new URL(urlPeticio);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Obtenir la resposta del servidor
            int responseCode = conn.getResponseCode();
            
            System.out.println("Ha arribat aquí 2.2.2");
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Llegeix el JSON de la resposta i parseja'l utilitzant Gson
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                Gson gson = new Gson();
                Incident[] incidentArray = gson.fromJson(response.toString(), Incident[].class);
                incidents = Arrays.asList(incidentArray);
                System.out.println("Ha arribat aquí 2.2.3");
            } else {
                showMessageDialog(null, "Problema amb la connexió: "+ responseCode);
            }
        }catch (ConnectException e){
            showMessageDialog(null, "Problema amb la connexió: Timeout");
            e.printStackTrace();
        } catch (IOException e) {
            showMessageDialog(null, "No s'ha pogut obtenir la llista d'incidències");
            e.printStackTrace();
        }
        
        return incidents;
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
     * Donat un objecte Incident, envia petició de modificació al servidor
     *
     * @param incident l'incident a modificar
     */
    public static void modifyIncident (Incident incident){
        String urlPeticio = BASE_URL + "modify";
        try {
            // Crear una connexió HTTP a la URL
            URL url = new URL(urlPeticio);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configurar la connexió per a enviar una petició PUT
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Crear l'objecte JSON que s'enviarà com a cos de la petició
            String jsonInputString = "{"
                    + "\"id\":" + incident.getId() + ","
                    + "\"UserId\":" + incident.getUserId() + ","
                    + "\"TecnicId\":" + incident.getTecnicId() + ","
                    + "\"IncidentTypeId\":" + incident.getIncidentTypeId() + ","
                    + "\"RoadName\":\"" + incident.getRoadName() + "\","
                    + "\"KM\":\"" + incident.getKM() + "\","
                    + "\"Geo\":\"" + incident.getGeo() + "\","
                    + "\"Description\":\"" + incident.getDescription() + "\","
                    + "\"StartDate\":\"" + incident.getStartDate() + "\","
                    + "\"EndDate\":\"" + incident.getEndDate() + "\","
                    + "\"Urgent\":" + incident.isUrgent()
                    + "}";

            // Enviar les dades JSON com a cos de la petició
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(jsonInputString);
            }

            // Obtindre la resposta del servidor
            int responseCode = connection.getResponseCode();

            // Llegir la resposta del servidor
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }

                // Imprimir la resposta del servidor
                System.out.println("Resposta del servidor: " + response.toString());
            }

            // Tancar la connexió
            connection.disconnect();

            if (responseCode == 200){
                showMessageDialog(null, "Incidència modificada");
            }
            else{
                showMessageDialog(null, "Hi ha hagut algun error. Resposta del servidor: "+responseCode);
            }
        }catch (ConnectException e){
            showMessageDialog(null, "Problema amb la connexió: Timeout");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            showMessageDialog(null, "No s'ha pogut modificar la incidència");
        }
    }
    
    /**
     * Donat un objecte Incident, envia petició de creació al servidor
     *
     * @param incident l'incident a crear
     */
    public static void createIncident (Incident incident){
        String urlPeticio = BASE_URL + "create";
        
        try {
            // Crear una connexió HTTP a la URL
            URL url = new URL(urlPeticio);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Configurar la connexió per a enviar una petició PUT
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            // Crear l'objecte JSON que s'enviarà com a cos de la petició
            String jsonInputString = "{"
                    + "\"UserId\":" + incident.getUserId() + ","
                    + "\"TecnicId\":" + incident.getTecnicId() + ","
                    + "\"IncidentTypeId\":" + incident.getIncidentTypeId() + ","
                    + "\"RoadName\":\"" + incident.getRoadName() + "\","
                    + "\"KM\":\"" + incident.getKM() + "\","
                    + "\"Geo\":\"" + incident.getGeo() + "\","
                    + "\"Description\":\"" + incident.getDescription() + "\","
                    + "\"StartDate\":\"" + incident.getStartDate() + "\","
                    + "\"EndDate\":\"" + incident.getEndDate() + "\","
                    + "\"Urgent\":" + incident.isUrgent()
                    + "}";

            // Enviar les dades JSON com a cos de la petició
            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                outputStream.writeBytes(jsonInputString);
            }

            // Obtindre la resposta del servidor
            int responseCode = connection.getResponseCode();

            // Llegir la resposta del servidor
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }

                // Imprimir la resposta del servidor
                System.out.println("Resposta del servidor: " + response.toString());
            }

            // Tancar la connexió
            connection.disconnect();

            if (responseCode == 200) {
                showMessageDialog(null, "Incidència creada");
            } else {
                showMessageDialog(null, "Hi ha hagut algun error. Resposta del servidor: "+responseCode);
            }
        }catch (ConnectException e){
            showMessageDialog(null, "Problema amb la connexió: Timeout");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            showMessageDialog(null, "No s'ha pogut crear la incidència");
        }
    }
}
