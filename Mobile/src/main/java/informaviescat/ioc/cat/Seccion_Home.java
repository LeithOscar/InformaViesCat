/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Seccion_Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    //Variables del usuario
    private int id;
    private String name;
    private String userName;
    private String lastName;
    private String email;
    private String password;
    private boolean logged;
    private int rolId;
    private String session_id;
    private String json_datos_usuario;
    Activity activity_de_origen = Seccion_Home.this;
    private TextView incidenciesTotalsTextView;
    private int incidentType1Count;
    private int incidentType2Count;
    private int incidentType3Count;
    private int incidentType4Count;
    private int incidentType5Count;
    private TextView statsTipo1Text;
    private TextView statsTipo2Text;
    private TextView statsTipo3Text;
    private TextView statsTipo4Text;
    private TextView statsTipo5Text;
    private TextView statsTipo6Text;

    //Se inicializa la clase que realiza conexiones
    ServerController serverController = new ServerController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Deshabilitar la animacion al abrir la actividad
        overridePendingTransition(0, 0);

        //Menú NavigationDrawer (el lateral) más la cabecera
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        Drawable drawable = toolbar.getNavigationIcon();
        toolbar.setNavigationIcon(drawable);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.obre_menu, R.string.tanca_menu);
        int color = ContextCompat.getColor(this, R.color.white);
        toggle.getDrawerArrowDrawable().setColor(color);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Se obtiene el Intent que inició esta activity
        Intent intent = getIntent();

        //Se extrae del intent el json con la info del usuario
        json_datos_usuario = intent.getStringExtra("json_datos_usuario");
        Log.d("Debug Vicent","JSON de usuario: " +  json_datos_usuario);


        //Se parsean los datos del usuario
        try {
            JSONObject json = new JSONObject(json_datos_usuario);

            //Extrae primero el usuario
            JSONObject userObject = json.getJSONObject("user");

            //Extrae todos los valores dentro de usuario
            id = userObject.getInt("id");
            name = userObject.getString("name");
            userName = userObject.getString("username");
            lastName = userObject.getString("lastname");
            email = userObject.getString("email");
            password = userObject.getString("password");
            rolId = userObject.getInt("rolid");

            //Extrae el userId
            session_id = json.getString("sessionId");

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        //Bloque para hacer invisibles algunas secciones del menú si el usuario es técnico
            Menu navMenu = navigationView.getMenu();
            MenuItem perValidarItem = navMenu.findItem(R.id.nav_per_validar);
            MenuItem resoltesItem = navMenu.findItem(R.id.nav_resolta);
            MenuItem tancadesItem = navMenu.findItem(R.id.nav_tancada);

            if (rolId == 2) {

                //Si el rol es 2 (técnico) se deshabilitan las secciones que son solo para admins, o ciudadanos.
                Log.d("Debug Vicent","Rol es: " + rolId + " (técnico) .Se esconden las secciones per validar, resolta y tancada.");
                perValidarItem.setVisible(false);
                resoltesItem.setVisible(false);
                tancadesItem.setVisible(false);
            }

        //TextView para el saludo
        TextView saludoTextView = findViewById(R.id.saludoTextView);

        //Se captura la hora actual
        Calendar calendar = Calendar.getInstance();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

        //Se pone el saludo en función de la hora actual
        String greetingMessage;
        if (hourOfDay >= 0 && hourOfDay < 12) {
            greetingMessage = "Bon dia " + name;
        } else if (hourOfDay >= 12 && hourOfDay < 18) {
            greetingMessage = "Bona tarda " + name;
        } else {
            greetingMessage = "Bona nit " + name;
        }
        saludoTextView.setText(greetingMessage);

        //Se gestiona la imagen que vincula al servicio real de incidencias viarias
        ImageView afectationsImageView = findViewById(R.id.afectationsImageView);
        afectationsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://cit.transit.gencat.cat/cit/AppJava/views/incidents.xhtml";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                try {
                    startActivity(browserIntent);
                } catch (ActivityNotFoundException e) {
                    Log.e("Error", "No app can handle this Intent");
                }
            }
        });

        incidenciesTotalsTextView = findViewById(R.id.incidencies_totals);

        serverController.getTotalIncidencias(session_id, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("Debug Vicent", "Fallo al realizar el getTotal");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                try {

                    //Instancia del criptografía controller
                    CriptografiaController criptografiaController = new CriptografiaController();

                    //Se guarda el responsebody cifrado
                    String json_recibido_cifrado = response.body().string();
                    Log.d("Debug Vicent", "Home Activity: respuesta de count all cifrada: " + json_recibido_cifrado);

                    //Se descifra
                    JSONObject json_recibido_descifrado = criptografiaController.decryptToJSONObject(json_recibido_cifrado);
                    String responseBody = String.valueOf(json_recibido_descifrado);

                    JSONObject jsonObject = new JSONObject(responseBody);
                    Log.d("Debug Vicent", "Home Activity Activity: respuesta de count all descifrada: " + responseBody);

                    //Se extra el valor entero de incidentscount
                    int incidentsCount = jsonObject.getInt("incidentscount");

                    Log.d("Debug Vicent", "Home: Hay en total " + incidentsCount + " incidencias.");

                    runOnUiThread(() -> {
                        incidenciesTotalsTextView.setText("Gràcies! Hem resolt " + incidentsCount + " incidències a través de les incidències notificades per ciutadans en aquesta app.");
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
           }
       });

        //Se inicializan los contadores
        statsTipo1Text = findViewById(R.id.statsTipo1Text);
        statsTipo2Text = findViewById(R.id.statsTipo2Text);
        statsTipo3Text = findViewById(R.id.statsTipo3Text);
        statsTipo4Text = findViewById(R.id.statsTipo4Text);
        statsTipo5Text = findViewById(R.id.statsTipo5Text);
        statsTipo6Text = findViewById(R.id.statsTipo6Text);

        CriptografiaController criptografiaController = new CriptografiaController();


        //Se obtienen todas las incidencias con su id de usuario
        serverController.getAllIncidencias(String.valueOf(id), String.valueOf(rolId), session_id,  new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                if (response.isSuccessful()) {

                    //Se guarda el responsebody cifrado
                    String json_recibido_cifrado = response.body().string();
                    Log.d("Debug Vicent", "Home Activity: respuesta de get all cifrada: " + json_recibido_cifrado);

                    //Se descifra
                    JSONObject json_recibido_descifrado = criptografiaController.decryptToJSONObject(json_recibido_cifrado);
                    String responseBody = String.valueOf(json_recibido_descifrado);
                    Log.d("Debug Vicent", "Home Activity: respuesta de get all descifrada: " + responseBody);

                    //Se crea la array de incidencias
                    List<Incidencia> incidentList = new ArrayList<>();

                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray jsonArray = jsonObject.getJSONArray("incidents");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonIncident = jsonArray.getJSONObject(i);
                                // Se parsean todos los campos de cada incidencia
                                Incidencia incident = new Incidencia();
                                incident.setId(jsonIncident.getInt("id"));
                                incident.setUserId(jsonIncident.getInt("userid"));
                                incident.setTecnicId(jsonIncident.getInt("tecnicid"));
                                incident.setIncidentTypeId(jsonIncident.getInt("incidenttypeid"));
                                incident.setRoadName(jsonIncident.getString("raodname"));
                                incident.setKm(jsonIncident.getString("km"));
                                incident.setGeo(jsonIncident.getString("geo"));
                                incident.setDescription(jsonIncident.getString("description"));
                                incident.setStartDate(jsonIncident.getString("startdate"));
                                incident.setEndDate(jsonIncident.getString("enddate"));
                                incident.setUrgent(jsonIncident.getBoolean("urgent"));

                                //Se añade la incidencia a la array de incidencias
                                incidentList.add(incident);

                            if (incident.getIncidentTypeId() == 1) {
                                incidentType1Count++;
                            }

                            if (incident.getIncidentTypeId() == 2) {
                                incidentType2Count++;
                            }

                            if (incident.getIncidentTypeId() == 3) {
                                incidentType3Count++;
                            }

                            if (incident.getIncidentTypeId() == 4) {
                                incidentType4Count++;
                            }

                            if (incident.getIncidentTypeId() == 5) {
                                incidentType5Count++;
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("Debug Vicent", "Hay un conteo de " + incidentType1Count + " " + incidentType2Count + " " + incidentType3Count + " " + incidentType4Count + " " + incidentType5Count);
                            setContadores(incidentType1Count, incidentType2Count, incidentType3Count, incidentType4Count, incidentType5Count);
                        }
                    });
                }}
            public void onFailure(@NonNull Call call, @NonNull IOException e) {}
        });
    }

    /**
     * Establece los contadores de incidencias en las vistas correspondientes.
     *
     * @param incidentType1Count Contador de incidencias de Tipo 1.
     * @param incidentType2Count Contador de incidencias de Tipo 2.
     * @param incidentType3Count Contador de incidencias de Tipo 3.
     * @param incidentType4Count Contador de incidencias de Tipo 4.
     * @param incidentType5Count Contador de incidencias de Tipo 5.
     */
    public void setContadores (int incidentType1Count, int incidentType2Count, int incidentType3Count, int incidentType4Count, int incidentType5Count) {
        statsTipo1Text.setText(incidentType1Count + "\nper validar");
        statsTipo2Text.setText(incidentType2Count + "\nobertes");
        statsTipo3Text.setText(incidentType3Count + "\nen progrés");
        statsTipo4Text.setText(incidentType4Count + "\nresoltes");
        statsTipo5Text.setText(incidentType5Count + "\ntancades");
        statsTipo6Text.setText(incidentType1Count + incidentType2Count + incidentType3Count + incidentType4Count + incidentType5Count + "\nen total");
    }

    /**
     * Este método se llama cuando un elemento de la barra de navegación lateral es seleccionado.
     *
     * @param item El elemento de la barra de navegación que ha sido seleccionado.
     * @return `true` si el evento ha sido manejado con éxito y `false` de lo contrario.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {

        } else if (item.getItemId() == R.id.nav_reportar) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Reportar.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_per_validar) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Per_Validar.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_oberta) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Obertes.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_en_progress) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_En_Progress.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_resolta) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Resoltes.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_tancada) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Tancades.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_usuari) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Usuario.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_logout) {
            Utility.logout(this, id, session_id);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Abre una nueva actividad pasando datos del usuario en formato JSON.
     *
     * @param activity           La actividad actual desde la que se lanza la nueva actividad.
     * @param destinationActivity La clase de la actividad de destino.
     * @param jsonData           Los datos del usuario en formato JSON que se pasarán a la nueva actividad.
     */
    public static void abrirActividadPasandoJSONUsuario(Activity activity, Class<?> destinationActivity, String jsonData) {
        //Crea un intent de la actual Activity a la de destino
        Intent intent = new Intent(activity, destinationActivity);

        //Pasa los datos del actual usuario
        intent.putExtra("json_datos_usuario", jsonData);

        //Arranca el intent
        activity.startActivity(intent);
    }

    //Si se hace click en "atrás" volveríamos a la pantalla de bienvenida, es decir, es necesario un logout
    @Override
    public void onBackPressed() {
        Utility.logout(this, id, session_id);
    }

}