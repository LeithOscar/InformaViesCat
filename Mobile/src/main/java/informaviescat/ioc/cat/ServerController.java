package informaviescat.ioc.cat;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ServerController {

    //IP y puerto
    private static String protocolo = "http://";
    private static String ip = "10.2.77.127";
    private static String puerto = "8080";

    //Nombres de las peticiones y su endpoint
    private static String endpoint_login = "/api/users/login/";
    private static String endpoint_logout = "/api/users/logout/";
    private static String endpoint_register = "/api/users/create/";
    private static String endpoint_crear_incidencia = "/api/incidents/create/";
    private static String endpoint_modificar_usuario = "/api/users/modify/";

    /**
     * Función: Hace login enviando un GET request al endpoint de login con el username y el password del usuario
     * @param username El username
     * @param password El password
     * @param callback Un callback interface para gestionar la respuesta
     *                "onSuccess" se llama si el login ha sido correcto y se recibe respuesta.
     *                "onFailure" se llama si hay un error y el login falla
     */
    public void login(String username, String password, final Callback callback) {

        //Se construye la URL
        String url_peticion = protocolo + ip + ":" + puerto + endpoint_login + username + "/" + password;
        Log.d("Debug Vicent","Petición de login en: " + url_peticion);

        //Se crea la instancia de OkHttp
        OkHttpClient client = new OkHttpClient();

        //Se crea la URL donde se hará la petición
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();

        //Se crea la petición
        Request request = new Request.Builder().url(urlBuilder.build()).get().build();

        //De forma asincrona se hace la petición y la respuesta se gestiona con un Callback
        client.newCall(request).enqueue(callback);
    }

    /**
     * Función: Hace lgout enviando un GET request al endpoint de logout con el username y el password del usuario
     * @param userId El username
     * @param callback Un callback interface para gestionar la respuesta
     *                "onSuccess" se llama si el logut ha sido correcto y se recibe respuesta.
     *                "onFailure" se llama si hay un error y el logout falla
     */
    public void logout(String userId, String session_id, final Callback callback) {

        //Se construye la URL
        String url_peticion = protocolo + ip + ":" + puerto + endpoint_logout + session_id + "/" + userId;
        Log.d("Debug Vicent","Petición de logout en: " + url_peticion);

        //Se crea la instancia de OkHttp
        OkHttpClient client = new OkHttpClient();

        //Se crea la URL donde se hará la petición
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();

        //Se crea la petición
        Request request = new Request.Builder().url(urlBuilder.build()).get().build();

        //De forma asincrona se hace la petición y la respuesta se gestiona con un Callback
        client.newCall(request).enqueue(callback);
    }

    /**
     * Realiza el registro de un nuevo usuario mediante una petición PUT al servidor.
     *
     * @param name      Nombre del usuario.
     * @param lastName  Apellido del usuario.
     * @param userName  Nombre de usuario único.
     * @param email     Dirección de correo electrónico del usuario.
     * @param password  Contraseña del usuario.
     * @param callback  Interfaz de devolución de llamada para gestionar la respuesta.
     *                  "onSuccess" se llama si el registro es exitoso y se recibe respuesta.
     *                  "onFailure" se llama si hay un error y el registro falla.
     */
    public void register(String name, String lastName, String userName, String email, String password, Callback callback) {
        try {

            //Se inicializa cliente
            OkHttpClient client = new OkHttpClient();

            //Se construye la URL
            String url_peticion = protocolo + ip + ":" + puerto + endpoint_register;
            Log.d("Debug Vicent","Petición de alta de usuario en: " + url_peticion);

            //Se constuye el JSON que la petición PUT pasa en el body
            JSONObject userData = new JSONObject();
            userData.put("rolId", "3");
            userData.put("name", name);
            userData.put("userName", userName);
            userData.put("lastName", lastName);
            userData.put("email", email);
            userData.put("password", password);
            userData.put("islogged", false);

            //Crea el body basado en el JSON anterior
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    userData.toString()
            );

            //Crea la petición PUT
            Request request = new Request.Builder()
                    .url(url_peticion)
                    .put(requestBody)
                    .build();

            Log.d("Debug Vicent", "Request hecha: " + request);
            Log.d("Debug Vicent", "Body de la request: " + requestBody);

            //Ejecuta la petición asyncrona
            client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Crea una nueva incidencia mediante una petición PUT al servidor.
     *
     * @param userId      Identificador del usuario que crea la incidencia.
     * @param carretera   Nombre de la carretera asociada a la incidencia.
     * @param km          Kilómetro asociado a la incidencia.
     * @param coordenadas Coordenadas geográficas de la incidencia.
     * @param descripcio  Descripción de la incidencia.
     * @param startDate   Fecha de inicio de la incidencia.
     * @param urgent      Indica si la incidencia es urgente o no.
     * @param callback    Objeto de tipo Callback para gestionar la respuesta de la petición.
     */
    public void crearIncidencia(int userId, String carretera, String km, String coordenadas, String descripcio, String startDate, boolean urgent, Callback callback) {
        try {

            //Se inicializa cliente
            OkHttpClient client = new OkHttpClient();

            //Se construye la URL
            String url_peticion = protocolo + ip + ":" + puerto + endpoint_crear_incidencia;
            Log.d("Debug Vicent","Petición de creación de incidencia en en: " + url_peticion);


            //Se constuye el JSON que la petición PUT pasa en el body
            JSONObject incidenciaData = new JSONObject();
            incidenciaData.put("UserId", userId);
            incidenciaData.put("TecnicId", 26);
            incidenciaData.put("IncidentTypeId",1);
            incidenciaData.put("RoadName", carretera);
            incidenciaData.put("KM", km);
            incidenciaData.put("Geo", coordenadas);
            incidenciaData.put("Description", descripcio);
            incidenciaData.put("StartDate", startDate);
            incidenciaData.put("EndDate", "");
            incidenciaData.put("Urgent",urgent);

            Log.d("Debug Vicent", "Request hecha: " + incidenciaData);

            //Crea el body basado en el JSON anterior
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    incidenciaData.toString()
            );

            //Crea la petición PUT
            Request request = new Request.Builder()
                    .url(url_peticion)
                    .put(requestBody)
                    .build();

            //Ejecuta la petición asyncrona
            client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void modificarUsuario(int id, int rollId, String name, String lastName, String userName, String email, String password, Callback callback) {
        try {

            //Se inicializa cliente
            OkHttpClient client = new OkHttpClient();

            //Se construye la URL
            String url_peticion = protocolo + ip + ":" + puerto + endpoint_modificar_usuario;
            Log.d("Debug Vicent","Petición de edición de usuario en: " + url_peticion);

            //Se constuye el JSON que la petición PUT pasa en el body
            JSONObject userData = new JSONObject();
            userData.put("id", id);
            userData.put("rolId", rollId);
            userData.put("name", name);
            userData.put("userName", userName);
            userData.put("lastName", lastName);
            userData.put("email", email);
            userData.put("password", password);
            userData.put("logged", true);

            //Crea el body basado en el JSON anterior
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    userData.toString()
            );

            //Crea la petición PUT
            Request request = new Request.Builder()
                    .url(url_peticion)
                    .put(requestBody)
                    .build();

            Log.d("Debug Vicent", "Request hecha: " + request);
            Log.d("Debug Vicent", "UserData: " + userData);
            Log.d("Debug Vicent", "Body de la request: " + String.valueOf(requestBody));

            //Ejecuta la petición asyncrona
            client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}













