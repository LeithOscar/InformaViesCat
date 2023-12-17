/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ServerController {

    //Protocol, IP y puerto
    private static String protocolo = "http://";
    private static String ip = "10.2.77.128";
    private static String puerto = "8080";

    //Nombres de las peticiones y su endpoint
    private static String endpoint_login = "/api/users/login/";
    private static String endpoint_logout = "/api/users/logout/";
    private static String endpoint_register = "/api/users/create/";
    private static String endpoint_crear_incidencia = "/api/incidents/create/";
    private static String endpoint_modificar_usuario = "/api/users/modify/";
    private static String endpoint_eliminar_usuario = "/api/users/delete/";
    private static String endpoint_get_all_incidencias = "/api/incidents/getall/";
    private static String endpoint_modificar_incidencias = "/api/incidents/modify/";
    private static String endpoint_get_total_incidencias = "/api/incidents/getAllCount/";

    //Instancia de criptografía para cifrar y descifrar
    CriptografiaController criptografiaController = new CriptografiaController();

    /**
     * Realiza una solicitud de inicio de sesión al servidor.
     *
     * @param username Nombre de usuario.
     * @param password Contraseña.
     * @param callback Objeto Callback para manejar la respuesta.
     */
    public void login(String username, String password, final Callback callback) {

        //Se crea el JSON
        JSONObject objeto = new JSONObject();

        try {
            //Se le insertan los valores
            objeto.put("username", username);
            objeto.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Debug Vicent", "ServerController: Objeto para petición de login antes de ser cifrado: " + objeto);

        String objeto_cifrado = criptografiaController.encryptFromJSONObject(objeto);

        Log.d("Debug Vicent", "ServerController: Objeto para petición de login cifrado " + objeto_cifrado);

        //Se construye la URL, instancia de OKhttp y el requestbody... se hace la request
        String url_peticion = protocolo + ip + ":" + puerto + endpoint_login;
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objeto_cifrado);
        Request request = new Request.Builder().url(urlBuilder.build()).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }


    /**
     * Realiza una solicitud de cierre de sesión al servidor.
     *
     * @param userId Identificador del usuario.
     * @param session_id Identificador de la sesión.
     * @param callback Objeto Callback para manejar la respuesta.
     */
    public void logout(String userId, String session_id, final Callback callback) {

        //Se crea el JSON
        JSONObject objeto = new JSONObject();

        try {
            //Se le insertan los valores
            objeto.put("userid", userId);
            objeto.put("sessionid", session_id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("Debug Vicent", "ServerController: Objeto para petición de logout antes de ser cifrado: " + objeto);

        String objeto_cifrado = criptografiaController.encryptFromJSONObject(objeto);

        Log.d("Debug Vicent", "ServerController: Objeto para petición de logout cifrado " + objeto_cifrado);

        //Se construye la URL, instancia de OKhttp y el requestbody... se hace la request
        String url_peticion = protocolo + ip + ":" + puerto + endpoint_logout;
        Log.d("Debug Vicent","Petición de logout en: " + url_peticion);
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objeto_cifrado);
        Request request = new Request.Builder().url(urlBuilder.build()).post(requestBody).build();
        client.newCall(request).enqueue(callback);
    }

    /**
     * Realiza una solicitud de registro al servidor con los datos proporcionados.
     *
     * @param name Nombre del usuario a registrar.
     * @param lastName Apellido del usuario a registrar.
     * @param userName Nombre de usuario para la autenticación.
     * @param email Dirección de correo electrónico del usuario.
     * @param password Contraseña asociada al nombre de usuario.
     * @param callback Objeto Callback que manejará la respuesta de la solicitud.
     *
     */
    public void register(String name, String lastName, String userName, String email, String password, Callback callback) {
        try {

            //Se constuye el JSON que la petición PUT pasa en el body
            JSONObject userData = new JSONObject();
            userData.put("rolid", "3");
            userData.put("name", name);
            userData.put("username", userName);
            userData.put("lastname", lastName);
            userData.put("email", email);
            userData.put("password", password);
            JSONObject objeto = new JSONObject();
            objeto.put("sessionid","");
            objeto.put("user",userData);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de register antes de ser cifrado: " + objeto);

            String objeto_cifrado = criptografiaController.encryptFromJSONObject(objeto);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de register cifrado " + objeto_cifrado);

            //Se construye la URL, instancia de OKhttp y el requestbody... se hace la request
            String url_peticion = protocolo + ip + ":" + puerto + endpoint_register;
            Log.d("Debug Vicent","Petición de register en: " + url_peticion);
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), String.valueOf(objeto_cifrado));
            Request request = new Request.Builder().url(urlBuilder.build()).put(requestBody).build();
            client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza una solicitud para crear una incidencia en el servidor con los datos proporcionados.
     *
     * @param userId Identificador del usuario que reporta la incidencia.
     * @param carretera Nombre de la carretera asociada a la incidencia.
     * @param km Kilómetro en el que se encuentra la incidencia.
     * @param coordenadas Coordenadas geográficas de la incidencia.
     * @param descripcio Descripción de la incidencia.
     * @param startDate Fecha de inicio de la incidencia.
     * @param urgent Indica si la incidencia es urgente o no.
     * @param session_id Identificador de la sesión del usuario.
     * @param callback Objeto Callback que manejará la respuesta de la solicitud
     */
    public void crearIncidencia(int userId, String carretera, String km, String coordenadas, String descripcio, String startDate, boolean urgent, String session_id, Callback callback) {
        try {

            //Se constuye el JSON que la petición PUT pasa en el body
            JSONObject incidenciaData = new JSONObject();
            incidenciaData.put("userid", userId);
            incidenciaData.put("tecnicid", 26);
            incidenciaData.put("incidenttypeid",1);
            incidenciaData.put("raodname", carretera);
            incidenciaData.put("km", km);
            incidenciaData.put("geo", coordenadas);
            incidenciaData.put("description", descripcio);
            incidenciaData.put("startdate", startDate);
            incidenciaData.put("enddate", "");
            incidenciaData.put("urgent",urgent);
            JSONObject objeto = new JSONObject();
            objeto.put("sessionid",session_id);
            objeto.put("incident",incidenciaData);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de crear incidencia antes de ser cifrado: " + objeto);

            String objeto_cifrado = criptografiaController.encryptFromJSONObject(objeto);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de crear incidencia cifrado " + objeto_cifrado);

            //Se construye la URL, instancia de OKhttp y el requestbody... se hace la request
            String url_peticion = protocolo + ip + ":" + puerto + endpoint_crear_incidencia;
            Log.d("Debug Vicent","Petición de crear incidencia en: " + url_peticion);
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), String.valueOf(objeto_cifrado));
            Request request = new Request.Builder().url(urlBuilder.build()).put(requestBody).build();
            client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza una solicitud para modificar los datos de un usuario en el servidor.
     *
     * @param id Identificador del usuario a modificar.
     * @param rollId Identificador del rol asignado al usuario.
     * @param name Nuevo nombre del usuario.
     * @param lastName Nuevo apellido del usuario.
     * @param userName Nuevo nombre de usuario.
     * @param email Nueva dirección de correo electrónico del usuario.
     * @param password Nueva contraseña del usuario.
     * @param session_id Identificador de la sesión del usuario.
     * @param callback Objeto Callback que manejará la respuesta de la solicitud.
     */
    public void modificarUsuario(int id, int rollId, String name, String lastName, String userName, String email, String password, String session_id, Callback callback) {
        try {

            //Se constuye el JSON que la petición PUT pasa en el body
            JSONObject userData = new JSONObject();
            userData.put("id", id);
            userData.put("parentid", 1);
            userData.put("name", name);
            userData.put("username", userName);
            userData.put("lastname", lastName);
            userData.put("email", email);
            userData.put("password", password);
            userData.put("islogged", true);
            userData.put("rolid", rollId);
            JSONObject objeto = new JSONObject();
            objeto.put("sessionid", session_id);
            objeto.put("user", userData);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de modify user antes de ser cifrado: " + objeto);

            String objeto_cifrado = criptografiaController.encryptFromJSONObject(objeto);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de modify user cifrado " + objeto_cifrado);

            //Se construye la URL, instancia de OKhttp y el requestbody... se hace la request
            String url_peticion = protocolo + ip + ":" + puerto + endpoint_modificar_usuario;
            Log.d("Debug Vicent", "Petición de modify en: " + url_peticion);
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objeto_cifrado);
            Request request = new Request.Builder().url(urlBuilder.build()).put(requestBody).build();
            client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Debug Vicent", "Exception occurred: " + e.getMessage());
        }
    }

    /**
     * Realiza una solicitud para eliminar un usuario en el servidor.
     *
     * @param id Identificador del usuario a eliminar.
     * @param session_id Identificador de la sesión del usuario que realiza la acción.
     * @param callback Objeto Callback que manejará la respuesta de la solicitud.
     */
    public void eliminarUsuario(int id, String session_id, Callback callback) {
        try {

            JSONObject objeto = new JSONObject();
            objeto.put("sessionid", session_id);
            objeto.put("userid", id);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de delete user antes de ser cifrado: " + objeto);

            String objeto_cifrado = criptografiaController.encryptFromJSONObject(objeto);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de delete user cifrado " + objeto_cifrado);

            //Se construye la URL, instancia de OKhttp y el requestbody... se hace la request
            String url_peticion = protocolo + ip + ":" + puerto + endpoint_eliminar_usuario;
            Log.d("Debug Vicent", "Petición de delete user en: " + url_peticion);
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), String.valueOf(objeto_cifrado));
            Request request = new Request.Builder().url(urlBuilder.build()).delete(requestBody).build();
            client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza una solicitud para obtener todas las incidencias asociadas a un usuario en el servidor.
     *
     * @param id Identificador del usuario.
     * @param rollId Identificador del rol del usuario.
     * @param session_id Identificador de la sesión del usuario.
     * @param callback Objeto Callback que manejará la respuesta de la solicitud.
     */
    public void getAllIncidencias(String id, String rollId, String session_id, final Callback callback) {

        try {

        JSONObject objeto = new JSONObject();
        objeto.put("userid", id);
        objeto.put("rolid", rollId);
        objeto.put("sessionid", session_id);

        Log.d("Debug Vicent", "ServerController: Objeto para petición de get all incidencias antes de ser cifrado: " + objeto);

        String objeto_cifrado = criptografiaController.encryptFromJSONObject(objeto);

        Log.d("Debug Vicent", "ServerController: Objeto para petición de get all incidencias cifrado " + objeto_cifrado);


        //Se construye la URL, instancia de OKhttp y el requestbody... se hace la request
        String url_peticion = protocolo + ip + ":" + puerto + endpoint_get_all_incidencias;
        Log.d("Debug Vicent", "Petición de get all incidencias en: " + url_peticion);
        OkHttpClient client = new OkHttpClient();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objeto_cifrado);
        Request request = new Request.Builder().url(urlBuilder.build()).post(requestBody).build();
        client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Realiza una solicitud para modificar una incidencia en el servidor con los datos proporcionados.
     *
     * @param id Identificador de la incidencia a modificar.
     * @param userId Identificador del usuario asociado a la incidencia.
     * @param tecnicId Identificador del técnico asociado a la incidencia.
     * @param incidentType Identificador del tipo de incidencia.
     * @param carretera Nombre de la carretera asociada a la incidencia.
     * @param km Kilómetro en el que se encuentra la incidencia.
     * @param geo Coordenadas geográficas de la incidencia.
     * @param description Descripción de la incidencia.
     * @param startDate Fecha de inicio de la incidencia.
     * @param endDate Fecha de finalización de la incidencia.
     * @param urgent Indica si la incidencia es urgente o no.
     * @param session_id Identificador de la sesión del usuario.
     * @param callback Objeto Callback que manejará la respuesta de la solicitud.
     */
    public void modificarIncidencia(int id, int userId, int tecnicId, int incidentType, String carretera, String km, String geo, String description, String startDate, String endDate, boolean urgent, String session_id, final Callback callback) {

        try {

            //Se constuye el JSON que la petición PUT pasa en el body
            JSONObject incidenciaData = new JSONObject();
            incidenciaData.put("id",id);
            incidenciaData.put("userid", userId);
            incidenciaData.put("tecnicid", tecnicId);
            incidenciaData.put("incidenttypeid",incidentType);
            incidenciaData.put("raodname", carretera);
            incidenciaData.put("km", km);
            incidenciaData.put("geo", geo);
            incidenciaData.put("description", description);
            incidenciaData.put("startdate", startDate);
            incidenciaData.put("enddate", endDate);
            incidenciaData.put("urgent",urgent);
            JSONObject objeto = new JSONObject();
            objeto.put("sessionid",session_id);
            objeto.put("incident",incidenciaData);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de modificar incidencia antes de ser cifrado: " + objeto);

            String objeto_cifrado = criptografiaController.encryptFromJSONObject(objeto);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de modificar incidencia cifrado " + objeto_cifrado);

            //Se construye la URL, instancia de OKhttp y el requestbody... se hace la request
            String url_peticion = protocolo + ip + ":" + puerto + endpoint_modificar_incidencias;
            Log.d("Debug Vicent","Petición de modificar incidencia en: " + url_peticion);
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), String.valueOf(objeto_cifrado));
            Request request = new Request.Builder().url(urlBuilder.build()).put(requestBody).build();
            client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Realiza una solicitud para obtener el total de incidencias en el servidor.
     *
     * @param session_id Identificador de la sesión del usuario.
     * @param callback Objeto Callback que manejará la respuesta de la solicitud.
     */
    public void getTotalIncidencias(String session_id, final Callback callback) {

        try {

            JSONObject objeto = new JSONObject();
            objeto.put("sessionid", session_id);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de get total incidencias antes de ser cifrado: " + objeto);

            String objeto_cifrado = criptografiaController.encryptFromJSONObject(objeto);

            Log.d("Debug Vicent", "ServerController: Objeto para petición de get total incidencias cifrado " + objeto_cifrado);

            //Se construye la URL, instancia de OKhttp y el requestbody... se hace la request
            String url_peticion = protocolo + ip + ":" + puerto + endpoint_get_total_incidencias;
            Log.d("Debug Vicent", "Petición de get all incidencias en: " + url_peticion);
            OkHttpClient client = new OkHttpClient();
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url_peticion).newBuilder();
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), objeto_cifrado);
            Request request = new Request.Builder().url(urlBuilder.build()).post(requestBody).build();
            client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}













