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
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

public class Seccion_Per_Validar extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
    Activity activity_de_origen = Seccion_Per_Validar.this;


    //Se inicializa la clase que realiza conexiones
    ServerController serverController = new ServerController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seccion_per_validar);

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

        //Se busca el floating button para añadir una incidencia
        FloatingActionButton afegirIncidenciaButton = findViewById(R.id.reportarIncidenciaButton);

        //Click listener para el floating button,que tendrá que abrir la actividad para reportar incidencias
        afegirIncidenciaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirActividadPasandoJSONUsuario(activity_de_origen, Seccion_Reportar.class, json_datos_usuario);
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