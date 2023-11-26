/**
 * @author Vicent Gil Esteve
 */

package informaviescat.ioc.cat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.Manifest;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Seccion_Reportar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private DrawerLayout drawerLayout;
    private Button buttonLogout;

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
    Activity activity_de_origen = Seccion_Reportar.this;
    private FusedLocationProviderClient fusedLocationClient;
    private GoogleMap googleMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private String latitud;
    private String longitud;

    private TextInputEditText carreteraEditText;
    private TextInputEditText kmEditText;
    private TextInputEditText descripcioEditText;
    private RadioGroup urgenteRadioGroup;
    private CheckBox confirmacionCheckBox;
    private Button reportarButton;
    private String carretera;
    private String km;
    private String descripcio;
    private String coordenadas;
    private boolean esUrgente;
    private String startDate;


    //Se inicializa la clase que realiza conexiones
    ServerController serverController = new ServerController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportar);

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

        //Se parsean los datos del usuario
        try {
            JSONObject json = new JSONObject(json_datos_usuario);

            //Extrae primero el usuario
            JSONObject userObject = json.getJSONObject("user");

            //Extrae todos los valores dentro de usuario
            id = userObject.getInt("id");
            name = userObject.getString("name");
            userName = userObject.getString("userName");
            lastName = userObject.getString("lastName");
            email = userObject.getString("email");
            password = userObject.getString("password");
            rolId = userObject.getInt("rolId");

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

        //Se inicializa el cliente de ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //Se obtiene el fragmnent del layout donde va el mapa
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Se inicializan las vistas de los campos de texto (Carretera, km, etc.)
        carreteraEditText = findViewById(R.id.carreteraTextView);
        kmEditText = findViewById(R.id.kmTextView);
        descripcioEditText = findViewById(R.id.descriptionTextView);
        urgenteRadioGroup = findViewById(R.id.urgenteRadioGroup);
        confirmacionCheckBox = findViewById(R.id.confirmacio);


        // Verificar la confirmación del checkbox y habilitar botón si se ha marcado
        boolean confirmacion = confirmacionCheckBox.isChecked();
        reportarButton = findViewById(R.id.reportar_incidencia_button);
        reportarButton.setEnabled(false);

        //Listener del checkbox para habilitar o deshabilitar el botón
        confirmacionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {


                descripcio = descripcioEditText.getText().toString();

                if (descripcio.length()>0) {
                    //Habilitar el botón para reportar incidencia
                    reportarButton.setEnabled(isChecked);
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Muestra el Toast en el hilo principal
                            Toast.makeText(Seccion_Reportar.this, "Has d'inclourer una descripció.", Toast.LENGTH_LONG).show();
                        }
                    });
                }



            }
        });

        //Una vez habilitado, listener del botón para reportar una incidencia
        reportarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Se obtienen los valores de los campos
                carretera = carreteraEditText.getText().toString();
                km = kmEditText.getText().toString();
                descripcio = descripcioEditText.getText().toString();
                coordenadas = "(" +  latitud + "," + longitud + ")";

                // Obtener valor de la variable booleana urgente
                int selectedRadioButtonId = urgenteRadioGroup.getCheckedRadioButtonId();
                esUrgente = selectedRadioButtonId == R.id.urgentRadioButton;

                Log.d("Debug Vicent", "Incidencia capturada: Coordenadas (" +  coordenadas +  ") , en carretera " +  carretera + " , en el km " + km + " , con la descripción: \"" + descripcio + ". Con urgencia:" + esUrgente);

                LocalDate today = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    today = LocalDate.now();
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                }


                //Proceder con el report d'incidencia
                serverController.crearIncidencia(id, carretera, km, coordenadas, descripcio, startDate, esUrgente, new Callback() {

                    @Override
                    public void onFailure(@NonNull Call call, @NonNull IOException e) {
                        //Toast con error de conexión
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast de error de conexión para el usuario
                                Toast.makeText(Seccion_Reportar.this, "Error de conexión", Toast.LENGTH_LONG).show();
                            }
                        });
                        Log.d("Debug Vicent", "Error en la conexión: " + e.getMessage());
                    }

                    @Override
                    public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Toast de incidencia notificada para el usuario
                                Toast.makeText(Seccion_Reportar.this, "Incidència notificada correctament.", Toast.LENGTH_LONG).show();
                            }
                        });

                        //Y vuelve a la actividad "home"
                        Intent intent = new Intent(Seccion_Reportar.this,Seccion_Home.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap map) {

        googleMap = map;

        // Se comprueban los permisos de ubicación
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // Solicitar permisos si no están concedidos ya
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            return;
        }

        // Se habilita "Mi ubicación" en el mapa
        googleMap.setMyLocationEnabled(true);

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
            if (location != null) {

                // Se obtiene la última ubicación conocida (currentLocation con getLatitude y getLongitude)
                LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

                latitud = String.valueOf(location.getLatitude());
                longitud= String.valueOf(location.getLongitude());

                // Se centra el mapa en esa ubicación con moveCamera
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 17.0f));

                // Se actualiza el textView con la latitud y longitud detectadas:
                actualizarTextViewConCoordenadas();

            } else {
                Toast.makeText(this, "No se ha podido capturar la ubicación", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up camera move listener
        googleMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                actualizarTextViewConCoordenadas();
            }
        });
    };

    /**
     * Actualiza un TextView con las coordenadas geográficas actuales.
     * Este método obtiene la ubicación actual del dispositivo y actualiza un TextView con las
     * coordenadas geográficas (latitud y longitud)
     */
    public void actualizarTextViewConCoordenadas() {
        if (googleMap != null) {

            //Pedir los permisos si no están ya dados
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
                return;
            }

            //Se obtiene la latitud y longitud y se actualiza en el textView
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    latitud = String.valueOf(location.getLatitude());
                    longitud = String.valueOf(location.getLongitude());
                    TextView coordenadasTextView = findViewById(R.id.coordenadas);
                    coordenadasTextView.setText("Coordenades geolocalitzades: (" + latitud + ", " + longitud + ")");

                    //Error en toast
                } else {
                    Toast.makeText(this, "No se ha podido capturar la ubicación", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Gestionar la respuesta a la solicitud de permisos de ubicación
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {

            //Si se conceden los permisos volver a ejecutar onMapReady
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onMapReady(googleMap);

                //Toast mostrando error si os permisos no se han concedido
            } else {
                Toast.makeText(this, "Permisos de ubicación no concedidos", Toast.LENGTH_SHORT).show();
            }
        }
    }












    //No tocar nada debajo de esto!




    /**
     * Este método se llama cuando un elemento de la barra de navegación lateral es seleccionado.
     *
     * @param item El elemento de la barra de navegación que ha sido seleccionado.
     * @return `true` si el evento ha sido manejado con éxito y `false` de lo contrario.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.nav_home) {
            abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Home.class, json_datos_usuario);
        } else if (item.getItemId() == R.id.nav_reportar) {

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

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}