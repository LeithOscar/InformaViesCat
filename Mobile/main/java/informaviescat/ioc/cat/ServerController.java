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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

public class ServerController {

    //IP y puerto
    private static String protocolo = "http://";
    private static String ip = "10.2.77.127";
    private static String puerto = "8080";

    //Nombres de las peticiones y su endpoint
    private static String endpoint_login = "/api/users/login/";
    private static String endpoint_logout = "/api/users/logout/";

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
        Log.d("Debug IOC","Petición de login en: " + url_peticion);

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
     * @param username El username
     * @param password El password
     * @param callback Un callback interface para gestionar la respuesta
     *                "onSuccess" se llama si el logut ha sido correcto y se recibe respuesta.
     *                "onFailure" se llama si hay un error y el logout falla
     */
    public void logout(String username, String password, final Callback callback) {

        //Se construye la URL
        String url_peticion = protocolo + ip + ":" + puerto + endpoint_login + username + "/" + password;
        Log.d("Debug IOC","Petición de logout en: " + url_peticion);

        //Se crea la instancia de OkHttp
        OkHttpClient client = new OkHttpClient();

        //Se crea la URL donde se hará la petición
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();

        //Se crea la petición
        Request request = new Request.Builder().url(urlBuilder.build()).get().build();

        //De forma asincrona se hace la petición y la respuesta se gestiona con un Callback
        client.newCall(request).enqueue(callback);
    }


}













