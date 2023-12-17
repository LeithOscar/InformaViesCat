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
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Seccion_Obertes extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    Activity activity_de_origen = Seccion_Obertes.this;
    String temp_userId = " ";

    private IncidentAdapter incidentAdapter;

    private int incidentType_seleccionado = 2;

    //Se inicializa la clase que realiza conexiones
    ServerController serverController = new ServerController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_obertes);

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

        //Gestión de las opciones del spiner para ordenar las incidencias
        String[] sortingOptions = {"Id", "Carretera", "Km", "Descripció", "Data d'inici"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sortingOptions);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sortSpinner = findViewById(R.id.sortSpinner);
        sortSpinner.setAdapter(spinnerAdapter);

        //Por defecto ordenaremos las incidencias por fecha
        int defaultSelectedPosition = 4;
        sortSpinner.setSelection(defaultSelectedPosition);

        //Se obtiene el Intent que inició esta activity
        Intent intent = getIntent();

        //Se extrae del intent el json con la info del usuario
        json_datos_usuario = intent.getStringExtra("json_datos_usuario");

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

            //Extrae el sessionId
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

            //La petición para obtener todas las incidencias recibirá eun userId vacío si se trata de un técnico, pero el userId habitual si es un ciudadano
            temp_userId = " ";
        } else if (rolId == 3) {
            temp_userId = String.valueOf(id);
        }

        //Se busca el floating button para añadir una incidencia
        FloatingActionButton afegirIncidenciaButton = findViewById(R.id.reportarIncidenciaButton);

        //Click listener para el floating button,que tendrá que abrir la actividad para reportar incidencias
        afegirIncidenciaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Reportar.class, json_datos_usuario);
            }
        });

        CriptografiaController criptografiaController = new CriptografiaController();

        //Se obtienen todas las incidencias con su id de usuario
        serverController.getAllIncidencias(String.valueOf(id), String.valueOf(rolId), session_id, new Callback() {

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    //Se guarda el responsebody cifrado
                    String json_recibido_cifrado = response.body().string();
                    Log.d("Debug Vicent", "Obertes Activity: respuesta de get all cifrada: " + json_recibido_cifrado);

                    //Se descifra
                    JSONObject json_recibido_descifrado = criptografiaController.decryptToJSONObject(json_recibido_cifrado);
                    String responseBody = String.valueOf(json_recibido_descifrado);
                    Log.d("Debug Vicent", "Obertes Activity: respuesta de get all descifrada: " + responseBody);

                    //Se crea la array de incidencias
                    List<Incidencia> incidentList = new ArrayList<>();

                    try {
                        JSONObject jsonObject = new JSONObject(responseBody);
                        JSONArray jsonArray = jsonObject.getJSONArray("incidents");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonIncident = jsonArray.getJSONObject(i);

                            if (jsonIncident.getInt("incidenttypeid") == incidentType_seleccionado) {
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
                            }
                        }

                        //Actualizar el RecyclerView después de obtener los datos
                        runOnUiThread(() -> {
                            RecyclerView recyclerView = findViewById(R.id.recyclerView);
                            recyclerView.setLayoutManager(new LinearLayoutManager(Seccion_Obertes.this));

                            //El incidentAdapter recibe el listado de incidencias y el rol del usuario
                            incidentAdapter = new IncidentAdapter(incidentList, rolId, session_id);
                            recyclerView.setAdapter(incidentAdapter);
                            incidentAdapter.sortBy("Data d'inici");
                            incidentAdapter.notifyDataSetChanged();
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                            //Selección actual
                            String selectedOption = sortingOptions[position];
                            runOnUiThread(() -> {
                                // UI update code here
                                incidentAdapter.sortBy(selectedOption);
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                        }
                    });
                } else {
                    // Se muestra un toast de error para el usuario
                    Log.d("Debug Vicent", "Error al descargar las incidencias: " + response.code());
                    runOnUiThread(() -> {
                        Toast.makeText(Seccion_Obertes.this, "Error al descargar las incidencias", Toast.LENGTH_LONG).show();
                    });
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(Seccion_Obertes.this, "Error al descargar las incidencias", Toast.LENGTH_LONG).show();
                    }
                });

                //Hacer un logcat del error de conexión
                Log.d("Debug Vicent", "Error al descargar las incidencias: " + e.getMessage());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Home.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_reportar) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Reportar.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_per_validar) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Per_Validar.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_oberta) {

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